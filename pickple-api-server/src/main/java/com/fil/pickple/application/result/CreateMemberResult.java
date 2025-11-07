package com.fil.pickple.application.result;

import com.fil.pickple.domain.Member;

public record CreateMemberResult(
    Long memberId,
    String email,
    String nickname,
    String avatarImage
) {
    public static CreateMemberResult from (Member member) {
        return new CreateMemberResult(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getAvatarImage()
        );
    }
}
