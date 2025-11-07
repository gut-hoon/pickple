package com.fil.pickple.application.result;

import com.fil.pickple.domain.Member;

import java.time.LocalDate;

public record RegisterMemberResult(
        Long memberId,
        String email,
        String fullName,
        LocalDate birthDate
) {
    public static RegisterMemberResult from(Member member) {
        return new RegisterMemberResult(
                member.getId(),
                member.getEmail(),
                member.getFullName(),
                member.getBirthDate()
        );
    }
}
