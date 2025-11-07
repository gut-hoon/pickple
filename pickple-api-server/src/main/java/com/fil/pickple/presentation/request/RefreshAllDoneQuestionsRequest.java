package com.fil.pickple.presentation.request;

import com.fil.pickple.application.command.RefreshDoneQuestionsCommand;
import jakarta.validation.constraints.NotNull;

public record RefreshAllDoneQuestionsRequest(
        @NotNull(message = "팀 ID는 필수입니다")
        Long teamId
) {
    public RefreshDoneQuestionsCommand toCommand() {
        return new RefreshDoneQuestionsCommand(teamId);
    }
}
