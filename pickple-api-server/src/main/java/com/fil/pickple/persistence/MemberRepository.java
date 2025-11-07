package com.fil.pickple.persistence;

import com.fil.pickple.domain.Member;
import com.fil.pickple.persistence.po.MemberHotPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByNickname(String nickname);

    Optional<Member> findByIdAndIsDeletedIsFalse(Long id);

    Optional<Member> findByEmail(String email);

    @Query("""
        SELECT new com.fil.pickple.persistence.po.MemberHotPo(
            m.id, m.nickname, m.avatarImage, COUNT(p)
        )
        FROM Pick p
        JOIN p.pickple pickplePart
        JOIN pickplePart.member m
        JOIN p.picker pickerPart
        JOIN pickerPart.member pickerM
        WHERE p.createdAt >= :since
            AND m.isDeleted = false
            AND m.visibility = VISIBLE
            AND pickerM.isDeleted = false
        GROUP BY m.id, m.nickname, m.avatarImage
        ORDER BY COUNT(p) DESC
    """)
    List<MemberHotPo> findHotMembersSince(@Param("since") LocalDateTime since, Pageable pageable);
}
