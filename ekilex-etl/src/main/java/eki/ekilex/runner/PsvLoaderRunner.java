package eki.ekilex.runner;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eki.common.constant.ClassifierName;
import eki.common.constant.FormMode;
import eki.common.constant.FreeformType;
import eki.common.data.Count;
import eki.ekilex.data.transform.Government;
import eki.ekilex.data.transform.Guid;
import eki.ekilex.data.transform.Lexeme;
import eki.ekilex.data.transform.Meaning;
import eki.ekilex.data.transform.Paradigm;
import eki.ekilex.data.transform.Usage;
import eki.ekilex.data.transform.Word;
import eki.ekilex.service.MabService;
import eki.ekilex.service.ReportComposer;

@Component
public class PsvLoaderRunner extends AbstractLoaderRunner {

	private final String dataLang = "est";
	private final String wordDisplayFormStripChars = ".+'`()¤:_|[]";
	private final String formStrCleanupChars = "()¤:_|[]̄̆̇’\"'`´;–+=";
	private final String defaultWordMorphCode = "SgN";

	private final static String ARTICLES_REPORT_NAME = "keywords";
	private final static String SYNONYMS_REPORT_NAME = "synonyms";
	private final static String ANTONYMS_REPORT_NAME = "antonyms";
	private final static String BASIC_WORDS_REPORT_NAME = "basic_words";
	private final static String COMPOUND_WORDS_REPORT_NAME = "compound_words";
	private final static String REFERENCE_FORMS_REPORT_NAME = "reference_forms";
	private final static String MEANING_REFERENCES_REPORT_NAME = "meaning_references";
	private final static String JOINT_REFERENCES_REPORT_NAME = "joint_references";
	private final static String COMPOUND_REFERENCES_REPORT_NAME = "compound_references";
	private final static String VORMELS_REPORT_NAME = "vormels";
	private final static String SINGLE_FORMS_REPORT_NAME = "single_forms";
	private final static String COMPOUND_FORMS_REPORT_NAME = "compound_forms";
	private final static String WORD_COMPARATIVES_REPORT_NAME = "word_comparatives";
	private final static String WORD_SUPERLATIVES_REPORT_NAME = "word_superlatives";

	private final static String WORD_RELATION_SUPERLATIVE = "superl";
	private final static String WORD_RELATION_COMPARATIVE = "komp";
	private final static String WORD_RELATION_POSITIVE = "posit";
	private final static String WORD_RELATION_UNION = "ühend";

	private final static String FORM_RELATION_REFERENCE_FORM = "mvt";

	private final static String LEXEME_RELATION_COMPOUND_FORM = "pyh";
	private final static String LEXEME_RELATION_SINGLE_FORM = "yvr";
	private final static String LEXEME_RELATION_VORMEL = "vor";
	private final static String LEXEME_RELATION_JOINT_REFERENCE = "yvt";
	private final static String LEXEME_RELATION_MEANING_REFERENCE = "tvt";
	private final static String LEXEME_RELATION_COMPOUND_WORD = "comp";
	private final static String LEXEME_RELATION_BASIC_WORD = "head";

	private final static String MEANING_RELATION_ANTONYM = "ant";

	private final static String sqlFormsOfTheWord = "select f.* from " + FORM + " f, " + PARADIGM + " p where p.word_id = :word_id and f.paradigm_id = p.id";
	private final static String sqlUpdateSoundFiles = "update " + FORM + " set sound_file = :soundFile where id in "
			+ "(select f.id from " + FORM + " f join " + PARADIGM + " p on f.paradigm_id = p.id "
			+ "where f.morph_code = :morphCode and p.word_id = :wordId and p.inflection_type_nr in (:inflectionTypeNumbers))";
	private final static String sqlWordLexemesByDataset = "select l.* from " + LEXEME + " l where l.word_id = :wordId and l.dataset_code = :dataset";

	private static Logger logger = LoggerFactory.getLogger(PsvLoaderRunner.class);

	private Map<String, String> posCodes;
	private Map<String, String> derivCodes;
	private Map<String, String> wordTypes;
	private Map<String, String> processStateCodes;
	private ReportComposer reportComposer;
	private boolean reportingEnabled;
	private String wordTypeAbbreviation;

	@Autowired
	private MabService mabService;

	@Override
	public String getDataset() {
		return "psv";
	}

	@Override
	void initialise() throws Exception {

		wordTypes = loadClassifierMappingsFor(EKI_CLASSIFIER_LIIKTYYP, ClassifierName.WORD_TYPE.name());
		wordTypeAbbreviation = wordTypes.get("l");
		derivCodes = loadClassifierMappingsFor(EKI_CLASSIFIER_DKTYYP, ClassifierName.DERIV.name());
		posCodes = loadClassifierMappingsFor(EKI_CLASSIFIER_SLTYYP);
		processStateCodes = loadClassifierMappingsFor(EKI_CLASSIFIER_ASTYYP);
	}

	@Transactional
	public void execute(
			String dataXmlFilePath,
			Map<String, List<Guid>> ssGuidMap,
			boolean isAddReporting) throws Exception {

		final String articleHeaderExp = "x:P";
		final String articleBodyExp = "x:S";
		final String reportingIdExp = "x:P/x:mg/x:m"; // use first word as id for reporting

		logger.info("Starting import");
		long t1, t2;
		t1 = System.currentTimeMillis();

		reportingEnabled = isAddReporting;

		if (reportingEnabled) {
			reportComposer = new ReportComposer("PSV import",
					ARTICLES_REPORT_NAME, SYNONYMS_REPORT_NAME, ANTONYMS_REPORT_NAME, BASIC_WORDS_REPORT_NAME, REFERENCE_FORMS_REPORT_NAME,
					COMPOUND_WORDS_REPORT_NAME, REFERENCE_FORMS_REPORT_NAME, MEANING_REFERENCES_REPORT_NAME, JOINT_REFERENCES_REPORT_NAME,
					COMPOUND_REFERENCES_REPORT_NAME, VORMELS_REPORT_NAME, SINGLE_FORMS_REPORT_NAME, COMPOUND_FORMS_REPORT_NAME,
					WORD_COMPARATIVES_REPORT_NAME, WORD_SUPERLATIVES_REPORT_NAME);
		}

		Document dataDoc = xmlReader.readDocument(dataXmlFilePath);

		Element rootElement = dataDoc.getRootElement();
		long articleCount = rootElement.content().stream().filter(o -> o instanceof Element).count();
		logger.debug("Extracted {} articles", articleCount);

		long articleCounter = 0;
		long progressIndicator = articleCount / Math.min(articleCount, 100);

		Context context = new Context();

		writeToLogFile("Artiklite töötlus", "", "");
		List<Node> articleNodes = rootElement.content().stream().filter(o -> o instanceof Element).collect(toList());
		for (Node articleNode : articleNodes) {
			String guid = extractGuid(articleNode);
			List<WordData> newWords = new ArrayList<>();
			Element headerNode = (Element) articleNode.selectSingleNode(articleHeaderExp);
			Element reportingIdNode = (Element) articleNode.selectSingleNode(reportingIdExp);
			String reportingId = reportingIdNode != null ? reportingIdNode.getTextTrim() : "";
			processArticleHeader(guid, reportingId, headerNode, newWords, ssGuidMap, context);

			Element contentNode = (Element) articleNode.selectSingleNode(articleBodyExp);
			if (contentNode != null) {
				processArticleContent(reportingId, contentNode, newWords, context);
			}

			articleCounter++;
			if (articleCounter % progressIndicator == 0) {
				long progressPercent = articleCounter / progressIndicator;
				logger.debug("{}% - {} articles iterated", progressPercent, articleCounter);
			}
			context.importedWords.addAll(newWords);
		}

		processSynonymsNotFoundInImportFile(context);
		processAntonyms(context);
		processBasicWords(context);
		processReferenceForms(context);
		processCompoundWords(context);
		processMeaningReferences(context);
		processJointReferences(context);
		processUnions(context);
		processVormels(context);
		processSingleForms(context);
		processCompoundForms(context);
		processWordComparatives(context);
		processWordSuperlatives(context);

		logger.debug("Found {} ss words", context.ssWordCount.getValue());
		logger.debug("Found {} word duplicates", context.reusedWordCount.getValue());
		logger.debug("Found {} lexeme duplicates", context.lexemeDuplicateCount.getValue());

		if (reportComposer != null) {
			reportComposer.end();
		}
		t2 = System.currentTimeMillis();
		logger.debug("Done in {} ms", (t2 - t1));
	}

	private String extractGuid(Node node) {

		final String articleGuidExp = "x:G";

		Element guidNode = (Element) node.selectSingleNode(articleGuidExp);
		return guidNode != null ? StringUtils.lowerCase(guidNode.getTextTrim()) : null;
	}

