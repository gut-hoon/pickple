package com.fil.pickple.application;

import com.fil.pickple.application.command.FindNextQuestionCommand;
import com.fil.pickple.application.command.RefreshDoneQuestionsCommand;
import com.fil.pickple.application.result.FindNextQuestionResult;
import com.fil.pickple.domain.Member;
import com.fil.pickple.domain.Participant;
import com.fil.pickple.domain.Question;
import com.fil.pickple.domain.Team;
import com.fil.pickple.exception.code.ParticipantErrorCode;
import com.fil.pickple.exception.PickpleException;
import com.fil.pickple.exception.code.MemberErrorCode;
import com.fil.pickple.exception.code.QuestionErrorCode;
import com.fil.pickple.exception.code.TeamErrorCode;
import com.fil.pickple.persistence.DoneQuestionRepository;
import com.fil.pickple.persistence.MemberRepository;
import com.fil.pickple.persistence.ParticipantRepository;
import com.fil.pickple.persistence.QuestionRepository;
import com.fil.pickple.persistence.TeamRepository;
import com.fil.pickple.application.command.DeleteQuestionCommand;
import com.fil.pickple.application.command.CreateQuestionCommand;
import com.fil.pickple.application.command.FindQuestionsCommand;
import com.fil.pickple.application.result.CreateQuestionResult;
import com.fil.pickple.application.result.FindQuestionsResult;
import com.fil.pickple.security.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionService {
    private final MemberRepository memberRepository;
    private final ParticipantRepository participantRepository;
    private final QuestionRepository questionRepository;
    private final TeamRepository teamRepository;
    private final DoneQuestionRepository doneQuestionRepository;

    @Transactional
    public CreateQuestionResult createQuestion(CreateQuestionCommand command) {
        Long requesterId = SecurityUtil.getCurrentMemberId();

        Member requester = memberRepository.findByIdAndIsDeletedIsFalse(requesterId)
                .orElseThrow(() -> new PickpleException(MemberErrorCode.NOT_FOUND));
        Team team = teamRepository.getReferenceById(command.teamId());

        Participant author = participantRepository.findByMemberAndTeamAndIsLeftIsFalse(requester, team)
                .orElseThrow(() -> new PickpleException(ParticipantErrorCode.FORBIDDEN));

        Question question = Question.builder()
                .author(author)
                .content(command.content())
                .build();

        questionRepository.save(question);

        return CreateQuestionResult.from(question);
    }

    @Transactional(readOnly = true)
    public FindQuestionsResult findQuestions(FindQuestionsCommand command) {
        Long requesterId = SecurityUtil.getCurrentMemberId();

        Member requester = memberRepository.findByIdAndIsDeletedIsFalse(requesterId)
                .orElseThrow(() -> new PickpleException(MemberErrorCode.NOT_FOUND));
        Team team = teamRepository.getReferenceById(command.teamId());

        if (!participantRepository.existsByMemberAndTeamAndIsLeftIsFalse(requester, team)) {
            throw new PickpleException(ParticipantErrorCode.FORBIDDEN);
        }

        List<Question> questions;
        if (command.memberId() == null) {
            questions = questionRepository.findCurrentSeasonQuestionsByAuthor_Team(team);
        } else {
            Member member = memberRepository.getReferenceById(command.memberId());
            Participant participant = participantRepository.findByMemberAndTeam(member, team)
                    .orElseThrow(() -> new PickpleException(ParticipantErrorCode.NOT_FOUND));
            questions = questionRepository.findCurrentSeasonQuestionsByAuthor(participant);
        }

        return FindQuestionsResult.of(questions);
    }

    @Transactional
    public void deleteQuestion(DeleteQuestionCommand command) {
        Long requesterId = SecurityUtil.getCurrentMemberId();

        Member requester = memberRepository.findByIdAndIsDeletedIsFalse(requesterId)
                .orElseThrow(() -> new PickpleException(MemberErrorCode.NOT_FOUND));

        Question question = questionRepository.findByIdWithAuthor(command.questionId())
                .orElseThrow(() -> new PickpleException(QuestionErrorCode.NOT_FOUND));

        question.delete(requester);
    }

    @Transactional(readOnly = true)
    public FindNextQuestionResult findNextQuestion(FindNextQuestionCommand command) {
        Long requesterId = SecurityUtil.getCurrentMemberId();

        Member requester = memberRepository.findByIdAndIsDeletedIsFalse(requesterId)
                .orElseThrow(() -> new PickpleException(MemberErrorCode.NOT_FOUND));
        Team team = teamRepository.findById(command.teamId())
                .orElseThrow(() -> new PickpleException(TeamErrorCode.NOT_FOUND));
        Participant participant = participantRepository.findByMemberAndTeamAndIsLeftIsFalse(requester, team)
                .orElseThrow(() -> new PickpleException(ParticipantErrorCode.FORBIDDEN));

        Question question = questionRepository.findNonPickedByTeamAndRequester(team, participant, PageRequest.of(0, 1))
                .stream().findFirst().orElse(null);

        return FindNextQuestionResult.from(question);
    }

    @Transactional
    public void refreshDoneQuestions(RefreshDoneQuestionsCommand command) {
        Long requesterId = SecurityUtil.getCurrentMemberId();

        Member requester = memberRepository.findByIdAndIsDeletedIsFalse(requesterId)
                .orElseThrow(() -> new PickpleException(MemberErrorCode.NOT_FOUND));
        Team team = teamRepository.getReferenceById(command.teamId());

        Participant participant = participantRepository.findByMemberAndTeamAndIsLeftIsFalse(requester, team)
                .orElseThrow(() -> new PickpleException(ParticipantErrorCode.NOT_FOUND));

        doneQuestionRepository.refreshAllByParticipant(participant);
    }
}
