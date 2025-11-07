package com.fil.pickple.application.command;


import java.time.LocalDateTime;

public record SearchReceivePickCommand(
        Integer pageSize,
        LocalDateTime lastCreatedAt,
        Long teamId
) {
    public static SearchReceivePickCommand of(Integer pageSize, LocalDateTime lastCreatedAt, Long teamId) {
        return new SearchReceivePickCommand(
                pageSize != null ? pageSize : 8,
                lastCreatedAt != null ? lastCreatedAt : LocalDateTime.now(),
                teamId
        );
    }
}
