/*
 * This file is generated by jOOQ.
 */
package eki.ekilex.data.db.tables;


import eki.ekilex.data.db.Indexes;
import eki.ekilex.data.db.Keys;
import eki.ekilex.data.db.Public;
import eki.ekilex.data.db.tables.records.FormRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Form extends TableImpl<FormRecord> {

    private static final long serialVersionUID = -2104887329;

    /**
     * The reference instance of <code>public.form</code>
     */
    public static final Form FORM = new Form();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<FormRecord> getRecordType() {
        return FormRecord.class;
    }

    /**
     * The column <code>public.form.id</code>.
     */
    public final TableField<FormRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('form_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>public.form.paradigm_id</code>.
     */
    public final TableField<FormRecord, Long> PARADIGM_ID = createField("paradigm_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.form.mode</code>.
     */
    public final TableField<FormRecord, String> MODE = createField("mode", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>public.form.morph_group1</code>.
     */
    public final TableField<FormRecord, String> MORPH_GROUP1 = createField("morph_group1", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.form.morph_group2</code>.
     */
    public final TableField<FormRecord, String> MORPH_GROUP2 = createField("morph_group2", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.form.morph_group3</code>.
     */
    public final TableField<FormRecord, String> MORPH_GROUP3 = createField("morph_group3", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.form.display_level</code>.
     */
    public final TableField<FormRecord, Integer> DISPLAY_LEVEL = createField("display_level", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("1", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.form.morph_code</code>.
     */
    public final TableField<FormRecord, String> MORPH_CODE = createField("morph_code", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>public.form.morph_exists</code>.
     */
    public final TableField<FormRecord, Boolean> MORPH_EXISTS = createField("morph_exists", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>public.form.value</code>.
     */
    public final TableField<FormRecord, String> VALUE = createField("value", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.form.value_prese</code>.
     */
    public final TableField<FormRecord, String> VALUE_PRESE = createField("value_prese", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.form.components</code>.
     */
    public final TableField<FormRecord, String[]> COMPONENTS = createField("components", org.jooq.impl.SQLDataType.VARCHAR(100).getArrayDataType(), this, "");

    /**
     * The column <code>public.form.display_form</code>.
     */
    public final TableField<FormRecord, String> DISPLAY_FORM = createField("display_form", org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.form.vocal_form</code>.
     */
    public final TableField<FormRecord, String> VOCAL_FORM = createField("vocal_form", org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.form.audio_file</code>.
     */
    public final TableField<FormRecord, String> AUDIO_FILE = createField("audio_file", org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.form.order_by</code>.
     */
    public final TableField<FormRecord, Integer> ORDER_BY = createField("order_by", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * Create a <code>public.form</code> table reference
     */
    public Form() {
        this(DSL.name("form"), null);
    }

    /**
     * Create an aliased <code>public.form</code> table reference
     */
    public Form(String alias) {
        this(DSL.name(alias), FORM);
    }

    /**
     * Create an aliased <code>public.form</code> table reference
     */
    public Form(Name alias) {
        this(alias, FORM);
    }

    private Form(Name alias, Table<FormRecord> aliased) {
        this(alias, aliased, null);
    }

    private Form(Name alias, Table<FormRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Form(Table<O> child, ForeignKey<O, FormRecord> key) {
        super(child, key, FORM);
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
        return Arrays.<Index>asList(Indexes.FORM_MODE_IDX, Indexes.FORM_MORPH_CODE_IDX, Indexes.FORM_PARADIGM_ID_IDX, Indexes.FORM_PKEY, Indexes.FORM_VALUE_IDX, Indexes.FORM_VALUE_MODE_IDX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<FormRecord, Long> getIdentity() {
        return Keys.IDENTITY_FORM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<FormRecord> getPrimaryKey() {
        return Keys.FORM_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<FormRecord>> getKeys() {
        return Arrays.<UniqueKey<FormRecord>>asList(Keys.FORM_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<FormRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<FormRecord, ?>>asList(Keys.FORM__FORM_PARADIGM_ID_FKEY, Keys.FORM__FORM_MORPH_CODE_FKEY);
    }

    public Paradigm paradigm() {
        return new Paradigm(this, Keys.FORM__FORM_PARADIGM_ID_FKEY);
    }

    public Morph morph() {
        return new Morph(this, Keys.FORM__FORM_MORPH_CODE_FKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Form as(String alias) {
        return new Form(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Form as(Name alias) {
        return new Form(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Form rename(String name) {
        return new Form(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Form rename(Name name) {
        return new Form(name, null);
    }
}
