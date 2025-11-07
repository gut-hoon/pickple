package com.fil.pickple.application.result;

import com.fil.pickple.domain.Question;

public record CreateQuestionResult(
        Long questionId,
        String content
) {
    public static CreateQuestionResult from(Question question) {
        return new CreateQuestionResult(
                question.getId(),
                question.getContent()
        );
    }
}
