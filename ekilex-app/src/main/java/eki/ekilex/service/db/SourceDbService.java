package eki.ekilex.service.db;

import static eki.ekilex.data.db.Tables.DEFINITION_SOURCE_LINK;
import static eki.ekilex.data.db.Tables.FREEFORM;
import static eki.ekilex.data.db.Tables.FREEFORM_SOURCE_LINK;
import static eki.ekilex.data.db.Tables.LEXEME_SOURCE_LINK;
import static eki.ekilex.data.db.Tables.SOURCE;
import static eki.ekilex.data.db.Tables.SOURCE_FREEFORM;
import static eki.ekilex.data.db.Tables.SOURCE_LIFECYCLE_LOG;
import static eki.ekilex.data.db.Tables.WORD_ETYMOLOGY_SOURCE_LINK;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eki.common.constant.FreeformType;
import eki.common.constant.SourceType;
import eki.ekilex.constant.SystemConstant;
import eki.ekilex.data.SourceProperty;
import eki.ekilex.data.SourcePropertyTuple;
import eki.ekilex.data.db.tables.Freeform;
import eki.ekilex.data.db.tables.Source;
import eki.ekilex.data.db.tables.SourceFreeform;
import eki.ekilex.data.db.tables.records.FreeformRecord;

@Component
public class SourceDbService implements SystemConstant {

	private DSLContext create;

	@Autowired
	public SourceDbService(DSLContext context) {
		create = context;
	}

	public List<SourcePropertyTuple> getSource(Long sourceId) {

		Source s = SOURCE.as("s");
		SourceFreeform sff = SOURCE_FREEFORM.as("sff");
		Freeform sp = FREEFORM.as("sp");

		return create
				.select(
						s.ID.as("source_id"),
						s.TYPE,
						sp.ID.as("source_property_id"),
						sp.TYPE.as("source_property_type"),
						sp.VALUE_TEXT.as("source_property_value_text"),
						sp.VALUE_DATE.as("source_property_value_date")
						)
				.from(
						s
						.innerJoin(sff).on(s.ID.eq(sff.SOURCE_ID))
						.innerJoin(sp).on(sff.FREEFORM_ID.eq(sp.ID)))
				.where(s.ID.equal(sourceId))
				.orderBy(sp.ORDER_BY)
				.fetch()
				.into(SourcePropertyTuple.class);
	}

	public String getSourcePropertyValue(Long sourcePropertyId) {

		return create
				.select(FREEFORM.VALUE_TEXT)
				.from(FREEFORM)
				.where(FREEFORM.ID.eq(sourcePropertyId))
				.fetchOneInto(String.class);
	}

	public List<SourcePropertyTuple> findSourcesByNameAndType(String searchFilterWithMetaCharacters, SourceType sourceType) {
		return findSourcesByNameAndTypeExcludingOneSource(searchFilterWithMetaCharacters, sourceType, null);
	}

	public List<SourcePropertyTuple> findSourcesByNameAndTypeExcludingOneSource(String searchFilterWithMetaCharacters, SourceType sourceType,
			Long sourceIdToExclude) {

		String searchFilter = searchFilterWithMetaCharacters.replace("*", "%").replace("?", "_").toLowerCase();

		Source s = SOURCE.as("s");
		SourceFreeform sff = SOURCE_FREEFORM.as("sff");
		Freeform sp = FREEFORM.as("sp");
		SourceFreeform sffc = SOURCE_FREEFORM.as("sffc");
		Freeform spc = FREEFORM.as("spc");

		SelectConditionStep<Record1<Long>> existCondition =
				DSL
				.select(sffc.ID)
				.from(sffc, spc)
				.where(
						sffc.SOURCE_ID.eq(s.ID)
						.and(sffc.FREEFORM_ID.eq(spc.ID))
						.and(spc.VALUE_TEXT.lower().like(searchFilter)));

		if (sourceType != null) {
			existCondition = existCondition.and(s.TYPE.eq(sourceType.name()));
		}
		if (sourceIdToExclude != null) {
			existCondition = existCondition.and(sffc.SOURCE_ID.notEqual(sourceIdToExclude));
		}

		Condition sex = DSL.exists(existCondition);

		Field<Boolean> is_source_property_match = DSL.field(sp.VALUE_TEXT.lower().like(searchFilter));

		return create
				.select(
						s.ID.as("source_id"),
						s.TYPE,
						sp.ID.as("source_property_id"),
						sp.TYPE.as("source_property_type"),
						sp.VALUE_TEXT.as("source_property_value_text"),
						sp.VALUE_DATE.as("source_property_value_date"),
						is_source_property_match.as("is_source_property_match")
						)
				.from(
						s
						.innerJoin(sff).on(s.ID.eq(sff.SOURCE_ID))
						.innerJoin(sp).on(sff.FREEFORM_ID.eq(sp.ID)))
				.where(sex)
				.orderBy(s.ID, sp.ORDER_BY)
				.fetch()
				.into(SourcePropertyTuple.class);
	}

	public Long addSource(SourceType sourceType, List<SourceProperty> sourceProperties) {

		Long sourceId =
				create.insertInto(SOURCE, SOURCE.TYPE)
						.values(sourceType.name())
						.returning(SOURCE.ID)
						.fetchOne()
						.getId();

		for (SourceProperty sourceProperty : sourceProperties) {
			addSourceProperty(sourceId, sourceProperty.getType(), sourceProperty.getValueText());
		}
		return sourceId;
	}