	private void processWordSuperlatives(Context context) throws Exception {

		logger.debug("Starting word superlatives processing.");
		setActivateReport(WORD_SUPERLATIVES_REPORT_NAME);
		writeToLogFile("Ülivõrrete töötlus <x:kmp>", "", "");

		long count = 0;
		List<WordData> words = new ArrayList<>();
		words.addAll(context.importedWords.stream().filter(wd -> !wd.superlatives.isEmpty()).collect(Collectors.toList()));

		for (WordData wordData : words) {
			for (String superlative : wordData.superlatives) {
				count++;
				Long superlativeId = findOrCreateWord(context, superlative);
				createWordRelation(wordData.id, superlativeId, WORD_RELATION_SUPERLATIVE);
				createWordRelation(superlativeId, wordData.id, WORD_RELATION_POSITIVE);
			}
		}
		logger.debug("Word superlatives processing done, {}.", count);
	}

	private void processWordComparatives(Context context) throws Exception {

		logger.debug("Starting word comparatives processing.");
		setActivateReport(WORD_COMPARATIVES_REPORT_NAME);
		writeToLogFile("Keskvõrrete töötlus <x:kmp>", "", "");

		long count = 0;
		List<WordData> words = new ArrayList<>();
		words.addAll(context.importedWords.stream().filter(wd -> !wd.comparatives.isEmpty()).collect(Collectors.toList()));

		for (WordData wordData : words) {
			for (String comparative : wordData.comparatives) {
				count++;
				Long comparativeId = findOrCreateWord(context, comparative);
				createWordRelation(wordData.id, comparativeId, WORD_RELATION_COMPARATIVE);
				createWordRelation(comparativeId, wordData.id, WORD_RELATION_POSITIVE);
			}
		}
		logger.debug("Word comparatives processing done, {}.", count);
	}

	private Long findOrCreateWord(Context context, String wordValue) throws Exception {
		Long wordId;
		List<WordData> existingWords = context.importedWords.stream()
				.filter(w -> wordValue.equals(w.value))
				.collect(Collectors.toList());
		if (existingWords.isEmpty()) {
			LexemeToWordData newLexemeData = new LexemeToWordData();
			newLexemeData.word = wordValue;
			wordId = createLexemeAndRelatedObjects(newLexemeData, context).wordId;
		} else {
			wordId = existingWords.get(0).id;
		}
		return wordId;
	}

	private void processCompoundForms(Context context) throws Exception {

		logger.debug("Found {} compound forms.", context.compoundForms.size());
		setActivateReport(COMPOUND_FORMS_REPORT_NAME);
		writeToLogFile("Ühendite töötlus <x:pyh>", "", "");

		for (LexemeToWordData compoundFormData : context.compoundForms) {
			List<WordData> existingWords = context.importedWords.stream()
					.filter(w -> compoundFormData.word.equals(w.value))
					.collect(Collectors.toList());
			Long lexemeId = findOrCreateLexemeForWord(existingWords, compoundFormData, context);
			if (lexemeId != null) {
				createLexemeRelation(compoundFormData.lexemeId, lexemeId, LEXEME_RELATION_COMPOUND_FORM);
			}
		}
		logger.debug("Compound form processing done.");
	}

	private void processSingleForms(Context context) throws Exception {

		logger.debug("Found {} single forms.", context.singleForms.size());
		setActivateReport(SINGLE_FORMS_REPORT_NAME);
		writeToLogFile("Üksikvormide töötlus <x:yvr>", "", "");

		for (LexemeToWordData singleFormData : context.singleForms) {
			List<WordData> existingWords = context.importedWords.stream()
					.filter(w -> singleFormData.word.equals(w.value))
					.collect(Collectors.toList());
			Long lexemeId = findOrCreateLexemeForWord(existingWords, singleFormData, context);
			if (lexemeId != null) {
				createLexemeRelation(singleFormData.lexemeId, lexemeId, LEXEME_RELATION_SINGLE_FORM);
			}
		}
		logger.debug("Single form processing done.");
	}

	private void processVormels(Context context) throws Exception {

		logger.debug("Found {} vormels.", context.vormels.size());
		setActivateReport(VORMELS_REPORT_NAME);
		writeToLogFile("Vormelite töötlus <x:vor>", "", "");

		for (LexemeToWordData vormelData : context.vormels) {
			List<WordData> existingWords = context.importedWords.stream()
					.filter(w -> vormelData.word.equals(w.value))
					.collect(Collectors.toList());
			Long lexemeId = findOrCreateLexemeForWord(existingWords, vormelData, context);
			if (lexemeId != null) {
				createLexemeRelation(vormelData.lexemeId, lexemeId, LEXEME_RELATION_VORMEL);
			}
		}
		logger.debug("Vormel processing done.");
	}

	private void processUnions(Context context) throws Exception {

		logger.debug("Found {} unions.", context.unionWords.size());
		setActivateReport(COMPOUND_REFERENCES_REPORT_NAME);
		writeToLogFile("Ühendite töötlus <x:yhvt>", "", "");

		for (WordData unionWord : context.unionWords) {
			Long wordId = getWordIdFor(unionWord.value, unionWord.homonymNr, context.importedWords, unionWord.reportingId);
			if (wordId != null) {
				createWordRelation(unionWord.id, wordId, WORD_RELATION_UNION);
			}
		}
		logger.debug("Unions processing done.");
	}

	private void processJointReferences(Context context) throws Exception {

		logger.debug("Found {} joint references.", context.jointReferences.size());
		setActivateReport(JOINT_REFERENCES_REPORT_NAME);
		writeToLogFile("Ühisviidete töötlus <x:yvt>", "", "");

		for (LexemeToWordData jointRefData : context.jointReferences) {
			List<WordData> existingWords = context.importedWords.stream()
					.filter(w -> jointRefData.word.equals(w.value))
					.filter(w -> jointRefData.homonymNr == 0 || jointRefData.homonymNr == w.homonymNr)
					.collect(Collectors.toList());
			Long lexemeId = findOrCreateLexemeForWord(existingWords, jointRefData, context);
			if (lexemeId != null) {
				String relationType = LEXEME_RELATION_JOINT_REFERENCE + ":" + jointRefData.relationType;
				createLexemeRelation(jointRefData.lexemeId, lexemeId, relationType);
			}
		}
		logger.debug("Joint references processing done.");
	}

	private void processMeaningReferences(Context context) throws Exception {

		logger.debug("Found {} meaning references.", context.meaningReferences.size());
		setActivateReport(MEANING_REFERENCES_REPORT_NAME);
		writeToLogFile("Tähendusviidete töötlus <x:tvt>", "", "");

		for (LexemeToWordData meaningRefData : context.meaningReferences) {
			List<WordData> existingWords = context.importedWords.stream()
					.filter(w -> meaningRefData.word.equals(w.value))
					.filter(w -> meaningRefData.homonymNr == 0 || meaningRefData.homonymNr == w.homonymNr)
					.collect(Collectors.toList());
			Long lexemeId = findOrCreateLexemeForWord(existingWords, meaningRefData, context);
			if (lexemeId != null) {
				String relationType = LEXEME_RELATION_MEANING_REFERENCE + ":" + meaningRefData.relationType;
				createLexemeRelation(meaningRefData.lexemeId, lexemeId, relationType);
			}
		}
		logger.debug("Meaning references processing done.");
	}

	private void processCompoundWords(Context context) throws Exception {

		logger.debug("Found {} compound words.", context.compoundWords.size());
		setActivateReport(COMPOUND_WORDS_REPORT_NAME);
		writeToLogFile("Liitsõnade töötlus <x:ls>", "", "");

		for (LexemeToWordData compData : context.compoundWords) {
			List<WordData> existingWords = context.importedWords.stream().filter(w -> compData.word.equals(w.value)).collect(Collectors.toList());
			Long lexemeId = findOrCreateLexemeForWord(existingWords, compData, context);
			if (lexemeId != null) {
				createLexemeRelation(compData.lexemeId, lexemeId, LEXEME_RELATION_COMPOUND_WORD);
			}
		}
		logger.debug("Compound words processing done.");
	}

	private Long findOrCreateLexemeForWord(List<WordData> existingWords, LexemeToWordData data, Context context) throws Exception {

		if (existingWords.size() > 1) {
			logger.debug("Found more than one word : {}.", data.word);
			writeToLogFile(data.reportingId, "Leiti rohkem kui üks vaste sõnale", data.word);
		}
		Long lexemeId;
		if (CollectionUtils.isEmpty(existingWords)) {
			logger.debug("No word found, adding word with objects : {}.", data.word);
			lexemeId = createLexemeAndRelatedObjects(data, context).lexemeId;
			createUsages(lexemeId, data.usages, dataLang);
			if (data.government != null) {
				Long governmentId = createLexemeFreeform(lexemeId, FreeformType.GOVERNMENT, data.government.getValue(), dataLang);
				if (isNotBlank(data.government.getType())) {
					createFreeformClassifier(FreeformType.GOVERNMENT_TYPE, governmentId, data.government.getType());
				}
			}
		} else {
			lexemeId = findLexemeIdForWord(existingWords.get(0).id, data);
			if (CollectionUtils.isNotEmpty(data.usages)) {
				logger.debug("Usages found for word, skipping them : {}.", data.word);
				writeToLogFile(data.reportingId, "Leiti kasutusnäited olemasolevale ilmikule", data.word);
			}
		}
		return lexemeId;
	}

