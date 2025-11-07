package com.fil.pickple.application.result;

import com.fil.pickple.domain.Question;

import java.time.LocalDateTime;
import java.util.List;

public record FindQuestionsResult(
        List<QuestionResult> questions
) {
    public record QuestionResult(
            Long id,
            String content,
            LocalDateTime createdAt
    ) {
        static QuestionResult from(Question question) {
            return new QuestionResult(
                    question.getId(),
                    question.getContent(),
                    question.getCreatedAt()
            );
        }
    }

    public static FindQuestionsResult of(List<Question> questions) {
        return new FindQuestionsResult(questions.stream().map(QuestionResult::from).toList());
    }
}
