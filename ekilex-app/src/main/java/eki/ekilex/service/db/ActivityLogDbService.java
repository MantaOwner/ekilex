package eki.ekilex.service.db;

import static eki.ekilex.data.db.Tables.ACTIVITY_LOG;
import static eki.ekilex.data.db.Tables.DEFINITION;
import static eki.ekilex.data.db.Tables.DEFINITION_FREEFORM;
import static eki.ekilex.data.db.Tables.DEFINITION_SOURCE_LINK;
import static eki.ekilex.data.db.Tables.FREEFORM;
import static eki.ekilex.data.db.Tables.FREEFORM_SOURCE_LINK;
import static eki.ekilex.data.db.Tables.LEXEME;
import static eki.ekilex.data.db.Tables.LEXEME_ACTIVITY_LOG;
import static eki.ekilex.data.db.Tables.LEXEME_FREEFORM;
import static eki.ekilex.data.db.Tables.LEXEME_SOURCE_LINK;
import static eki.ekilex.data.db.Tables.LEX_RELATION;
import static eki.ekilex.data.db.Tables.MEANING;
import static eki.ekilex.data.db.Tables.MEANING_ACTIVITY_LOG;
import static eki.ekilex.data.db.Tables.MEANING_DOMAIN;
import static eki.ekilex.data.db.Tables.MEANING_FREEFORM;
import static eki.ekilex.data.db.Tables.MEANING_RELATION;
import static eki.ekilex.data.db.Tables.SOURCE;
import static eki.ekilex.data.db.Tables.SOURCE_ACTIVITY_LOG;
import static eki.ekilex.data.db.Tables.SOURCE_FREEFORM;
import static eki.ekilex.data.db.Tables.WORD;
import static eki.ekilex.data.db.Tables.WORD_ACTIVITY_LOG;
import static eki.ekilex.data.db.Tables.WORD_ETYMOLOGY;
import static eki.ekilex.data.db.Tables.WORD_FREEFORM;
import static eki.ekilex.data.db.Tables.WORD_RELATION;
import static eki.ekilex.data.db.Tables.WORD_WORD_TYPE;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import eki.common.constant.GlobalConstant;
import eki.ekilex.data.ActivityLog;
import eki.ekilex.data.TypeActivityLogDiff;
import eki.ekilex.data.WordLexemeMeaningIds;
import eki.ekilex.data.db.tables.Definition;
import eki.ekilex.data.db.tables.DefinitionFreeform;
import eki.ekilex.data.db.tables.Freeform;
import eki.ekilex.data.db.tables.Lexeme;
import eki.ekilex.data.db.tables.LexemeFreeform;
import eki.ekilex.data.db.tables.Meaning;
import eki.ekilex.data.db.tables.MeaningFreeform;
import eki.ekilex.data.db.tables.SourceFreeform;
import eki.ekilex.data.db.tables.Word;
import eki.ekilex.data.db.tables.WordFreeform;
import eki.ekilex.data.db.udt.records.TypeActivityLogDiffRecord;

@Component
public class ActivityLogDbService implements GlobalConstant {

	private static final String LOADER_USERNAME_PATTERN = "Ekilex %laadur";

	@Autowired
	private DSLContext create;

	public List<ActivityLog> getWordActivityLog(Long wordId) {

		return create.select(
				ACTIVITY_LOG.ID,
				ACTIVITY_LOG.EVENT_BY,
				ACTIVITY_LOG.EVENT_ON,
				ACTIVITY_LOG.FUNCT_NAME,
				ACTIVITY_LOG.OWNER_ID,
				ACTIVITY_LOG.OWNER_NAME,
				ACTIVITY_LOG.ENTITY_ID,
				ACTIVITY_LOG.ENTITY_NAME,
				ACTIVITY_LOG.PREV_DIFFS,
				ACTIVITY_LOG.CURR_DIFFS)
				.from(ACTIVITY_LOG, WORD_ACTIVITY_LOG)
				.where(
						WORD_ACTIVITY_LOG.WORD_ID.eq(wordId)
								.and(WORD_ACTIVITY_LOG.ACTIVITY_LOG_ID.eq(ACTIVITY_LOG.ID)))
				.orderBy(ACTIVITY_LOG.EVENT_ON.desc())
				.fetchInto(ActivityLog.class);
	}

