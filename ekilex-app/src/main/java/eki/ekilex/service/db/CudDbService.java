package eki.ekilex.service.db;

import static eki.ekilex.data.db.Tables.DEFINITION;
import static eki.ekilex.data.db.Tables.DEFINITION_DATASET;
import static eki.ekilex.data.db.Tables.DEFINITION_FREEFORM;
import static eki.ekilex.data.db.Tables.DEFINITION_SOURCE_LINK;
import static eki.ekilex.data.db.Tables.FORM;
import static eki.ekilex.data.db.Tables.FREEFORM;
import static eki.ekilex.data.db.Tables.FREEFORM_SOURCE_LINK;
import static eki.ekilex.data.db.Tables.LEXEME;
import static eki.ekilex.data.db.Tables.LEXEME_DERIV;
import static eki.ekilex.data.db.Tables.LEXEME_FREEFORM;
import static eki.ekilex.data.db.Tables.LEXEME_LIFECYCLE_LOG;
import static eki.ekilex.data.db.Tables.LEXEME_POS;
import static eki.ekilex.data.db.Tables.LEXEME_PROCESS_LOG;
import static eki.ekilex.data.db.Tables.LEXEME_REGION;
import static eki.ekilex.data.db.Tables.LEXEME_REGISTER;
import static eki.ekilex.data.db.Tables.LEXEME_SOURCE_LINK;
import static eki.ekilex.data.db.Tables.LEX_RELATION;
import static eki.ekilex.data.db.Tables.LIFECYCLE_LOG;
import static eki.ekilex.data.db.Tables.MEANING;
import static eki.ekilex.data.db.Tables.MEANING_DOMAIN;
import static eki.ekilex.data.db.Tables.MEANING_FREEFORM;
import static eki.ekilex.data.db.Tables.MEANING_LIFECYCLE_LOG;
import static eki.ekilex.data.db.Tables.MEANING_PROCESS_LOG;
import static eki.ekilex.data.db.Tables.MEANING_RELATION;
import static eki.ekilex.data.db.Tables.MEANING_SEMANTIC_TYPE;
import static eki.ekilex.data.db.Tables.PARADIGM;
import static eki.ekilex.data.db.Tables.PROCESS_LOG;
import static eki.ekilex.data.db.Tables.WORD;
import static eki.ekilex.data.db.Tables.WORD_ETYMOLOGY;
import static eki.ekilex.data.db.Tables.WORD_FREEFORM;
import static eki.ekilex.data.db.Tables.WORD_GROUP;
import static eki.ekilex.data.db.Tables.WORD_GROUP_MEMBER;
import static eki.ekilex.data.db.Tables.WORD_LIFECYCLE_LOG;
import static eki.ekilex.data.db.Tables.WORD_PROCESS_LOG;
import static eki.ekilex.data.db.Tables.WORD_RELATION;
import static eki.ekilex.data.db.Tables.WORD_RELATION_PARAM;
import static eki.ekilex.data.db.Tables.WORD_WORD_TYPE;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import eki.common.constant.Complexity;
import eki.common.constant.DbConstant;
import eki.common.constant.FormMode;
import eki.common.constant.FreeformType;
import eki.common.constant.ReferenceType;
import eki.ekilex.data.Classifier;
import eki.ekilex.data.ListData;
import eki.ekilex.data.SynRelation;
import eki.ekilex.data.WordLexeme;
import eki.ekilex.data.db.tables.Lexeme;
import eki.ekilex.data.db.tables.records.DefinitionFreeformRecord;
import eki.ekilex.data.db.tables.records.FreeformRecord;
import eki.ekilex.data.db.tables.records.LexRelationRecord;
import eki.ekilex.data.db.tables.records.LexemeDerivRecord;
import eki.ekilex.data.db.tables.records.LexemeFreeformRecord;
import eki.ekilex.data.db.tables.records.LexemePosRecord;
import eki.ekilex.data.db.tables.records.LexemeRecord;
import eki.ekilex.data.db.tables.records.LexemeRegionRecord;
import eki.ekilex.data.db.tables.records.LexemeRegisterRecord;
import eki.ekilex.data.db.tables.records.MeaningDomainRecord;
import eki.ekilex.data.db.tables.records.MeaningFreeformRecord;
import eki.ekilex.data.db.tables.records.MeaningRecord;
import eki.ekilex.data.db.tables.records.MeaningRelationRecord;
import eki.ekilex.data.db.tables.records.MeaningSemanticTypeRecord;
import eki.ekilex.data.db.tables.records.WordFreeformRecord;
import eki.ekilex.data.db.tables.records.WordGroupMemberRecord;
import eki.ekilex.data.db.tables.records.WordGroupRecord;
import eki.ekilex.data.db.tables.records.WordRelationRecord;
import eki.ekilex.data.db.tables.records.WordWordTypeRecord;

@Component
public class CudDbService implements DbConstant {

	private DSLContext create;

	public CudDbService(DSLContext context) {
		create = context;
	}

	public Long getWordWordTypeId(Long wordId, String typeCode) {
		WordWordTypeRecord wordWordTypeRecord = create.fetchOne(WORD_WORD_TYPE, WORD_WORD_TYPE.WORD_ID.eq(wordId).and(WORD_WORD_TYPE.WORD_TYPE_CODE.eq(typeCode)));
		return wordWordTypeRecord.getId();
	}

	public List<WordLexeme> getWordPrimaryLexemes(Long lexemeId) {
		Lexeme l1 = LEXEME.as("l1");
		Lexeme l2 = LEXEME.as("l2");
		return create
				.select(
						l2.ID.as("lexeme_id"),
						l2.LEVEL1,
						l2.LEVEL2)
				.from(l2, l1)
				.where(
						l1.ID.eq(lexemeId)
								.and(l1.WORD_ID.eq(l2.WORD_ID))
								.and(l1.DATASET_CODE.eq(l2.DATASET_CODE))
								.and(l2.TYPE.eq(LEXEME_TYPE_PRIMARY)))
				.orderBy(l2.LEVEL1, l2.LEVEL2)
				.fetchInto(WordLexeme.class);
	}

	public Long getWordRelationGroupId(String groupType, Long wordId) {
		Long id = create
				.select(WORD_GROUP.ID)
				.from(WORD_GROUP.join(WORD_GROUP_MEMBER).on(WORD_GROUP_MEMBER.WORD_GROUP_ID.eq(WORD_GROUP.ID)))
				.where(WORD_GROUP.WORD_REL_TYPE_CODE.eq(groupType).and(WORD_GROUP_MEMBER.WORD_ID.eq(wordId)))
				.fetchOneInto(Long.class);
		return id;
	}

	public Long getWordRelationGroupId(Long relationId) {
		Long id = create.select(WORD_GROUP.ID)
				.from(WORD_GROUP.join(WORD_GROUP_MEMBER).on(WORD_GROUP_MEMBER.WORD_GROUP_ID.eq(WORD_GROUP.ID)))
				.where(WORD_GROUP_MEMBER.ID.eq(relationId))
				.fetchOneInto(Long.class);
		return id;
	}

