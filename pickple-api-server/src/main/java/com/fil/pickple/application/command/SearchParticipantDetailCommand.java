package com.fil.pickple.application.command;

public record SearchParticipantDetailCommand(
        Long participantId
) {
    public static SearchParticipantDetailCommand of(Long teamId){
        return new SearchParticipantDetailCommand(teamId);
    }
}
