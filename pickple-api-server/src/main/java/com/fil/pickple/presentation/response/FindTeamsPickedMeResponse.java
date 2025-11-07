package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.FindTeamsPickedMeResult;

import java.util.List;

public record FindTeamsPickedMeResponse(
        Boolean success,
        List<Team> teams
) {
    public static FindTeamsPickedMeResponse from(FindTeamsPickedMeResult result) {
        return new FindTeamsPickedMeResponse(
                true,
                result.teams().stream().map(Team::from).toList()
        );
    }

    public record Team(
            Long id,
            String name,
            String avatarImage
    ) {
        public static Team from(FindTeamsPickedMeResult.TeamResult result) {
            return new Team(
                    result.id(),
                    result.name(),
                    result.avatarImage()
            );
        }
    }
}
