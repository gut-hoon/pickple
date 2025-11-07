package com.fil.pickple.application.result;

import com.fil.pickple.domain.Team;

public record CreateTeamResult(
        Long teamId,
        String teamName,
        String avatarImage,
        String backgroundImage,
        String invitationCode,
        Long participantId
) {
    public static CreateTeamResult of(Team team, Long participantId) {
        return new CreateTeamResult(team.getId(), team.getName(), team.getAvatarImage(), team.getBackgroundImage(), team.getInvitationCode(), participantId);
    }
}