	public List<ActivityLog> getMeaningActivityLog(Long meaningId) {

		return create.select(
				ACTIVITY_LOG.ID,
				ACTIVITY_LOG.EVENT_BY,
				ACTIVITY_LOG.EVENT_ON,
				ACTIVITY_LOG.FUNCT_NAME,
				ACTIVITY_LOG.OWNER_ID,
				ACTIVITY_LOG.OWNER_NAME,
				ACTIVITY_LOG.ENTITY_ID,
				ACTIVITY_LOG.ENTITY_NAME,
				ACTIVITY_LOG.PREV_DIFFS,
				ACTIVITY_LOG.CURR_DIFFS)
				.from(ACTIVITY_LOG, MEANING_ACTIVITY_LOG)
				.where(
						MEANING_ACTIVITY_LOG.MEANING_ID.eq(meaningId)
								.and(MEANING_ACTIVITY_LOG.ACTIVITY_LOG_ID.eq(ACTIVITY_LOG.ID)))
				.orderBy(ACTIVITY_LOG.EVENT_ON.desc())
				.fetchInto(ActivityLog.class);
	}

	public List<ActivityLog> getSourceActivityLog(Long sourceId) {

		return create.select(
				ACTIVITY_LOG.ID,
				ACTIVITY_LOG.EVENT_BY,
				ACTIVITY_LOG.EVENT_ON,
				ACTIVITY_LOG.FUNCT_NAME,
				ACTIVITY_LOG.OWNER_ID,
				ACTIVITY_LOG.OWNER_NAME,
				ACTIVITY_LOG.ENTITY_ID,
				ACTIVITY_LOG.ENTITY_NAME,
				ACTIVITY_LOG.PREV_DIFFS,
				ACTIVITY_LOG.CURR_DIFFS)
				.from(ACTIVITY_LOG, SOURCE_ACTIVITY_LOG)
				.where(
						SOURCE_ACTIVITY_LOG.SOURCE_ID.eq(sourceId)
								.and(SOURCE_ACTIVITY_LOG.ACTIVITY_LOG_ID.eq(ACTIVITY_LOG.ID)))
				.orderBy(ACTIVITY_LOG.EVENT_ON.desc())
				.fetchInto(ActivityLog.class);
	}

	public Timestamp getLatestLogTimeForWord(Long wordId) {

		return create
				.select(DSL.max(ACTIVITY_LOG.EVENT_ON))
				.from(ACTIVITY_LOG, WORD_ACTIVITY_LOG)
				.where(
						WORD_ACTIVITY_LOG.WORD_ID.eq(wordId)
								.and(WORD_ACTIVITY_LOG.ACTIVITY_LOG_ID.eq(ACTIVITY_LOG.ID))
								.andNot(ACTIVITY_LOG.EVENT_BY.like(LOADER_USERNAME_PATTERN)))
				.fetchSingleInto(Timestamp.class);
	}

	public Timestamp getLatestLogTimeForMeaning(Long meaningId) {

		return create
				.select(DSL.max(ACTIVITY_LOG.EVENT_ON))
				.from(ACTIVITY_LOG, MEANING_ACTIVITY_LOG)
				.where(
						MEANING_ACTIVITY_LOG.MEANING_ID.eq(meaningId)
								.and(MEANING_ACTIVITY_LOG.ACTIVITY_LOG_ID.eq(ACTIVITY_LOG.ID))
								.andNot(ACTIVITY_LOG.EVENT_BY.like(LOADER_USERNAME_PATTERN)))
				.fetchSingleInto(Timestamp.class);
	}

