package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.SearchMeResult;
import com.fil.pickple.domain.ProfileVisibility;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SearchMeResponse(
        Long id,
        String email,
        String avatarImage,
        String backgroundImage,
        String name,
        String nickname,
        LocalDate birthDate,
        ProfileVisibility profileVisibility
) {
    public static SearchMeResponse from(SearchMeResult result) {
        return SearchMeResponse.builder()
                .id(result.id())
                .email(result.email())
                .avatarImage(result.avatarImage())
                .backgroundImage(result.backgroundImage())
                .name(result.name())
                .nickname(result.nickname())
                .birthDate(result.birthDate())
                .profileVisibility(result.profileVisibility())
                .build();
    }
}
