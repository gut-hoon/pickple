package com.fil.pickple.application.result;

import com.fil.pickple.domain.BaseProfileImage;
import com.fil.pickple.domain.Participant;
import com.fil.pickple.domain.Question;

import java.util.List;

public record SearchParticipantProfileResult(
    Long participantId,
    Long memberId,
    Long teamId,
    String nickname,
    String avatarImage,
    String backgroundImage,
    List<QuestionResult> seasonQuestions,
    List<QuestionResult> totalQuestions
) {
    public static SearchParticipantProfileResult of(Participant participant, List<Question> seasonQuestions, List<Question> totalQuestions, BaseProfileImage baseProfileImage) {
        return new SearchParticipantProfileResult(
                participant.getId(),
                participant.getMember().getId(),
                participant.getTeam().getId(),
                participant.getNickname(),
                participant.getAvatarImage() != null ? participant.getAvatarImage() : baseProfileImage.getPersonalAvatarImage(),
                participant.getBackgroundImage() != null ? participant.getBackgroundImage() : baseProfileImage.getPersonalBackgroundImage(),
                seasonQuestions.stream().map(QuestionResult::from).toList(),
                totalQuestions.stream().map(QuestionResult::from).toList()
        );
    }

    public record QuestionResult(
            String content
    ) {
        static QuestionResult from(Question question) {
            return new QuestionResult(question.getContent());
        }
    }
}
