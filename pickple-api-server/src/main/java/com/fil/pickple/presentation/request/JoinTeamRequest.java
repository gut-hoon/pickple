package com.fil.pickple.presentation.request;

import com.fil.pickple.application.command.JoinTeamCommand;
import com.fil.pickple.validation.ImageFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record JoinTeamRequest(
        @NotBlank(message = "닉네임은 필수 입력사항입니다")
        String nickname,

        @ImageFile(message = "아바타가 이미지 파일이 아닙니다")
        MultipartFile avatarImage,

        @ImageFile(message = "배경이 이미지 파일이 아닙니다")
        MultipartFile backgroundImage,

        @NotBlank(message = "초대 코드는 필수 입력사항입니다")
        String invitationCode
) {
    public JoinTeamCommand toCommand() {
        return JoinTeamCommand.builder()
                .nickname(nickname)
                .avatarImage(avatarImage)
                .backgroundImage(backgroundImage)
                .invitationCode(invitationCode)
                .build();
    }
}
