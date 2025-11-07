package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.SearchBaseProfileResult;

import java.util.List;

public record SearchBaseProfileResponse(
        Boolean success,
        String nickname,
        String profileImage,
        String backgroundImage,
        List<Question> seasonTopQuestions,
        List<Question> totalTopQuestions
) {
    public static SearchBaseProfileResponse from(SearchBaseProfileResult result) {
        return new SearchBaseProfileResponse(
                true,
                result.nickname(),
                result.profileImage(),
                result.backgroundImage(),
                result.seasonTopQuestions().stream().map(Question::from).toList(),
                result.totalTopQuestions().stream().map(Question::from).toList()
        );
    }

    record Question(
            String content
    ) {
        public static Question from(SearchBaseProfileResult.QuestionResult result) {
            return new Question(result.content());
        }
    }
}
