package eki.ekilex.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eki.common.constant.DbConstant;
import eki.common.constant.FreeformType;
import eki.ekilex.data.Classifier;
import eki.ekilex.data.ClassifierSelect;
import eki.ekilex.data.Definition;
import eki.ekilex.data.DefinitionLangGroup;
import eki.ekilex.data.DefinitionRefTuple;
import eki.ekilex.data.FreeForm;
import eki.ekilex.data.Image;
import eki.ekilex.data.ImageSourceTuple;
import eki.ekilex.data.Lexeme;
import eki.ekilex.data.LexemeLangGroup;
import eki.ekilex.data.Meaning;
import eki.ekilex.data.MeaningsResult;
import eki.ekilex.data.Note;
import eki.ekilex.data.NoteSourceTuple;
import eki.ekilex.data.OrderedClassifier;
import eki.ekilex.data.Relation;
import eki.ekilex.data.SearchDatasetsRestriction;
import eki.ekilex.data.SearchFilter;
import eki.ekilex.data.SourceLink;
import eki.ekilex.data.TermMeaning;
import eki.ekilex.data.TermMeaningWordTuple;
import eki.ekilex.data.Usage;
import eki.ekilex.data.UsageTranslationDefinitionTuple;
import eki.ekilex.service.db.TermSearchDbService;

@Component
public class TermSearchService extends AbstractSearchService implements DbConstant {

	@Autowired
	private TermSearchDbService termSearchDbService;

	@Transactional
	public MeaningsResult getMeanings(String searchFilter, List<String> selectedDatasetCodes, String resultLang, boolean fetchAll, int offset) {

		List<TermMeaning> termMeanings;
		int meaningCount;
		int wordCount;
		if (StringUtils.isBlank(searchFilter)) {
			termMeanings = Collections.emptyList();
			meaningCount = 0;
			wordCount = 0;
		} else {
			SearchDatasetsRestriction searchDatasetsRestriction = composeDatasetsRestriction(selectedDatasetCodes);
			List<TermMeaningWordTuple> termMeaningWordTuples = termSearchDbService
					.getMeanings(searchFilter, searchDatasetsRestriction, resultLang, fetchAll, offset);
			termMeanings = conversionUtil.composeTermMeanings(termMeaningWordTuples);
			meaningCount = termSearchDbService.countMeanings(searchFilter, searchDatasetsRestriction);
			wordCount = termSearchDbService.countWords(searchFilter, searchDatasetsRestriction, resultLang);
		}
		boolean resultExist = meaningCount > 0;
		MeaningsResult meaningsResult = new MeaningsResult();
		meaningsResult.setMeaningCount(meaningCount);
		meaningsResult.setWordCount(wordCount);
		meaningsResult.setTermMeanings(termMeanings);
		meaningsResult.setResultExist(resultExist);

		boolean showPaging = meaningCount > MAX_RESULTS_LIMIT;
		meaningsResult.setShowPaging(showPaging);
		if (showPaging) {
			setPagingData(offset, meaningCount, meaningsResult);
		}

		return meaningsResult;
	}

	@Transactional
	public MeaningsResult getMeanings(SearchFilter searchFilter, List<String> selectedDatasetCodes, String resultLang, boolean fetchAll, int offset)
			throws Exception {

		List<TermMeaning> termMeanings;
		int meaningCount;
		int wordCount;
		if (CollectionUtils.isEmpty(searchFilter.getCriteriaGroups())) {
			termMeanings = Collections.emptyList();
			meaningCount = 0;
			wordCount = 0;
		} else {
			SearchDatasetsRestriction searchDatasetsRestriction = composeDatasetsRestriction(selectedDatasetCodes);
			List<TermMeaningWordTuple> termMeaningWordTuples = termSearchDbService
					.getMeanings(searchFilter, searchDatasetsRestriction, resultLang, fetchAll, offset);
			termMeanings = conversionUtil.composeTermMeanings(termMeaningWordTuples);
			meaningCount = termSearchDbService.countMeanings(searchFilter, searchDatasetsRestriction);
			wordCount = termSearchDbService.countWords(searchFilter, searchDatasetsRestriction, resultLang);
		}
		boolean resultExist = meaningCount > 0;
		MeaningsResult meaningsResult = new MeaningsResult();
		meaningsResult.setMeaningCount(meaningCount);
		meaningsResult.setWordCount(wordCount);
		meaningsResult.setTermMeanings(termMeanings);
		meaningsResult.setResultExist(resultExist);

		boolean showPaging = meaningCount > MAX_RESULTS_LIMIT;
		meaningsResult.setShowPaging(showPaging);
		if (showPaging) {
			setPagingData(offset, meaningCount, meaningsResult);
		}

		return meaningsResult;
	}

	@Transactional
	public String getMeaningFirstWordValue(Long meaningId, List<String> datasets) {

		SearchDatasetsRestriction searchDatasetsRestriction = composeDatasetsRestriction(datasets);
		return termSearchDbService.getMeaningFirstWord(meaningId, searchDatasetsRestriction);
	}

