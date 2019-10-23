package eki.ekilex.runner;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.replaceChars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eki.common.constant.ClassifierName;
import eki.common.data.AbstractDataObject;
import eki.common.data.Count;
import eki.ekilex.data.transform.Lexeme;
import eki.ekilex.data.transform.Paradigm;
import eki.ekilex.data.transform.Usage;
import eki.ekilex.data.transform.Word;
import eki.ekilex.service.MabService;
import eki.ekilex.service.MeaningNrService;
import eki.ekilex.service.ReportComposer;

public abstract class SsBasedLoaderRunner extends AbstractLoaderRunner {

	private static Logger logger = LoggerFactory.getLogger(SsBasedLoaderRunner.class);

	protected final static String sqlWordLexemesByDataset = "select l.* from " + LEXEME + " l where l.word_id = :wordId and l.dataset_code = :dataset";
	private final static String formStrCleanupChars = "()¤:_|[]̄̆̇\"`´–+=*";  // here is not the regular -, but the special minus symbol

	protected final static String dataLang = "est";
	protected final static String latinLang = "lat";

	protected final static String ARTICLES_REPORT_NAME = "keywords";
	protected final static String DESCRIPTIONS_REPORT_NAME = "keywords_descriptions";
	protected final static String MEANINGS_REPORT_NAME = "keywords_meanings";

	protected final static String LEXEME_RELATION_ABBREVIATION = "lyh";
	protected final static String LEXEME_RELATION_SUB_WORD = "sub_word";

	protected final static String WORD_RELATION_DERIVATIVE = "deriv";
	protected final static String WORD_RELATION_DERIVATIVE_BASE = "deriv_base";
	protected final static String WORD_RELATION_UNION = "ühend";

	protected ReportComposer reportComposer;
	protected boolean reportingPaused;

	protected Map<String, String> wordTypes;
	protected Map<String, String> displayMorpCodes;
	protected Map<String, String> frequencyGroupCodes;
	protected Map<String, String> posCodes;
	protected Map<String, String> processStateCodes;
	protected Map<String, String> registerCodes;

	protected abstract Map<String, String> xpathExpressions();

	@Autowired
	private MabService mabService;

	@Autowired
	protected MeaningNrService mnrService;

	@Override
	void initialise() throws Exception {
		wordTypes = loadClassifierMappingsFor(EKI_CLASSIFIER_LIIKTYYP, ClassifierName.WORD_TYPE.name());
		displayMorpCodes = loadClassifierMappingsFor(EKI_CLASSIFIER_VKTYYP, ClassifierName.DISPLAY_MORPH.name());
		frequencyGroupCodes = loadClassifierMappingsFor(EKI_CLASSIFIER_MSAGTYYP);
		posCodes = loadClassifierMappingsFor(EKI_CLASSIFIER_SLTYYP);
		processStateCodes = loadClassifierMappingsFor(EKI_CLASSIFIER_ASTYYP);
		registerCodes = loadClassifierMappingsFor(EKI_CLASSIFIER_STYYP);
	}

	protected void saveRegisters(Long lexemeId, List<String> lexemeRegisterCodes, String headword) throws Exception {
		if (CollectionUtils.isEmpty(lexemeRegisterCodes)) {
			return;
		}
		for (String registerCode : lexemeRegisterCodes) {
			String ekilexRegisterCode = registerCodes.get(registerCode);
			if (ekilexRegisterCode == null) {
				writeToLogFile(headword, "Tundmatu registri kood", registerCode);
			} else {
				createLexemeRegister(lexemeId, ekilexRegisterCode);
			}
		}
	}

	protected WordToMeaningData findExistingMeaning(Context context, WordData newWord, int level1, List<LexemeToWordData> connectedWords,
			List<String> definitions) {

		List<String> connectedWordValues = connectedWords.stream().map(w -> w.word).collect(toList());
		List<WordToMeaningData> existingMeanings = context.meanings.stream()
				.filter(cachedMeaning -> newWord.value.equals(cachedMeaning.word) &&
						newWord.homonymNr == cachedMeaning.homonymNr &&
						level1 == cachedMeaning.lexemeLevel1 &&
						connectedWordValues.contains(cachedMeaning.meaningWord))
				.collect(toList());
		Optional<WordToMeaningData> existingMeaning;
		if (existingMeanings.size() == 1) {
			return existingMeanings.get(0);
		} else {
			existingMeaning = existingMeanings.stream().filter(meaning -> meaning.meaningDefinitions.containsAll(definitions)).findFirst();
		}
		if (!existingMeaning.isPresent() && !connectedWords.isEmpty()) {
			LexemeToWordData connectedWord = connectedWords.get(0);
			existingMeaning = context.meanings.stream()
					.filter(cachedMeaning -> connectedWord.word.equals(cachedMeaning.meaningWord) &&
							connectedWord.homonymNr == cachedMeaning.meaningHomonymNr &&
							connectedWord.lexemeLevel1 == cachedMeaning.meaningLevel1)
					.findFirst();
		}
		return existingMeaning.orElse(null);
	}

