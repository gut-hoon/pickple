package com.fil.pickple.application.result;

import com.fil.pickple.domain.Pick;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public record SearchReceivePickResult(
        List<PickResult> picks,
        boolean hasNext
) {
    public static SearchReceivePickResult of(Slice<Pick> picks) {
        return new SearchReceivePickResult(
                picks.getContent().stream().map(PickResult::from).toList(),
                picks.hasNext()
        );
    }

    public record PickResult(
            String content,
            LocalDateTime createdAt,
            Long teamId,
            String teamName,
            String teamAvatarImage
    ) {
        public static PickResult from(Pick pick) {
            String teamName = pick.getPickple().getIsLeft() ? pick.getPickple().getTeamSnapshot().getName() : pick.getPickple().getTeam().getName();
            String teamAvatarImage = pick.getPickple().getIsLeft() ? pick.getPickple().getTeamSnapshot().getAvatarImage() : pick.getPickple().getTeam().getAvatarImage();

            return new PickResult(
                    pick.getQuestion().getContent(),
                    pick.getCreatedAt(),
                    pick.getPickple().getTeam().getId(),
                    teamName,
                    teamAvatarImage
            );
        }
    }
}