	private LexemeData createLexemeAndRelatedObjects(LexemeToWordData wordData, Context context) throws Exception {

		LexemeData createdLexemeData = new LexemeData();
		WordData newWord = createDefaultWordFrom(wordData.word, wordData.wordType);
		context.importedWords.add(newWord);
		createdLexemeData.wordId = newWord.id;
		Long meaningId = createMeaning();
		Lexeme lexeme = new Lexeme();
		lexeme.setMeaningId(meaningId);
		lexeme.setWordId(newWord.id);
		lexeme.setLevel1(1);
		lexeme.setLevel2(1);
		lexeme.setLevel3(1);
		if (isNotBlank(wordData.definition)) {
			createDefinition(meaningId, wordData.definition, dataLang, getDataset());
		}
		createdLexemeData.lexemeId = createLexeme(lexeme, getDataset());
		return createdLexemeData;
	}

	private WordData createDefaultWordFrom(String wordValue, String wordType) throws Exception {

		WordData createdWord = new WordData();
		createdWord.value = wordValue;
		createdWord.wordType = wordType;
		int homonymNr = getWordMaxHomonymNr(wordValue, dataLang) + 1;
		Word word = new Word(wordValue, dataLang, null, null, null, null, homonymNr, defaultWordMorphCode, null, wordType);
		createdWord.id = createOrSelectWord(word, null, null, null);
		return createdWord;
	}

	private Long findLexemeIdForWord(Long wordId, LexemeToWordData data) throws Exception {

		Long lexemeId = null;
		Map<String, Object> params = new HashMap<>();
		params.put("wordId", wordId);
		params.put("dataset", getDataset());
		if (data.lexemeLevel1 != 0) {
			params.put("level1", data.lexemeLevel1);
		}
		List<Map<String, Object>> lexemes = basicDbService.queryList(sqlWordLexemesByDataset, params);
		if (data.lexemeLevel1 != 0) {
			lexemes = lexemes.stream().filter(l -> (Integer)l.get("level1") == data.lexemeLevel1).collect(Collectors.toList());
		}
		if (lexemes.isEmpty()) {
			logger.debug("Lexeme not found for word : {}.", data.word);
			writeToLogFile(data.reportingId, "Ei leitud ilmikut sõnale", data.word);
		} else {
			if (lexemes.size() > 1) {
				logger.debug("Found more than one lexeme for : {}.", data.word);
				writeToLogFile(data.reportingId, "Leiti rohkem kui üks ilmik sõnale", data.word);
			}
			lexemeId = (Long) lexemes.get(0).get("id");
		}
		return lexemeId;
	}

	private void processReferenceForms(Context context) throws Exception {

		logger.debug("Found {} reference forms.", context.referenceForms.size());
		setActivateReport(REFERENCE_FORMS_REPORT_NAME);
		writeToLogFile("Vormid mis viitavad põhisõnale töötlus <x:mvt>", "", "");

		for (ReferenceFormData referenceForm : context.referenceForms) {
			Optional<WordData> word = context.importedWords.stream()
					.filter(w -> referenceForm.wordValue.equals(w.value) && referenceForm.wordHomonymNr == w.homonymNr).findFirst();
			if (word.isPresent()) {
				Map<String, Object> params = new HashMap<>();
				params.put("word_id", word.get().id);
				List<Map<String, Object>> forms = basicDbService.queryList(sqlFormsOfTheWord, params);
				List<Map<String, Object>> wordForms = forms.stream().filter(f -> {
					String mode = (String) f.get("mode");
					return StringUtils.equals(mode, FormMode.WORD.name());
				}).collect(Collectors.toList());
				if (wordForms.size() > 1) {
					logger.debug("More than one word form found for word : {}, id : {}", referenceForm.wordValue, word.get().id);
					continue;
				}
				Map<String, Object> wordForm = wordForms.get(0);
				Optional<Map<String, Object>> form = forms.stream().filter(f -> referenceForm.formValue.equals(f.get("value"))).findFirst();
				if (!form.isPresent()) {
					logger.debug("Form not found for {}, {} -> {}", referenceForm.reportingId, referenceForm.formValue, referenceForm.wordValue);
					writeToLogFile(referenceForm.reportingId, "Vormi ei leitud", referenceForm.formValue + " -> " + referenceForm.wordValue);
					continue;
				}
				params.clear();
				params.put("form1_id", form.get().get("id"));
				params.put("form2_id", wordForm.get("id"));
				params.put("form_rel_type_code", FORM_RELATION_REFERENCE_FORM);
				basicDbService.create(FORM_RELATION, params);
			} else {
				logger.debug("Word not found {}, {}, {}", referenceForm.reportingId, referenceForm.wordValue, referenceForm.wordHomonymNr);
				writeToLogFile(referenceForm.reportingId, "Sihtsõna ei leitud", referenceForm.wordValue + ", " + referenceForm.wordHomonymNr);
			}
		}
		logger.debug("Reference forms processing done.");
	}

	private void processBasicWords(Context context) throws Exception {

		logger.debug("Found {} basic words.", context.basicWords.size());
		setActivateReport(BASIC_WORDS_REPORT_NAME);
		writeToLogFile("Märksõna põhisõna seoste töötlus <x:ps>", "", "");

		for (WordData basicWord : context.basicWords) {
			Long wordId = getWordIdFor(basicWord.value, basicWord.homonymNr, context.importedWords, basicWord.reportingId);
			if (wordId != null) {
				Map<String, Object> params = new HashMap<>();
				params.put("wordId", basicWord.id);
				params.put("dataset", getDataset());
				List<Map<String, Object>> secondaryWordLexemes = basicDbService.queryList(sqlWordLexemesByDataset, params);
				for (Map<String, Object> secondaryWordLexeme : secondaryWordLexemes) {
					params.put("wordId", wordId);
					List<Map<String, Object>> lexemes = basicDbService.queryList(sqlWordLexemesByDataset, params);
					for (Map<String, Object> lexeme : lexemes) {
						createLexemeRelation((Long) secondaryWordLexeme.get("id"), (Long) lexeme.get("id"), LEXEME_RELATION_BASIC_WORD);
					}
				}
			}
		}
		logger.debug("Basic words processing done.");
	}

	private void processAntonyms(Context context) throws Exception {

		logger.debug("Found {} antonyms <x:ant>.", context.antonyms.size());
		setActivateReport(ANTONYMS_REPORT_NAME);
		writeToLogFile("Antonüümide töötlus <s:ant>", "", "");
		createMeaningRelations(context, context.antonyms, MEANING_RELATION_ANTONYM, "Ei leitud mõistet antonüümile");
		logger.debug("Antonyms import done.");
	}

	private void createMeaningRelations(Context context, List<WordToMeaningData> items, String meaningRelationType, String logMessage) throws Exception {

		for (WordToMeaningData item : items) {
			Optional<WordToMeaningData> connectedItem = items.stream()
					.filter(i -> Objects.equals(item.word, i.meaningWord) &&
							Objects.equals(item.homonymNr, i.meaningHomonymNr) &&
							Objects.equals(item.lexemeLevel1, i.meaningLevel1))
					.findFirst();
			if (connectedItem.isPresent()) {
				createMeaningRelation(item.meaningId, connectedItem.get().meaningId, meaningRelationType);
			} else {
				Long wordId = getWordIdFor(item.word, item.homonymNr, context.importedWords, item.meaningWord);
				if (wordId != null) {
					Map<String, Object> params = new HashMap<>();
					params.put("wordId", wordId);
					params.put("dataset", getDataset());
					try {
						List<Map<String, Object>> lexemeObjects = basicDbService.queryList(sqlWordLexemesByDataset, params);
						Optional<Map<String, Object>> lexemeObject =
								lexemeObjects.stream().filter(l -> (Integer)l.get("level1") == item.lexemeLevel1).findFirst();
						if (lexemeObject.isPresent()) {
							createMeaningRelation(item.meaningId, (Long) lexemeObject.get().get("meaning_id"), meaningRelationType);
						} else {
							logger.debug("Meaning not found for word : {}, lexeme level1 : {}.", item.word, item.lexemeLevel1);
							writeToLogFile(item.meaningWord, logMessage, item.word + ", level1 " + item.lexemeLevel1);
						}
					} catch (Exception e) {
						logger.error("{} | {} | {}", e.getMessage(), item.word, wordId);
					}
				}
			}
		}
	}

