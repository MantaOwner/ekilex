/*
 * This file is generated by jOOQ.
*/
package eki.ekilex.data.db.tables;


import eki.ekilex.data.db.Indexes;
import eki.ekilex.data.db.Keys;
import eki.ekilex.data.db.Public;
import eki.ekilex.data.db.tables.records.LifecycleLogRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
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
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LifecycleLog extends TableImpl<LifecycleLogRecord> {

    private static final long serialVersionUID = -372271683;

    /**
     * The reference instance of <code>public.lifecycle_log</code>
     */
    public static final LifecycleLog LIFECYCLE_LOG = new LifecycleLog();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<LifecycleLogRecord> getRecordType() {
        return LifecycleLogRecord.class;
    }

    /**
     * The column <code>public.lifecycle_log.id</code>.
     */
    public final TableField<LifecycleLogRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('lifecycle_log_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>public.lifecycle_log.owner_id</code>.
     */
    public final TableField<LifecycleLogRecord, Long> OWNER_ID = createField("owner_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.lifecycle_log.owner_name</code>.
     */
    public final TableField<LifecycleLogRecord, String> OWNER_NAME = createField("owner_name", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>public.lifecycle_log.type</code>.
     */
    public final TableField<LifecycleLogRecord, String> TYPE = createField("type", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>public.lifecycle_log.event_by</code>.
     */
    public final TableField<LifecycleLogRecord, String> EVENT_BY = createField("event_by", org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.lifecycle_log.event_on</code>.
     */
    public final TableField<LifecycleLogRecord, Timestamp> EVENT_ON = createField("event_on", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");

    /**
     * Create a <code>public.lifecycle_log</code> table reference
     */
    public LifecycleLog() {
        this(DSL.name("lifecycle_log"), null);
    }

    /**
     * Create an aliased <code>public.lifecycle_log</code> table reference
     */
    public LifecycleLog(String alias) {
        this(DSL.name(alias), LIFECYCLE_LOG);
    }

    /**
     * Create an aliased <code>public.lifecycle_log</code> table reference
     */
    public LifecycleLog(Name alias) {
        this(alias, LIFECYCLE_LOG);
    }

    private LifecycleLog(Name alias, Table<LifecycleLogRecord> aliased) {
        this(alias, aliased, null);
    }

    private LifecycleLog(Name alias, Table<LifecycleLogRecord> aliased, Field<?>[] parameters) {
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
        return Arrays.<Index>asList(Indexes.LIFECYCLE_LOG_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<LifecycleLogRecord, Long> getIdentity() {
        return Keys.IDENTITY_LIFECYCLE_LOG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<LifecycleLogRecord> getPrimaryKey() {
        return Keys.LIFECYCLE_LOG_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<LifecycleLogRecord>> getKeys() {
        return Arrays.<UniqueKey<LifecycleLogRecord>>asList(Keys.LIFECYCLE_LOG_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LifecycleLog as(String alias) {
        return new LifecycleLog(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LifecycleLog as(Name alias) {
        return new LifecycleLog(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public LifecycleLog rename(String name) {
        return new LifecycleLog(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public LifecycleLog rename(Name name) {
        return new LifecycleLog(name, null);
    }
}