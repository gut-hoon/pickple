package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.SearchTeamParticipantsResult;
import com.fil.pickple.domain.Participant;

import java.util.List;

public record SearchTeamParticipantsResponse(
        Boolean succes,
        List<Participant> participants
) {
    public record Participant(
            Long id,
            String nickname,
            String avatarImage
    ){
        static Participant from(SearchTeamParticipantsResult.ParticipantsResult result){
            return new Participant(result.id(), result.nickName(), result.avatarImage());

        }
    }

    public static SearchTeamParticipantsResponse from(SearchTeamParticipantsResult result){
        return new SearchTeamParticipantsResponse(
                true,
                result.participants().stream().map(Participant::from).toList()
        );
    }
}