	private void processSynonymsNotFoundInImportFile(Context context) throws Exception {

		logger.debug("Found {} synonyms", context.synonyms.size());
		setActivateReport(SYNONYMS_REPORT_NAME);
		writeToLogFile("Sünonüümide töötlus <x:syn>", "", "");

		Count newSynonymWordCount = new Count();
		for (SynonymData synonymData : context.synonyms) {
			List<WordData> existingWords = context.importedWords.stream().filter(w -> synonymData.word.equals(w.value)).collect(Collectors.toList());
			if (existingWords.isEmpty()) {
				WordData newWord = createDefaultWordFrom(synonymData.word, null);
				context.importedWords.add(newWord);
				newSynonymWordCount.increment();
				Long wordId = newWord.id;

				Lexeme lexeme = new Lexeme();
				lexeme.setWordId(wordId);
				lexeme.setMeaningId(synonymData.meaningId);
				lexeme.setLevel1(1);
				lexeme.setLevel2(1);
				lexeme.setLevel3(1);
				createLexeme(lexeme, getDataset());
			}
		}
		logger.debug("Synonym words created {}", newSynonymWordCount.getValue());
		logger.debug("Synonyms import done.");
	}

	private Long getWordIdFor(String wordValue, int homonymNr, List<WordData> words, String reportingId) throws Exception {

		List<WordData> existingWords = words.stream().filter(w -> wordValue.equals(w.value)).collect(Collectors.toList());
		Long wordId = null;
		if (!existingWords.isEmpty()) {
			if (existingWords.size() == 1) {
				wordId = existingWords.get(0).id;
			} else {
				Optional<WordData> matchingWord = existingWords.stream().filter(w -> w.homonymNr == homonymNr).findFirst();
				if (matchingWord.isPresent()) {
					wordId = matchingWord.get().id;
				} else {
					logger.debug("No matching word was found for: \"{}\", word: \"{}\", homonym: \"{}\"", reportingId, wordValue, homonymNr);
					writeToLogFile(reportingId, "Ei leitud sihtsõna", wordValue + " : " + homonymNr);
				}
			}
		}
		return wordId;
	}

	private void processArticleContent(String reportingId, Element contentNode, List<WordData> newWords, Context context) throws Exception {

		final String meaningNumberGroupExp = "x:tp";
		final String lexemeLevel1Attr = "tnr";
		final String meaningGroupExp = "x:tg";
		final String usageGroupExp = "x:ng";
		final String commonInfoNodeExp = "x:tyg2";
		final String lexemePosCodeExp = "x:grg/x:sl";
		final String conceptIdExp = "x:tpid";
		final String learnerCommentExp = "x:qkom";
		final String imageNameExp = "x:plp/x:plg/x:plf";
		final String asTyypAttr = "as";

		List<Node> meaningNumberGroupNodes = contentNode.selectNodes(meaningNumberGroupExp);
		List<LexemeToWordData> jointReferences = extractJointReferences(contentNode, reportingId);
		Element commonInfoNode = (Element) contentNode.selectSingleNode(commonInfoNodeExp);
		List<LexemeToWordData> articleVormels = extractVormels(commonInfoNode);
		List<WordData> unionWords = extractUnionWords(contentNode, newWords, reportingId);
		context.unionWords.addAll(unionWords);

		for (Node meaningNumberGroupNode : meaningNumberGroupNodes) {
			saveSymbol(meaningNumberGroupNode, context, reportingId);
			WordData abbreviation = extractAbbreviation(meaningNumberGroupNode, context);
			String lexemeLevel1Str = ((Element)meaningNumberGroupNode).attributeValue(lexemeLevel1Attr);
			String processStateCode =  processStateCodes.get(((Element)meaningNumberGroupNode).attributeValue(asTyypAttr));
			Integer lexemeLevel1 = Integer.valueOf(lexemeLevel1Str);
			List<Node> meaningGroupNodes = meaningNumberGroupNode.selectNodes(meaningGroupExp);
			List<String> compoundWords = extractCompoundWords(meaningNumberGroupNode);
			List<LexemeToWordData> meaningReferences = extractMeaningReferences(meaningNumberGroupNode, reportingId);
			List<LexemeToWordData> vormels = extractVormels(meaningNumberGroupNode);
			List<LexemeToWordData> singleForms = extractSingleForms(meaningNumberGroupNode);
			List<LexemeToWordData> compoundForms = extractCompoundForms(meaningNumberGroupNode, reportingId);
			List<Long> newLexemes = new ArrayList<>();
			List<String> meaningPosCodes = extractPosCodes(meaningNumberGroupNode, lexemePosCodeExp);
			Element conceptIdNode = (Element) meaningNumberGroupNode.selectSingleNode(conceptIdExp);
			String conceptId = conceptIdNode == null ? null : conceptIdNode.getTextTrim();
			Element learnerCommentNode = (Element) meaningNumberGroupNode.selectSingleNode(learnerCommentExp);
			String learnerComment = nodeToString(learnerCommentNode);
			Element imageNameNode = (Element) meaningNumberGroupNode.selectSingleNode(imageNameExp);
			String imageName = imageNameNode == null ? null : imageNameNode.getTextTrim();

			for (Node meaningGroupNode : meaningGroupNodes) {
				List<Node> usageGroupNodes = meaningGroupNode.selectNodes(usageGroupExp);
				List<Usage> usages = extractUsages(usageGroupNodes);
				List<String> definitions = extractDefinitions(meaningGroupNode);

				Long meaningId = findExistingMeaningId(context, newWords.get(0), definitions);
				if (meaningId == null) {
					Meaning meaning = new Meaning();
					meaning.setProcessStateCode(processStateCode);
					meaningId = createMeaning(meaning);
					for (String definition : definitions) {
						createDefinition(meaningId, definition, dataLang, getDataset());
					}
					if (definitions.size() > 1) {
						writeToLogFile(reportingId, "Leitud rohkem kui üks seletus <x:d>", newWords.get(0).value);
					}
					if (isNotBlank(learnerComment)) {
						createMeaningFreeform(meaningId, FreeformType.LEARNER_COMMENT, learnerComment);
					}
				} else {
					logger.debug("synonym meaning found : {}", newWords.get(0).value);
				}

				if (isNotBlank(conceptId)) {
					createMeaningFreeform(meaningId, FreeformType.CONCEPT_ID, conceptId);
				}
				if (isNotBlank(imageName)) {
					createMeaningFreeform(meaningId, FreeformType.IMAGE_FILE, imageName);
				}
				if (abbreviation != null) {
					addAbbreviationLexeme(abbreviation, meaningId);
				}

				List<SynonymData> meaningSynonyms = extractSynonyms(reportingId, meaningGroupNode, meaningId, definitions);
				context.synonyms.addAll(meaningSynonyms);
				List<WordToMeaningData> meaningAntonyms = extractAntonyms(meaningGroupNode, meaningId, newWords.get(0), lexemeLevel1, reportingId);
				context.antonyms.addAll(meaningAntonyms);

				int lexemeLevel2 = 0;
				for (WordData newWordData : newWords) {
					lexemeLevel2++;
					Lexeme lexeme = new Lexeme();
					lexeme.setWordId(newWordData.id);
					// FIXME: from where we get value state
					//lexeme.setValueState(newWordData.lexemeType);
					lexeme.setMeaningId(meaningId);
					lexeme.setLevel1(lexemeLevel1);
					lexeme.setLevel2(lexemeLevel2);
					lexeme.setLevel3(1);
					lexeme.setFrequencyGroup(newWordData.frequencyGroup);
					Long lexemeId = createLexeme(lexeme, getDataset());
					if (lexemeId == null) {
						context.lexemeDuplicateCount.increment();
					} else {
						createUsages(lexemeId, usages, dataLang);
						saveGovernmentsAndUsages(meaningNumberGroupNode, lexemeId);
						savePosAndDeriv(lexemeId, newWordData, meaningPosCodes, reportingId);
						saveGrammars(meaningNumberGroupNode, lexemeId, newWordData);
						for (String compoundWord : compoundWords) {
							LexemeToWordData compData = new LexemeToWordData();
							compData.word = compoundWord;
							compData.lexemeId = lexemeId;
							compData.reportingId = reportingId;
							context.compoundWords.add(compData);
						}
						for (LexemeToWordData meaningReference : meaningReferences) {
							LexemeToWordData referenceData = meaningReference.copy();
							referenceData.lexemeId = lexemeId;
							referenceData.reportingId = reportingId;
							context.meaningReferences.add(referenceData);
						}
						for (LexemeToWordData vormel : vormels) {
							LexemeToWordData vormelData = vormel.copy();
							vormelData.lexemeId = lexemeId;
							vormelData.reportingId = reportingId;
							context.vormels.add(vormelData);
						}
						for (LexemeToWordData singleForm : singleForms) {
							LexemeToWordData singleFormData = singleForm.copy();
							singleFormData.lexemeId = lexemeId;
							singleFormData.reportingId = reportingId;
							context.singleForms.add(singleFormData);
						}
						for (LexemeToWordData compoundForm : compoundForms) {
							LexemeToWordData compoundFormData = compoundForm.copy();
							compoundFormData.lexemeId = lexemeId;
							compoundFormData.reportingId = reportingId;
							context.compoundForms.add(compoundFormData);
						}
						newLexemes.add(lexemeId);
					}
				}
			}
			for (Long lexemeId : newLexemes) {
				for (LexemeToWordData jointReference : jointReferences) {
					LexemeToWordData referenceData = jointReference.copy();
					referenceData.lexemeId = lexemeId;
					referenceData.reportingId = reportingId;
					context.jointReferences.add(referenceData);
				}
				for (LexemeToWordData vormel : articleVormels) {
					LexemeToWordData vormelData = vormel.copy();
					vormelData.lexemeId = lexemeId;
					vormelData.reportingId = reportingId;
					context.vormels.add(vormelData);
				}
			}
		}
	}

