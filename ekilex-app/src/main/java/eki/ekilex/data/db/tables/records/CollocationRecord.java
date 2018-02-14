/*
 * This file is generated by jOOQ.
*/
package eki.ekilex.data.db.tables.records;


import eki.ekilex.data.db.tables.Collocation;

import java.math.BigDecimal;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CollocationRecord extends UpdatableRecordImpl<CollocationRecord> implements Record6<Long, Long, Long, String, BigDecimal, BigDecimal> {

    private static final long serialVersionUID = 1515600342;

    /**
     * Setter for <code>public.collocation.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.collocation.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.collocation.collocation_rel_group_id</code>.
     */
    public void setCollocationRelGroupId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.collocation.collocation_rel_group_id</code>.
     */
    public Long getCollocationRelGroupId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.collocation.lexeme_id</code>.
     */
    public void setLexemeId(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.collocation.lexeme_id</code>.
     */
    public Long getLexemeId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.collocation.value</code>.
     */
    public void setValue(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.collocation.value</code>.
     */
    public String getValue() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.collocation.frequency</code>.
     */
    public void setFrequency(BigDecimal value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.collocation.frequency</code>.
     */
    public BigDecimal getFrequency() {
        return (BigDecimal) get(4);
    }

    /**
     * Setter for <code>public.collocation.score</code>.
     */
    public void setScore(BigDecimal value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.collocation.score</code>.
     */
    public BigDecimal getScore() {
        return (BigDecimal) get(5);
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
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Long, Long, Long, String, BigDecimal, BigDecimal> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Long, Long, Long, String, BigDecimal, BigDecimal> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Collocation.COLLOCATION.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return Collocation.COLLOCATION.COLLOCATION_REL_GROUP_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return Collocation.COLLOCATION.LEXEME_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Collocation.COLLOCATION.VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field5() {
        return Collocation.COLLOCATION.FREQUENCY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field6() {
        return Collocation.COLLOCATION.SCORE;
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
        return getCollocationRelGroupId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component3() {
        return getLexemeId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal component5() {
        return getFrequency();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal component6() {
        return getScore();
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
        return getCollocationRelGroupId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getLexemeId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value5() {
        return getFrequency();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value6() {
        return getScore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CollocationRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CollocationRecord value2(Long value) {
        setCollocationRelGroupId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CollocationRecord value3(Long value) {
        setLexemeId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CollocationRecord value4(String value) {
        setValue(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CollocationRecord value5(BigDecimal value) {
        setFrequency(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CollocationRecord value6(BigDecimal value) {
        setScore(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CollocationRecord values(Long value1, Long value2, Long value3, String value4, BigDecimal value5, BigDecimal value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached CollocationRecord
     */
    public CollocationRecord() {
        super(Collocation.COLLOCATION);
    }

    /**
     * Create a detached, initialised CollocationRecord
     */
    public CollocationRecord(Long id, Long collocationRelGroupId, Long lexemeId, String value, BigDecimal frequency, BigDecimal score) {
        super(Collocation.COLLOCATION);

        set(0, id);
        set(1, collocationRelGroupId);
        set(2, lexemeId);
        set(3, value);
        set(4, frequency);
        set(5, score);
    }
}
