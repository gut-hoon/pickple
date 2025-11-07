package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.CreateTeamResult;

public record CreateTeamResponse(
        Boolean success,
        Long teamId,
        String teamName,
        String teamAvatarImage,
        String teamBackgroundImage,
        String invitationCode,
        Long participantId
) {
    public static CreateTeamResponse from(CreateTeamResult result){
        return new CreateTeamResponse(
                true,
                result.teamId(),
                result.teamName(),
                result.avatarImage(),
                result.backgroundImage(),
                result.invitationCode(),
                result.participantId()
        );
    }

}
