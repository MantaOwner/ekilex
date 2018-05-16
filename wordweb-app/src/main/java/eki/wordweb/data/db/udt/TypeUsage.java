/*
 * This file is generated by jOOQ.
*/
package eki.wordweb.data.db.udt;


import eki.wordweb.data.db.Public;
import eki.wordweb.data.db.udt.records.TypeUsageRecord;

import javax.annotation.Generated;

import org.jooq.Schema;
import org.jooq.UDTField;
import org.jooq.impl.UDTImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TypeUsage extends UDTImpl<TypeUsageRecord> {

    private static final long serialVersionUID = 738912386;

    /**
     * The reference instance of <code>public.type_usage</code>
     */
    public static final TypeUsage TYPE_USAGE = new TypeUsage();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TypeUsageRecord> getRecordType() {
        return TypeUsageRecord.class;
    }

    /**
     * The attribute <code>public.type_usage.usage</code>.
     */
    public static final UDTField<TypeUsageRecord, String> USAGE = createField("usage", org.jooq.impl.SQLDataType.CLOB, TYPE_USAGE, "");

    /**
     * The attribute <code>public.type_usage.usage_author</code>.
     */
    public static final UDTField<TypeUsageRecord, String> USAGE_AUTHOR = createField("usage_author", org.jooq.impl.SQLDataType.CLOB, TYPE_USAGE, "");

    /**
     * The attribute <code>public.type_usage.usage_translator</code>.
     */
    public static final UDTField<TypeUsageRecord, String> USAGE_TRANSLATOR = createField("usage_translator", org.jooq.impl.SQLDataType.CLOB, TYPE_USAGE, "");

    /**
     * No further instances allowed
     */
    private TypeUsage() {
        super("type_usage", null, null, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }
}
