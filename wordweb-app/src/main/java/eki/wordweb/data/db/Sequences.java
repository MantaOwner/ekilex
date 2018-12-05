/*
 * This file is generated by jOOQ.
 */
package eki.wordweb.data.db;


import javax.annotation.Generated;

import org.jooq.Sequence;
import org.jooq.impl.SequenceImpl;


/**
 * Convenience access to all sequences in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

    /**
     * The sequence <code>public.lexical_decision_data_id_seq</code>
     */
    public static final Sequence<Long> LEXICAL_DECISION_DATA_ID_SEQ = new SequenceImpl<Long>("lexical_decision_data_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>public.lexical_decision_result_id_seq</code>
     */
    public static final Sequence<Long> LEXICAL_DECISION_RESULT_ID_SEQ = new SequenceImpl<Long>("lexical_decision_result_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>public.similarity_judgement_data_id_seq</code>
     */
    public static final Sequence<Long> SIMILARITY_JUDGEMENT_DATA_ID_SEQ = new SequenceImpl<Long>("similarity_judgement_data_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

    /**
     * The sequence <code>public.similarity_judgement_result_id_seq</code>
     */
    public static final Sequence<Long> SIMILARITY_JUDGEMENT_RESULT_ID_SEQ = new SequenceImpl<Long>("similarity_judgement_result_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false));
}
