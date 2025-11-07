package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.CreateQuestionResult;

public record CreateQuestionResponse(
        Boolean success,
        Long questionId,
        String content
) {
    public static CreateQuestionResponse from(CreateQuestionResult result) {
        return new CreateQuestionResponse(
                true,
                result.questionId(),
                result.content()
        );
    }
}