	public Long create(ActivityLog activityLog) {

		return create.insertInto(
				ACTIVITY_LOG,
				ACTIVITY_LOG.EVENT_BY,
				ACTIVITY_LOG.FUNCT_NAME,
				ACTIVITY_LOG.OWNER_ID,
				ACTIVITY_LOG.OWNER_NAME,
				ACTIVITY_LOG.ENTITY_ID,
				ACTIVITY_LOG.ENTITY_NAME,
				ACTIVITY_LOG.PREV_DATA,
				ACTIVITY_LOG.CURR_DATA,
				ACTIVITY_LOG.PREV_DIFFS,
				ACTIVITY_LOG.CURR_DIFFS)
				.values(
						activityLog.getEventBy(),
						activityLog.getFunctName(),
						activityLog.getOwnerId(),
						activityLog.getOwnerName().name(),
						activityLog.getEntityId(),
						activityLog.getEntityName().name(),
						JSONB.valueOf(activityLog.getPrevData()),
						JSONB.valueOf(activityLog.getCurrData()),
						convert(activityLog.getPrevDiffs()),
						convert(activityLog.getCurrDiffs()))
				.returning(ACTIVITY_LOG.ID)
				.fetchOne()
				.getId();
	}

	public void createLexemesActivityLogs(Long activityLogId, Long... lexemeIds) {

		for (Long lexemeId : lexemeIds) {
			create
					.insertInto(LEXEME_ACTIVITY_LOG, LEXEME_ACTIVITY_LOG.LEXEME_ID, LEXEME_ACTIVITY_LOG.ACTIVITY_LOG_ID)
					.select(DSL
							.select(DSL.val(lexemeId), DSL.val(activityLogId))
							.whereExists(DSL.select(LEXEME.ID).from(LEXEME).where(LEXEME.ID.eq(lexemeId))))
					.execute();
		}
	}

	public void createWordsActivityLogs(Long activityLogId, Long... wordIds) {

		for (Long wordId : wordIds) {
			create
					.insertInto(WORD_ACTIVITY_LOG, WORD_ACTIVITY_LOG.WORD_ID, WORD_ACTIVITY_LOG.ACTIVITY_LOG_ID)
					.select(DSL
							.select(DSL.val(wordId), DSL.val(activityLogId))
							.whereExists(DSL.select(WORD.ID).from(WORD).where(WORD.ID.eq(wordId))))
					.execute();
		}
	}

	public void createMeaningsActivityLogs(Long activityLogId, Long... meaningIds) {

		for (Long meaningId : meaningIds) {
			create
					.insertInto(MEANING_ACTIVITY_LOG, MEANING_ACTIVITY_LOG.MEANING_ID, MEANING_ACTIVITY_LOG.ACTIVITY_LOG_ID)
					.select(DSL
							.select(DSL.val(meaningId), DSL.val(activityLogId))
							.whereExists(DSL.select(MEANING.ID).from(MEANING).where(MEANING.ID.eq(meaningId))))
					.execute();
		}
	}

	public void createSourceActivityLog(Long activityLogId, Long sourceId) {

		create
				.insertInto(SOURCE_ACTIVITY_LOG, SOURCE_ACTIVITY_LOG.SOURCE_ID, SOURCE_ACTIVITY_LOG.ACTIVITY_LOG_ID)
				.select(DSL
						.select(DSL.val(sourceId), DSL.val(activityLogId))
						.whereExists(DSL.select(SOURCE.ID).from(SOURCE).where(SOURCE.ID.eq(sourceId))))
				.execute();
	}

	private TypeActivityLogDiffRecord[] convert(List<TypeActivityLogDiff> logDiffs) {
		if (CollectionUtils.isEmpty(logDiffs)) {
			return new TypeActivityLogDiffRecord[0];
		}
		int logSize = logDiffs.size();
		TypeActivityLogDiffRecord[] diffRecords = new TypeActivityLogDiffRecord[logSize];
		for (int recordIndex = 0; recordIndex < logSize; recordIndex++) {
			TypeActivityLogDiff diffObj = logDiffs.get(recordIndex);
			diffRecords[recordIndex] = new TypeActivityLogDiffRecord(diffObj.getOp(), diffObj.getPath(), diffObj.getValue());
		}
		return diffRecords;
	}

