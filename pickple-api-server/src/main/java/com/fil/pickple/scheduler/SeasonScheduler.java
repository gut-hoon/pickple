package com.fil.pickple.scheduler;

import com.fil.pickple.application.SeasonService;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SeasonScheduler {
    private final SeasonService seasonService;

    @PostConstruct
    public void init() {
        seasonService.updateSeason();
    }

    @Scheduled(cron = "0 0 5 * * *")
    public void updateSeason() {
        seasonService.updateSeason();
    }
}
