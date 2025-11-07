package com.fil.pickple.application.result;

import com.fil.pickple.domain.Participant;
import com.fil.pickple.domain.Team;

import java.util.List;

public record FindTeamsResult(List<TeamResult> teams) {
    public record TeamResult(Long participantId, Long id, String avatarImage, String name) {
        static TeamResult from(Participant participant) {
            return new TeamResult(
                    participant.getId(),
                    participant.getTeam().getId(),
                    participant.getTeam().getAvatarImage(),
                    participant.getTeam().getName());
        }
    }

    public static FindTeamsResult of(List<Participant> teams) {
        return new FindTeamsResult(teams.stream().map(TeamResult::from).toList());
    }
}
