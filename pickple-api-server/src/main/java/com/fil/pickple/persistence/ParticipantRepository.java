package com.fil.pickple.persistence;

import com.fil.pickple.domain.Member;
import com.fil.pickple.domain.Participant;
import com.fil.pickple.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>
{
    boolean existsByTeamIdAndNicknameAndIsLeftFalse(Long teamId, String nickname);

    @Query("""
        SELECT p
        FROM Participant p
            JOIN FETCH p.member
        WHERE p.id = :participantId
        AND p.isLeft = false
    """)
    Optional<Participant> findByIdAndNotLeftWithMember(Long participantId);

    Optional<Participant> findByMemberIdAndTeamId(Long memberId, Long participantId);

    Optional<Participant> findByMemberAndTeamAndIsLeftIsFalse(Member member, Team team);

    boolean existsByMemberAndTeamAndIsLeftIsFalse(Member member, Team team);

    Optional<Participant> findByMemberAndTeam(Member member, Team team);

    boolean existsByTeamIdAndMemberIdAndIsLeftFalse(Long teamId, Long currentMemberId);

    Optional<Participant> findByMemberIdAndTeam(Long memberId, Team team);

    List<Participant> findAllByTeam_Id(Long teamId);

    Optional<Participant> findByMemberIdAndTeamIdAndIsLeftIsFalse(Long memberId, Long teamId);

    Optional<Participant> findByTeamIdAndMemberIdAndIsLeftTrue(Long id, Long requesterId);

    Optional<Participant> findByIdAndIsLeftIsFalse(Long id);
}
