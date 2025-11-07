package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.SearchCurrentSeasonResult;
import com.fil.pickple.domain.Season;

public record SearchCurrentSeasonResponse(
        Season currentSeason
) {
    public static SearchCurrentSeasonResponse from(SearchCurrentSeasonResult result){
        return new SearchCurrentSeasonResponse(
                result.currentSeason()
        );
    }
}
