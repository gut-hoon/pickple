package com.fil.pickple.application.result;

import com.fil.pickple.domain.Season;

public record SearchCurrentSeasonResult(
        Season currentSeason
) {
    public static SearchCurrentSeasonResult from(Season currentSeason) {
        return new SearchCurrentSeasonResult(currentSeason);
    }
}
