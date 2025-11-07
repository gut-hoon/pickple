package com.fil.pickple.application.command;

public record LeaveTeamCommand(
        Long participantId
) {
    public static LeaveTeamCommand of(Long participantId){
        return new LeaveTeamCommand(participantId);
    }
}