	public List<Map<String, Object>> getWordRelationGroupMembers(Long groupId) {
		return create
				.selectDistinct(
						WORD_GROUP_MEMBER.ID,
						WORD_GROUP_MEMBER.WORD_ID,
						FORM.VALUE,
						WORD_GROUP.WORD_REL_TYPE_CODE)
				.from(WORD_GROUP_MEMBER)
				.join(WORD_GROUP).on(WORD_GROUP.ID.eq(WORD_GROUP_MEMBER.WORD_GROUP_ID))
				.join(PARADIGM).on(PARADIGM.WORD_ID.eq(WORD_GROUP_MEMBER.WORD_ID))
				.join(FORM).on(FORM.PARADIGM_ID.eq(PARADIGM.ID))
				.where(WORD_GROUP_MEMBER.WORD_GROUP_ID.eq(groupId)
						.and(FORM.MODE.eq("WORD")))
				.fetchMaps();
	}

	public boolean isMemberOfWordRelationGroup(Long groupId, Long wordId) {
		Long id = create.select(WORD_GROUP.ID)
				.from(WORD_GROUP.join(WORD_GROUP_MEMBER).on(WORD_GROUP_MEMBER.WORD_GROUP_ID.eq(WORD_GROUP.ID)))
				.where(WORD_GROUP.ID.eq(groupId).and(WORD_GROUP_MEMBER.WORD_ID.eq(wordId)))
				.fetchOneInto(Long.class);
		boolean exists = (id != null);
		return exists;
	}

	//TODO unused method?
	public LexemeRecord getLexeme(Long lexemeId) {
		return create.fetchOne(LEXEME, LEXEME.ID.eq(lexemeId));
	}

	public Long getLexemePosId(Long lexemeId, String posCode) {
		LexemePosRecord lexemePosRecord = create.fetchOne(LEXEME_POS, LEXEME_POS.LEXEME_ID.eq(lexemeId).and(LEXEME_POS.POS_CODE.eq(posCode)));
		return lexemePosRecord.getId();
	}

	public Long getLexemeDerivId(Long lexemeId, String derivCode) {
		LexemeDerivRecord lexemeDerivRecord = create.fetchOne(LEXEME_DERIV, LEXEME_DERIV.LEXEME_ID.eq(lexemeId).and(LEXEME_DERIV.DERIV_CODE.eq(derivCode)));
		return lexemeDerivRecord.getId();
	}

	public Long getLexemeRegisterId(Long lexemeId, String registerCode) {
		LexemeRegisterRecord lexemeRegisterRecord = create.fetchOne(LEXEME_REGISTER,
				LEXEME_REGISTER.LEXEME_ID.eq(lexemeId).and(LEXEME_REGISTER.REGISTER_CODE.eq(registerCode)));
		return lexemeRegisterRecord.getId();
	}

	public Long getLexemeRegionId(Long lexemeId, String regionCode) {
		LexemeRegionRecord lexemeRegionRecord = create.fetchOne(LEXEME_REGION,
				LEXEME_REGION.LEXEME_ID.eq(lexemeId).and(LEXEME_REGION.REGION_CODE.eq(regionCode)));
		return lexemeRegionRecord.getId();
	}

	public Long getMeaningDomainId(Long meaningId, Classifier domain) {
		MeaningDomainRecord meaningDomainRecord = create.fetchOne(MEANING_DOMAIN,
				MEANING_DOMAIN.MEANING_ID.eq(meaningId)
						.and(MEANING_DOMAIN.DOMAIN_ORIGIN.eq(domain.getOrigin()))
						.and(MEANING_DOMAIN.DOMAIN_CODE.eq(domain.getCode())));
		return meaningDomainRecord.getId();
	}

	public Long getMeaningSemanticTypeId(Long meaningId, String semanticTypeCode) {
		MeaningSemanticTypeRecord meaningSemanticTypeRecord = create
				.fetchOne(MEANING_SEMANTIC_TYPE, MEANING_SEMANTIC_TYPE.MEANING_ID.eq(meaningId).and(MEANING_SEMANTIC_TYPE.SEMANTIC_TYPE_CODE.eq(semanticTypeCode)));
		return meaningSemanticTypeRecord.getId();
	}

	public String getImageTitle(Long imageId) {
		return create
				.select(FREEFORM.VALUE_TEXT)
				.from(FREEFORM)
				.where(FREEFORM.PARENT_ID.eq(imageId)
						.and(FREEFORM.TYPE.eq(FreeformType.IMAGE_TITLE.name())))
				.fetchOneInto(String.class);
	}

	public List<SynRelation> getWordRelations(Long wordId, String relTypeCode) {
		return create.select(WORD_RELATION.ID, WORD_RELATION.ORDER_BY)
				.from(WORD_RELATION)
				.where(WORD_RELATION.WORD1_ID.eq(wordId))
				.and(WORD_RELATION.WORD_REL_TYPE_CODE.eq(relTypeCode))
				.orderBy(WORD_RELATION.ORDER_BY)
				.fetchInto(SynRelation.class);
	}

	public void updateFreeformTextValue(Long id, String value, String valuePrese) {
		create.update(FREEFORM)
				.set(FREEFORM.VALUE_TEXT, value)
				.set(FREEFORM.VALUE_PRESE, valuePrese)
				.where(FREEFORM.ID.eq(id))
				.execute();
	}

	public void updateFreeformTextValueAndComplexity(Long id, String value, String valuePrese, Complexity complexity) {
		create.update(FREEFORM)
				.set(FREEFORM.VALUE_TEXT, value)
				.set(FREEFORM.VALUE_PRESE, valuePrese)
				.set(FREEFORM.COMPLEXITY, complexity.name())
				.where(FREEFORM.ID.eq(id))
				.execute();
	}

	public void updateDefinition(Long id, String value, String valuePrese, Complexity complexity, String typeCode) {
		create.update(DEFINITION)
				.set(DEFINITION.VALUE, value)
				.set(DEFINITION.VALUE_PRESE, valuePrese)
				.set(DEFINITION.COMPLEXITY, complexity.name())
				.set(DEFINITION.DEFINITION_TYPE_CODE, typeCode)
				.where(DEFINITION.ID.eq(id))
				.execute();
	}

	public void updateDefinitionOrderby(ListData item) {
		create
				.update(DEFINITION)
				.set(DEFINITION.ORDER_BY, item.getOrderby())
				.where(DEFINITION.ID.eq(item.getId()))
				.execute();
	}

	public void updateLexemeRelationOrderby(ListData item) {
		create
				.update(LEX_RELATION)
				.set(LEX_RELATION.ORDER_BY, item.getOrderby())
				.where(LEX_RELATION.ID.eq(item.getId()))
				.execute();
	}

	public void updateMeaningRelationOrderby(ListData item) {
		create
				.update(MEANING_RELATION)
				.set(MEANING_RELATION.ORDER_BY, item.getOrderby())
				.where(MEANING_RELATION.ID.eq(item.getId()))
				.execute();
	}

