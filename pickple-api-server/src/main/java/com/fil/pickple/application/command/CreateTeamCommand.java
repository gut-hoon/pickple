package com.fil.pickple.application.command;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record CreateTeamCommand(
        String teamName,
        MultipartFile avatarImage,
        MultipartFile backgroundImage
) {
}
