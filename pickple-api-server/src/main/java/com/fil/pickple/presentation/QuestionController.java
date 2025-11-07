package com.fil.pickple.presentation;

import com.fil.pickple.application.command.RefreshDoneQuestionsCommand;
import com.fil.pickple.presentation.request.CreateQuestionRequest;
import com.fil.pickple.application.command.DeleteQuestionCommand;
import com.fil.pickple.presentation.request.RefreshAllDoneQuestionsRequest;
import com.fil.pickple.presentation.response.CreateQuestionResponse;
import com.fil.pickple.presentation.response.DeleteQuestionResponse;
import com.fil.pickple.presentation.response.FindQuestionsResponse;
import com.fil.pickple.application.QuestionService;
import com.fil.pickple.application.command.CreateQuestionCommand;
import com.fil.pickple.application.command.FindQuestionsCommand;
import com.fil.pickple.application.result.CreateQuestionResult;
import com.fil.pickple.application.result.FindQuestionsResult;
import com.fil.pickple.presentation.response.RefreshAllDoneQuestionsResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<CreateQuestionResponse> createQuestion(
            @RequestBody @Valid CreateQuestionRequest createQuestionRequest) {
        CreateQuestionCommand command = createQuestionRequest.toCommand();

        CreateQuestionResult result = questionService.createQuestion(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CreateQuestionResponse.from(result));
    }

    @GetMapping
    public ResponseEntity<FindQuestionsResponse> findQuestions(
            @RequestParam(required = true, name = "team") Long teamId,
            @RequestParam(required = false, name = "member") Long memberId) {
        FindQuestionsCommand command = new FindQuestionsCommand(teamId, memberId);

        FindQuestionsResult result = questionService.findQuestions(command);

        return ResponseEntity.ok(FindQuestionsResponse.from(result));
    }

    @DeleteMapping("/{question-id}")
    public ResponseEntity<DeleteQuestionResponse> deleteQuestion(
            @PathVariable("question-id") Long questionId) {
        DeleteQuestionCommand command = new DeleteQuestionCommand(questionId);

        questionService.deleteQuestion(command);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new DeleteQuestionResponse(true));
    }

    @PatchMapping("/refresh")
    public ResponseEntity<RefreshAllDoneQuestionsResponse> refreshAllDoneQuestions(
            @RequestBody @Valid RefreshAllDoneQuestionsRequest request) {
        RefreshDoneQuestionsCommand command = request.toCommand();

        questionService.refreshDoneQuestions(command);

        return ResponseEntity.ok(new RefreshAllDoneQuestionsResponse(true));
    }
}
