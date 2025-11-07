package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.SearchParticipantProfileResult;

import java.util.List;

public record SearchParticipantProfileResponse(
    boolean success,
    Long participantId,
    Long memberId,
    Long teamId,
    String nickname,
    String avatarImage,
    String backgroundImage,
    List<Question> seasonQuestions,
    List<Question> totalQuestions
) {
    public static SearchParticipantProfileResponse from(SearchParticipantProfileResult result) {
        return new SearchParticipantProfileResponse(
                true,
                result.participantId(),
                result.memberId(),
                result.teamId(),
                result.nickname(),
                result.avatarImage(),
                result.backgroundImage(),
                result.seasonQuestions().stream().map(Question::from).toList(),
                result.totalQuestions().stream().map(Question::from).toList()
        );
    }

    record Question(
            String content
    ) {
        static Question from(SearchParticipantProfileResult.QuestionResult result) {
            return new Question(result.content());
        }
    }
}
