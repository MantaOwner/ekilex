/*
 * This file is generated by jOOQ.
 */
package eki.ekilex.data.db.tables;


import eki.ekilex.data.db.Public;
import eki.ekilex.data.db.tables.records.ViewWwWordRecord;
import eki.ekilex.data.db.udt.records.TypeDefinitionRecord;
import eki.ekilex.data.db.udt.records.TypeLangComplexityRecord;
import eki.ekilex.data.db.udt.records.TypeMeaningWordRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row20;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ViewWwWord extends TableImpl<ViewWwWordRecord> {

    private static final long serialVersionUID = -1090403096;

    /**
     * The reference instance of <code>public.view_ww_word</code>
     */
    public static final ViewWwWord VIEW_WW_WORD = new ViewWwWord();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ViewWwWordRecord> getRecordType() {
        return ViewWwWordRecord.class;
    }

    /**
     * The column <code>public.view_ww_word.word_id</code>.
     */
    public final TableField<ViewWwWordRecord, Long> WORD_ID = createField(DSL.name("word_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.view_ww_word.word</code>.
     */
    public final TableField<ViewWwWordRecord, String> WORD = createField(DSL.name("word"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.view_ww_word.word_prese</code>.
     */
    public final TableField<ViewWwWordRecord, String> WORD_PRESE = createField(DSL.name("word_prese"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.view_ww_word.as_word</code>.
     */
    public final TableField<ViewWwWordRecord, String> AS_WORD = createField(DSL.name("as_word"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.view_ww_word.lang</code>.
     */
    public final TableField<ViewWwWordRecord, String> LANG = createField(DSL.name("lang"), org.jooq.impl.SQLDataType.CHAR(3), this, "");

    /**
     * The column <code>public.view_ww_word.lang_order_by</code>.
     */
    public final TableField<ViewWwWordRecord, Long> LANG_ORDER_BY = createField(DSL.name("lang_order_by"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.view_ww_word.homonym_nr</code>.
     */
    public final TableField<ViewWwWordRecord, Integer> HOMONYM_NR = createField(DSL.name("homonym_nr"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.view_ww_word.word_class</code>.
     */
    public final TableField<ViewWwWordRecord, String> WORD_CLASS = createField(DSL.name("word_class"), org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.view_ww_word.word_type_codes</code>.
     */
    public final TableField<ViewWwWordRecord, String[]> WORD_TYPE_CODES = createField(DSL.name("word_type_codes"), org.jooq.impl.SQLDataType.VARCHAR.getArrayDataType(), this, "");

    /**
     * The column <code>public.view_ww_word.morph_code</code>.
     */
    public final TableField<ViewWwWordRecord, String> MORPH_CODE = createField(DSL.name("morph_code"), org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.view_ww_word.display_morph_code</code>.
     */
    public final TableField<ViewWwWordRecord, String> DISPLAY_MORPH_CODE = createField(DSL.name("display_morph_code"), org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.view_ww_word.aspect_code</code>.
     */
    public final TableField<ViewWwWordRecord, String> ASPECT_CODE = createField(DSL.name("aspect_code"), org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.view_ww_word.dataset_codes</code>.
     */
    public final TableField<ViewWwWordRecord, String[]> DATASET_CODES = createField(DSL.name("dataset_codes"), org.jooq.impl.SQLDataType.VARCHAR.getArrayDataType(), this, "");

    /**
     * The column <code>public.view_ww_word.lang_complexities</code>.
     */
    public final TableField<ViewWwWordRecord, TypeLangComplexityRecord[]> LANG_COMPLEXITIES = createField(DSL.name("lang_complexities"), eki.ekilex.data.db.udt.TypeLangComplexity.TYPE_LANG_COMPLEXITY.getDataType().getArrayDataType(), this, "");

    /**
     * The column <code>public.view_ww_word.meaning_words</code>.
     */
    public final TableField<ViewWwWordRecord, TypeMeaningWordRecord[]> MEANING_WORDS = createField(DSL.name("meaning_words"), eki.ekilex.data.db.udt.TypeMeaningWord.TYPE_MEANING_WORD.getDataType().getArrayDataType(), this, "");

    /**
     * The column <code>public.view_ww_word.definitions</code>.
     */
    public final TableField<ViewWwWordRecord, TypeDefinitionRecord[]> DEFINITIONS = createField(DSL.name("definitions"), eki.ekilex.data.db.udt.TypeDefinition.TYPE_DEFINITION.getDataType().getArrayDataType(), this, "");

    /**
     * The column <code>public.view_ww_word.od_word_recommendations</code>.
     */
    public final TableField<ViewWwWordRecord, String[]> OD_WORD_RECOMMENDATIONS = createField(DSL.name("od_word_recommendations"), org.jooq.impl.SQLDataType.CLOB.getArrayDataType(), this, "");

    /**
     * The column <code>public.view_ww_word.lex_dataset_exists</code>.
     */
    public final TableField<ViewWwWordRecord, Boolean> LEX_DATASET_EXISTS = createField(DSL.name("lex_dataset_exists"), org.jooq.impl.SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>public.view_ww_word.term_dataset_exists</code>.
     */
    public final TableField<ViewWwWordRecord, Boolean> TERM_DATASET_EXISTS = createField(DSL.name("term_dataset_exists"), org.jooq.impl.SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>public.view_ww_word.forms_exist</code>.
     */
    public final TableField<ViewWwWordRecord, Boolean> FORMS_EXIST = createField(DSL.name("forms_exist"), org.jooq.impl.SQLDataType.BOOLEAN, this, "");

    /**
     * Create a <code>public.view_ww_word</code> table reference
     */
    public ViewWwWord() {
        this(DSL.name("view_ww_word"), null);
    }

    /**
     * Create an aliased <code>public.view_ww_word</code> table reference
     */
    public ViewWwWord(String alias) {
        this(DSL.name(alias), VIEW_WW_WORD);
    }

    /**
     * Create an aliased <code>public.view_ww_word</code> table reference
     */
    public ViewWwWord(Name alias) {
        this(alias, VIEW_WW_WORD);
    }

    private ViewWwWord(Name alias, Table<ViewWwWordRecord> aliased) {
        this(alias, aliased, null);
    }

    private ViewWwWord(Name alias, Table<ViewWwWordRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("create view \"view_ww_word\" as  SELECT w.word_id,\n    w.word,\n    w.word_prese,\n    w.as_word,\n    w.lang,\n    w.lang_order_by,\n    w.homonym_nr,\n    w.word_class,\n    wt.word_type_codes,\n    w.morph_code,\n    w.display_morph_code,\n    w.aspect_code,\n    w.dataset_codes,\n    lc.lang_complexities,\n    mw.meaning_words,\n    wd.definitions,\n    od_ws.od_word_recommendations,\n    w.lex_dataset_exists,\n    w.term_dataset_exists,\n    w.forms_exist\n   FROM (((((( SELECT w_1.id AS word_id,\n            array_to_string(array_agg(DISTINCT f.value), ','::text, '*'::text) AS word,\n            array_to_string(array_agg(DISTINCT f.value_prese), ','::text, '*'::text) AS word_prese,\n            (( SELECT array_agg(DISTINCT f_1.value) AS array_agg\n                   FROM paradigm p_1,\n                    form f_1\n                  WHERE ((p_1.word_id = w_1.id) AND (f_1.paradigm_id = p_1.id) AND ((f_1.mode)::text = 'AS_WORD'::text))))[1] AS as_word,\n            w_1.lang,\n            ( SELECT lc_1.order_by\n                   FROM language lc_1\n                  WHERE (lc_1.code = w_1.lang)\n                 LIMIT 1) AS lang_order_by,\n            w_1.homonym_nr,\n            w_1.word_class,\n            w_1.morph_code,\n            w_1.display_morph_code,\n            w_1.aspect_code,\n            ( SELECT array_agg(DISTINCT l.dataset_code) AS array_agg\n                   FROM lexeme l,\n                    dataset ds\n                  WHERE (((l.type)::text = 'PRIMARY'::text) AND ((l.process_state_code)::text = 'avalik'::text) AND (l.word_id = w_1.id) AND ((ds.code)::text = (l.dataset_code)::text) AND (ds.is_public = true))\n                  GROUP BY w_1.id) AS dataset_codes,\n            ( SELECT (count(l.id) > 0)\n                   FROM lexeme l,\n                    dataset ds\n                  WHERE (((l.type)::text = 'PRIMARY'::text) AND ((l.process_state_code)::text = 'avalik'::text) AND (l.word_id = w_1.id) AND ((ds.code)::text = (l.dataset_code)::text) AND (ds.is_public = true) AND ((ds.type)::text = 'LEX'::text))) AS lex_dataset_exists,\n            ( SELECT (count(l.id) > 0)\n                   FROM lexeme l,\n                    dataset ds\n                  WHERE (((l.type)::text = 'PRIMARY'::text) AND ((l.process_state_code)::text = 'avalik'::text) AND (l.word_id = w_1.id) AND ((ds.code)::text = (l.dataset_code)::text) AND (ds.is_public = true) AND ((ds.type)::text = 'TERM'::text))) AS term_dataset_exists,\n            ( SELECT (count(f_1.id) > 0)\n                   FROM paradigm p_1,\n                    form f_1\n                  WHERE ((p_1.word_id = w_1.id) AND (f_1.paradigm_id = p_1.id) AND ((f_1.mode)::text = 'FORM'::text))) AS forms_exist\n           FROM ((word w_1\n             JOIN paradigm p ON ((p.word_id = w_1.id)))\n             JOIN form f ON (((f.paradigm_id = p.id) AND ((f.mode)::text = 'WORD'::text))))\n          WHERE (EXISTS ( SELECT l.id\n                   FROM lexeme l,\n                    dataset ds\n                  WHERE ((l.word_id = w_1.id) AND ((l.type)::text = 'PRIMARY'::text) AND ((l.process_state_code)::text = 'avalik'::text) AND ((ds.code)::text = (l.dataset_code)::text) AND (ds.is_public = true))))\n          GROUP BY w_1.id) w\n     LEFT JOIN ( SELECT wt_1.word_id,\n            array_agg(wt_1.word_type_code ORDER BY wt_1.order_by) AS word_type_codes\n           FROM word_word_type wt_1\n          GROUP BY wt_1.word_id) wt ON ((wt.word_id = w.word_id)))\n     LEFT JOIN ( SELECT mw_1.word_id,\n            array_agg(ROW(mw_1.lexeme_id, mw_1.meaning_id, mw_1.mw_lex_id, mw_1.mw_lex_complexity, mw_1.mw_lex_weight, NULL::type_freeform[], NULL::character varying(100)[], NULL::character varying(100), mw_1.mw_word_id, (' '::text || mw_1.mw_word), (' '::text || mw_1.mw_word_prese), mw_1.mw_homonym_nr, mw_1.mw_lang, (mw_1.mw_word_type_codes)::character varying(100)[], mw_1.mw_aspect_code)::type_meaning_word ORDER BY mw_1.hw_lex_level1, mw_1.hw_lex_level2, mw_1.hw_lex_order_by, mw_1.mw_lex_order_by) AS meaning_words\n           FROM ( SELECT DISTINCT l1.word_id,\n                    l1.id AS lexeme_id,\n                    l1.meaning_id,\n                    l1.level1 AS hw_lex_level1,\n                    l1.level2 AS hw_lex_level2,\n                    l1.order_by AS hw_lex_order_by,\n                    l2.id AS mw_lex_id,\n                    l2.complexity AS mw_lex_complexity,\n                    l2.weight AS mw_lex_weight,\n                    w2.id AS mw_word_id,\n                    (' '::text || f2.value) AS mw_word,\n                    (' '::text || f2.value_prese) AS mw_word_prese,\n                    w2.homonym_nr AS mw_homonym_nr,\n                    w2.lang AS mw_lang,\n                    ( SELECT array_agg(wt_1.word_type_code ORDER BY wt_1.order_by) AS array_agg\n                           FROM word_word_type wt_1\n                          WHERE (wt_1.word_id = w2.id)\n                          GROUP BY wt_1.word_id) AS mw_word_type_codes,\n                    w2.aspect_code AS mw_aspect_code,\n                    l2.order_by AS mw_lex_order_by\n                   FROM ((((((lexeme l1\n                     JOIN dataset l1ds ON (((l1ds.code)::text = (l1.dataset_code)::text)))\n                     JOIN lexeme l2 ON (((l2.meaning_id = l1.meaning_id) AND (l2.word_id <> l1.word_id))))\n                     JOIN dataset l2ds ON (((l2ds.code)::text = (l2.dataset_code)::text)))\n                     JOIN word w2 ON ((w2.id = l2.word_id)))\n                     JOIN paradigm p2 ON ((p2.word_id = w2.id)))\n                     JOIN form f2 ON (((f2.paradigm_id = p2.id) AND ((f2.mode)::text = 'WORD'::text))))\n                  WHERE (((l1.type)::text = 'PRIMARY'::text) AND ((l1.process_state_code)::text = 'avalik'::text) AND (l1ds.is_public = true) AND ((l2.process_state_code)::text = 'avalik'::text) AND (l2ds.is_public = true))) mw_1\n          GROUP BY mw_1.word_id) mw ON ((mw.word_id = w.word_id)))\n     JOIN ( SELECT lc_1.word_id,\n            array_agg(DISTINCT ROW((\n                CASE\n                    WHEN (lc_1.lang = ANY (ARRAY['est'::bpchar, 'rus'::bpchar, 'eng'::bpchar])) THEN lc_1.lang\n                    ELSE 'other'::bpchar\n                END)::character varying(10), (rtrim((lc_1.complexity)::text, '12'::text))::character varying(100))::type_lang_complexity) AS lang_complexities\n           FROM ( SELECT l1.word_id,\n                    w2.lang,\n                    l2.complexity\n                   FROM ((((lexeme l1\n                     JOIN dataset l1ds ON (((l1ds.code)::text = (l1.dataset_code)::text)))\n                     JOIN lexeme l2 ON (((l2.meaning_id = l1.meaning_id) AND ((l2.dataset_code)::text = (l1.dataset_code)::text) AND (l2.word_id <> l1.word_id))))\n                     JOIN dataset l2ds ON (((l2ds.code)::text = (l2.dataset_code)::text)))\n                     JOIN word w2 ON ((w2.id = l2.word_id)))\n                  WHERE (((l1.type)::text = 'PRIMARY'::text) AND ((l1.process_state_code)::text = 'avalik'::text) AND (l1ds.is_public = true) AND ((l2.process_state_code)::text = 'avalik'::text) AND (l2ds.is_public = true))\n                UNION ALL\n                 SELECT l.word_id,\n                    COALESCE(ff.lang, w_1.lang) AS lang,\n                    ff.complexity\n                   FROM word w_1,\n                    lexeme l,\n                    lexeme_freeform lff,\n                    freeform ff,\n                    dataset ds\n                  WHERE (((l.type)::text = 'PRIMARY'::text) AND ((l.process_state_code)::text = 'avalik'::text) AND ((ds.code)::text = (l.dataset_code)::text) AND (ds.is_public = true) AND (l.word_id = w_1.id) AND (lff.lexeme_id = l.id) AND (lff.freeform_id = ff.id) AND (ff.is_public = true) AND ((ff.type)::text = ANY ((ARRAY['USAGE'::character varying, 'GRAMMAR'::character varying, 'GOVERNMENT'::character varying, 'NOTE'::character varying])::text[])))\n                UNION ALL\n                 SELECT l.word_id,\n                    ut.lang,\n                    u.complexity\n                   FROM lexeme l,\n                    lexeme_freeform lff,\n                    freeform u,\n                    freeform ut,\n                    dataset ds\n                  WHERE (((l.type)::text = 'PRIMARY'::text) AND ((l.process_state_code)::text = 'avalik'::text) AND ((ds.code)::text = (l.dataset_code)::text) AND (ds.is_public = true) AND (lff.lexeme_id = l.id) AND (lff.freeform_id = u.id) AND ((u.type)::text = 'USAGE'::text) AND (u.is_public = true) AND (ut.parent_id = u.id) AND ((ut.type)::text = 'USAGE_TRANSLATION'::text))\n                UNION ALL\n                 SELECT l.word_id,\n                    d.lang,\n                    d.complexity\n                   FROM lexeme l,\n                    definition d,\n                    dataset ds\n                  WHERE (((l.type)::text = 'PRIMARY'::text) AND ((l.process_state_code)::text = 'avalik'::text) AND ((ds.code)::text = (l.dataset_code)::text) AND (ds.is_public = true) AND (l.meaning_id = d.meaning_id) AND (d.is_public = true))\n                UNION ALL\n                 SELECT r.word1_id AS word_id,\n                    w2.lang,\n                    l2.complexity\n                   FROM word_relation r,\n                    lexeme l2,\n                    word w2,\n                    dataset ds\n                  WHERE ((w2.id = r.word2_id) AND (l2.word_id = w2.id) AND ((l2.type)::text = 'PRIMARY'::text) AND ((l2.process_state_code)::text = 'avalik'::text) AND ((ds.code)::text = (l2.dataset_code)::text) AND (ds.is_public = true))) lc_1\n          GROUP BY lc_1.word_id) lc ON ((lc.word_id = w.word_id)))\n     LEFT JOIN ( SELECT wd_1.word_id,\n            array_agg(ROW(wd_1.lexeme_id, wd_1.meaning_id, wd_1.definition_id, (' '::text || wd_1.value), (' '::text || wd_1.value_prese), wd_1.lang, wd_1.complexity, NULL::text[])::type_definition ORDER BY wd_1.ds_order_by, wd_1.level1, wd_1.level2, wd_1.lex_order_by, wd_1.def_order_by) AS definitions\n           FROM ( SELECT l.word_id,\n                    l.id AS lexeme_id,\n                    l.meaning_id,\n                    l.level1,\n                    l.level2,\n                    l.order_by AS lex_order_by,\n                    d.id AS definition_id,\n                    \"substring\"(d.value, 1, 200) AS value,\n                    \"substring\"(d.value_prese, 1, 200) AS value_prese,\n                    d.lang,\n                    d.complexity,\n                    d.order_by AS def_order_by,\n                    ds.order_by AS ds_order_by\n                   FROM ((lexeme l\n                     JOIN dataset ds ON (((ds.code)::text = (l.dataset_code)::text)))\n                     JOIN definition d ON (((d.meaning_id = l.meaning_id) AND (d.is_public = true))))\n                  WHERE (((l.type)::text = 'PRIMARY'::text) AND ((l.process_state_code)::text = 'avalik'::text) AND (ds.is_public = true))) wd_1\n          GROUP BY wd_1.word_id) wd ON ((wd.word_id = w.word_id)))\n     LEFT JOIN ( SELECT wf.word_id,\n            array_agg(ff.value_prese ORDER BY ff.order_by) AS od_word_recommendations\n           FROM word_freeform wf,\n            freeform ff\n          WHERE ((wf.freeform_id = ff.id) AND ((ff.type)::text = 'OD_WORD_RECOMMENDATION'::text))\n          GROUP BY wf.word_id) od_ws ON ((od_ws.word_id = w.word_id)));"));
    }

    public <O extends Record> ViewWwWord(Table<O> child, ForeignKey<O, ViewWwWordRecord> key) {
        super(child, key, VIEW_WW_WORD);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public ViewWwWord as(String alias) {
        return new ViewWwWord(DSL.name(alias), this);
    }

    @Override
    public ViewWwWord as(Name alias) {
        return new ViewWwWord(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ViewWwWord rename(String name) {
        return new ViewWwWord(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ViewWwWord rename(Name name) {
        return new ViewWwWord(name, null);
    }

    // -------------------------------------------------------------------------
    // Row20 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row20<Long, String, String, String, String, Long, Integer, String, String[], String, String, String, String[], TypeLangComplexityRecord[], TypeMeaningWordRecord[], TypeDefinitionRecord[], String[], Boolean, Boolean, Boolean> fieldsRow() {
        return (Row20) super.fieldsRow();
    }
}
