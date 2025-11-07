package com.fil.pickple.application.command;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record UpdateTeamProfileCommand(
        Long teamId,
        String teamName,
        MultipartFile teamAvatarImage,
        MultipartFile teamBackgroundImage,
        Boolean removeAvatarImage,
        Boolean removeBackgroundImage
) {
}
