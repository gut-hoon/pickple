package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.FindNextQuestionResult;
import com.fil.pickple.application.result.PickAndGetNextQuestionResult;

import java.time.LocalDateTime;

public record PickAndGetNextQuestionResponse(
        Boolean success,
        Question question
) {
    public static PickAndGetNextQuestionResponse from(PickAndGetNextQuestionResult result) {
        return new PickAndGetNextQuestionResponse(
                true,
                Question.from(result.question())
        );
    }

    public record Question(
            Long id,
            String content,
            LocalDateTime createdAt,
            Author author
    ) {
        public static Question from(FindNextQuestionResult.QuestionResult result) {
            if (result == null) {
                return null;
            }
            return new Question(
                    result.id(),
                    result.content(),
                    result.createdAt(),
                    Author.from(result.author())
            );
        }
    }

    public record Author(
            Long id,
            String nickname,
            String avatarImage
    ) {
        public static Author from(FindNextQuestionResult.AuthorResult result) {
            return new Author(
                    result.id(),
                    result.nickname(),
                    result.avatarImage()
            );
        }
    }
}
