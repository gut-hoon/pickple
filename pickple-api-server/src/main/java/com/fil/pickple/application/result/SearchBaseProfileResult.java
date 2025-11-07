package com.fil.pickple.application.result;

import com.fil.pickple.domain.BaseProfileImage;
import com.fil.pickple.domain.Member;
import com.fil.pickple.domain.Question;

import java.util.List;

public record SearchBaseProfileResult(
        String nickname,
        String profileImage,
        String backgroundImage,
        List<QuestionResult> seasonTopQuestions,
        List<QuestionResult> totalTopQuestions
) {
    public static SearchBaseProfileResult of(Member member, List<Question> seasonTopQuestions, List<Question> totalTopQuestions, BaseProfileImage baseProfileImage) {
        return new SearchBaseProfileResult(
                member.getNickname(),
                member.getAvatarImage() != null ? member.getAvatarImage() : baseProfileImage.getPersonalAvatarImage(),
                member.getBackgroundImage() != null ? member.getBackgroundImage() : baseProfileImage.getPersonalBackgroundImage(),
                seasonTopQuestions.stream().map(QuestionResult::from).toList(),
                totalTopQuestions.stream().map(QuestionResult::from).toList()
        );
    }

    public record QuestionResult(
            String content
    ) {
        public static QuestionResult from(Question question) {
            return new QuestionResult(question.getContent());
        }
    }
}
