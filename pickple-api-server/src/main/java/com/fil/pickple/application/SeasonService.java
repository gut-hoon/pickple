package com.fil.pickple.application;

import com.fil.pickple.application.result.SearchCurrentSeasonResult;
import com.fil.pickple.domain.Season;
import com.fil.pickple.exception.PickpleException;
import com.fil.pickple.exception.code.SeasonErrorCode;
import com.fil.pickple.persistence.SeasonRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SeasonService {
    private final SeasonRepository seasonRepository;
    private final int SEASON_DURATION_DAYS = 3;
    private final int SEASON_START_HOURS = 5;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy년 M월");

    @Transactional
    public void updateSeason() {
        if (seasonRepository.existsByEndAfter(LocalDateTime.now())) {
            return;
        }

        LocalDateTime curDateTime = LocalDate.now().atStartOfDay();

        int seasonCount;
        String currentMonthName = curDateTime.format(dtf);

        Season lastSeason = seasonRepository.findByEndIsBefore(curDateTime, Limit.of(1))
                .orElseGet(() -> Season.builder()
                        .name(dtf.format(curDateTime) + " 시즌0")
                        .build());

        String[] lastNameParts = lastSeason.getName().split(" ");
        String lastMonthName = lastNameParts[0] + " " + lastNameParts[1];

        if (lastMonthName.equals(currentMonthName)) {
            String lastNumberPart = lastNameParts[lastNameParts.length - 1].replace("시즌", "");
            try {
                seasonCount = Integer.parseInt(lastNumberPart) + 1;
            } catch (NumberFormatException e) {
                seasonCount = 1;
            }
        } else {
            seasonCount = 1;
        }

        // 시즌 이름 예시 : yy년 m월 시즌n
        String newName = currentMonthName + " 시즌" + seasonCount;

        Season newSeason = Season.builder()
                .name(newName)
                .start(curDateTime.withHour(SEASON_START_HOURS))
                .end(curDateTime.plusDays(SEASON_DURATION_DAYS).withHour(SEASON_START_HOURS))
                .build();

        seasonRepository.save(newSeason);
    }

    @Transactional(readOnly = true)
    public SearchCurrentSeasonResult searchCurrentSeason() {
        LocalDateTime now = LocalDateTime.now();
        Season currentSeason = seasonRepository.findCurrentSeason(now)
                .orElseThrow(() -> new PickpleException(SeasonErrorCode.NOT_FOUND));
        return SearchCurrentSeasonResult.from(currentSeason);
    }
}