	protected boolean validateMeaning(WordToMeaningData meaningData, List<String> definitions, String headword) throws Exception {

		if (meaningData.meaningDefinitions.isEmpty() || definitions.isEmpty()) {
			return true;
		}
		String definition = definitions.isEmpty() ? null : definitions.get(0);
		String meaningDefinition = meaningData.meaningDefinitions.isEmpty() ? null : meaningData.meaningDefinitions.get(0);
		if (Objects.equals(meaningDefinition, definition)) {
			return true;
		}
		writeToLogFile(MEANINGS_REPORT_NAME, headword, "Tähenduse seletused on erinevad", definition + " : " + meaningDefinition);
		return false;
	}

	protected void processLatinTerms(Context context) throws Exception {

		logger.debug("Found {} latin terms <s:ld>.", context.latinTermins.size());
		logger.debug("Processing started.");
		reportingPaused = true;

		Count newLatinTermWordCount = processLexemeToWord(context, context.latinTermins, null, "Ei leitud ladina terminit, loome uue", latinLang);

		reportingPaused = false;
		logger.debug("Latin terms created {}", newLatinTermWordCount.getValue());
		logger.debug("Latin term import done.");
	}

	protected Count processLexemeToWord(Context context, List<LexemeToWordData> items, String defaultWordType, String logMessage, String lang) throws Exception {
		Count newWordCount = new Count();
		for (LexemeToWordData itemData : items) {
			Long wordId;
			int level1 = itemData.lexemeLevel1;
			boolean addLexeme = true;
			Optional<WordData> existingWord = context.importedWords.stream()
					.filter(w -> itemData.word.equals(w.value) && lang.equals(w.language) && itemData.homonymNr == w.homonymNr)
					.findFirst();
			if (!existingWord.isPresent()) {
				if (defaultWordType != null) {
					itemData.wordTypeCodes.clear();
					itemData.wordTypeCodes.add(defaultWordType);
				}
				WordData newWord = createDefaultWordFrom(itemData.word, lang, null, itemData.displayForm, itemData.vocalForm, itemData.wordTypeCodes, null, null);
				context.importedWords.add(newWord);
				newWordCount.increment();
				wordId = newWord.id;
				if (!reportingPaused) {
					//logger.debug("new word created : {}", itemData.word);
				}
				writeToLogFile(itemData.headword, logMessage, itemData.word);
			} else {
				//logger.debug("word found ; {}", existingWord.get().value);
				wordId = existingWord.get().id;
				if (hasLexemeForMeaning(wordId, itemData.meaningId)) {
					addLexeme = false;
				} else {
					existingWord.get().level1++;
					level1 = existingWord.get().level1;
				}
			}
			if (addLexeme) {
				Lexeme lexeme = new Lexeme();
				lexeme.setWordId(wordId);
				lexeme.setMeaningId(itemData.meaningId);
				lexeme.setLevel1(level1);
				lexeme.setLevel2(1);
				createLexemeIfNotExists(lexeme);
			}
		}
		return newWordCount;
	}

