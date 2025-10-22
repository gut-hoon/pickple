package com.fil.pickple.persistence;

import com.fil.pickple.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("""
        SELECT p.question
        FROM Pick p
        WHERE p.pickple.member.id = :memberId
        GROUP BY p.question.id
        ORDER BY COUNT(p) DESC
        """)
    List<Question> findTopQuestion(Long memberId);

    @Query("""
        SELECT p.question
        FROM Pick p
        JOIN Season s ON s.id = (SELECT last_seoson.id FROM Season last_seoson ORDER BY last_seoson.start DESC LIMIT 1)
        AND p.pickple.member.id = :memberId
        AND p.createdAt BETWEEN s.start AND s.end
        GROUP BY p.question.id
        ORDER BY COUNT(p) DESC
        """)
    List<Question> findTopQuestionInLastSeason(Long memberId);
}
