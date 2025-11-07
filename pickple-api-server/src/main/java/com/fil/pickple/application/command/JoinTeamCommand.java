package com.fil.pickple.application.command;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record JoinTeamCommand(
        String nickname,
        MultipartFile avatarImage,
        MultipartFile backgroundImage,
        String invitationCode
) {
    public JoinTeamCommand {
        nickname = nickname.trim();
    }
}
