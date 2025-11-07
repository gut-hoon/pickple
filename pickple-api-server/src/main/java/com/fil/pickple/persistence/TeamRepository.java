package com.fil.pickple.persistence;

import com.fil.pickple.domain.Participant;
import com.fil.pickple.domain.Team;
import com.fil.pickple.persistence.po.TeamHotPo;
import com.fil.pickple.persistence.po.TeamViewPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String teamName);

    boolean existsByInvitationCode(String invitationCode);

    @Query("""
        SELECT p
        FROM Participant p
            JOIN FETCH p.team t
        WHERE p.member.id = :memberId
        AND p.isLeft = false
    """)
    List<Participant> findAllByMemberId(Long memberId);

    @Query("""
        SELECT new com.fil.pickple.persistence.po.TeamHotPo(
            t.id, t.name, t.avatarImage, COUNT(p)
        )
        FROM Pick p
        JOIN p.picker pickerPart
        JOIN pickerPart.member pickerM
        JOIN p.pickple pickplePart
        JOIN pickplePart.member pickpleM
        JOIN pickerPart.team t
        WHERE p.createdAt >= :since
            AND pickerM.isDeleted = false
            AND pickpleM.isDeleted = false
        GROUP BY t.id, t.name, t.avatarImage
        ORDER BY COUNT(p) DESC
    """)
    List<TeamHotPo> findHotTeamsSince(@Param("since") LocalDateTime since, Pageable pageable);

    @Query("""
        SELECT new com.fil.pickple.persistence.po.TeamViewPo(
            p.pickple.team.id,
            CASE WHEN p.pickple.isLeft = true
                THEN p.pickple.teamSnapshot.name
                ELSE p.pickple.team.name
            END,
            CASE WHEN p.pickple.isLeft = true
                THEN p.pickple.teamSnapshot.avatarImage
                ELSE p.pickple.team.avatarImage
            END
        )
        FROM Pick p
        WHERE p.pickple.member.id = :memberId
        GROUP BY p.pickple.team
        ORDER BY MAX(p.pickple.joinedAt) DESC
    """)
    List<TeamViewPo> findPickedMeWithMember(Long memberId);

    Optional<Team> findByInvitationCode(String invitationCode);
}