	public void updateWordRelationOrderby(ListData item) {
		create
				.update(WORD_RELATION)
				.set(WORD_RELATION.ORDER_BY, item.getOrderby())
				.where(WORD_RELATION.ID.eq(item.getId()))
				.execute();
	}

	public void updateWordEtymologyOrderby(ListData item) {
		create
				.update(WORD_ETYMOLOGY)
				.set(WORD_ETYMOLOGY.ORDER_BY, item.getOrderby())
				.where(WORD_ETYMOLOGY.ID.eq(item.getId()))
				.execute();
	}

	public void updateLexemeOrderby(ListData item) {
		create
				.update(LEXEME)
				.set(LEXEME.ORDER_BY, item.getOrderby())
				.where(LEXEME.ID.eq(item.getId()))
				.execute();
	}
	public void updateMeaningDomainOrderby(ListData item) {
		create
				.update(MEANING_DOMAIN)
				.set(MEANING_DOMAIN.ORDER_BY, item.getOrderby())
				.where(MEANING_DOMAIN.ID.eq(item.getId()))
				.execute();
	}

	public void updateLexemeLevels(Long lexemeId, Integer level1, Integer level2) {
		create.update(LEXEME)
				.set(LEXEME.LEVEL1, level1)
				.set(LEXEME.LEVEL2, level2)
				.where(LEXEME.ID.eq(lexemeId))
				.execute();
	}

	public void updateLexemeLevel1(Long lexemeId, Integer level1) {
		create.update(LEXEME)
				.set(LEXEME.LEVEL1, level1)
				.where(LEXEME.ID.eq(lexemeId))
				.execute();
	}

	public void updateLexemeLevel2(Long lexemeId, Integer level2) {
		create.update(LEXEME)
				.set(LEXEME.LEVEL2, level2)
				.where(LEXEME.ID.eq(lexemeId))
				.execute();
	}

	public void updateLexemeFrequencyGroup(Long lexemeId, String frequencyGroupCode) {
		create.update(LEXEME)
				.set(LEXEME.FREQUENCY_GROUP_CODE, frequencyGroupCode)
				.where(LEXEME.ID.eq(lexemeId))
				.execute();
	}

	public void updateLexemeValueState(Long lexemeId, String valueStateCode) {
		create.update(LEXEME)
				.set(LEXEME.VALUE_STATE_CODE, valueStateCode)
				.where(LEXEME.ID.eq(lexemeId))
				.execute();
	}

	public void updateWordValue(Long wordId, String value, String valuePrese) {
		create.update(FORM)
				.set(FORM.VALUE, value)
				.set(FORM.VALUE_PRESE, valuePrese)
				.from(PARADIGM)
				.where(PARADIGM.WORD_ID.eq(wordId)
						.and(FORM.PARADIGM_ID.eq(PARADIGM.ID))
						.and(FORM.MODE.eq(FormMode.WORD.name())))
				.execute();
	}

	public void updateWordVocalForm(Long wordId, String vocalForm) {
		create.update(FORM)
				.set(FORM.VOCAL_FORM, vocalForm)
				.from(PARADIGM)
				.where(PARADIGM.WORD_ID.eq(wordId)
						.and(FORM.PARADIGM_ID.eq(PARADIGM.ID))
						.and(FORM.MODE.eq(FormMode.WORD.name())))
				.execute();
	}

	public void updateWordGender(Long wordId, String genderCode) {
		create.update(WORD)
				.set(WORD.GENDER_CODE, genderCode)
				.where(WORD.ID.eq(wordId))
				.execute();
	}

	public Long updateWordType(Long wordId, String currentTypeCode, String newTypeCode) {
		Long wordWordTypeId = create
				.update(WORD_WORD_TYPE)
				.set(WORD_WORD_TYPE.WORD_TYPE_CODE, newTypeCode)
				.where(WORD_WORD_TYPE.WORD_ID.eq(wordId).and(WORD_WORD_TYPE.WORD_TYPE_CODE.eq(currentTypeCode)))
				.returning(WORD_WORD_TYPE.ID)
				.fetchOne()
				.getId();
		return wordWordTypeId;
	}

	public void updateWordAspect(Long wordId, String aspectCode) {
		create.update(WORD)
				.set(WORD.ASPECT_CODE, aspectCode)
				.where(WORD.ID.eq(wordId))
				.execute();
	}

	public void updateWordLang(Long wordId, String langCode) {
		create.update(WORD)
				.set(WORD.LANG, langCode)
				.where(WORD.ID.eq(wordId))
				.execute();
	}

	public void updateLexemeComplexity(Long lexemeId, String complexity) {
		create.update(LEXEME)
				.set(LEXEME.COMPLEXITY, complexity)
				.where(LEXEME.ID.eq(lexemeId))
				.execute();
	}

	public void updateLexemeDataset(Long lexemeId, String dataset) {
		create.update(LEXEME)
				.set(LEXEME.DATASET_CODE, dataset)
				.where(LEXEME.ID.eq(lexemeId))
				.execute();
	}

	public Long updateLexemePos(Long lexemeId, String currentPos, String newPos) {
		Long lexemePosId = create
				.update(LEXEME_POS)
				.set(LEXEME_POS.POS_CODE, newPos)
				.where(LEXEME_POS.LEXEME_ID.eq(lexemeId).and(LEXEME_POS.POS_CODE.eq(currentPos)))
				.returning(LEXEME_POS.ID)
				.fetchOne()
				.getId();
		return lexemePosId;
	}

	public Long updateLexemeDeriv(Long lexemeId, String currentDeriv, String newDeriv) {
		Long lexemeDerivId = create
				.update(LEXEME_DERIV)
				.set(LEXEME_DERIV.DERIV_CODE, newDeriv)
				.where(LEXEME_DERIV.LEXEME_ID.eq(lexemeId).and(LEXEME_DERIV.DERIV_CODE.eq(currentDeriv)))
				.returning(LEXEME_DERIV.ID)
				.fetchOne()
				.getId();
		return lexemeDerivId;
	}

	public Long updateLexemeRegister(Long lexemeId, String currentRegister, String newRegister) {
		Long lexemeRegisterId = create
				.update(LEXEME_REGISTER)
				.set(LEXEME_REGISTER.REGISTER_CODE, newRegister)
				.where(LEXEME_REGISTER.LEXEME_ID.eq(lexemeId).and(LEXEME_REGISTER.REGISTER_CODE.eq(currentRegister)))
				.returning(LEXEME_REGISTER.ID)
				.fetchOne()
				.getId();
		return lexemeRegisterId;
	}

	public Long updateLexemeRegion(Long lexemeId, String currentRegion, String newRegion) {
		Long lexemeRegionId = create
				.update(LEXEME_REGION)
				.set(LEXEME_REGION.REGION_CODE, newRegion)
				.where(LEXEME_REGION.LEXEME_ID.eq(lexemeId).and(LEXEME_REGION.REGION_CODE.eq(currentRegion)))
				.returning(LEXEME_REGION.ID)
				.fetchOne()
				.getId();
		return lexemeRegionId;
	}

