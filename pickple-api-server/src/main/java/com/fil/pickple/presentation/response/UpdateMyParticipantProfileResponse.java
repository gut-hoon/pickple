package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.EditParticipantResult;

public record UpdateMyParticipantProfileResponse(
        Boolean success,
        String nickname,
        String avatarImage,
        String backgroundImage
) {
    public static UpdateMyParticipantProfileResponse from(EditParticipantResult result){
        return new UpdateMyParticipantProfileResponse(
                true,
                result.nickname(),
                result.avatarImage(),
                result.backgroundImage()
        );
    }
}
