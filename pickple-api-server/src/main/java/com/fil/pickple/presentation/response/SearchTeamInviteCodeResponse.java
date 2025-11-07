package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.SearchTeamInviteCodeResult;

public record SearchTeamInviteCodeResponse(
        Boolean success,
        String invitationCode
) {
    public static SearchTeamInviteCodeResponse from(SearchTeamInviteCodeResult result){
        return new SearchTeamInviteCodeResponse(
                true,
                result.invitationCode()
        );
    }
}
