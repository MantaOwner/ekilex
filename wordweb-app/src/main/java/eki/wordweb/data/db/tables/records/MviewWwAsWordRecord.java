/*
 * This file is generated by jOOQ.
 */
package eki.wordweb.data.db.tables.records;


import eki.wordweb.data.db.tables.MviewWwAsWord;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.TableRecordImpl;


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
public class MviewWwAsWordRecord extends TableRecordImpl<MviewWwAsWordRecord> implements Record3<Long, String, String> {

    private static final long serialVersionUID = 1210450430;

    /**
     * Setter for <code>public.mview_ww_as_word.word_id</code>.
     */
    public void setWordId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.mview_ww_as_word.word_id</code>.
     */
    public Long getWordId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.mview_ww_as_word.word</code>.
     */
    public void setWord(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.mview_ww_as_word.word</code>.
     */
    public String getWord() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.mview_ww_as_word.as_word</code>.
     */
    public void setAsWord(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.mview_ww_as_word.as_word</code>.
     */
    public String getAsWord() {
        return (String) get(2);
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Long, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Long, String, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return MviewWwAsWord.MVIEW_WW_AS_WORD.WORD_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return MviewWwAsWord.MVIEW_WW_AS_WORD.WORD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return MviewWwAsWord.MVIEW_WW_AS_WORD.AS_WORD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getWordId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getWord();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getAsWord();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getWordId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getWord();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getAsWord();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwAsWordRecord value1(Long value) {
        setWordId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwAsWordRecord value2(String value) {
        setWord(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwAsWordRecord value3(String value) {
        setAsWord(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwAsWordRecord values(Long value1, String value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached MviewWwAsWordRecord
     */
    public MviewWwAsWordRecord() {
        super(MviewWwAsWord.MVIEW_WW_AS_WORD);
    }

    /**
     * Create a detached, initialised MviewWwAsWordRecord
     */
    public MviewWwAsWordRecord(Long wordId, String word, String asWord) {
        super(MviewWwAsWord.MVIEW_WW_AS_WORD);

        set(0, wordId);
        set(1, word);
        set(2, asWord);
    }
}