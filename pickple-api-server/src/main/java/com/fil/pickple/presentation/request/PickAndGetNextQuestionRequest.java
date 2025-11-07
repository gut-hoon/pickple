package com.fil.pickple.presentation.request;

import com.fil.pickple.application.command.PickAndGetNextQuestionCommand;
import jakarta.validation.constraints.NotNull;

public record PickAndGetNextQuestionRequest(
        @NotNull(message = "팀 ID는 필수입니다")
        Long teamId,
        Long questionId,
        Long pickpleId
) {
    public PickAndGetNextQuestionCommand toCommand() {
        return new PickAndGetNextQuestionCommand(
                teamId,
                questionId,
                pickpleId
        );
    }
}