	public Long updateMeaningDomain(Long meaningId, Classifier currentDomain, Classifier newDomain) {
		Long meaningDomainId = create
				.update(MEANING_DOMAIN)
				.set(MEANING_DOMAIN.DOMAIN_CODE, newDomain.getCode())
				.set(MEANING_DOMAIN.DOMAIN_ORIGIN, newDomain.getOrigin())
				.where(
						MEANING_DOMAIN.MEANING_ID.eq(meaningId).and(
								MEANING_DOMAIN.DOMAIN_CODE.eq(currentDomain.getCode())).and(
										MEANING_DOMAIN.DOMAIN_ORIGIN.eq(currentDomain.getOrigin())))
				.returning(MEANING_DOMAIN.ID)
				.fetchOne()
				.getId();
		return meaningDomainId;
	}

	public Long updateMeaningSemanticType(Long meaningId, String currentSemanticType, String newSemanticType) {
		Long meaningSemanticTypeId = create
				.update(MEANING_SEMANTIC_TYPE)
				.set(MEANING_SEMANTIC_TYPE.SEMANTIC_TYPE_CODE, newSemanticType)
				.where(MEANING_SEMANTIC_TYPE.MEANING_ID.eq(meaningId).and(MEANING_SEMANTIC_TYPE.SEMANTIC_TYPE_CODE.eq(currentSemanticType)))
				.returning(MEANING_SEMANTIC_TYPE.ID)
				.fetchOne()
				.getId();
		return meaningSemanticTypeId;
	}

	public void updateImageTitle(Long imageFreeformId, String title) {
		create
				.update(FREEFORM)
				.set(FREEFORM.VALUE_TEXT, title)
				.set(FREEFORM.VALUE_PRESE, title)
				.where(FREEFORM.PARENT_ID.eq(imageFreeformId)
						.and(FREEFORM.TYPE.eq(FreeformType.IMAGE_TITLE.name())))
				.execute();
	}

	public void updateWordRelationOrderBy(Long relationId, Long orderBy) {
		create
				.update(WORD_RELATION)
				.set(WORD_RELATION.ORDER_BY, orderBy)
				.where(WORD_RELATION.ID.eq(relationId))
				.execute();
	}

	public void updateLexemeWeight(Long lexemeId, BigDecimal lexemeWeight) {
		create
				.update(LEXEME)
				.set(LEXEME.WEIGHT, lexemeWeight)
				.where(LEXEME.ID.eq(lexemeId))
				.execute();
	}

	public Long createWordAndLexeme(String value, String valuePrese, String datasetCode, String language, String morphCode, Long meaningId) {
		Integer currentHomonymNumber = create
				.select(DSL.max(WORD.HOMONYM_NR))
				.from(WORD, PARADIGM, FORM)
				.where(
						WORD.LANG.eq(language)
								.and(FORM.MODE.eq(FormMode.WORD.name()))
								.and(FORM.VALUE.eq(value))
								.and(PARADIGM.ID.eq(FORM.PARADIGM_ID))
								.and(PARADIGM.WORD_ID.eq(WORD.ID)))
				.fetchOneInto(Integer.class);
		int homonymNumber = 1;
		if (currentHomonymNumber != null) {
			homonymNumber = currentHomonymNumber + 1;
		}
		Long wordId = create.insertInto(WORD, WORD.HOMONYM_NR, WORD.LANG).values(homonymNumber, language).returning(WORD.ID).fetchOne().getId();
		Long paradigmId = create.insertInto(PARADIGM, PARADIGM.WORD_ID).values(wordId).returning(PARADIGM.ID).fetchOne().getId();
		create
				.insertInto(FORM, FORM.PARADIGM_ID, FORM.VALUE, FORM.DISPLAY_FORM, FORM.VALUE_PRESE, FORM.MODE, FORM.MORPH_CODE, FORM.MORPH_EXISTS)
				.values(paradigmId, value, value, valuePrese, FormMode.WORD.name(), morphCode, true)
				.execute();
		if (meaningId == null) {
			meaningId = create.insertInto(MEANING).defaultValues().returning(MEANING.ID).fetchOne().getId();
		}
		create
				.insertInto(
						LEXEME, LEXEME.MEANING_ID, LEXEME.WORD_ID, LEXEME.DATASET_CODE, LEXEME.TYPE,
						LEXEME.LEVEL1, LEXEME.LEVEL2, LEXEME.PROCESS_STATE_CODE, LEXEME.COMPLEXITY)
				.values(meaningId, wordId, datasetCode, LEXEME_TYPE_PRIMARY,
						1, 1, PROCESS_STATE_IN_WORK, COMPLEXITY_DETAIL)
				.execute();
		return wordId;
	}

	public Long createWordType(Long wordId, String typeCode) {
		Long wordWordTypeId = create
				.select(WORD_WORD_TYPE.ID)
				.from(WORD_WORD_TYPE)
				.where(WORD_WORD_TYPE.WORD_ID.eq(wordId)
						.and(WORD_WORD_TYPE.WORD_TYPE_CODE.eq(typeCode)))
				.limit(1)
				.fetchOneInto(Long.class);
		if (wordWordTypeId == null) {
			wordWordTypeId = create
					.insertInto(WORD_WORD_TYPE, WORD_WORD_TYPE.WORD_ID, WORD_WORD_TYPE.WORD_TYPE_CODE)
					.values(wordId, typeCode)
					.returning(WORD_WORD_TYPE.ID)
					.fetchOne()
					.getId();
		}
		return wordWordTypeId;
	}

	public Long createWordRelation(Long wordId, Long targetWordId, String wordRelationCode) {
	
		Long wordRelationId = create
				.select(WORD_RELATION.ID)
				.from(WORD_RELATION)
				.where(
						WORD_RELATION.WORD1_ID.eq(wordId)
								.and(WORD_RELATION.WORD2_ID.eq(targetWordId))
								.and(WORD_RELATION.WORD_REL_TYPE_CODE.eq(wordRelationCode)))
				.limit(1)
				.fetchOneInto(Long.class);
		if (wordRelationId == null) {
			WordRelationRecord newRelation = create.newRecord(WORD_RELATION);
			newRelation.setWord1Id(wordId);
			newRelation.setWord2Id(targetWordId);
			newRelation.setWordRelTypeCode(wordRelationCode);
			newRelation.store();
			wordRelationId = newRelation.getId();
		}
		return wordRelationId;
	}

	public Long createWordRelationGroup(String groupType) {
		WordGroupRecord wordGroupRecord = create.newRecord(WORD_GROUP);
		wordGroupRecord.setWordRelTypeCode(groupType);
		wordGroupRecord.store();
		return wordGroupRecord.getId();
	}

	public Long createWordRelationGroupMember(Long groupId, Long wordId) {
		WordGroupMemberRecord wordGroupMember = create.newRecord(WORD_GROUP_MEMBER);
		wordGroupMember.setWordGroupId(groupId);
		wordGroupMember.setWordId(wordId);
		wordGroupMember.store();
		return wordGroupMember.getId();
	}

