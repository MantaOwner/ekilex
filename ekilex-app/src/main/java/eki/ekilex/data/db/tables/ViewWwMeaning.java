/*
 * This file is generated by jOOQ.
 */
package eki.ekilex.data.db.tables;


import eki.ekilex.data.db.Public;
import eki.ekilex.data.db.tables.records.ViewWwMeaningRecord;
import eki.ekilex.data.db.udt.records.TypeDefinitionRecord;
import eki.ekilex.data.db.udt.records.TypeDomainRecord;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ViewWwMeaning extends TableImpl<ViewWwMeaningRecord> {

    private static final long serialVersionUID = -535745465;

    /**
     * The reference instance of <code>public.view_ww_meaning</code>
     */
    public static final ViewWwMeaning VIEW_WW_MEANING = new ViewWwMeaning();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ViewWwMeaningRecord> getRecordType() {
        return ViewWwMeaningRecord.class;
    }

    /**
     * The column <code>public.view_ww_meaning.meaning_id</code>.
     */
    public final TableField<ViewWwMeaningRecord, Long> MEANING_ID = createField("meaning_id", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.view_ww_meaning.domain_codes</code>.
     */
    public final TableField<ViewWwMeaningRecord, TypeDomainRecord[]> DOMAIN_CODES = createField("domain_codes", eki.ekilex.data.db.udt.TypeDomain.TYPE_DOMAIN.getDataType().getArrayDataType(), this, "");

    /**
     * The column <code>public.view_ww_meaning.image_files</code>.
     */
    public final TableField<ViewWwMeaningRecord, String[]> IMAGE_FILES = createField("image_files", org.jooq.impl.SQLDataType.CLOB.getArrayDataType(), this, "");

    /**
     * The column <code>public.view_ww_meaning.systematic_polysemy_patterns</code>.
     */
    public final TableField<ViewWwMeaningRecord, String[]> SYSTEMATIC_POLYSEMY_PATTERNS = createField("systematic_polysemy_patterns", org.jooq.impl.SQLDataType.CLOB.getArrayDataType(), this, "");

    /**
     * The column <code>public.view_ww_meaning.semantic_types</code>.
     */
    public final TableField<ViewWwMeaningRecord, String[]> SEMANTIC_TYPES = createField("semantic_types", org.jooq.impl.SQLDataType.CLOB.getArrayDataType(), this, "");

    /**
     * The column <code>public.view_ww_meaning.learner_comments</code>.
     */
    public final TableField<ViewWwMeaningRecord, String[]> LEARNER_COMMENTS = createField("learner_comments", org.jooq.impl.SQLDataType.CLOB.getArrayDataType(), this, "");

    /**
     * The column <code>public.view_ww_meaning.definitions</code>.
     */
    public final TableField<ViewWwMeaningRecord, TypeDefinitionRecord[]> DEFINITIONS = createField("definitions", eki.ekilex.data.db.udt.TypeDefinition.TYPE_DEFINITION.getDataType().getArrayDataType(), this, "");

    /**
     * Create a <code>public.view_ww_meaning</code> table reference
     */
    public ViewWwMeaning() {
        this(DSL.name("view_ww_meaning"), null);
    }

    /**
     * Create an aliased <code>public.view_ww_meaning</code> table reference
     */
    public ViewWwMeaning(String alias) {
        this(DSL.name(alias), VIEW_WW_MEANING);
    }

    /**
     * Create an aliased <code>public.view_ww_meaning</code> table reference
     */
    public ViewWwMeaning(Name alias) {
        this(alias, VIEW_WW_MEANING);
    }

    private ViewWwMeaning(Name alias, Table<ViewWwMeaningRecord> aliased) {
        this(alias, aliased, null);
    }

    private ViewWwMeaning(Name alias, Table<ViewWwMeaningRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> ViewWwMeaning(Table<O> child, ForeignKey<O, ViewWwMeaningRecord> key) {
        super(child, key, VIEW_WW_MEANING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ViewWwMeaning as(String alias) {
        return new ViewWwMeaning(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ViewWwMeaning as(Name alias) {
        return new ViewWwMeaning(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ViewWwMeaning rename(String name) {
        return new ViewWwMeaning(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ViewWwMeaning rename(Name name) {
        return new ViewWwMeaning(name, null);
    }
}
