package com.fil.pickple.application.result;

import com.fil.pickple.domain.Question;
import com.fil.pickple.domain.Team;

import java.util.List;

public record SearchTeamProfileResult(
        String teamName,
        String teamAvatarImage,
        String teamBackgroundImage,
        List<QuestionResult> seasonTopQuestions,
        List<QuestionResult> currentTopQuestions
) {
    public static SearchTeamProfileResult of(Team team, List<Question> seasonTopQuestions, List<Question> currentTopQuestions){
        return new SearchTeamProfileResult(
                team.getName(),
                team.getAvatarImage(),
                team.getBackgroundImage(),
                seasonTopQuestions.stream().map(QuestionResult::from).toList(),
                currentTopQuestions.stream().map(QuestionResult::from).toList()
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