	private boolean hasLexemeForMeaning(Long wordId, Long meaningId) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("word_id", wordId);
		params.put("meaning_id", meaningId);
		Map<String, Object> result = basicDbService.select(LEXEME, params);
		return MapUtils.isNotEmpty(result);
	}

	protected void processDomains(Node node, Long meaningId, List<String> additionalDomains) throws Exception {

		final String domainExp = xpathExpressions().get("domain");

		List<String> domainCodes = node == null ? new ArrayList<>() : extractCleanValues(node, domainExp);
		if (additionalDomains != null) {
			domainCodes.addAll(additionalDomains);
		}
		saveDomains(meaningId, domainCodes);
	}

	protected void saveDomains(Long meaningId, List<String> domainCodes) throws Exception {
		if (CollectionUtils.isEmpty(domainCodes)) {
			return;
		}
		for (String domainCode : domainCodes) {
			createMeaningDomain(meaningId, DOMAIN_ORIGIN_EKI_GENERIC, domainCode);
		}
	}

	protected WordData createDefaultWordFrom(
			String wordValue, String lang, String guid, String displayForm, String vocalForm, List<String> wordTypeCodes, String displayMorph, String aspectCode) throws Exception {

		int homonymNr = getWordMaxHomonymNr(wordValue, lang) + 1;
		Word word = new Word(wordValue, lang, homonymNr, guid, DEFAULT_WORD_MORPH_CODE, null, null, displayForm, vocalForm, wordTypeCodes);
		word.setDisplayMorph(displayMorph);
		word.setAspectCode(aspectCode);
		WordData createdWord = new WordData();
		createdWord.value = wordValue;
		createdWord.displayForm = displayForm;
		createdWord.language = lang;
		createdWord.id = createOrSelectWord(word, null, null);
		return createdWord;
	}

	protected List<LexemeToWordData> extractLexemeMetadata(Node node, String lexemeMetadataExp, String relationTypeAttr, String headword) throws Exception {

		final String lexemeLevel1Attr = "t";
		final String homonymNrAttr = "i";
		final String wordTypeAttr = "liik";
		final int defaultLexemeLevel1 = 1;

		List<LexemeToWordData> metadataList = new ArrayList<>();
		List<Node> metadataNodes = node.selectNodes(lexemeMetadataExp);
		for (Node metadataNode : metadataNodes) {
			if (isRestricted(metadataNode)) {
				continue;
			}
			Element metadataElement = (Element) metadataNode;
			LexemeToWordData lexemeMetadata = new LexemeToWordData();
			lexemeMetadata.displayForm = cleanEkiEntityMarkup(metadataElement.getTextTrim());
			lexemeMetadata.word = cleanUpWord(lexemeMetadata.displayForm);
			lexemeMetadata.headword = headword;
			String lexemeLevel1AttrValue = metadataElement.attributeValue(lexemeLevel1Attr);
			if (StringUtils.isBlank(lexemeLevel1AttrValue)) {
				lexemeMetadata.lexemeLevel1 = defaultLexemeLevel1;
			} else {
				lexemeMetadata.lexemeLevel1 = Integer.parseInt(lexemeLevel1AttrValue);
			}
			String homonymNrAttrValue = metadataElement.attributeValue(homonymNrAttr);
			if (StringUtils.isNotBlank(homonymNrAttrValue)) {
				lexemeMetadata.homonymNr = Integer.parseInt(homonymNrAttrValue);
			}
			if (relationTypeAttr != null) {
				lexemeMetadata.relationType = metadataElement.attributeValue(relationTypeAttr);
			}
			String wordTypeAttrValue = metadataElement.attributeValue(wordTypeAttr);
			if (StringUtils.isNotBlank(wordTypeAttrValue)) {
				String mappedWordTypeCode = wordTypes.get(wordTypeAttrValue);
				if (mappedWordTypeCode == null) {
					logger.debug("unknown word type {}", wordTypeAttrValue);
					writeToLogFile(headword, "Tundmatu märksõnaliik", wordTypeAttrValue);
				} else {
					lexemeMetadata.wordTypeCodes.add(mappedWordTypeCode);
				}
			}
			metadataList.add(lexemeMetadata);
		}
		return metadataList;
	}

	protected List<Paradigm> extractParadigms(Node wordGroupNode, WordData word) throws Exception {

		String morphGroupExp = xpathExpressions().get("morphGroup");

		List<Paradigm> paradigms = new ArrayList<>();
		if (mabService.isMabLoaded() && mabService.homonymsExist(word.value)) {
			Element morphGroupNode = morphGroupExp == null ? null : (Element) wordGroupNode.selectSingleNode(morphGroupExp);
			List<Paradigm> paradigmsFromMab = fetchParadigmsFromMab(word.value, morphGroupNode);
			if (!paradigmsFromMab.isEmpty()) {
				paradigms.addAll(paradigmsFromMab);
			}
		}
		return paradigms;
	}

	private List<Paradigm> fetchParadigmsFromMab(String wordValue, Element node) throws Exception {

		final String formsNodeExp = xpathExpressions().get("formsNode");
		final String formsNodeExp2 = xpathExpressions().get("formsNode2");

		if (!mabService.homonymsExist(wordValue)) {
			return Collections.emptyList();
		}
		List<String> formEndings = extractFormEndings(node, formsNodeExp);
		formEndings.addAll(extractFormEndings(node, formsNodeExp2));
		List<String> suggestedMorphCodes = asList("SgG", "Inf", "IndPrSg1");
		return mabService.getMatchingWordParadigms(wordValue, formEndings, suggestedMorphCodes);
	}

	private List<String> extractFormEndings(Element node, String formsNodeExp) {

		List<String> formEndings = new ArrayList<>();
		if (node == null || formsNodeExp == null) {
			return formEndings;
		}

		Element formsNode = (Element) node.selectSingleNode(formsNodeExp);
		if (formsNode != null) {
			formEndings.addAll(Arrays.stream(formsNode.getTextTrim().split(","))
					.map(v -> v.substring(v.indexOf("-") + 1).trim())
					.collect(Collectors.toList()));
		}

		return formEndings;
	}

	protected Word extractWordData(Node wordGroupNode, WordData wordData, String guid, int index) throws Exception {

		String wordExp = xpathExpressions().get("word");//		final String wordExp = "s:m";
		String wordDisplayMorphExp = xpathExpressions().get("wordDisplayMorph");//	final String wordDisplayMorphExp = "s:vk";
		String wordVocalFormExp = xpathExpressions().get("wordVocalForm"); //final String wordVocalFormExp = "s:hld";
		String homonymNrAttr = "i";
		String wordTypeAttr = "liik";
		String wordFrequencyGroupExp = xpathExpressions().get("wordFrequencyGroup"); //final String wordFrequencyGroupExp = "s:msag";

		Element wordNode = (Element) wordGroupNode.selectNodes(wordExp).get(index);
		if (wordNode.attributeValue(homonymNrAttr) != null) {
			wordData.homonymNr = Integer.parseInt(wordNode.attributeValue(homonymNrAttr));
		}
		if (wordNode.attributeValue(wordTypeAttr) != null) {
			String mappedWordTypeCode = wordTypes.get(wordNode.attributeValue(wordTypeAttr));
			wordData.wordTypeCodes.add(mappedWordTypeCode);
		}
		String wordDisplayForm = cleanEkiEntityMarkup(wordNode.getTextTrim());
		String wordValue = cleanUpWord(wordDisplayForm);
		wordData.value = wordValue;
		wordData.language = dataLang;
		wordData.guid = guid;
		int homonymNr = getWordMaxHomonymNr(wordValue, dataLang) + 1;

		String wordVocalForm = null;
		if (index == 0) {
			Element vocalFormNode = (Element) wordGroupNode.selectSingleNode(wordVocalFormExp);
			if (vocalFormNode != null) {
				wordVocalForm = vocalFormNode.getTextTrim();
			}
		}

		Word word = new Word(wordValue, dataLang, homonymNr, guid, DEFAULT_WORD_MORPH_CODE, null, null, wordDisplayForm, wordVocalForm, wordData.wordTypeCodes);

		Element wordDisplayMorphNode = (Element) wordGroupNode.selectSingleNode(wordDisplayMorphExp);
		if (wordDisplayMorphNode != null) {
			word.setDisplayMorph(displayMorpCodes.get(wordDisplayMorphNode.getTextTrim()));
			if (displayMorpCodes.get(wordDisplayMorphNode.getTextTrim()) == null && index == 0) {
				logger.warn("Unknown display morph code : {} : {}", wordDisplayMorphNode.getTextTrim(), wordValue);
			}
		}
		if (wordFrequencyGroupExp != null) {
			Element frequencyGroupNode = (Element) wordGroupNode.selectSingleNode(wordFrequencyGroupExp);
			if (frequencyGroupNode != null) {
				wordData.frequencyGroup = frequencyGroupCodes.get(frequencyGroupNode.getTextTrim());
			}
		}
		wordData.grammars = extractGrammar(wordGroupNode);
		return word;
	}

	protected List<String> extractGrammar(Node node) {
		String grammarValueExp = xpathExpressions().get("grammarValue");
		return extractCleanValues(node, grammarValueExp);
	}

	protected List<String> extractPosCodes(Node node, String wordPosCodeExp) {
		return extractCleanValues(node, wordPosCodeExp);
	}

	protected String extractHeadword(Node node) {

		String headwordExp = xpathExpressions().get("headword");

		Element headwordNode = (Element) node.selectSingleNode(headwordExp);
		String headword = headwordNode != null ? cleanUpWord(headwordNode.getTextTrim()) : "";
		return headword;
	}

	protected String extractGuid(Node node, String articleGuidExp) {
		Element guidNode = (Element) node.selectSingleNode(articleGuidExp);
		return guidNode != null ? StringUtils.lowerCase(guidNode.getTextTrim()) : null;
	}

	protected List<String> extractCleanValues(Node node, String valueExp) {

		List<String> values = new ArrayList<>();
		List<Node> valueNodes = node.selectNodes(valueExp);
		for (Node valueNode : valueNodes) {
			if (!isRestricted(valueNode)) {
				String value = ((Element) valueNode).getTextTrim();
				value = cleanEkiEntityMarkup(value);
				values.add(value);
			}
		}
		return values;
	}

	protected List<String> extractOriginalValues(Node node, String valueExp) {

		List<String> values = new ArrayList<>();
		List<Node> valueNodes = node.selectNodes(valueExp);
		for (Node valueNode : valueNodes) {
			if (!isRestricted(valueNode)) {
				String value = ((Element) valueNode).getTextTrim();
				values.add(value);
			}
		}
		return values;
	}

	protected boolean isRestricted(Node node) {

		final String restrictedAttr = "as";
		final String restrictedAttr2 = "l";

		String restrictedValue = ((Element)node).attributeValue(restrictedAttr);
		String restrictedValue2 = ((Element)node).attributeValue(restrictedAttr2);
		return asList("ab", "ap").contains(restrictedValue) || asList("ka").contains(restrictedValue2);
	}

	protected String cleanUpWord(String value) {
		String cleanedWord = cleanEkiEntityMarkup(value);
		cleanedWord = unifyAfixoids(cleanedWord);
		cleanedWord = replaceChars(cleanedWord, formStrCleanupChars, "");
		return cleanedWord;
	}

	protected void writeToLogFile(String headword, String message, String values) throws Exception {
		writeToLogFile(null, headword, message, values);
	}

	protected void writeToLogFile(String reportFile, String headword, String message, String values) throws Exception {
		if (doReports && !reportingPaused) {
			String logMessage = String.join(String.valueOf(CSV_SEPARATOR), asList(headword, message, values));
			if (reportFile == null) {
				reportComposer.append(logMessage);
			} else {
				reportComposer.append(reportFile, logMessage);
			}
		}
	}

	protected void setActivateReport(String reportName) {
		if (reportComposer != null) {
			reportComposer.setActiveStream(reportName);
		}
	}

	protected List<WordToMeaningData> convertToMeaningData(List<LexemeToWordData> items, WordData meaningWord, int level1, List<String> definitions) {

		List<WordToMeaningData> meanings = new ArrayList<>();
		for (LexemeToWordData item : items) {
			WordToMeaningData meaning = new WordToMeaningData();
			meaning.meaningId = item.meaningId;
			meaning.meaningWord = meaningWord.value;
			meaning.meaningHomonymNr = meaningWord.homonymNr;
			meaning.meaningLevel1 = level1;
			meaning.meaningDefinitions.addAll(definitions);
			meaning.word = item.word;
			meaning.homonymNr = item.homonymNr;
			meaning.lexemeLevel1 = item.lexemeLevel1;
			meanings.add(meaning);
		}
		return meanings;
	}

	protected void createLexemeRelations(
			Context context,
			List<LexemeToWordData> items,
			String lexemeRelationType,
			String logMessage,
			boolean directionFromLexemeToWord) throws Exception {

		for (LexemeToWordData itemData : items) {
			Long wordId = getWordIdFor(itemData.word, itemData.homonymNr, context.importedWords, itemData.headword);
			if (wordId != null) {
				Map<String, Object> params = new HashMap<>();
				params.put("wordId", wordId);
				params.put("dataset", getDataset());
				try {
					List<Map<String, Object>> lexemeObjects = basicDbService.queryList(sqlWordLexemesByDataset, params);
					Optional<Map<String, Object>> lexemeObject =
							lexemeObjects.stream().filter(l -> (Integer)l.get("level1") == itemData.lexemeLevel1).findFirst();
					if (lexemeObject.isPresent()) {
						if (directionFromLexemeToWord) {
							createLexemeRelation(itemData.lexemeId, (Long) lexemeObject.get().get("id"), lexemeRelationType);
						} else {
							createLexemeRelation((Long) lexemeObject.get().get("id"), itemData.lexemeId, lexemeRelationType);
						}
					} else {
						logger.debug("Lexeme not found for word : {}, lexeme level1 : {}.", itemData.word, itemData.lexemeLevel1);
						writeToLogFile(itemData.headword, logMessage, itemData.word + ", level1 " + itemData.lexemeLevel1);
					}
				} catch (Exception e) {
					logger.error("{} | {} | {}", e.getMessage(), itemData.word, wordId);
				}
			}
		}
	}

	protected Long getWordIdFor(String wordValue, int homonymNr, List<WordData> words, String headword) throws Exception {

		Long wordId = null;
		Optional<WordData> matchingWord = words.stream().filter(w -> w.value.equals(wordValue) && w.homonymNr == homonymNr).findFirst();
		if (matchingWord.isPresent()) {
			wordId = matchingWord.get().id;
		}
		if (wordId == null) {
			if (!reportingPaused) {
				//logger.debug("No matching word was found for: \"{}\", word: \"{}\", homonym: \"{}\"", reportingId, wordValue, homonymNr);
			}
			writeToLogFile(headword, "Ei leitud sihtsõna", wordValue + " : " + homonymNr);
		}
		return wordId;
	}

	protected class WordData extends AbstractDataObject {
		private static final long serialVersionUID = 1L;

		Long id;
		String value;
		int homonymNr = 0;
		String headword;
		String guid;
		List<String> wordTypeCodes = new ArrayList<>();
		List<String> posCodes = new ArrayList<>();
		List<String> grammars = new ArrayList<>();
		List<String> governments = new ArrayList<>();
		List<String> registerCodes = new ArrayList<>();
		List<String> domainCodes = new ArrayList<>();
		String frequencyGroup;
		String displayMorph;
		int level1 = 1;
		String language;
		String displayForm;
		Long meaningId;
		String mnr;
	}

	protected class WordToMeaningData extends AbstractDataObject {
		private static final long serialVersionUID = 1L;

		String word;
		int homonymNr = 0;
		int lexemeLevel1 = 1;
		Long meaningId;
		List<String> meaningDefinitions = new ArrayList<>();
		String meaningWord;
		int meaningHomonymNr = 0;
		int meaningLevel1 = 1;
	}

	protected class LexemeToWordData extends AbstractDataObject {
		private static final long serialVersionUID = 1L;

		String headword;
		Long meaningId;
		Long lexemeId;
		Long wordId;
		String word;
		String displayForm;
		String vocalForm;
		int lexemeLevel1 = 1;
		int homonymNr = 0;
		String relationType;
		List<String> governments = new ArrayList<>();
		List<Usage> usages = new ArrayList<>();
		List<String> wordTypeCodes = new ArrayList<>();
		List<String> registerCodes = new ArrayList<>();
		List<String> sources = new ArrayList<>();
		String aspect;
		Float corpFrequency;

		LexemeToWordData copy() {
			LexemeToWordData newData = new LexemeToWordData();
			newData.meaningId = this.meaningId;
			newData.lexemeId = this.lexemeId;
			newData.wordId = this.wordId;
			newData.word = this.word;
			newData.displayForm = this.displayForm;
			newData.vocalForm = this.vocalForm;
			newData.lexemeLevel1 = this.lexemeLevel1;
			newData.homonymNr = this.homonymNr;
			newData.relationType = this.relationType;
			newData.governments.addAll(this.governments);
			newData.headword = this.headword;
			newData.usages.addAll(this.usages);
			newData.wordTypeCodes.addAll(this.wordTypeCodes);
			newData.registerCodes.addAll(this.registerCodes);
			newData.sources.addAll(sources);
			newData.aspect = this.aspect;
			newData.corpFrequency = this.corpFrequency;
			return newData;
		}
	}

	protected class WordSeries {
		Long groupId;
		List<WordData> words = new ArrayList<>();
	}

	protected class Context extends AbstractDataObject {
		private static final long serialVersionUID = 1L;

		Count ssWordCount = new Count();
		Count ssMeaningCount = new Count();
		Count reusedWordCount = new Count();
		List<WordData> importedWords = new ArrayList<>();
		List<WordData> unionWords = new ArrayList<>();
		List<WordData> derivativeWords = new ArrayList<>();
		List<LexemeToWordData> synonyms = new ArrayList<>();
		List<WordToMeaningData> antonyms = new ArrayList<>();
		List<LexemeToWordData> abbreviations = new ArrayList<>();
		List<LexemeToWordData> abbreviationFullWords = new ArrayList<>();
		List<WordToMeaningData> cohyponyms = new ArrayList<>();
		List<LexemeToWordData> tokens = new ArrayList<>();
		List<LexemeToWordData> latinTermins = new ArrayList<>();
		List<WordToMeaningData> meanings = new ArrayList<>();
		List<WordSeries> series = new ArrayList<>();
	}

}
