package com.fil.pickple.persistence;

import com.fil.pickple.domain.Season;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer> {

    @Query("""
    SELECT s
    FROM Season s
    ORDER BY s.end DESC
    """)
    Optional<Season> findByEndIsBefore(LocalDateTime endBefore, Limit limit);

    boolean existsByEndAfter(LocalDateTime endAfter);

    /**
     * 현재 날짜가 포함되는 시즌을 조회합니다.
     * @param now 현재 시간
     * @return 현재 진행 중인 시즌 (Optional)
     */
    @Query("""
        SELECT s
        FROM Season s
        WHERE :now BETWEEN s.start AND s.end
    """)
    Optional<Season> findCurrentSeason(LocalDateTime now);
}
