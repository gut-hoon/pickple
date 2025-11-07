package com.fil.pickple.persistence;

import com.fil.pickple.domain.Participant;
import com.fil.pickple.domain.Question;
import com.fil.pickple.domain.Team;
import com.fil.pickple.persistence.po.QuestionHotPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = """
        SELECT q.*
        FROM question q
            JOIN pick p ON p.question_id = q.id
            JOIN participant pt ON p.pickple_id = pt.id
        WHERE pt.member_id = :memberId
        GROUP BY q.id
        ORDER BY COUNT(p.id) DESC
        LIMIT 3
    """, nativeQuery = true)
    List<Question> findTopQuestion(Long memberId);

    @Query(value = """
        SELECT q.*
        FROM question q
            JOIN pick p ON p.question_id = q.id
            JOIN participant pt ON p.pickple_id = pt.id
            JOIN season s ON s.id = (
                SELECT id FROM season ORDER BY start DESC LIMIT 1
            )
        WHERE pt.member_id = :memberId
            AND p.created_at BETWEEN s.start AND s.end
        GROUP BY q.id
        ORDER BY COUNT(p.id) DESC
        LIMIT 3
    """, nativeQuery = true)
    List<Question> findTopQuestionInLastSeason(Long memberId);

    @Query("""
        SELECT q
        FROM Question q
        WHERE q.author.team = :team
          AND q.createdAt BETWEEN
              (SELECT s.start FROM Season s WHERE CURRENT_TIMESTAMP BETWEEN s.start AND s.end)
              AND
              (SELECT s.end   FROM Season s WHERE CURRENT_TIMESTAMP BETWEEN s.start AND s.end)
          AND q.isDeleted = false
        ORDER BY q.createdAt DESC
    """)
    List<Question> findCurrentSeasonQuestionsByAuthor_Team(Team team);

    @Query("""
        SELECT q
        FROM Question q
        WHERE q.author = :author
          AND q.createdAt BETWEEN
              (SELECT s.start FROM Season s WHERE CURRENT_TIMESTAMP BETWEEN s.start AND s.end)
              AND
              (SELECT s.end   FROM Season s WHERE CURRENT_TIMESTAMP BETWEEN s.start AND s.end)
          AND q.isDeleted = false
        ORDER BY q.createdAt DESC
    """)
    List<Question> findCurrentSeasonQuestionsByAuthor(Participant author);

    @Query("""
        SELECT q
        FROM Question q
            JOIN FETCH q.author
        WHERE q.id = :id
    """)
    Optional<Question> findByIdWithAuthor(Long id);

    @Query("""
        SELECT q
        FROM Pick p
        JOIN p.question q
        WHERE p.pickple.team.id = :teamId
          AND p.createdAt BETWEEN
              (SELECT s1.start
                 FROM Season s1
                WHERE s1.start = (
                        select max(s2.start)
                          FROM Season s2
                         WHERE s2.end < CURRENT_TIMESTAMP
                      )
              )
              AND
              (SELECT s1.end
                 FROM Season s1
                WHERE s1.start = (
                        SELECT MAX(s2.start)
                          FROM Season s2
                         WHERE s2.end < CURRENT_TIMESTAMP
                      )
              )
        GROUP BY q
        ORDER BY COUNT(p) DESC
    """)
    List<Question> findLatestSeasonTopQuestionsByTeamId(Long teamId, Pageable pageable);

    @Query("""
        SELECT q
        FROM Pick p
        JOIN p.question q
        WHERE p.pickple.team.id = :teamId
          AND p.createdAt BETWEEN
              (SELECT s.start FROM Season s WHERE CURRENT_TIMESTAMP BETWEEN s.start AND s.end)
              AND
              (SELECT s.end   FROM Season s WHERE CURRENT_TIMESTAMP BETWEEN s.start AND s.end)
        GROUP BY q
        ORDER BY COUNT(p) DESC
    """)
    List<Question> findCurrentSeasonTopQuestionsByTeamId(Long teamId, Pageable pageable);

    @Query(value = """
        SELECT q.*
        FROM question q
            JOIN pick p ON p.question_id = q.id
            JOIN participant pt ON p.pickple_id = pt.id
        WHERE pt.id = :participantId
        GROUP BY q.id
        ORDER BY COUNT(p.id) DESC
        LIMIT 3
    """, nativeQuery = true)
    List<Question> findTopQuestionByParticipant(Long participantId);

    @Query(value = """
        SELECT q.*
        FROM question q
            JOIN pick p ON p.question_id = q.id
            JOIN participant pt ON p.pickple_id = pt.id
            JOIN season s ON s.id = (
                SELECT id FROM season ORDER BY start DESC LIMIT 1
            )
        WHERE pt.id = :participantId
            AND p.created_at BETWEEN s.start AND s.end
        GROUP BY q.id
        ORDER BY COUNT(p.id) DESC
        LIMIT 3
    """, nativeQuery = true)
    List<Question> findTopQuestionInLastSeasonByParticipant(Long participantId);

    @Query("""
        SELECT q
        FROM Question q
            JOIN FETCH q.author
        WHERE q.author.team = :team
            AND q.isDeleted = false
            AND q.createdAt BETWEEN
              (SELECT s.start FROM Season s WHERE CURRENT_TIMESTAMP BETWEEN s.start AND s.end)
              AND
              (SELECT s.end   FROM Season s WHERE CURRENT_TIMESTAMP BETWEEN s.start AND s.end)
            AND q.id NOT IN (
                SELECT dq.question.id
                FROM DoneQuestion dq
                WHERE dq.picker = :picker
                    AND dq.isRefreshed = false
            )
        ORDER BY function('RAND')
    """)
    List<Question> findNonPickedByTeamAndRequester(Team team, Participant picker, Pageable pageable);

    @Query("""
        SELECT new com.fil.pickple.persistence.po.QuestionHotPo(
            q.id, q.content, COUNT(p)
        )
        FROM Pick p
        JOIN p.picker pickerPart
        JOIN pickerPart.member pickerM
        JOIN p.pickple pickplePart
        JOIN pickplePart.member pickpleM
        JOIN p.question q
        WHERE p.createdAt >= :since
            AND pickerM.isDeleted = false
            AND pickpleM.isDeleted = false
        GROUP BY q.id, q.content
        ORDER BY COUNT(p) DESC
    """)
    List<QuestionHotPo> findHotQuestionsSince(@Param("since") LocalDateTime since, Pageable pageable);
}