	public Long addSourceProperty(Long sourceId, FreeformType type, String valueText) {

		Long sourceFreeformId = create
				.insertInto(FREEFORM, FREEFORM.TYPE, FREEFORM.VALUE_TEXT, FREEFORM.VALUE_PRESE)
				.values(type.name(), valueText, valueText)
				.returning(FREEFORM.ID)
				.fetchOne()
				.getId();
		create.insertInto(SOURCE_FREEFORM, SOURCE_FREEFORM.SOURCE_ID, SOURCE_FREEFORM.FREEFORM_ID).values(sourceId, sourceFreeformId).execute();
		return sourceFreeformId;
	}

	public void updateSourceProperty(Long sourceFreeformId, String valueText) {

		create.update(FREEFORM)
				.set(FREEFORM.VALUE_TEXT, valueText)
				.set(FREEFORM.VALUE_PRESE, valueText)
				.where(FREEFORM.ID.eq(sourceFreeformId))
				.execute();
	}

	public void deleteSourceProperty(Long sourceFreeformId) {

		create.delete(FREEFORM)
				.where(FREEFORM.ID.eq(sourceFreeformId))
				.execute();
		create.delete(SOURCE_FREEFORM)
				.where(SOURCE_FREEFORM.FREEFORM_ID.eq(sourceFreeformId))
				.execute();
	}

	public void editSourceType(Long sourceId, SourceType type) {

		create.update(SOURCE)
				.set(SOURCE.TYPE, type.name())
				.where(SOURCE.ID.eq(sourceId))
				.execute();
	}

	public void deleteSource(Long sourceId) {

		List<Long> freeformIds = create
				.select(SOURCE_FREEFORM.FREEFORM_ID)
				.from(SOURCE_FREEFORM)
				.where(SOURCE_FREEFORM.SOURCE_ID.eq(sourceId))
				.fetchInto(Long.class);

		for (Long freeformId : freeformIds) {
			create.delete(FREEFORM)
					.where(FREEFORM.ID.eq(freeformId))
					.execute();
		}

		create.delete(SOURCE)
				.where(SOURCE.ID.eq(sourceId))
				.execute();
	}

	public void joinSources(Long firstSourceId, Long secondSourceId) {

		Result<FreeformRecord> firstSourceFreeforms = getSourceFreeformRecords(firstSourceId);
		Result<FreeformRecord> secondSourceFreeforms = getSourceFreeformRecords(secondSourceId);

		List<Long> uniqueFreeformsIds = secondSourceFreeforms.stream()
				.filter(second -> firstSourceFreeforms.stream()
						.noneMatch(first ->
								first.getType().equals(second.getType())
								&& Objects.nonNull(first.getValueText())
								&& first.getValueText().equals(second.getValueText())))
				.map(FreeformRecord::getId)
				.collect(Collectors.toList());

		for (Long freeformId : uniqueFreeformsIds) {
			create.update(SOURCE_FREEFORM)
					.set(SOURCE_FREEFORM.SOURCE_ID, firstSourceId)
					.where(
							SOURCE_FREEFORM.SOURCE_ID.eq(secondSourceId)
							.and(SOURCE_FREEFORM.FREEFORM_ID.eq(freeformId)))
					.execute();
		}

		create.update(FREEFORM_SOURCE_LINK)
				.set(FREEFORM_SOURCE_LINK.SOURCE_ID, firstSourceId)
				.where(FREEFORM_SOURCE_LINK.SOURCE_ID.eq(secondSourceId))
				.execute();

		create.update(SOURCE_LIFECYCLE_LOG)
				.set(SOURCE_LIFECYCLE_LOG.SOURCE_ID, firstSourceId)
				.where(SOURCE_LIFECYCLE_LOG.SOURCE_ID.eq(secondSourceId))
				.execute();

		deleteSource(secondSourceId);
	}

	// TODO refactor - Yogesh
	public boolean isSourceDeletePossible(Long sourceId) {

		return countDefinitionSourceLinksBySourceId(sourceId) == 0
				&& countFreeformSourceLinksBySourceId(sourceId) == 0
				&& countLexemeSourceLinksBySourceId(sourceId) == 0
				&& countWordSourceLinksBySourceId(sourceId) == 0;
	}

	private int countDefinitionSourceLinksBySourceId(Long sourceId) {

		return create.fetchCount(DSL.select(DEFINITION_SOURCE_LINK.ID)
				.from(DEFINITION_SOURCE_LINK)
				.where(DEFINITION_SOURCE_LINK.SOURCE_ID.eq(sourceId)));
	}

	private int countFreeformSourceLinksBySourceId(Long sourceId) {

		return create.fetchCount(DSL.select(FREEFORM_SOURCE_LINK.ID)
				.from(FREEFORM_SOURCE_LINK)
				.where(FREEFORM_SOURCE_LINK.SOURCE_ID.eq(sourceId)));
	}

	private int countLexemeSourceLinksBySourceId(Long sourceId) {

		return create.fetchCount(DSL.select(LEXEME_SOURCE_LINK.ID)
				.from(LEXEME_SOURCE_LINK)
				.where(LEXEME_SOURCE_LINK.SOURCE_ID.eq(sourceId)));
	}

	private int countWordSourceLinksBySourceId(Long sourceId) {

		return create.fetchCount(DSL.select(WORD_ETYMOLOGY_SOURCE_LINK.ID)
				.from(WORD_ETYMOLOGY_SOURCE_LINK)
				.where(WORD_ETYMOLOGY_SOURCE_LINK.SOURCE_ID.eq(sourceId)));
	}

	private Result<FreeformRecord> getSourceFreeformRecords(Long sourceId) {

		return create
				.selectFrom(FREEFORM)
				.where(FREEFORM.ID.in(DSL.select(SOURCE_FREEFORM.FREEFORM_ID)
						.from(SOURCE_FREEFORM)
						.where(SOURCE_FREEFORM.SOURCE_ID.eq(sourceId))))
				.fetch();
	}

}
