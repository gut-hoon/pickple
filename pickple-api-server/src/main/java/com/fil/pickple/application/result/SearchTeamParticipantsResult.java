package com.fil.pickple.application.result;

import com.fil.pickple.domain.Participant;

import java.util.List;

public record SearchTeamParticipantsResult(
        List<ParticipantsResult> participants
) {
    public record ParticipantsResult(
            Long id,
            String nickName,
            String avatarImage
    ){
        static ParticipantsResult from(Participant participant){
            return new ParticipantsResult(participant.getId(), participant.getNickname(), participant.getAvatarImage());
        }
    }

    public static SearchTeamParticipantsResult of(List<Participant> participants){
        return new SearchTeamParticipantsResult(participants.stream().map(ParticipantsResult::from).toList());
    }
}
