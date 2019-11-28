package eki.ekilex.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eki.common.constant.DbConstant;
import eki.common.constant.LexemeType;
import eki.common.constant.RelationStatus;
import eki.common.service.util.LexemeLevelPreseUtil;
import eki.ekilex.data.Classifier;
import eki.ekilex.data.Definition;
import eki.ekilex.data.DefinitionRefTuple;
import eki.ekilex.data.LexemeData;
import eki.ekilex.data.SearchDatasetsRestriction;
import eki.ekilex.data.SynMeaningWord;
import eki.ekilex.data.SynRelation;
import eki.ekilex.data.SynRelationParamTuple;
import eki.ekilex.data.Usage;
import eki.ekilex.data.UsageTranslationDefinitionTuple;
import eki.ekilex.data.WordSynDetails;
import eki.ekilex.data.WordSynLexeme;
import eki.ekilex.service.db.CudDbService;
import eki.ekilex.service.db.ProcessDbService;

@Component
public class SynSearchService extends AbstractWordSearchService {

	private static final String RAW_RELATION_CODE = "raw";

	@Autowired
	private SynSearchDbService synSearchDbService;

	@Autowired
	private ProcessDbService processDbService;

	@Autowired
	private CudDbService cudDbService;

	@Autowired
	private LexemeLevelPreseUtil lexemeLevelPreseUtil;

	@Transactional
	public WordSynDetails getWordSynDetails(Long wordId, String datasetCode) {

		List<String> datasetCodeList = new ArrayList<>(Collections.singletonList(datasetCode));
		SearchDatasetsRestriction searchDatasetsRestriction = composeDatasetsRestriction(datasetCodeList);
		WordSynDetails wordDetails = synSearchDbService.getWordDetails(wordId);
		List<LexemeData> lexemeDatas = processDbService.getLexemeDatas(wordId, datasetCode);
		boolean isSynLayerComplete = lexemeDatas.stream().allMatch(lexemeData -> StringUtils.equals(DbConstant.PROCESS_STATE_COMPLETE, lexemeData.getSynLayerProcessStateCode()));

		List<WordSynLexeme> synLexemes = synSearchDbService.getWordPrimarySynonymLexemes(wordId, searchDatasetsRestriction);
		synLexemes.forEach(lexeme -> populateSynLexeme(lexeme, wordDetails.getLanguage()));
		lexemeLevelPreseUtil.combineLevels(synLexemes); //TODO check is this necessary ?

		List<SynRelationParamTuple> relationTuples =
				synSearchDbService.getWordSynRelations(wordId, RAW_RELATION_CODE, datasetCode, classifierLabelLang, classifierLabelTypeDescrip);
		List<SynRelation> relations = conversionUtil.composeSynRelations(relationTuples);

		wordDetails.setLexemes(synLexemes);
		wordDetails.setRelations(relations);
		wordDetails.setSynLayerComplete(isSynLayerComplete);

		return wordDetails;
	}

	private void populateSynLexeme(WordSynLexeme lexeme, String language) {

		Long lexemeId = lexeme.getLexemeId();
		Long meaningId = lexeme.getMeaningId();
		String datasetCode = lexeme.getDatasetCode();

		List<SynMeaningWord> meaningWords = synSearchDbService.getSynMeaningWords(lexemeId, language);
		List<Classifier> lexemePos = commonDataDbService.getLexemePos(lexemeId, classifierLabelLang, classifierLabelTypeDescrip);
		List<DefinitionRefTuple> definitionRefTuples =
				commonDataDbService.getMeaningDefinitionRefTuples(meaningId, datasetCode, classifierLabelLang, classifierLabelTypeDescrip);
		List<Definition> definitions = conversionUtil.composeMeaningDefinitions(definitionRefTuples);

		List<UsageTranslationDefinitionTuple> usageTranslationDefinitionTuples =
				commonDataDbService.getLexemeUsageTranslationDefinitionTuples(lexemeId, classifierLabelLang, classifierLabelTypeDescrip);
		List<Usage> usages = conversionUtil.composeUsages(usageTranslationDefinitionTuples);

		lexeme.setPos(lexemePos);
		lexeme.setMeaningWords(meaningWords);
		lexeme.setDefinitions(definitions);
		lexeme.setUsages(usages);

	}

	@Transactional
	public void changeRelationStatus(Long id, String status) {
		synSearchDbService.changeRelationStatus(id, status);
		if (RelationStatus.DELETED.name().equals(status)) {
			moveChangedRelationToLast(id);
		}
	}

	@Transactional
	public void createSecondarySynLexeme(Long meaningId, Long wordId, String datasetCode, Long existingLexemeId, Long relationId) {
		synSearchDbService.createLexeme(wordId, meaningId, datasetCode, LexemeType.SECONDARY, existingLexemeId);
		synSearchDbService.changeRelationStatus(relationId, RelationStatus.PROCESSED.name());
	}

	private void moveChangedRelationToLast(Long relationId) {
		List<SynRelation> existingRelations = synSearchDbService.getExistingFollowingRelationsForWord(relationId, RAW_RELATION_CODE);

		if (existingRelations.size() > 1) {
			SynRelation lastRelation = existingRelations.get(existingRelations.size() - 1);
			List<Long> existingOrderByValues = existingRelations.stream().map(SynRelation::getOrderBy).collect(Collectors.toList());

			cudDbService.updateWordRelationOrderBy(relationId, lastRelation.getOrderBy());
			existingRelations.remove(0);

			existingOrderByValues.remove(existingOrderByValues.size() - 1);

			int relIdx = 0;
			for (SynRelation relation : existingRelations) {
				cudDbService.updateWordRelationOrderBy(relation.getId(), existingOrderByValues.get(relIdx));
				relIdx++;
			}
		}
	}

}
