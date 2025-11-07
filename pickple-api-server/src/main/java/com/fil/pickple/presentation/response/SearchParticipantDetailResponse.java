package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.SearchParticipantDetailResult;
import lombok.Builder;

@Builder
public record SearchParticipantDetailResponse(
        Boolean success,
        Long participantId,
        Long teamId,
        String teamName,
        String nickname,
        String avatarImage,
        String backgroundImage
) {
    public static SearchParticipantDetailResponse from(SearchParticipantDetailResult result){
        return SearchParticipantDetailResponse.builder()
                .success(true)
                .participantId(result.participantId())
                .teamId(result.teamId())
                .teamName(result.teamName())
                .nickname(result.nickname())
                .avatarImage(result.avatarImage())
                .backgroundImage(result.backgroundImage())
                .build();
    }
}
