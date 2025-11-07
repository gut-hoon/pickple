package com.fil.pickple.application.command;

public record SearchTeamInviteCodeCommand(
        Long teamId
) {
    public static SearchTeamInviteCodeCommand of(Long teamId) {
        return new SearchTeamInviteCodeCommand(teamId);
    }
}
