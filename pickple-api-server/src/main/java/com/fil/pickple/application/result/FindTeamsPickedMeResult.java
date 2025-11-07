package com.fil.pickple.application.result;

import com.fil.pickple.persistence.po.TeamViewPo;

import java.util.List;

public record FindTeamsPickedMeResult(
        List<TeamResult> teams
) {
    public static FindTeamsPickedMeResult of(List<TeamViewPo> teams) {
        return new FindTeamsPickedMeResult(
                teams.stream().map(TeamResult::from).toList()
        );
    }

    public record TeamResult(
            Long id,
            String name,
            String avatarImage
    ) {

        public static TeamResult from(TeamViewPo team) {
            return new TeamResult(
                    team.id(),
                    team.name(),
                    team.avatarImage()
            );
        }
    }
}
