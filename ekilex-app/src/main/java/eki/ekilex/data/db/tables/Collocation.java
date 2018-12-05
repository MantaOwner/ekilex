/*
 * This file is generated by jOOQ.
 */
package eki.ekilex.data.db.tables;


import eki.ekilex.data.db.Indexes;
import eki.ekilex.data.db.Keys;
import eki.ekilex.data.db.Public;
import eki.ekilex.data.db.tables.records.CollocationRecord;

import java.math.BigDecimal;
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
        "jOOQ version:3.11.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Collocation extends TableImpl<CollocationRecord> {

    private static final long serialVersionUID = -1135431805;

    /**
     * The reference instance of <code>public.collocation</code>
     */
    public static final Collocation COLLOCATION = new Collocation();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CollocationRecord> getRecordType() {
        return CollocationRecord.class;
    }

    /**
     * The column <code>public.collocation.id</code>.
     */
    public final TableField<CollocationRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('collocation_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>public.collocation.value</code>.
     */
    public final TableField<CollocationRecord, String> VALUE = createField("value", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.collocation.definition</code>.
     */
    public final TableField<CollocationRecord, String> DEFINITION = createField("definition", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.collocation.frequency</code>.
     */
    public final TableField<CollocationRecord, BigDecimal> FREQUENCY = createField("frequency", org.jooq.impl.SQLDataType.NUMERIC(14, 4), this, "");

    /**
     * The column <code>public.collocation.score</code>.
     */
    public final TableField<CollocationRecord, BigDecimal> SCORE = createField("score", org.jooq.impl.SQLDataType.NUMERIC(14, 4), this, "");

    /**
     * The column <code>public.collocation.usages</code>.
     */
    public final TableField<CollocationRecord, String[]> USAGES = createField("usages", org.jooq.impl.SQLDataType.CLOB.getArrayDataType(), this, "");

    /**
     * Create a <code>public.collocation</code> table reference
     */
    public Collocation() {
        this(DSL.name("collocation"), null);
    }

    /**
     * Create an aliased <code>public.collocation</code> table reference
     */
    public Collocation(String alias) {
        this(DSL.name(alias), COLLOCATION);
    }

    /**
     * Create an aliased <code>public.collocation</code> table reference
     */
    public Collocation(Name alias) {
        this(alias, COLLOCATION);
    }

    private Collocation(Name alias, Table<CollocationRecord> aliased) {
        this(alias, aliased, null);
    }

    private Collocation(Name alias, Table<CollocationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Collocation(Table<O> child, ForeignKey<O, CollocationRecord> key) {
        super(child, key, COLLOCATION);
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
        return Arrays.<Index>asList(Indexes.COLLOCATION_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<CollocationRecord, Long> getIdentity() {
        return Keys.IDENTITY_COLLOCATION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CollocationRecord> getPrimaryKey() {
        return Keys.COLLOCATION_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CollocationRecord>> getKeys() {
        return Arrays.<UniqueKey<CollocationRecord>>asList(Keys.COLLOCATION_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collocation as(String alias) {
        return new Collocation(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collocation as(Name alias) {
        return new Collocation(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Collocation rename(String name) {
        return new Collocation(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Collocation rename(Name name) {
        return new Collocation(name, null);
    }
}
