/*
 * This file is generated by jOOQ.
 */
package eki.ekilex.data.db.tables;


import eki.ekilex.data.db.Public;
import eki.ekilex.data.db.tables.records.ViewWwCollocationRecord;
import eki.ekilex.data.db.udt.records.TypeCollocMemberRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row15;
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
public class ViewWwCollocation extends TableImpl<ViewWwCollocationRecord> {

    private static final long serialVersionUID = -184708554;

    /**
     * The reference instance of <code>public.view_ww_collocation</code>
     */
    public static final ViewWwCollocation VIEW_WW_COLLOCATION = new ViewWwCollocation();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ViewWwCollocationRecord> getRecordType() {
        return ViewWwCollocationRecord.class;
    }

    /**
     * The column <code>public.view_ww_collocation.lexeme_id</code>.
     */
    public final TableField<ViewWwCollocationRecord, Long> LEXEME_ID = createField(DSL.name("lexeme_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.view_ww_collocation.word_id</code>.
     */
    public final TableField<ViewWwCollocationRecord, Long> WORD_ID = createField(DSL.name("word_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.view_ww_collocation.pos_group_id</code>.
     */
    public final TableField<ViewWwCollocationRecord, Long> POS_GROUP_ID = createField(DSL.name("pos_group_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.view_ww_collocation.pos_group_code</code>.
     */
    public final TableField<ViewWwCollocationRecord, String> POS_GROUP_CODE = createField(DSL.name("pos_group_code"), org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.view_ww_collocation.pos_group_order_by</code>.
     */
    public final TableField<ViewWwCollocationRecord, Long> POS_GROUP_ORDER_BY = createField(DSL.name("pos_group_order_by"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.view_ww_collocation.rel_group_id</code>.
     */
    public final TableField<ViewWwCollocationRecord, Long> REL_GROUP_ID = createField(DSL.name("rel_group_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.view_ww_collocation.rel_group_name</code>.
     */
    public final TableField<ViewWwCollocationRecord, String> REL_GROUP_NAME = createField(DSL.name("rel_group_name"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.view_ww_collocation.rel_group_order_by</code>.
     */
    public final TableField<ViewWwCollocationRecord, Long> REL_GROUP_ORDER_BY = createField(DSL.name("rel_group_order_by"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.view_ww_collocation.colloc_group_order</code>.
     */
    public final TableField<ViewWwCollocationRecord, Integer> COLLOC_GROUP_ORDER = createField(DSL.name("colloc_group_order"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.view_ww_collocation.colloc_id</code>.
     */
    public final TableField<ViewWwCollocationRecord, Long> COLLOC_ID = createField(DSL.name("colloc_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.view_ww_collocation.colloc_value</code>.
     */
    public final TableField<ViewWwCollocationRecord, String> COLLOC_VALUE = createField(DSL.name("colloc_value"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.view_ww_collocation.colloc_definition</code>.
     */
    public final TableField<ViewWwCollocationRecord, String> COLLOC_DEFINITION = createField(DSL.name("colloc_definition"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.view_ww_collocation.colloc_usages</code>.
     */
    public final TableField<ViewWwCollocationRecord, String[]> COLLOC_USAGES = createField(DSL.name("colloc_usages"), org.jooq.impl.SQLDataType.CLOB.getArrayDataType(), this, "");

    /**
     * The column <code>public.view_ww_collocation.colloc_members</code>.
     */
    public final TableField<ViewWwCollocationRecord, TypeCollocMemberRecord[]> COLLOC_MEMBERS = createField(DSL.name("colloc_members"), eki.ekilex.data.db.udt.TypeCollocMember.TYPE_COLLOC_MEMBER.getDataType().getArrayDataType(), this, "");

    /**
     * The column <code>public.view_ww_collocation.complexity</code>.
     */
    public final TableField<ViewWwCollocationRecord, String> COMPLEXITY = createField(DSL.name("complexity"), org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * Create a <code>public.view_ww_collocation</code> table reference
     */
    public ViewWwCollocation() {
        this(DSL.name("view_ww_collocation"), null);
    }

    /**
     * Create an aliased <code>public.view_ww_collocation</code> table reference
     */
    public ViewWwCollocation(String alias) {
        this(DSL.name(alias), VIEW_WW_COLLOCATION);
    }

    /**
     * Create an aliased <code>public.view_ww_collocation</code> table reference
     */
    public ViewWwCollocation(Name alias) {
        this(alias, VIEW_WW_COLLOCATION);
    }

    private ViewWwCollocation(Name alias, Table<ViewWwCollocationRecord> aliased) {
        this(alias, aliased, null);
    }

    private ViewWwCollocation(Name alias, Table<ViewWwCollocationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("create view \"view_ww_collocation\" as  SELECT l1.id AS lexeme_id,\n    l1.word_id,\n    pgr1.id AS pos_group_id,\n    pgr1.pos_group_code,\n    pgr1.order_by AS pos_group_order_by,\n    rgr1.id AS rel_group_id,\n    rgr1.name AS rel_group_name,\n    rgr1.order_by AS rel_group_order_by,\n    lc1.group_order AS colloc_group_order,\n    c.id AS colloc_id,\n    c.value AS colloc_value,\n    c.definition AS colloc_definition,\n    c.usages AS colloc_usages,\n    array_agg(ROW(lw2.lexeme_id, lw2.word_id, (' '::text || lw2.word), (' '::text || lc2.member_form), lw2.homonym_nr, lw2.word_exists, lc2.conjunct, lc2.weight)::type_colloc_member ORDER BY lc2.member_order) AS colloc_members,\n    c.complexity\n   FROM (((((((collocation c\n     JOIN lex_colloc lc1 ON ((lc1.collocation_id = c.id)))\n     JOIN lex_colloc lc2 ON ((lc2.collocation_id = c.id)))\n     JOIN lexeme l1 ON ((l1.id = lc1.lexeme_id)))\n     JOIN dataset l1ds ON (((l1ds.code)::text = (l1.dataset_code)::text)))\n     JOIN ( SELECT DISTINCT l2.id AS lexeme_id,\n            l2.word_id,\n            f2.value AS word,\n            w2.homonym_nr,\n            ((f2.mode)::text = 'WORD'::text) AS word_exists\n           FROM lexeme l2,\n            word w2,\n            paradigm p2,\n            form f2,\n            dataset l2ds\n          WHERE (((l2.type)::text = 'PRIMARY'::text) AND ((l2.process_state_code)::text = 'avalik'::text) AND ((l2ds.code)::text = (l2.dataset_code)::text) AND (l2ds.is_public = true) AND (l2.word_id = w2.id) AND (p2.word_id = w2.id) AND (f2.paradigm_id = p2.id) AND ((f2.mode)::text = ANY ((ARRAY['WORD'::character varying, 'UNKNOWN'::character varying])::text[])))) lw2 ON ((lw2.lexeme_id = lc2.lexeme_id)))\n     JOIN lex_colloc_rel_group rgr1 ON ((lc1.rel_group_id = rgr1.id)))\n     JOIN lex_colloc_pos_group pgr1 ON ((pgr1.id = rgr1.pos_group_id)))\n  WHERE (((l1.type)::text = 'PRIMARY'::text) AND ((l1.process_state_code)::text = 'avalik'::text) AND (l1ds.is_public = true))\n  GROUP BY l1.id, c.id, pgr1.id, rgr1.id, lc1.id\n  ORDER BY l1.level1, l1.level2, pgr1.order_by, rgr1.order_by, lc1.group_order, c.id;"));
    }

    public <O extends Record> ViewWwCollocation(Table<O> child, ForeignKey<O, ViewWwCollocationRecord> key) {
        super(child, key, VIEW_WW_COLLOCATION);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public ViewWwCollocation as(String alias) {
        return new ViewWwCollocation(DSL.name(alias), this);
    }

    @Override
    public ViewWwCollocation as(Name alias) {
        return new ViewWwCollocation(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ViewWwCollocation rename(String name) {
        return new ViewWwCollocation(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ViewWwCollocation rename(Name name) {
        return new ViewWwCollocation(name, null);
    }

    // -------------------------------------------------------------------------
    // Row15 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row15<Long, Long, Long, String, Long, Long, String, Long, Integer, Long, String, String, String[], TypeCollocMemberRecord[], String> fieldsRow() {
        return (Row15) super.fieldsRow();
    }
}
