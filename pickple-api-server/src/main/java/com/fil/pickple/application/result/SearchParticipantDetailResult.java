package com.fil.pickple.application.result;

import com.fil.pickple.domain.Participant;
import lombok.Builder;

@Builder
public record SearchParticipantDetailResult(
        Long participantId,
        Long teamId,
        String teamName,
        String nickname,
        String avatarImage,
        String backgroundImage
) {
    public static SearchParticipantDetailResult of(Participant participant, String baseProfileImage, String baseBackgroundImage) {
        return SearchParticipantDetailResult.builder()
                .participantId(participant.getId())
                .teamId(participant.getTeam().getId())
                .teamName(participant.getTeam().getName())
                .nickname(participant.getNickname())
                .avatarImage(participant.getAvatarImage() == null ? baseProfileImage : participant.getAvatarImage())
                .backgroundImage(participant.getBackgroundImage() == null ? baseBackgroundImage : participant.getBackgroundImage())
                .build();
    }
}
