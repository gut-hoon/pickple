package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.FindTeamsResult;

import java.util.List;

public record FindTeamsResponse(
        Boolean success,
        List<Team> teams
) {
    public record Team(
            long participantId,
            long id,
            String name,
            String avatarImage
    ) {
        static Team from(FindTeamsResult.TeamResult result) {
            return new Team(result.participantId(), result.id(), result.name(), result.avatarImage());
        }
    }

    public static FindTeamsResponse from(FindTeamsResult result) {
        return new FindTeamsResponse(
                true,
                result.teams().stream().map(Team::from).toList()
        );
    }
}