	@Transactional
	public Long getMeaningFirstLexemeId(Long meaningId, List<String> datasets) {

		SearchDatasetsRestriction searchDatasetsRestriction = composeDatasetsRestriction(datasets);
		return termSearchDbService.getMeaningFirstLexemeId(meaningId, searchDatasetsRestriction);
	}

	@Transactional
	public Meaning getMeaning(Long meaningId, List<String> selectedDatasetCodes, List<ClassifierSelect> languagesOrder) {

		final String[] excludeMeaningAttributeTypes = new String[] {FreeformType.LEARNER_COMMENT.name(), FreeformType.PUBLIC_NOTE.name()};
		final String[] excludeLexemeAttributeTypes = new String[] {FreeformType.GOVERNMENT.name(), FreeformType.GRAMMAR.name(), FreeformType.USAGE.name(), FreeformType.PUBLIC_NOTE.name()};

		final String classifierLabelLang = "est";
		final String classifierLabelTypeDescrip = "descrip";
		final String classifierLabelTypeFull = "full";
		SearchDatasetsRestriction searchDatasetsRestriction = composeDatasetsRestriction(selectedDatasetCodes);
		Map<String, String> datasetNameMap = commonDataDbService.getDatasetNameMap();

		Meaning meaning = termSearchDbService.getMeaning(meaningId, searchDatasetsRestriction);
		List<DefinitionRefTuple> definitionRefTuples =
				commonDataDbService.getMeaningDefinitionRefTuples(meaningId, classifierLabelLang, classifierLabelTypeDescrip);

		List<Definition> definitions = conversionUtil.composeMeaningDefinitions(definitionRefTuples);
		for (Definition definition : definitions) {
			Long definitionId = definition.getId();
			List<NoteSourceTuple> definitionPublicNoteSourceTuples = commonDataDbService.getDefinitionPublicNoteSourceTuples(definitionId);
			List<Note> definitionPublicNotes = conversionUtil.composeNotes(definitionPublicNoteSourceTuples);
			definition.setPublicNotes(definitionPublicNotes);
		}

		List<DefinitionLangGroup> definitionLangGroups = conversionUtil.composeMeaningDefinitionLangGroups(definitions, languagesOrder);
		List<OrderedClassifier> domains = commonDataDbService.getMeaningDomains(meaningId);
		domains = conversionUtil.removeOrderedClassifierDuplicates(domains);
		List<FreeForm> meaningFreeforms = commonDataDbService.getMeaningFreeforms(meaningId, excludeMeaningAttributeTypes);
		List<FreeForm> learnerComments = commonDataDbService.getMeaningLearnerComments(meaningId);
		List<ImageSourceTuple> imageSourceTuples = commonDataDbService.getMeaningImageSourceTuples(meaningId);
		List<Image> images = conversionUtil.composeMeaningImages(imageSourceTuples);
		List<NoteSourceTuple> meaningPublicNoteSourceTuples = commonDataDbService.getMeaningPublicNoteSourceTuples(meaningId);
		List<Note> meaningPublicNotes = conversionUtil.composeNotes(meaningPublicNoteSourceTuples);
		List<Relation> meaningRelations = commonDataDbService.getMeaningRelations(meaningId, classifierLabelLang, classifierLabelTypeDescrip);
		List<List<Relation>> groupedRelations = conversionUtil.groupRelationsById(meaningRelations);

		List<Long> lexemeIds = meaning.getLexemeIds();
		List<Lexeme> lexemes = new ArrayList<>();

		for (Long lexemeId : lexemeIds) {

			Lexeme lexeme = termSearchDbService.getLexeme(lexemeId);
			List<Classifier> wordTypes = commonDataDbService.getWordTypes(lexeme.getWordId(), classifierLabelLang, classifierLabelTypeDescrip);
			List<Classifier> lexemePos = commonDataDbService.getLexemePos(lexemeId, classifierLabelLang, classifierLabelTypeDescrip);
			List<Classifier> lexemeDerivs = commonDataDbService.getLexemeDerivs(lexemeId, classifierLabelLang, classifierLabelTypeDescrip);
			List<Classifier> lexemeRegisters = commonDataDbService.getLexemeRegisters(lexemeId, classifierLabelLang, classifierLabelTypeDescrip);
			List<Classifier> lexemeRegions = commonDataDbService.getLexemeRegions(lexemeId);
			List<FreeForm> lexemeFreeforms = commonDataDbService.getLexemeFreeforms(lexemeId, excludeLexemeAttributeTypes);
			List<UsageTranslationDefinitionTuple> usageTranslationDefinitionTuples =
					commonDataDbService.getLexemeUsageTranslationDefinitionTuples(lexemeId, classifierLabelLang, classifierLabelTypeDescrip);
			List<Usage> usages = conversionUtil.composeUsages(usageTranslationDefinitionTuples);
			List<NoteSourceTuple> lexemePublicNoteSourceTuples = commonDataDbService.getLexemePublicNoteSourceTuples(lexemeId);
			List<Note> lexemePublicNotes = conversionUtil.composeNotes(lexemePublicNoteSourceTuples);
			List<FreeForm> lexemeGrammars = commonDataDbService.getLexemeGrammars(lexemeId);
			List<SourceLink> lexemeRefLinks = commonDataDbService.getLexemeSourceLinks(lexemeId);
			List<Relation> lexemeRelations = commonDataDbService.getLexemeRelations(lexemeId, classifierLabelLang, classifierLabelTypeFull);

			boolean classifiersExist =
					StringUtils.isNotBlank(lexeme.getWordGenderCode())
					|| StringUtils.isNotBlank(lexeme.getLexemeValueStateCode())
					|| StringUtils.isNotBlank(lexeme.getLexemeProcessStateCode())
					|| StringUtils.isNotBlank(lexeme.getLexemeFrequencyGroupCode())
					|| CollectionUtils.isNotEmpty(wordTypes)
					|| CollectionUtils.isNotEmpty(lexemePos)
					|| CollectionUtils.isNotEmpty(lexemeDerivs)
					|| CollectionUtils.isNotEmpty(lexemeRegisters)
					|| CollectionUtils.isNotEmpty(lexemeGrammars);

			String dataset = lexeme.getDataset();
			dataset = datasetNameMap.get(dataset);
			String levels = composeLevels(lexeme);
			boolean isAffixoid = isAffixoid(wordTypes);

			lexeme.setLevels(levels);
			lexeme.setDataset(dataset);
			lexeme.setWordTypes(wordTypes);
			lexeme.setAffixoid(isAffixoid);
			lexeme.setPos(lexemePos);
			lexeme.setDerivs(lexemeDerivs);
			lexeme.setRegisters(lexemeRegisters);
			lexeme.setRegions(lexemeRegions);
			lexeme.setFreeforms(lexemeFreeforms);
			lexeme.setPublicNotes(lexemePublicNotes);
			lexeme.setUsages(usages);
			lexeme.setGrammars(lexemeGrammars);
			lexeme.setClassifiersExist(classifiersExist);
			lexeme.setSourceLinks(lexemeRefLinks);
			lexeme.setLexemeRelations(lexemeRelations);
			lexemes.add(lexeme);
		}

		List<LexemeLangGroup> lexemeLangGroups = conversionUtil.composeLexemeLangGroups(lexemes, languagesOrder);

		boolean contentExists =
				CollectionUtils.isNotEmpty(definitions)
				|| CollectionUtils.isNotEmpty(domains)
				|| CollectionUtils.isNotEmpty(meaningFreeforms)
				|| CollectionUtils.isNotEmpty(meaningRelations)
				;

		meaning.setDefinitionLangGroups(definitionLangGroups);
		meaning.setDomains(domains);
		meaning.setFreeforms(meaningFreeforms);
		meaning.setLearnerComments(learnerComments);
		meaning.setImages(images);
		meaning.setPublicNotes(meaningPublicNotes);
		meaning.setLexemeLangGroups(lexemeLangGroups);
		meaning.setRelations(meaningRelations);
		meaning.setContentExists(contentExists);
		meaning.setGroupedRelations(groupedRelations);

		return meaning;
	}

