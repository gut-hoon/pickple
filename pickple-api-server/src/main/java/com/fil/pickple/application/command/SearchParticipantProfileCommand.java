package com.fil.pickple.application.command;

public record SearchParticipantProfileCommand(
        Long participantId
) {
    public static  SearchParticipantProfileCommand of(Long participantId) {
        return new SearchParticipantProfileCommand(participantId);
    }
}
