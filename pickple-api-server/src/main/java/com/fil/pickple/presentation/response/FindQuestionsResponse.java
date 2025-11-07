package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.FindQuestionsResult;

import java.time.LocalDateTime;
import java.util.List;

public record FindQuestionsResponse(
        Boolean success,
        List<Question> questions
) {
    public record Question(
            Long id,
            String content,
            LocalDateTime createdAt
    ) {
        static Question from(FindQuestionsResult.QuestionResult result) {
            return new Question(
                    result.id(),
                    result.content(),
                    result.createdAt()
            );
        }
    }

    public static FindQuestionsResponse from(FindQuestionsResult result) {
        return new FindQuestionsResponse(
                true,
                result.questions().stream().map(Question::from).toList()
        );
    }
}