	private Long findExistingMeaningId(Context context, WordData newWord, List<String> definitions) {

		String definition = definitions.isEmpty() ? null : definitions.get(0);
		Optional<SynonymData> existingSynonym = context.synonyms.stream()
				.filter(s -> newWord.value.equals(s.word) && newWord.homonymNr == s.homonymNr && Objects.equals(definition, s.definition))
				.findFirst();
		return existingSynonym.orElse(new SynonymData()).meaningId;
	}

	private void addAbbreviationLexeme(WordData abbreviation, Long meaningId) throws Exception {
		Lexeme lexeme = new Lexeme();
		lexeme.setMeaningId(meaningId);
		lexeme.setWordId(abbreviation.id);
		lexeme.setLevel1(abbreviation.level1);
		lexeme.setLevel2(1);
		lexeme.setLevel3(1);
		createLexeme(lexeme, getDataset());
		abbreviation.level1++;
	}

	private WordData extractAbbreviation(Node node, Context context) throws Exception {

		final String abbreviationExp = "x:lyh";
		Node abbreviationNode = node.selectSingleNode(abbreviationExp);
		if (abbreviationNode == null) {
			return null;
		}
		String abbreviationValue = nodeToString(abbreviationNode);
		Optional<WordData> abbreviation = context.importedWords.stream().filter(w -> w.value.equals(abbreviationValue)).findFirst();
		if (abbreviation.isPresent()) {
			return abbreviation.get();
		} else {
			WordData abbrData =  createDefaultWordFrom(abbreviationValue, wordTypeAbbreviation);
			context.importedWords.add(abbrData);
			return abbrData;
		}
	}

	private List<LexemeToWordData> extractCompoundForms(Node node, String reportingId) throws Exception {

		final String compoundFormGroupNodeExp = "x:pyp/x:pyg";
		final String compoundFormNodeExp = "x:pyh";
		final String definitionGroupNodeExp = "x:pyt";
		final String definitionExp = "x:pyd";
		final String usageExp = "x:ng/x:n";
		final String governmentExp = "x:rek";
		final String usageDefinitionExp = "x:nd";
		final String wordTypeAttr = "liik";

		List<LexemeToWordData> compoundForms = new ArrayList<>();
		List<Node> compoundFormGroupNodes = node.selectNodes(compoundFormGroupNodeExp);
		for (Node compoundFormGroupNode : compoundFormGroupNodes) {
			List<LexemeToWordData> forms = new ArrayList<>();
			List<Node> compoundFormNodes = compoundFormGroupNode.selectNodes(compoundFormNodeExp);
			for (Node compoundFormNode : compoundFormNodes) {
				LexemeToWordData data = new LexemeToWordData();
				data.word = nodeToString(compoundFormNode);
				if (((Element)compoundFormNode).hasMixedContent()) {
					data.government = extractGovernment(compoundFormNode.selectSingleNode(governmentExp));
				}
				if (((Element)compoundFormNode).attributeValue(wordTypeAttr) != null) {
					String wordType = ((Element)compoundFormNode).attributeValue(wordTypeAttr);
					data.wordType = wordTypes.get(wordType);
					if (data.wordType == null) {
						writeToLogFile(reportingId, "Tundmatu märksõnaliik", wordType);
					}
				}
				forms.add(data);
			}
			for (LexemeToWordData data : forms) {
				Element definitionGroupNodeNode = (Element) compoundFormGroupNode.selectSingleNode(definitionGroupNodeExp);
				Node definitionNode = definitionGroupNodeNode.selectSingleNode(definitionExp);
				if (definitionNode != null) {
					data.definition = nodeToString(definitionNode);
				}
				List<Node> usageNodes = definitionGroupNodeNode.selectNodes(usageExp);
				for (Node usageNode : usageNodes) {
					Usage usage = new Usage();
					usage.setValue(nodeToString(usageNode));
					usage.setDefinitions(new ArrayList<>());
					if (((Element)usageNode).hasMixedContent()) {
						Node usageDefinitionNode = usageNode.selectSingleNode(usageDefinitionExp);
						String usageDefinition = nodeToString(usageDefinitionNode);
						usage.getDefinitions().add(usageDefinition);
					}
					data.usages.add(usage);
				}
			}
			compoundForms.addAll(forms);
		}
		return compoundForms;
	}

	private Government extractGovernment(Node governmentNode) {

		final String governmentTypeAttr = "rliik";
		final String governmentVariantAttr = "var";
		final String governmentOptionalAttr = "fak";

		Government government = new Government();
		Element governmentElement = (Element) governmentNode;
		government.setValue(nodeToString(governmentNode));
		government.setType(governmentElement.attributeValue(governmentTypeAttr));
		government.setVariant(governmentElement.attributeValue(governmentVariantAttr));
		government.setOptional(governmentElement.attributeValue(governmentOptionalAttr));
		return government;
	}

	private void saveSymbol(Node node, Context context, String reportingId) throws Exception {

		final String symbolExp = "x:symb";

		Node symbolNode = node.selectSingleNode(symbolExp);
		if (symbolNode != null) {
			String symbolValue = nodeToString(symbolNode);
			WordData data = createDefaultWordFrom(symbolValue, null);
			data.reportingId = reportingId;
			context.importedWords.add(data);
		}
	}

	private List<LexemeToWordData> extractSingleForms(Node node) {

		final String singleFormGroupNodeExp = "x:yvp/x:yvg";
		final String singleFormNodeExp = "x:yvrg";
		final String formValueExp = "x:yvr";
		final String formDefinitionExp = "x:yvd";
		final String usageExp = "x:ng/x:n";
		final String governmentExp = "x:rek";
		final String usageDefinitionExp = "x:nd";

		List<LexemeToWordData> singleForms = new ArrayList<>();
		List<Node> singleFormGroupNodes = node.selectNodes(singleFormGroupNodeExp);
		for (Node singleFormGroupNode : singleFormGroupNodes) {
			List<Usage> usages = new ArrayList<>();
			List<Node> formUsageNodes = singleFormGroupNode.selectNodes(usageExp);
			for (Node usageNode : formUsageNodes) {
				Usage usage = new Usage();
				usage.setValue(nodeToString(usageNode));
				usage.setDefinitions(new ArrayList<>());
				usages.add(usage);
				if (((Element)usageNode).hasMixedContent()) {
					Node usageDefinitionNode = usageNode.selectSingleNode(usageDefinitionExp);
					String usageDefinition = nodeToString(usageDefinitionNode);
					usage.getDefinitions().add(usageDefinition);
				}
			}
			List<Node> singleFormNodes = singleFormGroupNode.selectNodes(singleFormNodeExp);
			for (Node singleFormNode : singleFormNodes) {
				LexemeToWordData data = new LexemeToWordData();
				Node formValueNode = singleFormNode.selectSingleNode(formValueExp);
				data.word = nodeToString(formValueNode);
				if (((Element)formValueNode).hasMixedContent()) {
					data.government = extractGovernment(formValueNode.selectSingleNode(governmentExp));
				}
				Node formDefinitionNode = singleFormNode.selectSingleNode(formDefinitionExp);
				if (formDefinitionNode != null) {
					data.definition = nodeToString(formDefinitionNode);
				}
				data.usages.addAll(usages);
				singleForms.add(data);
			}
		}
		return singleForms;
	}

