package com.fil.pickple.application.result;

import com.fil.pickple.domain.Member;
import com.fil.pickple.domain.ProfileVisibility;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SearchMeResult(
        Long id,
        String email,
        String avatarImage,
        String backgroundImage,
        String name,
        String nickname,
        LocalDate birthDate,
        ProfileVisibility profileVisibility
) {
    public static SearchMeResult of(Member member, String baseAvatarImage, String baseBackgroundImage) {
        return SearchMeResult.builder()
                .id(member.getId())
                .email(member.getEmail())
                .avatarImage(member.getAvatarImage() == null ? baseAvatarImage : member.getAvatarImage())
                .backgroundImage(member.getBackgroundImage() == null ? baseBackgroundImage : member.getAvatarImage())
                .name(member.getFullName())
                .nickname(member.getNickname())
                .birthDate(member.getBirthDate())
                .profileVisibility(member.getVisibility())
                .build();
    }
}
