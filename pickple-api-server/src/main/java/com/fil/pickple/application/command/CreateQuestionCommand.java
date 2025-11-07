package com.fil.pickple.application.command;

public record CreateQuestionCommand(
        Long teamId,
        String content
) {
    public static CreateQuestionCommand of(Long teamId, String content) {
        return new CreateQuestionCommand(
                teamId,
                content
        );
    }
}
