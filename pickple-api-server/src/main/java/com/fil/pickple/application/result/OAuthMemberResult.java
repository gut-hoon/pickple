package com.fil.pickple.application.result;

import com.fil.pickple.domain.Member;

public record OAuthMemberResult(
        Long memberId,
        String email,
        boolean isNewMember,
        boolean isRegisteredMember
) {
    public static OAuthMemberResult of(Long memberId, String email, boolean isNewMember, boolean isRegisteredMember) {
        return new OAuthMemberResult(memberId, email, isNewMember, isRegisteredMember);
    }
}