	@Transactional
	public Map<String, Integer[]> getMeaningsWordsWithMultipleHomonymNumbers(List<Long> meaningIds) {
		return termSearchDbService.getMeaningsWordsWithMultipleHomonymNumbers(meaningIds);
	}

	private String composeLevels(Lexeme lexeme) {

		Integer level1 = lexeme.getLevel1();
		Integer level2 = lexeme.getLevel2();
		Integer level3 = lexeme.getLevel3();
		String levels = null;
		if (level1 > 0) {
			levels = String.valueOf(level1);
			if (level2 > 0) {
				levels += "." + level2;
			}
			if (level3 > 0) {
				levels += "." + level3;
			}
		}
		return levels;
	}

	private void setPagingData(int offset, int meaningCount, MeaningsResult meaningsResult) {

		int currentPage = offset / MAX_RESULTS_LIMIT + 1;
		int totalPages = (meaningCount + MAX_RESULTS_LIMIT - 1) / MAX_RESULTS_LIMIT;
		boolean previousPageExists = currentPage > 1;
		boolean nextPageExists = currentPage < totalPages;

		meaningsResult.setCurrentPage(currentPage);
		meaningsResult.setTotalPages(totalPages);
		meaningsResult.setPreviousPageExists(previousPageExists);
		meaningsResult.setNextPageExists(nextPageExists);
	}

	private boolean isAffixoid(List<Classifier> wordTypes) {

		boolean isAffixoid = wordTypes.stream()
				.anyMatch(type -> type.getCode().equals(WORD_TYPE_CODE_PREFIXOID) || type.getCode().equals(WORD_TYPE_CODE_SUFFIXOID));
		return isAffixoid;
	}
}
