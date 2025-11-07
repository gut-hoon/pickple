package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.SearchReceivePickResult;

import java.time.LocalDateTime;
import java.util.List;

public record SearchReceivePickResponse(
        boolean success,
        boolean hasNext,
        List<Pick> picks
) {
    public static SearchReceivePickResponse from(SearchReceivePickResult result) {
        return new SearchReceivePickResponse(
                true,
                result.hasNext(),
                result.picks().stream().map(Pick::from).toList()
        );
    }

    record Pick(
            String question,
            LocalDateTime createdAt,
            Long teamId,
            String teamName,
            String teamAvatarImage
    ) {
        static Pick from(SearchReceivePickResult.PickResult result) {
            return new Pick(
                    result.content(),
                    result.createdAt(),
                    result.teamId(),
                    result.teamName(),
                    result.teamAvatarImage()
            );
        }
    }
}
