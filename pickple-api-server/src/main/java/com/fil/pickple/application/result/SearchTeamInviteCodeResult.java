package com.fil.pickple.application.result;

import com.fil.pickple.domain.Team;

public record SearchTeamInviteCodeResult(
        String invitationCode
) {
    public static SearchTeamInviteCodeResult of(Team team){
        return new SearchTeamInviteCodeResult(team.getInvitationCode());
    }
}