	public Long createDefinition(Long meaningId, String value, String valuePrese, String languageCode, String definitionTypeCode, Complexity complexity) {
		return create
				.insertInto(
						DEFINITION,
						DEFINITION.MEANING_ID,
						DEFINITION.LANG,
						DEFINITION.VALUE,
						DEFINITION.VALUE_PRESE,
						DEFINITION.DEFINITION_TYPE_CODE,
						DEFINITION.COMPLEXITY)
				.values(meaningId, languageCode, value, valuePrese, definitionTypeCode, complexity.name())
				.returning(DEFINITION.ID)
				.fetchOne()
				.getId();
	}

	public void createDefinitionDataset(Long definitionId, String datasetCode) {
		create.insertInto(DEFINITION_DATASET, DEFINITION_DATASET.DEFINITION_ID, DEFINITION_DATASET.DATASET_CODE)
				.values(definitionId, datasetCode)
				.execute();
	}

	public Long createDefinitionSourceLink(Long definitionId, Long sourceId, ReferenceType refType, String sourceValue, String sourceName) {
		return create
				.insertInto(
						DEFINITION_SOURCE_LINK,
						DEFINITION_SOURCE_LINK.DEFINITION_ID,
						DEFINITION_SOURCE_LINK.SOURCE_ID,
						DEFINITION_SOURCE_LINK.TYPE,
						DEFINITION_SOURCE_LINK.VALUE,
						DEFINITION_SOURCE_LINK.NAME)
				.values(definitionId, sourceId, refType.name(), sourceValue, sourceName).returning(DEFINITION_SOURCE_LINK.ID)
				.fetchOne()
				.getId();
	}

	public Long createDefinitionPublicNote(Long definitionId, String value, String valuePrese) {
		FreeformRecord freeform = create.newRecord(FREEFORM);
		freeform.setType(FreeformType.PUBLIC_NOTE.name());
		freeform.setValueText(value);
		freeform.setValuePrese(valuePrese);
		freeform.store();

		DefinitionFreeformRecord definitionFreeform = create.newRecord(DEFINITION_FREEFORM);
		definitionFreeform.setDefinitionId(definitionId);
		definitionFreeform.setFreeformId(freeform.getId());
		definitionFreeform.store();

		return freeform.getId();
	}

	public Long createFreeformSourceLink(Long freeformId, Long sourceId, ReferenceType refType, String sourceValue, String sourceName) {
		return create
				.insertInto(
						FREEFORM_SOURCE_LINK,
						FREEFORM_SOURCE_LINK.FREEFORM_ID,
						FREEFORM_SOURCE_LINK.SOURCE_ID,
						FREEFORM_SOURCE_LINK.TYPE,
						FREEFORM_SOURCE_LINK.VALUE,
						FREEFORM_SOURCE_LINK.NAME)
				.values(freeformId, sourceId, refType.name(), sourceValue, sourceName).returning(FREEFORM_SOURCE_LINK.ID)
				.fetchOne()
				.getId();
	}

	public Long createLexemeSourceLink(Long lexemeId, Long sourceId, ReferenceType refType, String sourceValue, String sourceName) {
		return create
				.insertInto(
						LEXEME_SOURCE_LINK,
						LEXEME_SOURCE_LINK.LEXEME_ID,
						LEXEME_SOURCE_LINK.SOURCE_ID,
						LEXEME_SOURCE_LINK.TYPE,
						LEXEME_SOURCE_LINK.VALUE,
						LEXEME_SOURCE_LINK.NAME)
				.values(lexemeId, sourceId, refType.name(), sourceValue, sourceName).returning(LEXEME_SOURCE_LINK.ID)
				.fetchOne()
				.getId();
	}

	public Long createLexemeRelation(Long lexemeId1, Long lexemeId2, String relationType) {
		LexRelationRecord lexemeRelation = create.newRecord(LEX_RELATION);
		lexemeRelation.setLexeme1Id(lexemeId1);
		lexemeRelation.setLexeme2Id(lexemeId2);
		lexemeRelation.setLexRelTypeCode(relationType);
		lexemeRelation.store();
		return lexemeRelation.getId();
	}

	public Long createMeaning() {
		MeaningRecord meaning = create.insertInto(MEANING).defaultValues().returning(MEANING.ID).fetchOne();
		return meaning.getId();
	}

	public Long createMeaningRelation(Long meaningId1, Long meaningId2, String relationType) {
		MeaningRelationRecord meaningRelation = create.newRecord(MEANING_RELATION);
		meaningRelation.setMeaning1Id(meaningId1);
		meaningRelation.setMeaning2Id(meaningId2);
		meaningRelation.setMeaningRelTypeCode(relationType);
		meaningRelation.store();
		return meaningRelation.getId();
	}

	public Long createMeaningLearnerComment(Long meaningId, String value, String valuePrese, String languageCode) {
		FreeformRecord freeform = create.newRecord(FREEFORM);
		freeform.setType(FreeformType.LEARNER_COMMENT.name());
		freeform.setValueText(value);
		freeform.setValuePrese(valuePrese);
		freeform.setLang(languageCode);
		freeform.store();
	
		MeaningFreeformRecord meaningFreeform = create.newRecord(MEANING_FREEFORM);
		meaningFreeform.setMeaningId(meaningId);
		meaningFreeform.setFreeformId(freeform.getId());
		meaningFreeform.store();
	
		return freeform.getId();
	}

	public Long createMeaningDomain(Long meaningId, Classifier domain) {
		Long meaningDomainId = create
				.select(MEANING_DOMAIN.ID).from(MEANING_DOMAIN)
				.where(MEANING_DOMAIN.MEANING_ID.eq(meaningId)
						.and(MEANING_DOMAIN.DOMAIN_CODE.eq(domain.getCode()))
						.and(MEANING_DOMAIN.DOMAIN_ORIGIN.eq(domain.getOrigin())))
				.limit(1)
				.fetchOneInto(Long.class);
		if (meaningDomainId == null) {
			meaningDomainId = create
					.insertInto(MEANING_DOMAIN, MEANING_DOMAIN.MEANING_ID, MEANING_DOMAIN.DOMAIN_ORIGIN, MEANING_DOMAIN.DOMAIN_CODE)
					.values(meaningId, domain.getOrigin(), domain.getCode())
					.returning(MEANING_DOMAIN.ID)
					.fetchOne()
					.getId();
		}
		return meaningDomainId;
	}

	public Long createMeaningPublicNote(Long meaningId, String value, String valuePrese) {
		FreeformRecord freeform = create.newRecord(FREEFORM);
		freeform.setType(FreeformType.PUBLIC_NOTE.name());
		freeform.setValueText(value);
		freeform.setValuePrese(valuePrese);
		freeform.store();
	
		MeaningFreeformRecord meaningFreeform = create.newRecord(MEANING_FREEFORM);
		meaningFreeform.setMeaningId(meaningId);
		meaningFreeform.setFreeformId(freeform.getId());
		meaningFreeform.store();
	
		return freeform.getId();
	}

