package com.fil.pickple.application.result;

public record PickAndGetNextQuestionResult(
        FindNextQuestionResult.QuestionResult question
) {
    public static PickAndGetNextQuestionResult from(FindNextQuestionResult result) {
        return new PickAndGetNextQuestionResult(result.question());
    }
}
