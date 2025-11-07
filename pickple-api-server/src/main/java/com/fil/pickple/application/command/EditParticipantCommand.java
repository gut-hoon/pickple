package com.fil.pickple.application.command;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record EditParticipantCommand(
        Long participantId,
        String nickname,
        MultipartFile participantAvatarImage,
        MultipartFile participantBackgroundImage,
        Boolean removeAvatarImage,
        Boolean removeBackgroundImage
) {
    public EditParticipantCommand {
        nickname = nickname != null ? nickname.trim() : nickname;
    }
}