	public Long createMeaningSemanticType(Long meaningId, String semanticTypeCode) {
		Long meaningSemanticTypeCodeId = create
				.select(MEANING_SEMANTIC_TYPE.ID).from(MEANING_SEMANTIC_TYPE)
				.where(MEANING_SEMANTIC_TYPE.MEANING_ID.eq(meaningId)
						.and(MEANING_SEMANTIC_TYPE.SEMANTIC_TYPE_CODE.eq(semanticTypeCode)))
				.limit(1)
				.fetchOneInto(Long.class);
		if (meaningSemanticTypeCodeId == null) {
			meaningSemanticTypeCodeId = create
					.insertInto(MEANING_SEMANTIC_TYPE, MEANING_SEMANTIC_TYPE.MEANING_ID, MEANING_SEMANTIC_TYPE.SEMANTIC_TYPE_CODE)
					.values(meaningId, semanticTypeCode)
					.returning(MEANING_SEMANTIC_TYPE.ID)
					.fetchOne()
					.getId();
		}
		return meaningSemanticTypeCodeId;
	}

	public Long createLexeme(Long wordId, String datasetCode, Long meaningId, int lexemeLevel1) {

		if (meaningId == null) {
			meaningId = create.insertInto(MEANING).defaultValues().returning(MEANING.ID).fetchOne().getId();
		} else {
			Long existingLexemeId = create
					.select(LEXEME.ID)
					.from(LEXEME)
					.where(
							LEXEME.WORD_ID.eq(wordId)
							.and(LEXEME.DATASET_CODE.eq(datasetCode))
							.and(LEXEME.MEANING_ID.eq(meaningId))
							.and(LEXEME.TYPE.eq(LEXEME_TYPE_PRIMARY)))
					.fetchOptionalInto(Long.class)
					.orElse(null);
			if (existingLexemeId != null) {
				return null;
			}
		}
		Long lexemeId = create
					.insertInto(
							LEXEME, LEXEME.MEANING_ID, LEXEME.WORD_ID, LEXEME.DATASET_CODE, LEXEME.TYPE,
							LEXEME.LEVEL1, LEXEME.LEVEL2, LEXEME.PROCESS_STATE_CODE, LEXEME.COMPLEXITY)
					.values(meaningId, wordId, datasetCode, LEXEME_TYPE_PRIMARY,
							lexemeLevel1, 1, PROCESS_STATE_IN_WORK, COMPLEXITY_DETAIL)
					.returning(LEXEME.ID)
					.fetchOne()
					.getId();
		return lexemeId;
	}

	public Long createUsage(Long lexemeId, String value, String valuePrese, String languageCode, Complexity complexity) {
		Long usageFreeformId = create
				.insertInto(FREEFORM, FREEFORM.TYPE, FREEFORM.VALUE_TEXT, FREEFORM.VALUE_PRESE, FREEFORM.LANG, FREEFORM.COMPLEXITY)
				.values(FreeformType.USAGE.name(), value, valuePrese, languageCode, complexity.name())
				.returning(FREEFORM.ID)
				.fetchOne()
				.getId();
		create.insertInto(LEXEME_FREEFORM, LEXEME_FREEFORM.LEXEME_ID, LEXEME_FREEFORM.FREEFORM_ID).values(lexemeId, usageFreeformId).execute();
		return usageFreeformId;
	}

	public Long createUsageTranslation(Long usageId, String value, String valuePrese, String languageCode) {
		return create
				.insertInto(FREEFORM, FREEFORM.TYPE, FREEFORM.PARENT_ID, FREEFORM.VALUE_TEXT, FREEFORM.VALUE_PRESE, FREEFORM.LANG)
				.values(FreeformType.USAGE_TRANSLATION.name(), usageId, value, valuePrese, languageCode)
				.returning(FREEFORM.ID)
				.fetchOne()
				.getId();
	}

	public Long createUsageDefinition(Long usageId, String value, String valuePrese, String languageCode) {
		return create
				.insertInto(FREEFORM, FREEFORM.TYPE, FREEFORM.PARENT_ID, FREEFORM.VALUE_TEXT, FREEFORM.VALUE_PRESE, FREEFORM.LANG)
				.values(FreeformType.USAGE_DEFINITION.name(), usageId, value, valuePrese, languageCode)
				.returning(FREEFORM.ID)
				.fetchOne()
				.getId();
	}

	public Long createLexemePublicNote(Long lexemeId, String value, String valuePrese, Complexity complexity) {
		FreeformRecord freeform = create.newRecord(FREEFORM);
		freeform.setType(FreeformType.PUBLIC_NOTE.name());
		freeform.setValueText(value);
		freeform.setValuePrese(valuePrese);
		freeform.setComplexity(complexity.name());
		freeform.store();
	
		LexemeFreeformRecord lexemeFreeform = create.newRecord(LEXEME_FREEFORM);
		lexemeFreeform.setLexemeId(lexemeId);
		lexemeFreeform.setFreeformId(freeform.getId());
		lexemeFreeform.store();
	
		return freeform.getId();
	}

	public Long createLexemeGovernment(Long lexemeId, String government, Complexity complexity) {
		Long governmentFreeformId = create
				.insertInto(FREEFORM, FREEFORM.TYPE, FREEFORM.VALUE_TEXT, FREEFORM.VALUE_PRESE, FREEFORM.COMPLEXITY)
				.values(FreeformType.GOVERNMENT.name(), government, government, complexity.name())
				.returning(FREEFORM.ID)
				.fetchOne()
				.getId();
		create.insertInto(LEXEME_FREEFORM, LEXEME_FREEFORM.LEXEME_ID, LEXEME_FREEFORM.FREEFORM_ID).values(lexemeId, governmentFreeformId).execute();
		return governmentFreeformId;
	}

	public Long createLexemeGrammar(Long lexemeId, String value, Complexity complexity) {
	
		Long grammarFreeformId = create
				.insertInto(FREEFORM, FREEFORM.TYPE, FREEFORM.VALUE_TEXT, FREEFORM.COMPLEXITY)
				.values(FreeformType.GRAMMAR.name(), value, complexity.name())
				.returning(FREEFORM.ID)
				.fetchOne()
				.getId();
		create.insertInto(LEXEME_FREEFORM, LEXEME_FREEFORM.LEXEME_ID, LEXEME_FREEFORM.FREEFORM_ID).values(lexemeId, grammarFreeformId).execute();
		return grammarFreeformId;
	}

	public Long createLexemePos(Long lexemeId, String posCode) {
		Long lexemePosId = create
				.select(LEXEME_POS.ID).from(LEXEME_POS)
				.where(LEXEME_POS.LEXEME_ID.eq(lexemeId)
						.and(LEXEME_POS.POS_CODE.eq(posCode)))
				.limit(1)
				.fetchOneInto(Long.class);
		if (lexemePosId == null) {
			lexemePosId = create
					.insertInto(LEXEME_POS, LEXEME_POS.LEXEME_ID, LEXEME_POS.POS_CODE)
					.values(lexemeId, posCode)
					.returning(LEXEME_POS.ID)
					.fetchOne()
					.getId();
		}
		return lexemePosId;
	}