	public WordLexemeMeaningIds getLexemeMeaningIds(Long wordId) {

		Word w = WORD.as("w");
		Condition entityIdCond = w.ID.eq(wordId);
		return getWordLexemeMeaningIds(entityIdCond, w.ID);
	}

	public WordLexemeMeaningIds getWordMeaningIds(Long lexemeId) {

		Lexeme l = LEXEME.as("l");
		Condition entityIdCond = l.ID.eq(lexemeId);
		return getWordLexemeMeaningIds(entityIdCond, l.ID);
	}

	public WordLexemeMeaningIds getLexemeWordIds(Long meaningId) {

		Meaning m = MEANING.as("m");
		Condition entityIdCond = m.ID.eq(meaningId);
		return getWordLexemeMeaningIds(entityIdCond, m.ID);
	}

	private WordLexemeMeaningIds getWordLexemeMeaningIds(Condition entityIdCond, Field<Long> groupByField) {

		Word w = WORD.as("w");
		Lexeme l = LEXEME.as("l");
		Meaning m = MEANING.as("m");

		return create
				.select(
						DSL.arrayAggDistinct(l.ID).as("lexeme_ids"),
						DSL.arrayAggDistinct(w.ID).as("word_ids"),
						DSL.arrayAggDistinct(m.ID).as("meaning_ids"))
				.from(w, l, m)
				.where(
						entityIdCond
								.and(l.WORD_ID.eq(w.ID))
								.and(l.MEANING_ID.eq(m.ID)))
				.groupBy(groupByField)
				.fetchOptionalInto(WordLexemeMeaningIds.class)
				.orElse(new WordLexemeMeaningIds());
	}

	public Map<String, Object> getFirstDepthFreeformOwnerDataMap(Long freeformId) {

		LexemeFreeform lff = LEXEME_FREEFORM.as("lff");
		WordFreeform wff = WORD_FREEFORM.as("wff");
		MeaningFreeform mff = MEANING_FREEFORM.as("mff");
		SourceFreeform sff = SOURCE_FREEFORM.as("sff");
		DefinitionFreeform dff = DEFINITION_FREEFORM.as("dff");
		Definition d = DEFINITION.as("d");
		Freeform ff = FREEFORM.as("ff");

		return create.select(
				ff.TYPE,
				lff.LEXEME_ID,
				wff.WORD_ID,
				mff.MEANING_ID,
				d.MEANING_ID.as("d_meaning_id"),
				sff.SOURCE_ID)
				.from(
						ff
								.leftOuterJoin(lff).on(lff.FREEFORM_ID.eq(ff.ID))
								.leftOuterJoin(wff).on(wff.FREEFORM_ID.eq(ff.ID))
								.leftOuterJoin(mff).on(mff.FREEFORM_ID.eq(ff.ID))
								.leftOuterJoin(sff).on(sff.FREEFORM_ID.eq(ff.ID))
								.leftOuterJoin(dff).on(dff.FREEFORM_ID.eq(ff.ID))
								.leftOuterJoin(d).on(d.ID.eq(dff.DEFINITION_ID)))
				.where(ff.ID.eq(freeformId))
				.fetchOptionalMap()
				.orElse(null);
	}

	public Map<String, Object> getSecondDepthFreeformOwnerDataMap(Long freeformId) {

		LexemeFreeform lff = LEXEME_FREEFORM.as("lff");
		WordFreeform wff = WORD_FREEFORM.as("wff");
		MeaningFreeform mff = MEANING_FREEFORM.as("mff");
		SourceFreeform sff = SOURCE_FREEFORM.as("sff");
		DefinitionFreeform dff = DEFINITION_FREEFORM.as("dff");
		Definition d = DEFINITION.as("d");
		Freeform ff = FREEFORM.as("ff");

		return create.select(
				ff.TYPE,
				lff.LEXEME_ID,
				wff.WORD_ID,
				mff.MEANING_ID,
				d.MEANING_ID.as("d_meaning_id"),
				sff.SOURCE_ID)
				.from(
						ff
								.leftOuterJoin(lff).on(lff.FREEFORM_ID.eq(ff.PARENT_ID))
								.leftOuterJoin(wff).on(wff.FREEFORM_ID.eq(ff.PARENT_ID))
								.leftOuterJoin(mff).on(mff.FREEFORM_ID.eq(ff.PARENT_ID))
								.leftOuterJoin(sff).on(sff.FREEFORM_ID.eq(ff.PARENT_ID))
								.leftOuterJoin(dff).on(dff.FREEFORM_ID.eq(ff.PARENT_ID))
								.leftOuterJoin(d).on(d.ID.eq(dff.DEFINITION_ID)))
				.where(ff.ID.eq(freeformId))
				.fetchOptionalMap()
				.orElse(null);
	}

