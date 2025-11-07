package com.fil.pickple.presentation;

import com.fil.pickple.application.SeasonService;
import com.fil.pickple.application.result.SearchCurrentSeasonResult;
import com.fil.pickple.presentation.response.SearchCurrentSeasonResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seasons")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SeasonController {

    private final SeasonService seasonService;

    @Transactional
    @GetMapping
    public ResponseEntity<SearchCurrentSeasonResponse> searchCurrentSeason(){

        SearchCurrentSeasonResult result = seasonService.searchCurrentSeason();

        return ResponseEntity.ok(SearchCurrentSeasonResponse.from(result));
    }
}