	public Long createLexemeDeriv(Long lexemeId, String derivCode) {
		Long lexemeDerivId = create
				.select(LEXEME_DERIV.ID).from(LEXEME_DERIV)
				.where(LEXEME_DERIV.LEXEME_ID.eq(lexemeId)
						.and(LEXEME_DERIV.DERIV_CODE.eq(derivCode)))
				.limit(1)
				.fetchOneInto(Long.class);
		if (lexemeDerivId == null) {
			lexemeDerivId = create
					.insertInto(LEXEME_DERIV, LEXEME_DERIV.LEXEME_ID, LEXEME_DERIV.DERIV_CODE)
					.values(lexemeId, derivCode)
					.returning(LEXEME_DERIV.ID)
					.fetchOne()
					.getId();
		}
		return lexemeDerivId;
	}

	public Long createLexemeRegister(Long lexemeId, String registerCode) {
		Long lexemeRegisterId = create
				.select(LEXEME_REGISTER.ID).from(LEXEME_REGISTER)
				.where(LEXEME_REGISTER.LEXEME_ID.eq(lexemeId)
						.and(LEXEME_REGISTER.REGISTER_CODE.eq(registerCode)))
				.limit(1)
				.fetchOneInto(Long.class);
		if (lexemeRegisterId == null) {
			lexemeRegisterId = create
					.insertInto(LEXEME_REGISTER, LEXEME_REGISTER.LEXEME_ID, LEXEME_REGISTER.REGISTER_CODE)
					.values(lexemeId, registerCode)
					.returning(LEXEME_REGISTER.ID)
					.fetchOne()
					.getId();
		}
		return lexemeRegisterId;
	}

	public Long createLexemeRegion(Long lexemeId, String regionCode) {
		Long lexemeRegionId = create
				.select(LEXEME_REGION.ID).from(LEXEME_REGION)
				.where(LEXEME_REGION.LEXEME_ID.eq(lexemeId)
						.and(LEXEME_REGION.REGION_CODE.eq(regionCode)))
				.limit(1)
				.fetchOneInto(Long.class);
		if (lexemeRegionId == null) {
			lexemeRegionId = create
					.insertInto(LEXEME_REGION, LEXEME_REGION.LEXEME_ID, LEXEME_REGION.REGION_CODE)
					.values(lexemeId, regionCode)
					.returning(LEXEME_REGION.ID)
					.fetchOne()
					.getId();
		}
		return lexemeRegionId;
	}

	public void createImageTitle(Long imageId, String value) {
		create
				.insertInto(FREEFORM, FREEFORM.PARENT_ID, FREEFORM.TYPE, FREEFORM.VALUE_TEXT, FREEFORM.VALUE_PRESE)
				.values(imageId, FreeformType.IMAGE_TITLE.name(), value, value)
				.execute();
	}

	public Long createOdWordRecommendation(Long wordId, String value, String valuePrese) {

		FreeformRecord freeform = create.newRecord(FREEFORM);
		freeform.setType(FreeformType.OD_WORD_RECOMMENDATION.name());
		freeform.setValueText(value);
		freeform.setValuePrese(valuePrese);
		freeform.store();

		WordFreeformRecord wordFreeform = create.newRecord(WORD_FREEFORM);
		wordFreeform.setWordId(wordId);
		wordFreeform.setFreeformId(freeform.getId());
		wordFreeform.store();

		return freeform.getId();
	}

	public Long createOdLexemeRecommendation(Long lexemeId, String value, String valuePrese) {

		FreeformRecord freeform = create.newRecord(FREEFORM);
		freeform.setType(FreeformType.OD_LEXEME_RECOMMENDATION.name());
		freeform.setValueText(value);
		freeform.setValuePrese(valuePrese);
		freeform.store();

		LexemeFreeformRecord lexemeFreeform = create.newRecord(LEXEME_FREEFORM);
		lexemeFreeform.setLexemeId(lexemeId);
		lexemeFreeform.setFreeformId(freeform.getId());
		lexemeFreeform.store();

		return freeform.getId();
	}

	public Long createOdUsageDefinition(Long usageId, String value, String valuePrese) {

		return create
				.insertInto(FREEFORM, FREEFORM.TYPE, FREEFORM.PARENT_ID, FREEFORM.VALUE_TEXT, FREEFORM.VALUE_PRESE)
				.values(FreeformType.OD_USAGE_DEFINITION.name(), usageId, value, valuePrese)
				.returning(FREEFORM.ID)
				.fetchOne()
				.getId();
	}

	public Long createOdUsageAlternative(Long usageId, String value, String valuePrese) {

		return create
				.insertInto(FREEFORM, FREEFORM.TYPE, FREEFORM.PARENT_ID, FREEFORM.VALUE_TEXT, FREEFORM.VALUE_PRESE)
				.values(FreeformType.OD_USAGE_ALTERNATIVE.name(), usageId, value, valuePrese)
				.returning(FREEFORM.ID)
				.fetchOne()
				.getId();
	}

	public void createWordRelationParam(Long wordRelationId, String paramName, BigDecimal paramValue) {

		 create.insertInto(WORD_RELATION_PARAM, WORD_RELATION_PARAM.WORD_RELATION_ID, WORD_RELATION_PARAM.NAME, WORD_RELATION_PARAM.VALUE)
				 .values(wordRelationId, paramName, paramValue)
				 .execute();
	}

	public void deleteWord(Long wordId) {
		create.delete(LIFECYCLE_LOG)
				.where(LIFECYCLE_LOG.ID.in(DSL
						.select(WORD_LIFECYCLE_LOG.LIFECYCLE_LOG_ID)
						.from(WORD_LIFECYCLE_LOG)
						.where(WORD_LIFECYCLE_LOG.WORD_ID.eq(wordId))))
				.execute();
		create.delete(PROCESS_LOG)
				.where(PROCESS_LOG.ID.in(DSL
						.select(WORD_PROCESS_LOG.PROCESS_LOG_ID)
						.from(WORD_PROCESS_LOG)
						.where(WORD_PROCESS_LOG.WORD_ID.eq(wordId))))
				.execute();
		create.delete(FREEFORM)
				.where(FREEFORM.ID.in(DSL
						.select(WORD_FREEFORM.FREEFORM_ID)
						.from(WORD_FREEFORM)
						.where(WORD_FREEFORM.WORD_ID.eq(wordId))))
				.execute();
		create.delete(WORD)
				.where(WORD.ID.eq(wordId))
				.execute();
	}

	public void deleteWordWordType(Long wordWordTypeId) {
		create.delete(WORD_WORD_TYPE)
				.where(WORD_WORD_TYPE.ID.eq(wordWordTypeId))
				.execute();
	}

	public void deleteWordRelation(Long relationId) {
		create.delete(WORD_RELATION)
				.where(WORD_RELATION.ID.eq(relationId))
				.execute();
	}

	public void deleteWordRelationGroupMember(Long relationId) {
		create.delete(WORD_GROUP_MEMBER)
				.where(WORD_GROUP_MEMBER.ID.eq(relationId))
				.execute();
	}