	private List<LexemeToWordData> extractVormels(Node node) {

		final String vormelNodeExp = "x:vop/x:vog";
		final String vormelExp = "x:vor";
		final String vormelDefinitionExp = "x:vod";
		final String vormelUsageExp = "x:ng/x:n";

		List<LexemeToWordData> vormels = new ArrayList<>();
		if (node == null) {
			return vormels;
		}
		List<Node> vormelNodes = node.selectNodes(vormelNodeExp);
		for (Node vormelNode : vormelNodes) {
			LexemeToWordData data = new LexemeToWordData();
			Node vormelValueNode = vormelNode.selectSingleNode(vormelExp);
			data.word = nodeToString(vormelValueNode);
			Node vormelDefinitionNode = vormelNode.selectSingleNode(vormelDefinitionExp);
			List<Node> vormelUsages = vormelNode.selectNodes(vormelUsageExp);
			if (vormelDefinitionNode != null) {
				data.definition = nodeToString(vormelDefinitionNode);
			}
			for (Node usageNode : vormelUsages) {
				Usage usage = new Usage();
				usage.setValue(nodeToString(usageNode));
				data.usages.add(usage);
			}
			vormels.add(data);
		}
		return vormels;
	}

	private List<WordData> extractUnionWords(Element node, List<WordData> newWords, String reportingId) {

		final String unionWordExp = "x:tyg2/x:yhvt";

		List<WordData> unionWordsMetadata = new ArrayList<>();
		for (WordData newWord : newWords) {
			unionWordsMetadata.addAll(extractWordMetadata(node, unionWordExp, newWord.id, reportingId));
		}
		return unionWordsMetadata;
	}

	private List<LexemeToWordData> extractJointReferences(Element node, String reportingId) throws Exception {

		final String jointReferenceExp = "x:tyg2/x:yvt";
		final String relationTypeAttr = "yvtl";

		return extractLexemeMetadata(node, jointReferenceExp, relationTypeAttr, reportingId);
	}

	private List<String> extractCompoundWords(Node node) {

		final String compoundWordExp = "x:smp/x:lsg/x:ls";

		List<String> compoundWords = new ArrayList<>();
		List<Node> compoundWordNodes = node.selectNodes(compoundWordExp);
		for (Node compoundWordNode : compoundWordNodes) {
			compoundWords.add(nodeToString(compoundWordNode));
		}
		return compoundWords;
	}

	private List<LexemeToWordData> extractMeaningReferences(Node node, String reportingId) throws Exception {

		final String meaningReferenceExp = "x:tvt";
		final String relationTypeAttr = "tvtl";

		return extractLexemeMetadata(node, meaningReferenceExp, relationTypeAttr, reportingId);
	}

	private List<WordToMeaningData> extractAntonyms(Node node, Long meaningId, WordData wordData, int level1, String reportingId) throws Exception {

		final String antonymExp = "x:ant";

		List<WordToMeaningData> antonyms = new ArrayList<>();
		List<LexemeToWordData> items = extractLexemeMetadata(node, antonymExp, null, reportingId);
		for (LexemeToWordData item : items) {
			WordToMeaningData meaningData = new WordToMeaningData();
			meaningData.meaningId = meaningId;
			meaningData.meaningWord = wordData.value;
			meaningData.meaningHomonymNr = wordData.homonymNr;
			meaningData.meaningLevel1 = level1;
			meaningData.word = item.word;
			meaningData.homonymNr = item.homonymNr;
			meaningData.lexemeLevel1 = item.lexemeLevel1;
			antonyms.add(meaningData);
		}
		return antonyms;
	}


