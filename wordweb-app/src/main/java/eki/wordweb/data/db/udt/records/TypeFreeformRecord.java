/*
 * This file is generated by jOOQ.
 */
package eki.wordweb.data.db.udt.records;


import eki.wordweb.data.db.udt.TypeFreeform;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UDTRecordImpl;


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
public class TypeFreeformRecord extends UDTRecordImpl<TypeFreeformRecord> implements Record4<Long, String, String, String> {

    private static final long serialVersionUID = -542168185;

    /**
     * Setter for <code>public.type_freeform.freeform_id</code>.
     */
    public void setFreeformId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.type_freeform.freeform_id</code>.
     */
    public Long getFreeformId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.type_freeform.type</code>.
     */
    public void setType(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.type_freeform.type</code>.
     */
    public String getType() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.type_freeform.value</code>.
     */
    public void setValue(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.type_freeform.value</code>.
     */
    public String getValue() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.type_freeform.complexity</code>.
     */
    public void setComplexity(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.type_freeform.complexity</code>.
     */
    public String getComplexity() {
        return (String) get(3);
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<Long, String, String, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<Long, String, String, String> valuesRow() {
        return (Row4) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return TypeFreeform.FREEFORM_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return TypeFreeform.TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return TypeFreeform.VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return TypeFreeform.COMPLEXITY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getFreeformId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getComplexity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getFreeformId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getComplexity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeFreeformRecord value1(Long value) {
        setFreeformId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeFreeformRecord value2(String value) {
        setType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeFreeformRecord value3(String value) {
        setValue(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeFreeformRecord value4(String value) {
        setComplexity(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeFreeformRecord values(Long value1, String value2, String value3, String value4) {
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
     * Create a detached TypeFreeformRecord
     */
    public TypeFreeformRecord() {
        super(TypeFreeform.TYPE_FREEFORM);
    }

    /**
     * Create a detached, initialised TypeFreeformRecord
     */
    public TypeFreeformRecord(Long freeformId, String type, String value, String complexity) {
        super(TypeFreeform.TYPE_FREEFORM);

        set(0, freeformId);
        set(1, type);
        set(2, value);
        set(3, complexity);
    }
}