	public Long getWordRelationOwnerId(Long wordRelationId) {
		return create.select(WORD_RELATION.WORD1_ID).from(WORD_RELATION).where(WORD_RELATION.ID.eq(wordRelationId)).fetchOptionalInto(Long.class).orElse(null);
	}

	public Long getWordTypeOwnerId(Long wordTypeId) {
		return create.select(WORD_WORD_TYPE.WORD_ID).from(WORD_WORD_TYPE).where(WORD_WORD_TYPE.ID.eq(wordTypeId)).fetchOptionalInto(Long.class).orElse(null);
	}

	public Long getWordEtymologyOwnerId(Long entityId) {
		return create.select(WORD_ETYMOLOGY.WORD_ID).from(WORD_ETYMOLOGY).where(WORD_ETYMOLOGY.ID.eq(entityId)).fetchOptionalInto(Long.class).orElse(null);
	}

	public Long getMeaningDomainOwnerId(Long meaningDomainId) {
		return create.select(MEANING_DOMAIN.MEANING_ID).from(MEANING_DOMAIN).where(MEANING_DOMAIN.ID.eq(meaningDomainId)).fetchOptionalInto(Long.class).orElse(null);
	}

	public Long getMeaningDefinitionOwnerId(Long definitionId) {
		return create.select(DEFINITION.MEANING_ID).from(DEFINITION).where(DEFINITION.ID.eq(definitionId)).fetchOptionalInto(Long.class).orElse(null);
	}

	public Long getLexemeRelationOwnerId(Long lexemeRelationId) {
		return create.select(LEX_RELATION.LEXEME1_ID).from(LEX_RELATION).where(LEX_RELATION.ID.eq(lexemeRelationId)).fetchOptionalInto(Long.class).orElse(null);
	}

	public Long getMeaningRelationOwnerId(Long meaningRelationId) {
		return create.select(MEANING_RELATION.MEANING1_ID).from(MEANING_RELATION).where(MEANING_RELATION.ID.eq(meaningRelationId)).fetchOptionalInto(Long.class).orElse(null);
	}

	public Long getFreeformSourceLinkFreeformId(Long sourceLinkId) {
		return create.select(FREEFORM_SOURCE_LINK.FREEFORM_ID).from(FREEFORM_SOURCE_LINK).where(FREEFORM_SOURCE_LINK.ID.eq(sourceLinkId)).fetchOptionalInto(Long.class).orElse(null);
	}

	public Long getLexemeSourceLinkOwnerId(Long sourceLinkId) {
		return create.select(LEXEME_SOURCE_LINK.LEXEME_ID).from(LEXEME_SOURCE_LINK).where(LEXEME_SOURCE_LINK.ID.eq(sourceLinkId)).fetchOptionalInto(Long.class).orElse(null);
	}

	public Long getDefinitionSourceLinkOwnerId(Long sourceLinkId) {
		return create
				.select(DEFINITION.MEANING_ID)
				.from(DEFINITION, DEFINITION_SOURCE_LINK)
				.where(DEFINITION_SOURCE_LINK.ID.eq(sourceLinkId).and(DEFINITION_SOURCE_LINK.DEFINITION_ID.eq(DEFINITION.ID)))
				.fetchOptionalInto(Long.class).orElse(null);
	}

}