	private List<LexemeToWordData> extractLexemeMetadata(Node node, String lexemeMetadataExp, String relationTypeAttr, String reportingId) throws Exception {

		final String lexemeLevel1Attr = "t";
		final String homonymNrAttr = "i";
		final String wordTypeAttr = "liik";
		final int defaultLexemeLevel1 = 1;

		List<LexemeToWordData> metadataList = new ArrayList<>();
		List<Node> metadataNodes = node.selectNodes(lexemeMetadataExp);
		for (Node metadataNode : metadataNodes) {
			LexemeToWordData lexemeMetadata = new LexemeToWordData();
			Element metadataElement = (Element) metadataNode;
			lexemeMetadata.word = nodeToString(metadataElement);
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
				lexemeMetadata.wordType = wordTypes.get(wordTypeAttrValue);
				if (lexemeMetadata.wordType == null) {
					writeToLogFile(reportingId, "Tundmatu märksõnaliik", wordTypeAttrValue);
				}
			}
			metadataList.add(lexemeMetadata);
		}
		return metadataList;
	}

	private List<SynonymData> extractSynonyms(String reportingId, Node node, Long meaningId, List<String> definitions) {

		final String synonymExp = "x:syn";
		final String homonymNrAttr = "i";

		List<SynonymData> synonyms = new ArrayList<>();
		List<Node> synonymNodes = node.selectNodes(synonymExp);
		for (Node synonymNode : synonymNodes) {
			SynonymData data = new SynonymData();
			data.reportingId = reportingId;
			data.word = nodeToString(synonymNode);
			data.meaningId = meaningId;
			String homonymNrAtrValue = ((Element)synonymNode).attributeValue(homonymNrAttr);
			if (StringUtils.isNotBlank(homonymNrAtrValue)) {
				data.homonymNr = Integer.parseInt(homonymNrAtrValue);
			}
			if (!definitions.isEmpty()) {
				data.definition = definitions.get(0);
			}
			synonyms.add(data);
		}
		return synonyms;
	}

	private void saveGrammars(Node node, Long lexemeId, WordData wordData) throws Exception {

		final String grammarValueExp = "x:grg/x:gki";

		List<Node> grammarNodes = node.selectNodes(grammarValueExp);
		for (Node grammarNode : grammarNodes) {
			createLexemeFreeform(lexemeId, FreeformType.GRAMMAR, nodeToString(grammarNode), dataLang);
		}
		if (isNotBlank(wordData.grammar)) {
			createLexemeFreeform(lexemeId, FreeformType.GRAMMAR, wordData.grammar, dataLang);
		}
	}

	//POS - part of speech
	private void savePosAndDeriv(Long lexemeId, WordData newWordData, List<String> meaningPosCodes, String reportingId) throws Exception {

		Set<String> lexemePosCodes = new HashSet<>();
		if (meaningPosCodes.isEmpty()) {
			lexemePosCodes.addAll(newWordData.posCodes);
		} else {
			lexemePosCodes.addAll(meaningPosCodes);
			if (lexemePosCodes.size() > 1) {
				writeToLogFile(reportingId, "Tähenduse juures leiti rohkem kui üks sõnaliik <x:tp/x:grg/x:sl>", "");
			}
		}
		for (String code : lexemePosCodes) {
			if (posCodes.containsKey(code)) {
				Map<String, Object> params = new HashMap<>();
				params.put("lexeme_id", lexemeId);
				params.put("pos_code", posCodes.get(code));
				basicDbService.create(LEXEME_POS, params);
			}
		}
		if (derivCodes.containsKey(newWordData.derivCode)) {
			Map<String, Object> params = new HashMap<>();
			params.put("lexeme_id", lexemeId);
			params.put("deriv_code", derivCodes.get(newWordData.derivCode));
			basicDbService.create(LEXEME_DERIV, params);
		}
	}

	private void saveGovernmentsAndUsages(Node node, Long lexemeId) throws Exception {

		final String governmentGroupExp = "x:rep/x:reg";
		final String usageGroupExp = "x:ng";
		final String governmentExp = "x:rek";
		final String governmentPlacementAttr = "koht";

		List<Node> governmentGroups = node.selectNodes(governmentGroupExp);
		for (Node governmentGroup : governmentGroups) {
			String governmentPlacement = ((Element)governmentGroup).attributeValue(governmentPlacementAttr);
			List<Node> usageGroupNodes = governmentGroup.selectNodes(usageGroupExp);
			List<Usage> usages = extractUsages(usageGroupNodes);
			createUsages(lexemeId, usages, dataLang);
			List<Node> governmentNodes = governmentGroup.selectNodes(governmentExp);
			for (Node governmentNode : governmentNodes) {
				Government government = extractGovernment(governmentNode);
				Long governmentId = createOrSelectLexemeFreeform(lexemeId, FreeformType.GOVERNMENT, government.getValue());
				if (isNotBlank(government.getType())) {
					createFreeformClassifier(FreeformType.GOVERNMENT_TYPE, governmentId, government.getType());
				}
				if (isNotBlank(governmentPlacement)) {
					createFreeformTextOrDate(FreeformType.GOVERNMENT_PLACEMENT, governmentId, governmentPlacement, null);
				}
				if (isNotBlank(government.getVariant())) {
					createFreeformTextOrDate(FreeformType.GOVERNMENT_VARIANT, governmentId, government.getVariant(), null);
				}
				if (isNotBlank(government.getOptional())) {
					createFreeformTextOrDate(FreeformType.GOVERNMENT_OPTIONAL, governmentId, government.getOptional(), null);
				}
			}
		}
	}

	private List<Usage> extractUsages(List<Node> usageGroupNodes) {

		final String usageExp = "x:n";

		List<Usage> usages = new ArrayList<>();
		for (Node usageGroupNode : usageGroupNodes) {
			List<Node> usageNodes = usageGroupNode.selectNodes(usageExp);
			for (Node usageNode : usageNodes) {
				Usage usage = new Usage();
				String usageValue = nodeToString(usageNode);
				usage.setValue(usageValue);
				usage.setDefinitions(new ArrayList<>());
				usages.add(usage);
				if (((Element)usageNode).hasMixedContent()) {
					Node usageDefinitionNode = usageNode.selectSingleNode("x:nd");
					String usageDefinition = nodeToString(usageDefinitionNode);
					usage.getDefinitions().add(usageDefinition);
				}
			}
		}
		return usages;
	}

	private void processArticleHeader(
			String guid,
			String reportingId,
			Element headerNode,
			List<WordData> newWords,
			Map<String, List<Guid>> ssGuidMap,
			Context context) throws Exception {

		final String referenceFormExp = "x:mvt";

		List<Node> referenceFormNodes = headerNode.selectNodes(referenceFormExp);
		boolean isReferenceForm = !referenceFormNodes.isEmpty();

		if (isReferenceForm) {
			processAsForm(reportingId, headerNode, referenceFormNodes, context.referenceForms);
		} else {
			processAsWord(guid, reportingId, headerNode, newWords, ssGuidMap, context);
		}
	}

	private void processAsForm(String reportingId, Element headerNode, List<Node> referenceFormNodes, List<ReferenceFormData> referenceForms) {

		final String wordGroupExp = "x:mg";
		final String wordExp = "x:m";
		final String homonymNrAttr = "i";

		List<Node> wordGroupNodes = headerNode.selectNodes(wordGroupExp);
		for (Node wordGroupNode : wordGroupNodes) {
			Node wordNode = wordGroupNode.selectSingleNode(wordExp);
			String formValue = nodeToString(wordNode);
			formValue = StringUtils.replaceChars(formValue, wordDisplayFormStripChars, "");
			for (Node referenceFormNode : referenceFormNodes) {
				Element referenceFormElement = (Element) referenceFormNode;
				ReferenceFormData referenceFormData = new ReferenceFormData();
				String wordValue = nodeToString(referenceFormElement);
				referenceFormData.formValue = formValue;
				referenceFormData.reportingId = reportingId;
				referenceFormData.wordValue = wordValue;
				if (referenceFormElement.attributeValue(homonymNrAttr) != null) {
					referenceFormData.wordHomonymNr = Integer.parseInt(referenceFormElement.attributeValue(homonymNrAttr));
				}
				referenceForms.add(referenceFormData);
			}
		}
	}

	private void processAsWord(
			String guid,
			String reportingId,
			Element headerNode,
			List<WordData> newWords,
			Map<String, List<Guid>> ssGuidMap,
			Context context) throws Exception {

		final String wordGroupExp = "x:mg";
		final String wordPosCodeExp = "x:sl";
		final String wordDerivCodeExp = "x:dk";
		final String wordGrammarExp = "x:mfp/x:gki";
		final String wordFrequencyGroupExp = "x:sag";
		final String wordComparativeExp = "x:mfp/x:kmpg/x:kmp";
		final String wordSuperlativeExp = "x:mfp/x:kmpg/x:suprl";
		final String basicWordExp = "x:ps";

		List<Node> wordGroupNodes = headerNode.selectNodes(wordGroupExp);
		for (Node wordGroupNode : wordGroupNodes) {
			WordData wordData = new WordData();
			wordData.reportingId = reportingId;

			Word word = extractWordData(wordGroupNode, wordData, guid, context);
			if (word != null) {
				List<Paradigm> paradigms = extractParadigms(wordGroupNode, wordData);
				wordData.id = createOrSelectWord(word, paradigms, getDataset(), ssGuidMap, context.ssWordCount, context.reusedWordCount);
			}

//			addSoundFileNamesToForms(wordData.id, wordGroupNode);

			List<WordData> basicWordsOfTheWord = extractWordMetadata(wordGroupNode, basicWordExp, wordData.id, reportingId);
			context.basicWords.addAll(basicWordsOfTheWord);

			wordData.posCodes.addAll(extractPosCodes(wordGroupNode, wordPosCodeExp));

			Element derivCodeNode = (Element) wordGroupNode.selectSingleNode(wordDerivCodeExp);
			wordData.derivCode = derivCodeNode == null ? null : derivCodeNode.getTextTrim();

			Node grammarNode = wordGroupNode.selectSingleNode(wordGrammarExp);
			wordData.grammar = nodeToString(grammarNode);

			Element frequencyNode = (Element) wordGroupNode.selectSingleNode(wordFrequencyGroupExp);
			wordData.frequencyGroup = frequencyNode == null ? null : frequencyNode.getTextTrim();

			List<Node> wordComparativeNodes = wordGroupNode.selectNodes(wordComparativeExp);
			wordData.comparatives = wordComparativeNodes.stream()
					.map(n -> {
						String value = nodeToString(n);
						value = StringUtils.replaceChars(value, formStrCleanupChars, "");
						return value;
						})
					.collect(Collectors.toList());

			List<Node> wordSuperlativeNodes = wordGroupNode.selectNodes(wordSuperlativeExp);
			wordData.superlatives = wordSuperlativeNodes.stream()
					.map(n -> {
						String value = nodeToString(n);
						value = StringUtils.replaceChars(value, formStrCleanupChars, "");
						return value;
						})
					.collect(Collectors.toList());

			newWords.add(wordData);
		}
	}

	private void addSoundFileNamesToForms(Long wordId, Node wordGroupNode) {

		final String paradigmGroupExp = "x:mfp";
		final String paradigmInflectionTypeNrExp = "x:mt";
		final String morphValueGroupExp = "x:gkg/x:mvg";
		final String morphCodeAttr = "vn";
		final String soundFileExp = "x:mvgp/x:hldf";

		List<SoundFileData> soundFiles = new ArrayList<>();
		List<Node> paradigmGroupNodes = wordGroupNode.selectNodes(paradigmGroupExp);
		for (Node paradigmGroupNode : paradigmGroupNodes) {
			Element paradigmInflectionTypeNrNode = (Element) paradigmGroupNode.selectSingleNode(paradigmInflectionTypeNrExp);
			if (paradigmInflectionTypeNrNode != null) {
				String inflectionTypeNrStr = paradigmInflectionTypeNrNode.getTextTrim().replace("?", "");
				List<String> inflectionTypeNumbers = asList(inflectionTypeNrStr.split("~"));
				List<Node> morphValueGroupNodes = paradigmGroupNode.selectNodes(morphValueGroupExp);
				for (Node morphValueGroupNode : morphValueGroupNodes) {
					Element soundFileNode = (Element) morphValueGroupNode.selectSingleNode(soundFileExp);
					if (soundFileNode != null) {
						String morphCode = ((Element)morphValueGroupNode).attributeValue(morphCodeAttr);
						SoundFileData data = new SoundFileData();
						data.soundFile = soundFileNode.getTextTrim();
						data.morphCode = morphCode;
						data.inflectionTypeNumbers = inflectionTypeNumbers;
						soundFiles.add(data);
					}
				}
			} else {
				logger.debug("missing inflection type : {}", wordId);
			}
		}
		for (SoundFileData soundFileData : soundFiles) {
			Map<String, Object> params = new HashMap<>();
			params.put("morphCode", soundFileData.morphCode);
			params.put("inflectionTypeNumbers", soundFileData.inflectionTypeNumbers);
			params.put("wordId", wordId);
			params.put("soundFile", soundFileData.soundFile);
			basicDbService.executeScript(sqlUpdateSoundFiles, params);
		}
	}

	private List<WordData> extractWordMetadata(Node node, String wordExp, Long wordId, String reportingId) {

		final String homonymNrAttr = "i";

		List<WordData> words = new ArrayList<>();
		List<Node> wordNodes = node.selectNodes(wordExp);
		for (Node wordNode : wordNodes) {
			WordData wordData = new WordData();
			String basicWordValue = nodeToString(wordNode);
			wordData.id = wordId;
			wordData.value = basicWordValue;
			wordData.reportingId = reportingId;
			if (((Element)wordNode).attributeValue(homonymNrAttr) != null) {
				wordData.homonymNr = Integer.parseInt(((Element)wordNode).attributeValue(homonymNrAttr));
			}
			words.add(wordData);
		}
		return words;
	}

	private List<Paradigm> fetchParadigmsFromMab(String wordValue, Node node) throws Exception {

		final String formsNodesExp = "x:mfp/x:gkg/x:mvg/x:mvgp/x:mvf";

		if (mabService.isSingleHomonym(wordValue)) {
			return mabService.getWordParadigms(wordValue);
		}

		List<Node> formsNodes = node.selectNodes(formsNodesExp);
		if (formsNodes.isEmpty()) {
			return Collections.emptyList();
		}
		List<String> formValues = formsNodes.stream()
				.map(n -> {
					String value = nodeToString(n);
					value = StringUtils.replaceChars(value, formStrCleanupChars, "");
					return value;
					})
				.collect(Collectors.toList());
		List<String> mabFormValues;
		Collection<String> formValuesIntersection;
		int bestFormValuesMatchCount = -1;
		Paradigm matchingParadigm = null;
		for (Paradigm paradigm : mabService.getWordParadigms(wordValue)) {
			mabFormValues = paradigm.getFormValues();
			formValuesIntersection = CollectionUtils.intersection(formValues, mabFormValues);
			if (formValuesIntersection.size() > bestFormValuesMatchCount) {
				bestFormValuesMatchCount = formValuesIntersection.size();
				matchingParadigm = paradigm;
			}
		}
		Integer matchingHomonymNumber = matchingParadigm.getHomonymNr();
		return mabService.getWordParadigmsForHomonym(wordValue, matchingHomonymNumber);
	}

	private Word extractWordData(Node wordGroupNode, WordData wordData, String guid, Context context) throws Exception {

		final String wordExp = "x:m";
		final String wordDisplayMorphExp = "x:vk";
		final String homonymNrAttr = "i";
		final String wordTypeAttr = "liik";

		Element wordNode = (Element) wordGroupNode.selectSingleNode(wordExp);
		if (wordNode.attributeValue(homonymNrAttr) != null) {
			wordData.homonymNr = Integer.parseInt(wordNode.attributeValue(homonymNrAttr));
		}
		if (wordNode.attributeValue(wordTypeAttr) != null) {
			wordData.wordType = wordTypes.get(wordNode.attributeValue(wordTypeAttr));
		}
		String wordValue = nodeToString(wordNode);
		String wordDisplayForm = wordValue;
		wordValue = StringUtils.replaceChars(wordValue, wordDisplayFormStripChars, "");
		wordData.value = wordValue;

		int homonymNr = getWordMaxHomonymNr(wordValue, dataLang) + 1;
		String wordMorphCode = extractWordMorphCode(wordValue, wordGroupNode);
		Word word = new Word(wordValue, dataLang, null, null, wordDisplayForm, null, homonymNr, wordMorphCode, guid, wordData.wordType);

		Node wordDisplayMorphNode = wordGroupNode.selectSingleNode(wordDisplayMorphExp);
		if (wordDisplayMorphNode != null) {
			word.setDisplayMorph(nodeToString(wordDisplayMorphNode));
		}
		return word;
	}

	private List<Paradigm> extractParadigms(Node wordGroupNode, WordData word) throws Exception {

		final String inflectionTypeNrExp = "x:mfp/x:mt";

		List<Paradigm> paradigms = new ArrayList<>();
		if (mabService.isMabLoaded() && mabService.paradigmsExist(word.value)) {
			paradigms.addAll(fetchParadigmsFromMab(word.value, wordGroupNode));
		}
		Element inflectionTypeNrNode = (Element) wordGroupNode.selectSingleNode(inflectionTypeNrExp);
		List<String> inflectionNumbers = new ArrayList<>();
		if (inflectionTypeNrNode != null) {
			String inflectionTypeNrStr = inflectionTypeNrNode.getTextTrim();
			inflectionNumbers = asList(inflectionTypeNrStr.split("~"));
		}
		if (!inflectionNumbers.isEmpty()) {
			if (paradigms.isEmpty()) {
				inflectionNumbers.forEach(inflectionNumber -> {
					Paradigm paradigm = new Paradigm();
					paradigm.setInflectionTypeNr(inflectionNumber.replace("?", ""));
					paradigm.setSecondary(inflectionNumber.contains("?"));
					paradigms.add(paradigm);
				});
			} else {
				inflectionNumbers.forEach(inflectionNumber -> {
					if (inflectionNumber.contains("?")) {
						paradigms.stream()
							.filter(paradigm -> paradigm.getInflectionTypeNr().equals(inflectionNumber.replace("?", "")))
							.forEach(paradigm -> paradigm.setSecondary(true));
					}
				});
			}
		}
		return paradigms;
	}

	private String extractWordMorphCode(String word, Node wordGroupNode) {

		final String formGroupExp = "x:mfp/x:gkg/x:mvg";
		final String formExp = "x:mvgp/x:mvf";
		final String morphCodeAttributeExp = "vn";

		List<Node> formGroupNodes = wordGroupNode.selectNodes(formGroupExp);
		for (Node formGroup : formGroupNodes) {
			String formValue = nodeToString(formGroup.selectSingleNode(formExp));
			formValue = StringUtils.replaceChars(formValue, wordDisplayFormStripChars, "");
			if (word.equals(formValue)) {
				return ((Element)formGroup).attributeValue(morphCodeAttributeExp);
			}
		}
		return defaultWordMorphCode;
	}

	private List<String> extractDefinitions(Node node) {

		final String definitionValueExp = "x:dg/x:d";

		List<String> definitions = new ArrayList<>();
		List<Node> definitionValueNodes = node.selectNodes(definitionValueExp);
		for (Node definitionValueNode : definitionValueNodes) {
			String definition = nodeToString(definitionValueNode);
			definitions.add(definition);
		}
		return definitions;
	}

	private List<String> extractPosCodes(Node node, String posCodeExp) {

		final String asTyypAttr = "as";

		List<String> posCodes = new ArrayList<>();
		for (Node posCodeNode : node.selectNodes(posCodeExp)) {
			if (((Element)posCodeNode).attributeValue(asTyypAttr) != null) {
				posCodes.add(nodeToString(posCodeNode));
			}
		}
		return posCodes;
	}

	private String nodeToString(Node node) {
		String stringValue = node == null ? null : ((Element)node).getTextTrim();
		return cleanEkiEntityMarkup(stringValue);
	}

	private void writeToLogFile(String reportingId, String message, String values) throws Exception {
		if (reportingEnabled) {
			String logMessage = String.join(String.valueOf(CSV_SEPARATOR), asList(reportingId, message, values));
			reportComposer.append(logMessage);
		}
	}

	private void setActivateReport(String reportName) {
		if (reportComposer != null) {
			reportComposer.setActiveStream(reportName);
		}
	}

	private class WordData {
		Long id;
		List<String> posCodes = new ArrayList<>();
		String derivCode;
		String grammar;
		String value;
		int homonymNr = 0;
		String reportingId;
		String frequencyGroup;
		String wordType;
		List<String> comparatives = new ArrayList<>();
		List<String> superlatives = new ArrayList<>();
		int level1 = 1;
	}

	private class WordToMeaningData {
		String word;
		int homonymNr = 0;
		int lexemeLevel1 = 1;
		Long meaningId;
		String meaningWord;
		int meaningHomonymNr = 0;
		int meaningLevel1 = 1;
	}

	private class SynonymData {
		String word;
		Long meaningId;
		int homonymNr = 0;
		String reportingId;
		String definition;
	}

	private class LexemeToWordData {
		Long lexemeId;
		String word;
		int lexemeLevel1 = 1;
		int homonymNr = 0;
		String relationType;
		Government government;
		String definition;
		List<Usage> usages = new ArrayList<>();
		String reportingId;
		String wordType;

		LexemeToWordData copy() {
			LexemeToWordData newData = new LexemeToWordData();
			newData.lexemeId = this.lexemeId;
			newData.word = this.word;
			newData.lexemeLevel1 = this.lexemeLevel1;
			newData.homonymNr = this.homonymNr;
			newData.relationType = this.relationType;
			newData.government = this.government;
			newData.definition = this.definition;
			newData.reportingId = this.reportingId;
			newData.usages.addAll(this.usages);
			newData.wordType = this.wordType;
			return newData;
		}
	}

	private class ReferenceFormData {
		String formValue;
		String wordValue;
		int wordHomonymNr = 0;
		String reportingId;
	}

	private class SoundFileData {
		String soundFile;
		String morphCode;
		List<String> inflectionTypeNumbers;
	}

	private class LexemeData {
		Long lexemeId;
		Long wordId;
	}

	private class Context {
		Count ssWordCount = new Count();
		Count reusedWordCount = new Count();
		Count lexemeDuplicateCount = new Count();
		List<SynonymData> synonyms = new ArrayList<>();
		List<WordToMeaningData> antonyms = new ArrayList<>();
		List<WordData> importedWords = new ArrayList<>();
		List<WordData> basicWords = new ArrayList<>();
		List<WordData> unionWords = new ArrayList<>(); // ühendiviide
		List<ReferenceFormData> referenceForms = new ArrayList<>(); // viitemärksõna
		List<LexemeToWordData> compoundWords = new ArrayList<>(); // liitsõnad
		List<LexemeToWordData> meaningReferences = new ArrayList<>(); // tähendusviide
		List<LexemeToWordData> jointReferences = new ArrayList<>(); // ühisviide
		List<LexemeToWordData> vormels = new ArrayList<>(); // vormel
		List<LexemeToWordData> singleForms = new ArrayList<>(); // üksikvorm
		List<LexemeToWordData> compoundForms = new ArrayList<>(); // ühend
	}

}
