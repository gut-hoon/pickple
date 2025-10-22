package com.fil.pickple.service;

import com.fil.pickple.domain.Question;
import com.fil.pickple.persistence.MemberRepository;
import com.fil.pickple.persistence.QuestionRepository;
import com.fil.pickple.domain.Member;
import com.fil.pickple.exception.PickpleException;
import com.fil.pickple.exception.code.MemberErrorCode;
import com.fil.pickple.service.command.SearchBaseProfileCommand;
import com.fil.pickple.service.result.SearchBaseProfileResult;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberService {
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public SearchBaseProfileResult searchBaseProfile(SearchBaseProfileCommand command) {
        Member member = memberRepository.findByIdAndIsDeletedIsFalse(command.memberId())
                .orElseThrow(() -> new PickpleException(MemberErrorCode.NOT_FOUND));

        List<Question> seasonTopQuestions = questionRepository.findTopQuestionInLastSeason(member.getId());
        List<Question> totalTopQuestions = questionRepository.findTopQuestion(member.getId());

        return SearchBaseProfileResult.of(member, seasonTopQuestions, totalTopQuestions);
    }
}
