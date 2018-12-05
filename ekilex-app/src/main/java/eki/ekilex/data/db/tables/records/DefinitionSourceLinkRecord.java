/*
 * This file is generated by jOOQ.
 */
package eki.ekilex.data.db.tables.records;


import eki.ekilex.data.db.tables.DefinitionSourceLink;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


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
public class DefinitionSourceLinkRecord extends UpdatableRecordImpl<DefinitionSourceLinkRecord> implements Record8<Long, Long, Long, String, String, String, String, Long> {

    private static final long serialVersionUID = -268529778;

    /**
     * Setter for <code>public.definition_source_link.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.definition_source_link.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.definition_source_link.definition_id</code>.
     */
    public void setDefinitionId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.definition_source_link.definition_id</code>.
     */
    public Long getDefinitionId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.definition_source_link.source_id</code>.
     */
    public void setSourceId(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.definition_source_link.source_id</code>.
     */
    public Long getSourceId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.definition_source_link.type</code>.
     */
    public void setType(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.definition_source_link.type</code>.
     */
    public String getType() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.definition_source_link.name</code>.
     */
    public void setName(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.definition_source_link.name</code>.
     */
    public String getName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.definition_source_link.value</code>.
     */
    public void setValue(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.definition_source_link.value</code>.
     */
    public String getValue() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.definition_source_link.process_state_code</code>.
     */
    public void setProcessStateCode(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.definition_source_link.process_state_code</code>.
     */
    public String getProcessStateCode() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.definition_source_link.order_by</code>.
     */
    public void setOrderBy(Long value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.definition_source_link.order_by</code>.
     */
    public Long getOrderBy() {
        return (Long) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Long, Long, Long, String, String, String, String, Long> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Long, Long, Long, String, String, String, String, Long> valuesRow() {
        return (Row8) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return DefinitionSourceLink.DEFINITION_SOURCE_LINK.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return DefinitionSourceLink.DEFINITION_SOURCE_LINK.DEFINITION_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return DefinitionSourceLink.DEFINITION_SOURCE_LINK.SOURCE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return DefinitionSourceLink.DEFINITION_SOURCE_LINK.TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return DefinitionSourceLink.DEFINITION_SOURCE_LINK.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return DefinitionSourceLink.DEFINITION_SOURCE_LINK.VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return DefinitionSourceLink.DEFINITION_SOURCE_LINK.PROCESS_STATE_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field8() {
        return DefinitionSourceLink.DEFINITION_SOURCE_LINK.ORDER_BY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component2() {
        return getDefinitionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component3() {
        return getSourceId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component7() {
        return getProcessStateCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component8() {
        return getOrderBy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value2() {
        return getDefinitionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getSourceId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getProcessStateCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value8() {
        return getOrderBy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefinitionSourceLinkRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefinitionSourceLinkRecord value2(Long value) {
        setDefinitionId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefinitionSourceLinkRecord value3(Long value) {
        setSourceId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefinitionSourceLinkRecord value4(String value) {
        setType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefinitionSourceLinkRecord value5(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefinitionSourceLinkRecord value6(String value) {
        setValue(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefinitionSourceLinkRecord value7(String value) {
        setProcessStateCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefinitionSourceLinkRecord value8(Long value) {
        setOrderBy(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefinitionSourceLinkRecord values(Long value1, Long value2, Long value3, String value4, String value5, String value6, String value7, Long value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached DefinitionSourceLinkRecord
     */
    public DefinitionSourceLinkRecord() {
        super(DefinitionSourceLink.DEFINITION_SOURCE_LINK);
    }

    /**
     * Create a detached, initialised DefinitionSourceLinkRecord
     */
    public DefinitionSourceLinkRecord(Long id, Long definitionId, Long sourceId, String type, String name, String value, String processStateCode, Long orderBy) {
        super(DefinitionSourceLink.DEFINITION_SOURCE_LINK);

        set(0, id);
        set(1, definitionId);
        set(2, sourceId);
        set(3, type);
        set(4, name);
        set(5, value);
        set(6, processStateCode);
        set(7, orderBy);
    }
}
