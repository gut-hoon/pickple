package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.SearchTeamProfileResult;

import java.util.List;

public record SearchTeamProfileResponse(
        Boolean success,
        String teamName,
        String teamAvatarImage,
        String teamBackgroundImage,
        List<Question> lastSeasonTopQuestions,
        List<Question> currentTopQuestions
) {
    public static SearchTeamProfileResponse from(SearchTeamProfileResult result){
        return new SearchTeamProfileResponse(
                true,
                result.teamName(),
                result.teamAvatarImage(),
                result.teamBackgroundImage(),
                result.seasonTopQuestions().stream().map(Question::from).toList(),
                result.currentTopQuestions().stream().map(Question::from).toList()
        );
    }

    record Question(
            String content

    ) {
        public static SearchTeamProfileResponse.Question from(SearchTeamProfileResult.QuestionResult result) {
            return new Question(result.content());
        }
    }
}
