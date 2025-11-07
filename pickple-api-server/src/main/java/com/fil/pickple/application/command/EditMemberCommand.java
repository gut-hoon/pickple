package com.fil.pickple.application.command;

import com.fil.pickple.domain.ProfileVisibility;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Builder
public record EditMemberCommand(
        String nickname,
        MultipartFile avatarImage,
        MultipartFile backgroundImage,
        LocalDate birthDate,
        ProfileVisibility profileVisibility,
        Boolean removeAvatarImage,
        Boolean removeBackgroundImage
) {
}
