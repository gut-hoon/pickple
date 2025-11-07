package com.fil.pickple.presentation.request;

import com.fil.pickple.application.command.EditParticipantCommand;
import com.fil.pickple.validation.ImageFile;
import jakarta.validation.constraints.AssertTrue;
import org.springframework.web.multipart.MultipartFile;

public record EditParticipantRequest(
        String nickname,

        @ImageFile(message = "아바타가 이미지 파일이 아닙니다")
        MultipartFile participantAvatarImage,

        @ImageFile(message = "배경이 이미지 파일이 아닙니다")
        MultipartFile participantBackgroundImage,

        Boolean removeAvatarImage,
        Boolean removeBackgroundImage
) {

    public EditParticipantRequest {
        removeAvatarImage = Boolean.TRUE.equals(removeAvatarImage);
        removeBackgroundImage = Boolean.TRUE.equals(removeBackgroundImage);
        if (nickname != null && nickname.isBlank()) nickname = null;
    }


    public EditParticipantCommand toCommand(Long participantId) {
        return EditParticipantCommand.builder()
                .participantId(participantId)
                .nickname(nickname)
                .participantAvatarImage(participantAvatarImage)
                .participantBackgroundImage(participantBackgroundImage)
                .removeAvatarImage(removeAvatarImage)
                .removeBackgroundImage(removeBackgroundImage)
                .build();
    }

    @AssertTrue(message = "수정할 필드가 없습니다")
    public boolean hasAnyField() {
        return nickname != null || participantAvatarImage != null || participantBackgroundImage != null ||  removeAvatarImage || removeBackgroundImage;
    }
}
