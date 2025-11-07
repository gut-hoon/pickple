package com.fil.pickple.application;

import com.fil.pickple.application.command.PickAndGetNextQuestionCommand;
import com.fil.pickple.application.result.FindNextQuestionResult;
import com.fil.pickple.application.result.PickAndGetNextQuestionResult;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PickFacade {
    private final QuestionService questionService;
    private final PickService pickService;

    public PickAndGetNextQuestionResult pickAndGetNextQuestion(PickAndGetNextQuestionCommand command) {
        if (command.questionId() != null) {
            pickService.createPick(command.toCreatePickCommand());
        }

        FindNextQuestionResult findNextQuestionResult = questionService.findNextQuestion(command.toFindNextQuestionCommand());

        return PickAndGetNextQuestionResult.from(findNextQuestionResult);
    };
}
