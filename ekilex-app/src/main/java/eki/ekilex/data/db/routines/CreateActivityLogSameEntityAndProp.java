/*
 * This file is generated by jOOQ.
 */
package eki.ekilex.data.db.routines;


import eki.ekilex.data.db.Public;
import eki.ekilex.data.db.tables.records.LifecycleLogRecord;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.Internal;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CreateActivityLogSameEntityAndProp extends AbstractRoutine<Long> {

    private static final long serialVersionUID = -888197024;

    /**
     * The parameter <code>public.create_activity_log_same_entity_and_prop.RETURN_VALUE</code>.
     */
    public static final Parameter<Long> RETURN_VALUE = Internal.createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.BIGINT, false, false);

    /**
     * The parameter <code>public.create_activity_log_same_entity_and_prop.ll_row</code>.
     */
    public static final Parameter<LifecycleLogRecord> LL_ROW = Internal.createParameter("ll_row", eki.ekilex.data.db.tables.LifecycleLog.LIFECYCLE_LOG.getDataType(), false, false);

    /**
     * The parameter <code>public.create_activity_log_same_entity_and_prop.diff_path</code>.
     */
    public static final Parameter<String> DIFF_PATH = Internal.createParameter("diff_path", org.jooq.impl.SQLDataType.CLOB, false, false);

    /**
     * Create a new routine call instance
     */
    public CreateActivityLogSameEntityAndProp() {
        super("create_activity_log_same_entity_and_prop", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT);

        setReturnParameter(RETURN_VALUE);
        addInParameter(LL_ROW);
        addInParameter(DIFF_PATH);
    }

    /**
     * Set the <code>ll_row</code> parameter IN value to the routine
     */
    public void setLlRow(LifecycleLogRecord value) {
        setValue(LL_ROW, value);
    }

    /**
     * Set the <code>ll_row</code> parameter to the function to be used with a {@link org.jooq.Select} statement
     */
    public void setLlRow(Field<LifecycleLogRecord> field) {
        setField(LL_ROW, field);
    }

    /**
     * Set the <code>diff_path</code> parameter IN value to the routine
     */
    public void setDiffPath(String value) {
        setValue(DIFF_PATH, value);
    }

    /**
     * Set the <code>diff_path</code> parameter to the function to be used with a {@link org.jooq.Select} statement
     */
    public void setDiffPath(Field<String> field) {
        setField(DIFF_PATH, field);
    }
}