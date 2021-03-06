-- matching ss and ps meanings ver2
select w_ss.word_id,
       (select distinct f.value
        from paradigm p,
             form f
        where p.word_id = w_ss.word_id
        and   f.paradigm_id = p.id
        and   f.mode = 'WORD') word,
       m_ss.lexeme_id ss_lexeme_id,
       m_ss.meaning_id ss_meaning_id,
       m_ps.lexeme_id comp_lexeme_id,
       m_ps.meaning_id comp_meaning_id,
       true is_override_complexity
from (select m_ss_sum.lexeme_id,
             m_ss_sum.meaning_id,
             m_ss_sum.word_id
      from (select l1.id lexeme_id,
                   l1.meaning_id,
                   l1.word_id
            from word w1,
                 lexeme l1
            where w1.lang = 'est'
            and   l1.word_id = w1.id
            and   l1.type = 'PRIMARY'
            and   l1.dataset_code = :datasetCode
            and   exists (select d1.id
                          from definition d1
                          where d1.meaning_id = l1.meaning_id
                          and   d1.complexity = 'DETAIL1')
            union all
            select l1.id lexeme_id,
                   l1.meaning_id,
                   l1.word_id
            from word w1,
                 lexeme l1
            where w1.lang = 'est'
            and   l1.word_id = w1.id
            and   l1.type = 'PRIMARY'
            and   l1.dataset_code = :datasetCode
            and   not exists (select d1.id
                              from definition d1
                              where d1.meaning_id = l1.meaning_id
                              and   d1.complexity = 'DETAIL1')
            and   exists (select wr1.id
                          from word_relation wr1
                          where wr1.word_rel_type_code = 'deriv_base'
                          and   wr1.word1_id = w1.id)
            and   not exists (select l2.id
                              from lexeme l2
                              where l2.meaning_id = l1.meaning_id
                              and   l2.dataset_code = :datasetCode
                              and   l2.type = 'PRIMARY'
                              and   l2.id != l1.id)
            and   not exists (select lcpg.id
                              from lex_colloc_pos_group lcpg
                              where lcpg.lexeme_id = l1.id)) m_ss_sum
      group by m_ss_sum.lexeme_id,
               m_ss_sum.meaning_id,
               m_ss_sum.word_id) m_ss,
     (select l1.id lexeme_id,
             l1.meaning_id,
             l1.word_id
      from word w1,
           lexeme l1
      where w1.lang = 'est'
      and   l1.word_id = w1.id
      and   l1.type = 'PRIMARY'
      and   l1.complexity = 'SIMPLE'
      and   l1.dataset_code = :datasetCode
      and   exists (select d1.id
                    from definition d1
                    where d1.meaning_id = l1.meaning_id
                    and   d1.complexity = 'SIMPLE1')
      and   not exists (select d1.id
                        from definition d1
                        where d1.meaning_id = l1.meaning_id
                        and   d1.complexity = 'DETAIL1')) m_ps,
     (select w_ss_sum.word_id,
             count(distinct w_ss_sum.meaning_id) m_cnt
      from (select w1.id word_id,
                   l1.meaning_id
            from word w1,
                 lexeme l1
            where w1.lang = 'est'
            and   l1.word_id = w1.id
            and   l1.type = 'PRIMARY'
            and   l1.dataset_code = :datasetCode
            and   exists (select d1.id
                          from definition d1
                          where d1.meaning_id = l1.meaning_id
                          and   d1.complexity = 'DETAIL1')
            union all
            select w1.id word_id,
                   l1.meaning_id
            from word w1,
                 lexeme l1
            where w1.lang = 'est'
            and   l1.word_id = w1.id
            and   l1.type = 'PRIMARY'
            and   l1.dataset_code = :datasetCode
            and   not exists (select d1.id
                              from definition d1
                              where d1.meaning_id = l1.meaning_id
                              and   d1.complexity = 'DETAIL1')
            and   exists (select wr1.id
                          from word_relation wr1
                          where wr1.word_rel_type_code = 'deriv_base'
                          and   wr1.word1_id = w1.id)
            and   not exists (select l2.id
                              from lexeme l2
                              where l2.meaning_id = l1.meaning_id
                              and   l2.dataset_code = :datasetCode
                              and   l2.type = 'PRIMARY'
                              and   l2.id != l1.id)
            and   not exists (select lcpg.id
                              from lex_colloc_pos_group lcpg
                              where lcpg.lexeme_id = l1.id)) w_ss_sum
      group by w_ss_sum.word_id) w_ss,
     (select w1.id word_id,
             count(l1.meaning_id) m_cnt
      from word w1,
           lexeme l1
      where w1.lang = 'est'
      and   l1.word_id = w1.id
      and   l1.type = 'PRIMARY'
      and   l1.complexity = 'SIMPLE'
      and   l1.dataset_code = :datasetCode
      and   exists (select d1.id
                    from definition d1
                    where d1.meaning_id = l1.meaning_id
                    and   d1.complexity = 'SIMPLE1')
      and   not exists (select d1.id
                        from definition d1
                        where d1.meaning_id = l1.meaning_id
                        and   d1.complexity = 'DETAIL1')
      group by w1.id) w_ps
where m_ss.word_id = m_ps.word_id
and   w_ss.word_id = w_ps.word_id
and   m_ss.word_id = w_ss.word_id
and   w_ss.m_cnt = 1
and   w_ps.m_cnt = 1
order by word
