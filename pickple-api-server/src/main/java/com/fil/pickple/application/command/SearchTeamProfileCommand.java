package com.fil.pickple.application.command;

public record SearchTeamProfileCommand(
        Long teamId
) {
    public static SearchTeamProfileCommand of(Long teamId){
        return new SearchTeamProfileCommand(teamId);
    }
}
