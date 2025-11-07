package com.fil.pickple.presentation.request;

import com.fil.pickple.application.command.UpdateTeamProfileCommand;
import com.fil.pickple.validation.ImageFile;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UpdateTeamProfileRequest(
        @Size(max = 10)
        String teamName,

        @ImageFile(message = "아바타가 이미지 파일이 아닙니다")
        MultipartFile teamAvatarImage,

        @ImageFile(message = "배경이 이미지 파일이 아닙니다")
        MultipartFile teamBackgroundImage,

        Boolean removeAvatarImage,
        Boolean removeBackgroundImage
) {
    public UpdateTeamProfileRequest {
        removeAvatarImage = Boolean.TRUE.equals(removeAvatarImage);
        removeBackgroundImage = Boolean.TRUE.equals(removeBackgroundImage);

        if (teamName != null && teamName.isBlank()) teamName = null;
    }

    public UpdateTeamProfileCommand toCommand(Long teamId) {
        return UpdateTeamProfileCommand.builder()
                .teamId(teamId)
                .teamName(teamName)
                .teamAvatarImage(teamAvatarImage)
                .teamBackgroundImage(teamBackgroundImage)
                .removeAvatarImage(removeAvatarImage)
                .removeBackgroundImage(removeBackgroundImage)
                .build();
    }

    @AssertTrue(message = "수정할 필드가 없습니다")
    public boolean hasAnyField() {
        return teamName != null || teamAvatarImage != null || teamBackgroundImage != null ||  removeAvatarImage || removeBackgroundImage;
    }
}
