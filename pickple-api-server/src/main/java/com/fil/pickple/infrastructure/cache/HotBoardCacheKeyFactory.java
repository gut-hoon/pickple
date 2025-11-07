package com.fil.pickple.infrastructure.cache;

import com.fil.pickple.application.command.HotBoardCommand;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
public class HotBoardCacheKeyFactory {

    private static final DateTimeFormatter KEY_TIME_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    public String keyFor(HotBoardCommand command){
        Objects.requireNonNull(command, "command는 null이 될 수 없습니다.");
        LocalDateTime since = command.since();

        LocalDateTime bucket = truncateTo5Minutes(since);

        String bucketStr = KEY_TIME_FMT.format(bucket);
        return "hotboard:" + bucketStr + ":limit=" + command.limit();
    }

    private static LocalDateTime truncateTo5Minutes(LocalDateTime dt) {
        LocalDateTime truncated = dt.truncatedTo(ChronoUnit.MINUTES); // 초/나노 제거
        int minute = truncated.getMinute();
        int rounded = (minute / 5) * 5;
        return truncated.withMinute(rounded);
    }
}