	public void deleteWordRelationGroup(Long groupId) {
		create.delete(WORD_GROUP)
				.where(WORD_GROUP.ID.eq(groupId))
				.execute();
	}

	public void deleteLexeme(Long lexemeId) {
		create.delete(LIFECYCLE_LOG)
				.where(LIFECYCLE_LOG.ID.in(DSL
						.select(LEXEME_LIFECYCLE_LOG.LIFECYCLE_LOG_ID)
						.from(LEXEME_LIFECYCLE_LOG)
						.where(LEXEME_LIFECYCLE_LOG.LEXEME_ID.eq(lexemeId))))
				.execute();
		create.delete(PROCESS_LOG)
				.where(PROCESS_LOG.ID.in(DSL
						.select(LEXEME_PROCESS_LOG.PROCESS_LOG_ID)
						.from(LEXEME_PROCESS_LOG)
						.where(LEXEME_PROCESS_LOG.LEXEME_ID.eq(lexemeId))))
				.execute();
		create.delete(FREEFORM)
				.where(FREEFORM.ID.in(DSL
						.select(LEXEME_FREEFORM.FREEFORM_ID)
						.from(LEXEME_FREEFORM)
						.where(LEXEME_FREEFORM.LEXEME_ID.eq(lexemeId))))
				.execute();
		create.delete(LEXEME)
				.where(LEXEME.ID.eq(lexemeId))
				.execute();
	}

	public void deleteLexemePos(Long lexemePosId) {
		create.delete(LEXEME_POS)
				.where(LEXEME_POS.ID.eq(lexemePosId))
				.execute();
	}

	public void deleteLexemeDeriv(Long lexemeDerivId) {
		create.delete(LEXEME_DERIV)
				.where(LEXEME_DERIV.ID.eq(lexemeDerivId))
				.execute();
	}

	public void deleteLexemeRegister(Long lexemeRegisterId) {
		create.delete(LEXEME_REGISTER)
				.where(LEXEME_REGISTER.ID.eq(lexemeRegisterId))
				.execute();
	}

	public void deleteLexemeRegion(Long lexemeRegionId) {
		create.delete(LEXEME_REGION)
				.where(LEXEME_REGION.ID.eq(lexemeRegionId))
				.execute();
	}

	public void deleteLexemeRefLink(Long refLinkId) {
		create.delete(LEXEME_SOURCE_LINK)
				.where(LEXEME_SOURCE_LINK.ID.eq(refLinkId))
				.execute();
	}

	public void deleteLexemeRelation(Long relationId) {
		create.delete(LEX_RELATION)
				.where(LEX_RELATION.ID.eq(relationId))
				.execute();
	}

	public void deleteDefinition(Long id) {
		create.delete(DEFINITION)
				.where(DEFINITION.ID.eq(id))
				.execute();
	}

	public void deleteDefinitionRefLink(Long refLinkId) {
		create.delete(DEFINITION_SOURCE_LINK)
				.where(DEFINITION_SOURCE_LINK.ID.eq(refLinkId))
				.execute();
	}

	public void deleteFreeform(Long id) {
		create.delete(FREEFORM)
				.where(FREEFORM.ID.eq(id))
				.execute();
	}

	public void deleteFreeformRefLink(Long refLinkId) {
		create.delete(FREEFORM_SOURCE_LINK)
				.where(FREEFORM_SOURCE_LINK.ID.eq(refLinkId))
				.execute();
	}

	public void deleteMeaning(Long meaningId) {
		List<Long> definitionIds = getMeaningDefinitionIds(meaningId);
		for (Long definitionId : definitionIds) {
			deleteDefinitionFreeforms(definitionId);
			deleteDefinition(definitionId);
		}
		deleteMeaningFreeforms(meaningId);

		create.delete(LIFECYCLE_LOG)
				.where(LIFECYCLE_LOG.ID.in(DSL
						.select(MEANING_LIFECYCLE_LOG.LIFECYCLE_LOG_ID)
						.from(MEANING_LIFECYCLE_LOG)
						.where(MEANING_LIFECYCLE_LOG.MEANING_ID.eq(meaningId))))
				.execute();
		create.delete(PROCESS_LOG)
				.where(PROCESS_LOG.ID.in(DSL
						.select(MEANING_PROCESS_LOG.PROCESS_LOG_ID)
						.from(MEANING_PROCESS_LOG)
						.where(MEANING_PROCESS_LOG.MEANING_ID.eq(meaningId))))
				.execute();
		create.delete(MEANING)
				.where(MEANING.ID.eq(meaningId))
				.execute();
	}

	public void deleteMeaningRelation(Long relationId) {
		create.delete(MEANING_RELATION)
				.where(MEANING_RELATION.ID.eq(relationId))
				.execute();
	}

	public void deleteMeaningDomain(Long meaningDomainId) {
		create.delete(MEANING_DOMAIN)
				.where(MEANING_DOMAIN.ID.eq(meaningDomainId))
				.execute();
	}

	public void deleteMeaningSemanticType(Long meaningSemanticTypeId) {
		create.delete(MEANING_SEMANTIC_TYPE)
				.where(MEANING_SEMANTIC_TYPE.ID.eq(meaningSemanticTypeId))
				.execute();
	}

	public void deleteImageTitle(Long imageFreeformId) {
		create.delete(FREEFORM)
				.where(FREEFORM.PARENT_ID.eq(imageFreeformId)
						.and(FREEFORM.TYPE.eq(FreeformType.IMAGE_TITLE.name())))
				.execute();
	}

	private List<Long> getMeaningDefinitionIds(Long meaningId) {
		return create
				.select(DEFINITION.ID)
				.from(DEFINITION)
				.where(DEFINITION.MEANING_ID.eq(meaningId))
				.fetchInto(Long.class);
	}

	private void deleteDefinitionFreeforms(Long definitionId) {
		create.delete(FREEFORM)
				.where(
						FREEFORM.ID.in(DSL.select(DEFINITION_FREEFORM.FREEFORM_ID)
								.from(DEFINITION_FREEFORM)
								.where(DEFINITION_FREEFORM.DEFINITION_ID.eq(definitionId))))
				.execute();
	}

	private void deleteMeaningFreeforms(Long meaningId) {
		create.delete(FREEFORM)
				.where(
						FREEFORM.ID.in(DSL.select(MEANING_FREEFORM.FREEFORM_ID)
								.from(MEANING_FREEFORM)
								.where(MEANING_FREEFORM.MEANING_ID.eq(meaningId))))
				.execute();
	}

	public SynRelation addSynRelation(Long word1Id, Long word2Id, String relationType, String relationStatus) {
		return create.insertInto(WORD_RELATION,
					WORD_RELATION.WORD1_ID,
					WORD_RELATION.WORD2_ID,
					WORD_RELATION.WORD_REL_TYPE_CODE,
					WORD_RELATION.RELATION_STATUS)
				.values(word1Id, word2Id, relationType, relationStatus)
				.returning()
				.fetchOne()
				.into(SynRelation.class);
	}

}
