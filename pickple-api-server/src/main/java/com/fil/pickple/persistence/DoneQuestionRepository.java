package com.fil.pickple.persistence;

import com.fil.pickple.domain.DoneQuestion;
import com.fil.pickple.domain.Participant;
import com.fil.pickple.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoneQuestionRepository extends JpaRepository<DoneQuestion, Long> {
    Optional<DoneQuestion> findByQuestionAndPicker(Question question, Participant participant);

    @Modifying
    @Query("""
        UPDATE DoneQuestion dq
        SET dq.isRefreshed = true
        WHERE dq.picker = :participant
    """)
    int refreshAllByParticipant(Participant participant);
}
