package com.fil.pickple.application.result;

import com.fil.pickple.domain.Participant;

public record EditParticipantResult(
        String nickname,
        String avatarImage,
        String backgroundImage
) {
    public static EditParticipantResult from(Participant participant) {
        return new EditParticipantResult(
                participant.getNickname(),
                participant.getAvatarImage(),
                participant.getBackgroundImage()
        );
    }
}
