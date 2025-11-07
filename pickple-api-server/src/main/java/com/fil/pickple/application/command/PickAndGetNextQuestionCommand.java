package com.fil.pickple.application.command;

public record PickAndGetNextQuestionCommand(
        Long teamId,
        Long questionId,
        Long pickpleId
) {
    public CreatePickCommand toCreatePickCommand() {
        return new CreatePickCommand(questionId, pickpleId);
    }

    public FindNextQuestionCommand toFindNextQuestionCommand() {
        return new FindNextQuestionCommand(teamId);
    }
}
