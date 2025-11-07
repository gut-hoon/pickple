package com.fil.pickple.application.result;

import com.fil.pickple.domain.Team;

public record UpdateTeamProfileResult(
        String teamName,
        String teamAvatarImage,
        String teamBackgroundImage
) {
    public static UpdateTeamProfileResult from(Team team){
        return new UpdateTeamProfileResult(
                team.getName(),
                team.getAvatarImage(),
                team.getBackgroundImage()
        );
    }
}
