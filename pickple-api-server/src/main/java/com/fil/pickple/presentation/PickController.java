package com.fil.pickple.presentation;

import com.fil.pickple.application.PickFacade;
import com.fil.pickple.application.PickService;
import com.fil.pickple.application.command.PickAndGetNextQuestionCommand;
import com.fil.pickple.application.command.SearchReceivePickCommand;
import com.fil.pickple.application.result.PickAndGetNextQuestionResult;
import com.fil.pickple.application.result.SearchReceivePickResult;
import com.fil.pickple.presentation.request.PickAndGetNextQuestionRequest;
import com.fil.pickple.presentation.response.PickAndGetNextQuestionResponse;
import com.fil.pickple.presentation.response.SearchReceivePickResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/picks")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PickController {
    private final PickService pickService;
    private final PickFacade pickFacade;

    @PostMapping("/next")
    public ResponseEntity<PickAndGetNextQuestionResponse> nextQuestion(
            @RequestBody @Valid PickAndGetNextQuestionRequest request) {
        PickAndGetNextQuestionCommand command = request.toCommand();

        PickAndGetNextQuestionResult result = pickFacade.pickAndGetNextQuestion(command);

        return ResponseEntity.ok(PickAndGetNextQuestionResponse.from(result));
    }

    @GetMapping("/received")
    public ResponseEntity<SearchReceivePickResponse> searchReceivePicks(
            @RequestParam(value = "page-size", required = false) Integer pageSize,
            @RequestParam(value = "last-created-at", required = false) LocalDateTime lastCreatedAt,
            @RequestParam(value = "team-id", required = false) Long teamId
    ) {
        SearchReceivePickCommand command = SearchReceivePickCommand.of(pageSize, lastCreatedAt, teamId);

        SearchReceivePickResult result = pickService.searchReceivePick(command);

        return ResponseEntity.ok(SearchReceivePickResponse.from(result));
    }

}
