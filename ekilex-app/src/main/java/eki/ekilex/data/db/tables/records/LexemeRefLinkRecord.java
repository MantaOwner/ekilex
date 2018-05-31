/*
 * This file is generated by jOOQ.
*/
package eki.ekilex.data.db.tables.records;


import eki.ekilex.data.db.tables.LexemeRefLink;

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
        "jOOQ version:3.10.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LexemeRefLinkRecord extends UpdatableRecordImpl<LexemeRefLinkRecord> implements Record8<Long, Long, String, Long, String, String, String, Long> {

    private static final long serialVersionUID = 1672804150;

    /**
     * Setter for <code>public.lexeme_ref_link.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.lexeme_ref_link.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.lexeme_ref_link.lexeme_id</code>.
     */
    public void setLexemeId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.lexeme_ref_link.lexeme_id</code>.
     */
    public Long getLexemeId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.lexeme_ref_link.ref_type</code>.
     */
    public void setRefType(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.lexeme_ref_link.ref_type</code>.
     */
    public String getRefType() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.lexeme_ref_link.ref_id</code>.
     */
    public void setRefId(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.lexeme_ref_link.ref_id</code>.
     */
    public Long getRefId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>public.lexeme_ref_link.name</code>.
     */
    public void setName(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.lexeme_ref_link.name</code>.
     */
    public String getName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.lexeme_ref_link.value</code>.
     */
    public void setValue(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.lexeme_ref_link.value</code>.
     */
    public String getValue() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.lexeme_ref_link.process_state_code</code>.
     */
    public void setProcessStateCode(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.lexeme_ref_link.process_state_code</code>.
     */
    public String getProcessStateCode() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.lexeme_ref_link.order_by</code>.
     */
    public void setOrderBy(Long value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.lexeme_ref_link.order_by</code>.
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
    public Row8<Long, Long, String, Long, String, String, String, Long> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Long, Long, String, Long, String, String, String, Long> valuesRow() {
        return (Row8) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return LexemeRefLink.LEXEME_REF_LINK.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return LexemeRefLink.LEXEME_REF_LINK.LEXEME_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return LexemeRefLink.LEXEME_REF_LINK.REF_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return LexemeRefLink.LEXEME_REF_LINK.REF_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return LexemeRefLink.LEXEME_REF_LINK.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return LexemeRefLink.LEXEME_REF_LINK.VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return LexemeRefLink.LEXEME_REF_LINK.PROCESS_STATE_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field8() {
        return LexemeRefLink.LEXEME_REF_LINK.ORDER_BY;
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
        return getLexemeId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getRefType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component4() {
        return getRefId();
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
        return getLexemeId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getRefType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value4() {
        return getRefId();
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
    public LexemeRefLinkRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LexemeRefLinkRecord value2(Long value) {
        setLexemeId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LexemeRefLinkRecord value3(String value) {
        setRefType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LexemeRefLinkRecord value4(Long value) {
        setRefId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LexemeRefLinkRecord value5(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LexemeRefLinkRecord value6(String value) {
        setValue(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LexemeRefLinkRecord value7(String value) {
        setProcessStateCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LexemeRefLinkRecord value8(Long value) {
        setOrderBy(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LexemeRefLinkRecord values(Long value1, Long value2, String value3, Long value4, String value5, String value6, String value7, Long value8) {
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
     * Create a detached LexemeRefLinkRecord
     */
    public LexemeRefLinkRecord() {
        super(LexemeRefLink.LEXEME_REF_LINK);
    }

    /**
     * Create a detached, initialised LexemeRefLinkRecord
     */
    public LexemeRefLinkRecord(Long id, Long lexemeId, String refType, Long refId, String name, String value, String processStateCode, Long orderBy) {
        super(LexemeRefLink.LEXEME_REF_LINK);

        set(0, id);
        set(1, lexemeId);
        set(2, refType);
        set(3, refId);
        set(4, name);
        set(5, value);
        set(6, processStateCode);
        set(7, orderBy);
    }
}