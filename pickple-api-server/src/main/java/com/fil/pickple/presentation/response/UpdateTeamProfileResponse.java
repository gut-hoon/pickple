package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.UpdateTeamProfileResult;

public record UpdateTeamProfileResponse(
        Boolean success,
        String teamName,
        String teamAvatarImage,
        String teamBackgroundImage
) {
    public static UpdateTeamProfileResponse from(UpdateTeamProfileResult result){
        return new UpdateTeamProfileResponse(
                true,
                result.teamName(),
                result.teamAvatarImage(),
                result.teamBackgroundImage()
        );
    }
}
