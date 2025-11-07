package com.fil.pickple.application.command;

import java.time.LocalDateTime;

public record HotBoardCommand(LocalDateTime since, int limit) {
    public static HotBoardCommand last24h(int limit) {
        return new HotBoardCommand(LocalDateTime.now().minusHours(24), limit);
    }
}
