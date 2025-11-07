package com.fil.pickple.application;

import com.fil.pickple.application.command.CreatePickCommand;
import com.fil.pickple.application.command.SearchReceivePickCommand;
import com.fil.pickple.application.result.SearchReceivePickResult;
import com.fil.pickple.domain.DoneQuestion;
import com.fil.pickple.domain.Participant;
import com.fil.pickple.domain.Pick;
import com.fil.pickple.domain.Question;
import com.fil.pickple.exception.PickpleException;
import com.fil.pickple.exception.code.MemberErrorCode;
import com.fil.pickple.exception.code.ParticipantErrorCode;
import com.fil.pickple.exception.code.QuestionErrorCode;
import com.fil.pickple.persistence.DoneQuestionRepository;
import com.fil.pickple.persistence.ParticipantRepository;
import com.fil.pickple.persistence.PickQueryRepository;
import com.fil.pickple.persistence.PickRepository;
import com.fil.pickple.persistence.QuestionRepository;
import com.fil.pickple.security.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PickService {
    private final PickRepository pickRepository;
    private final QuestionRepository questionRepository;
    private final DoneQuestionRepository doneQuestionRepository;
    private final ParticipantRepository participantRepository;
    private final PickQueryRepository pickQueryRepository;

    @Transactional
    public void createPick(CreatePickCommand command) {
        Long requesterId = SecurityUtil.getCurrentMemberId();

        Question question = questionRepository.findByIdWithAuthor(command.questionId())
                .orElseThrow(() -> new PickpleException(QuestionErrorCode.NOT_FOUND));
        Participant picker = participantRepository.findByMemberIdAndTeam(requesterId, question.getAuthor().getTeam())
                .orElseThrow(() -> new PickpleException(ParticipantErrorCode.NOT_FOUND));
        DoneQuestion doneQuestion = doneQuestionRepository.findByQuestionAndPicker(question, picker)
                .orElse(null);

        if (doneQuestion == null) {
            doneQuestion = new DoneQuestion(question, picker);
            doneQuestionRepository.save(doneQuestion);
        } else if (doneQuestion.getIsRefreshed()) {
            doneQuestion.repick(picker);
        } else {
            return;
        }

        if (command.pickpleId() != null) {
            Participant pickple = participantRepository.findByIdAndIsLeftIsFalse(command.pickpleId())
                    .orElseThrow(() -> new PickpleException(ParticipantErrorCode.NOT_FOUND));
            Pick pick = Pick.builder()
                    .question(question)
                    .picker(picker)
                    .pickple(pickple)
                    .build();
            pickRepository.save(pick);
        }
    }

    @Transactional(readOnly = true)
    public SearchReceivePickResult searchReceivePick(SearchReceivePickCommand command) {
        Long requesterId = SecurityUtil.getCurrentMemberId();

        Long participantId = null;
        if (command.teamId() != null) {
            Participant participant = participantRepository.findByMemberIdAndTeamId(requesterId, command.teamId())
                    .orElseThrow(() -> new PickpleException(MemberErrorCode.NOT_FOUND));
            participantId = participant.getId();
        }

        Slice<Pick> picks = pickQueryRepository.findReceivePicks(requesterId, participantId, command.pageSize(), command.lastCreatedAt());

        return SearchReceivePickResult.of(picks);
    }
}
