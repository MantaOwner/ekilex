/*
 * This file is generated by jOOQ.
*/
package eki.wordweb.data.db.tables;


import eki.wordweb.data.db.Indexes;
import eki.wordweb.data.db.Public;
import eki.wordweb.data.db.tables.records.MviewWwMeaningRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Index;
import org.jooq.Name;
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
        "jOOQ version:3.10.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MviewWwMeaning extends TableImpl<MviewWwMeaningRecord> {

    private static final long serialVersionUID = 947548619;

    /**
     * The reference instance of <code>public.mview_ww_meaning</code>
     */
    public static final MviewWwMeaning MVIEW_WW_MEANING = new MviewWwMeaning();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<MviewWwMeaningRecord> getRecordType() {
        return MviewWwMeaningRecord.class;
    }

    /**
     * The column <code>public.mview_ww_meaning.word_id</code>.
     */
    public final TableField<MviewWwMeaningRecord, Long> WORD_ID = createField("word_id", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.mview_ww_meaning.meaning_id</code>.
     */
    public final TableField<MviewWwMeaningRecord, Long> MEANING_ID = createField("meaning_id", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.mview_ww_meaning.lexeme_id</code>.
     */
    public final TableField<MviewWwMeaningRecord, Long> LEXEME_ID = createField("lexeme_id", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.mview_ww_meaning.definition_id</code>.
     */
    public final TableField<MviewWwMeaningRecord, Long> DEFINITION_ID = createField("definition_id", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.mview_ww_meaning.dataset_code</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> DATASET_CODE = createField("dataset_code", org.jooq.impl.SQLDataType.VARCHAR(10), this, "");

    /**
     * The column <code>public.mview_ww_meaning.level1</code>.
     */
    public final TableField<MviewWwMeaningRecord, Integer> LEVEL1 = createField("level1", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.mview_ww_meaning.level2</code>.
     */
    public final TableField<MviewWwMeaningRecord, Integer> LEVEL2 = createField("level2", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.mview_ww_meaning.level3</code>.
     */
    public final TableField<MviewWwMeaningRecord, Integer> LEVEL3 = createField("level3", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.mview_ww_meaning.lexeme_type_code</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> LEXEME_TYPE_CODE = createField("lexeme_type_code", org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.mview_ww_meaning.lexeme_type_value</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> LEXEME_TYPE_VALUE = createField("lexeme_type_value", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.mview_ww_meaning.lexeme_type_lang</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> LEXEME_TYPE_LANG = createField("lexeme_type_lang", org.jooq.impl.SQLDataType.CHAR(3), this, "");

    /**
     * The column <code>public.mview_ww_meaning.register_code</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> REGISTER_CODE = createField("register_code", org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.mview_ww_meaning.register_value</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> REGISTER_VALUE = createField("register_value", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.mview_ww_meaning.register_lang</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> REGISTER_LANG = createField("register_lang", org.jooq.impl.SQLDataType.CHAR(3), this, "");

    /**
     * The column <code>public.mview_ww_meaning.pos_code</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> POS_CODE = createField("pos_code", org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.mview_ww_meaning.pos_value</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> POS_VALUE = createField("pos_value", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.mview_ww_meaning.pos_lang</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> POS_LANG = createField("pos_lang", org.jooq.impl.SQLDataType.CHAR(3), this, "");

    /**
     * The column <code>public.mview_ww_meaning.deriv_code</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> DERIV_CODE = createField("deriv_code", org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.mview_ww_meaning.deriv_value</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> DERIV_VALUE = createField("deriv_value", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.mview_ww_meaning.deriv_lang</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> DERIV_LANG = createField("deriv_lang", org.jooq.impl.SQLDataType.CHAR(3), this, "");

    /**
     * The column <code>public.mview_ww_meaning.domain_origin</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> DOMAIN_ORIGIN = createField("domain_origin", org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.mview_ww_meaning.domain_code</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> DOMAIN_CODE = createField("domain_code", org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.mview_ww_meaning.domain_value</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> DOMAIN_VALUE = createField("domain_value", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.mview_ww_meaning.domain_lang</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> DOMAIN_LANG = createField("domain_lang", org.jooq.impl.SQLDataType.CHAR(3), this, "");

    /**
     * The column <code>public.mview_ww_meaning.definition</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> DEFINITION = createField("definition", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.mview_ww_meaning.definition_lang</code>.
     */
    public final TableField<MviewWwMeaningRecord, String> DEFINITION_LANG = createField("definition_lang", org.jooq.impl.SQLDataType.CHAR(3), this, "");

    /**
     * Create a <code>public.mview_ww_meaning</code> table reference
     */
    public MviewWwMeaning() {
        this(DSL.name("mview_ww_meaning"), null);
    }

    /**
     * Create an aliased <code>public.mview_ww_meaning</code> table reference
     */
    public MviewWwMeaning(String alias) {
        this(DSL.name(alias), MVIEW_WW_MEANING);
    }

    /**
     * Create an aliased <code>public.mview_ww_meaning</code> table reference
     */
    public MviewWwMeaning(Name alias) {
        this(alias, MVIEW_WW_MEANING);
    }

    private MviewWwMeaning(Name alias, Table<MviewWwMeaningRecord> aliased) {
        this(alias, aliased, null);
    }

    private MviewWwMeaning(Name alias, Table<MviewWwMeaningRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
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
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.MVIEW_WW_MEANING_DEFINITION_LANG_IDX, Indexes.MVIEW_WW_MEANING_DERIV_LANG_IDX, Indexes.MVIEW_WW_MEANING_DOMAIN_LANG_IDX, Indexes.MVIEW_WW_MEANING_LEXEME_ID_IDX, Indexes.MVIEW_WW_MEANING_LEXEME_TYPE_LANG_IDX, Indexes.MVIEW_WW_MEANING_MEANING_ID_IDX, Indexes.MVIEW_WW_MEANING_POS_LANG_IDX, Indexes.MVIEW_WW_MEANING_REGISTER_LANG_IDX, Indexes.MVIEW_WW_MEANING_WORD_ID_IDX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwMeaning as(String alias) {
        return new MviewWwMeaning(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwMeaning as(Name alias) {
        return new MviewWwMeaning(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public MviewWwMeaning rename(String name) {
        return new MviewWwMeaning(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public MviewWwMeaning rename(Name name) {
        return new MviewWwMeaning(name, null);
    }
}
