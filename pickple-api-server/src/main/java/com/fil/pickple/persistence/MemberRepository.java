package com.fil.pickple.persistence;

import com.fil.pickple.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdAndIsDeletedIsFalse(Long id);
}
