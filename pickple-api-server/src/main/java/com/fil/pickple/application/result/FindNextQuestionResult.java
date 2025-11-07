package com.fil.pickple.application.result;

import com.fil.pickple.domain.Participant;
import com.fil.pickple.domain.Question;

import java.time.LocalDateTime;

public record FindNextQuestionResult(
        QuestionResult question
) {
    public static FindNextQuestionResult from(Question question) {
        return new FindNextQuestionResult(
                question == null ? null : QuestionResult.from(question)
        );
    }

    public record QuestionResult(
            Long id,
            String content,
            LocalDateTime createdAt,
            AuthorResult author
    ) {
        public static QuestionResult from(Question question) {
            return new QuestionResult(
                    question.getId(),
                    question.getContent(),
                    question.getCreatedAt(),
                    AuthorResult.from(question.getAuthor())
            );
        }
    }

    public record AuthorResult(
            Long id,
            String nickname,
            String avatarImage
    ) {
        public static AuthorResult from(Participant author) {
            return new AuthorResult(
                    author.getId(),
                    author.getNickname(),
                    author.getAvatarImage()
            );
        }
    }
}
