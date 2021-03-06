/*
 * This file is generated by jOOQ.
 */
package eki.ekilex.data.db.tables.records;


import eki.ekilex.data.db.tables.Tag;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TagRecord extends UpdatableRecordImpl<TagRecord> implements Record4<String, Boolean, Long, Boolean> {

    private static final long serialVersionUID = 1460524241;

    /**
     * Setter for <code>public.tag.name</code>.
     */
    public void setName(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.tag.name</code>.
     */
    public String getName() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.tag.set_automatically</code>.
     */
    public void setSetAutomatically(Boolean value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.tag.set_automatically</code>.
     */
    public Boolean getSetAutomatically() {
        return (Boolean) get(1);
    }

    /**
     * Setter for <code>public.tag.order_by</code>.
     */
    public void setOrderBy(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.tag.order_by</code>.
     */
    public Long getOrderBy() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.tag.remove_to_complete</code>.
     */
    public void setRemoveToComplete(Boolean value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.tag.remove_to_complete</code>.
     */
    public Boolean getRemoveToComplete() {
        return (Boolean) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, Boolean, Long, Boolean> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<String, Boolean, Long, Boolean> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Tag.TAG.NAME;
    }

    @Override
    public Field<Boolean> field2() {
        return Tag.TAG.SET_AUTOMATICALLY;
    }

    @Override
    public Field<Long> field3() {
        return Tag.TAG.ORDER_BY;
    }

    @Override
    public Field<Boolean> field4() {
        return Tag.TAG.REMOVE_TO_COMPLETE;
    }

    @Override
    public String component1() {
        return getName();
    }

    @Override
    public Boolean component2() {
        return getSetAutomatically();
    }

    @Override
    public Long component3() {
        return getOrderBy();
    }

    @Override
    public Boolean component4() {
        return getRemoveToComplete();
    }

    @Override
    public String value1() {
        return getName();
    }

    @Override
    public Boolean value2() {
        return getSetAutomatically();
    }

    @Override
    public Long value3() {
        return getOrderBy();
    }

    @Override
    public Boolean value4() {
        return getRemoveToComplete();
    }

    @Override
    public TagRecord value1(String value) {
        setName(value);
        return this;
    }

    @Override
    public TagRecord value2(Boolean value) {
        setSetAutomatically(value);
        return this;
    }

    @Override
    public TagRecord value3(Long value) {
        setOrderBy(value);
        return this;
    }

    @Override
    public TagRecord value4(Boolean value) {
        setRemoveToComplete(value);
        return this;
    }

    @Override
    public TagRecord values(String value1, Boolean value2, Long value3, Boolean value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TagRecord
     */
    public TagRecord() {
        super(Tag.TAG);
    }

    /**
     * Create a detached, initialised TagRecord
     */
    public TagRecord(String name, Boolean setAutomatically, Long orderBy, Boolean removeToComplete) {
        super(Tag.TAG);

        set(0, name);
        set(1, setAutomatically);
        set(2, orderBy);
        set(3, removeToComplete);
    }
}
