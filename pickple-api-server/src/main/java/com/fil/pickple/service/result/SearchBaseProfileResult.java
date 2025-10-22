package com.fil.pickple.service.result;

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
    public static SearchBaseProfileResult of(Member member, List<Question> seasonTopQuestions, List<Question> totalTopQuestions) {
        return new SearchBaseProfileResult(
                member.getNickname(),
                member.getProfileImage(),
                member.getBackgroundImage(),
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
