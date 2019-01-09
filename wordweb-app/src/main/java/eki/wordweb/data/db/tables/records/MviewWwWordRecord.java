/*
 * This file is generated by jOOQ.
 */
package eki.wordweb.data.db.tables.records;


import eki.wordweb.data.db.tables.MviewWwWord;
import eki.wordweb.data.db.udt.records.TypeDefinitionRecord;
import eki.wordweb.data.db.udt.records.TypeWordRecord;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record15;
import org.jooq.Row15;
import org.jooq.impl.TableRecordImpl;


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
public class MviewWwWordRecord extends TableRecordImpl<MviewWwWordRecord> implements Record15<Long, String, Integer, String, String, String, String, String, String, String, String, String[], Integer, TypeWordRecord[], TypeDefinitionRecord[]> {

    private static final long serialVersionUID = 1650565181;

    /**
     * Setter for <code>public.mview_ww_word.word_id</code>.
     */
    public void setWordId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.word_id</code>.
     */
    public Long getWordId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.mview_ww_word.word</code>.
     */
    public void setWord(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.word</code>.
     */
    public String getWord() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.mview_ww_word.homonym_nr</code>.
     */
    public void setHomonymNr(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.homonym_nr</code>.
     */
    public Integer getHomonymNr() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>public.mview_ww_word.word_class</code>.
     */
    public void setWordClass(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.word_class</code>.
     */
    public String getWordClass() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.mview_ww_word.lang</code>.
     */
    public void setLang(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.lang</code>.
     */
    public String getLang() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.mview_ww_word.morph_code</code>.
     */
    public void setMorphCode(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.morph_code</code>.
     */
    public String getMorphCode() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.mview_ww_word.display_morph_code</code>.
     */
    public void setDisplayMorphCode(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.display_morph_code</code>.
     */
    public String getDisplayMorphCode() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.mview_ww_word.type_code</code>.
     */
    public void setTypeCode(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.type_code</code>.
     */
    public String getTypeCode() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.mview_ww_word.aspect_code</code>.
     */
    public void setAspectCode(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.aspect_code</code>.
     */
    public String getAspectCode() {
        return (String) get(8);
    }

    /**
     * Setter for <code>public.mview_ww_word.etymology_year</code>.
     */
    public void setEtymologyYear(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.etymology_year</code>.
     */
    public String getEtymologyYear() {
        return (String) get(9);
    }

    /**
     * Setter for <code>public.mview_ww_word.etymology_type_code</code>.
     */
    public void setEtymologyTypeCode(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.etymology_type_code</code>.
     */
    public String getEtymologyTypeCode() {
        return (String) get(10);
    }

    /**
     * Setter for <code>public.mview_ww_word.dataset_codes</code>.
     */
    public void setDatasetCodes(String... value) {
        set(11, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.dataset_codes</code>.
     */
    public String[] getDatasetCodes() {
        return (String[]) get(11);
    }

    /**
     * Setter for <code>public.mview_ww_word.meaning_count</code>.
     */
    public void setMeaningCount(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.meaning_count</code>.
     */
    public Integer getMeaningCount() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>public.mview_ww_word.meaning_words</code>.
     */
    public void setMeaningWords(TypeWordRecord... value) {
        set(13, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.meaning_words</code>.
     */
    public TypeWordRecord[] getMeaningWords() {
        return (TypeWordRecord[]) get(13);
    }

    /**
     * Setter for <code>public.mview_ww_word.definitions</code>.
     */
    public void setDefinitions(TypeDefinitionRecord... value) {
        set(14, value);
    }

    /**
     * Getter for <code>public.mview_ww_word.definitions</code>.
     */
    public TypeDefinitionRecord[] getDefinitions() {
        return (TypeDefinitionRecord[]) get(14);
    }

    // -------------------------------------------------------------------------
    // Record15 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row15<Long, String, Integer, String, String, String, String, String, String, String, String, String[], Integer, TypeWordRecord[], TypeDefinitionRecord[]> fieldsRow() {
        return (Row15) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row15<Long, String, Integer, String, String, String, String, String, String, String, String, String[], Integer, TypeWordRecord[], TypeDefinitionRecord[]> valuesRow() {
        return (Row15) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return MviewWwWord.MVIEW_WW_WORD.WORD_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return MviewWwWord.MVIEW_WW_WORD.WORD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return MviewWwWord.MVIEW_WW_WORD.HOMONYM_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return MviewWwWord.MVIEW_WW_WORD.WORD_CLASS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return MviewWwWord.MVIEW_WW_WORD.LANG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return MviewWwWord.MVIEW_WW_WORD.MORPH_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return MviewWwWord.MVIEW_WW_WORD.DISPLAY_MORPH_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return MviewWwWord.MVIEW_WW_WORD.TYPE_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return MviewWwWord.MVIEW_WW_WORD.ASPECT_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return MviewWwWord.MVIEW_WW_WORD.ETYMOLOGY_YEAR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return MviewWwWord.MVIEW_WW_WORD.ETYMOLOGY_TYPE_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String[]> field12() {
        return MviewWwWord.MVIEW_WW_WORD.DATASET_CODES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field13() {
        return MviewWwWord.MVIEW_WW_WORD.MEANING_COUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<TypeWordRecord[]> field14() {
        return MviewWwWord.MVIEW_WW_WORD.MEANING_WORDS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<TypeDefinitionRecord[]> field15() {
        return MviewWwWord.MVIEW_WW_WORD.DEFINITIONS;
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
    public Integer component3() {
        return getHomonymNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getWordClass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getLang();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getMorphCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component7() {
        return getDisplayMorphCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getTypeCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component9() {
        return getAspectCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component10() {
        return getEtymologyYear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component11() {
        return getEtymologyTypeCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] component12() {
        return getDatasetCodes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component13() {
        return getMeaningCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeWordRecord[] component14() {
        return getMeaningWords();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeDefinitionRecord[] component15() {
        return getDefinitions();
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
    public Integer value3() {
        return getHomonymNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getWordClass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getLang();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getMorphCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getDisplayMorphCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getTypeCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getAspectCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getEtymologyYear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getEtymologyTypeCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] value12() {
        return getDatasetCodes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value13() {
        return getMeaningCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeWordRecord[] value14() {
        return getMeaningWords();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeDefinitionRecord[] value15() {
        return getDefinitions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value1(Long value) {
        setWordId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value2(String value) {
        setWord(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value3(Integer value) {
        setHomonymNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value4(String value) {
        setWordClass(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value5(String value) {
        setLang(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value6(String value) {
        setMorphCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value7(String value) {
        setDisplayMorphCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value8(String value) {
        setTypeCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value9(String value) {
        setAspectCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value10(String value) {
        setEtymologyYear(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value11(String value) {
        setEtymologyTypeCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value12(String... value) {
        setDatasetCodes(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value13(Integer value) {
        setMeaningCount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value14(TypeWordRecord... value) {
        setMeaningWords(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord value15(TypeDefinitionRecord... value) {
        setDefinitions(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MviewWwWordRecord values(Long value1, String value2, Integer value3, String value4, String value5, String value6, String value7, String value8, String value9, String value10, String value11, String[] value12, Integer value13, TypeWordRecord[] value14, TypeDefinitionRecord[] value15) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        value15(value15);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached MviewWwWordRecord
     */
    public MviewWwWordRecord() {
        super(MviewWwWord.MVIEW_WW_WORD);
    }

    /**
     * Create a detached, initialised MviewWwWordRecord
     */
    public MviewWwWordRecord(Long wordId, String word, Integer homonymNr, String wordClass, String lang, String morphCode, String displayMorphCode, String typeCode, String aspectCode, String etymologyYear, String etymologyTypeCode, String[] datasetCodes, Integer meaningCount, TypeWordRecord[] meaningWords, TypeDefinitionRecord[] definitions) {
        super(MviewWwWord.MVIEW_WW_WORD);

        set(0, wordId);
        set(1, word);
        set(2, homonymNr);
        set(3, wordClass);
        set(4, lang);
        set(5, morphCode);
        set(6, displayMorphCode);
        set(7, typeCode);
        set(8, aspectCode);
        set(9, etymologyYear);
        set(10, etymologyTypeCode);
        set(11, datasetCodes);
        set(12, meaningCount);
        set(13, meaningWords);
        set(14, definitions);
    }
}
