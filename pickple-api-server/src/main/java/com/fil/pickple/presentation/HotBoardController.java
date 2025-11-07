package com.fil.pickple.presentation;

import com.fil.pickple.application.HotBoardService;
import com.fil.pickple.application.command.HotBoardCommand;
import com.fil.pickple.application.result.HotBoardResult;
import com.fil.pickple.presentation.response.HotBoardResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/hotboard")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class HotBoardController {

    private final HotBoardService hotBoardService;

    @GetMapping
    public ResponseEntity<HotBoardResponse> getHotBoard() {
        // 현재 : 최근 24시간, top 1
        HotBoardCommand command = HotBoardCommand.last24h(1);

        HotBoardResult result = hotBoardService.getHotBoardCached(command);

        return ResponseEntity.ok(HotBoardResponse.from(result));
    }
}