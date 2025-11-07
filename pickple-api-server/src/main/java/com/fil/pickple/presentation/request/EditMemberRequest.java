package com.fil.pickple.presentation.request;

import com.fil.pickple.validation.ImageFile;
import com.fil.pickple.domain.ProfileVisibility;
import com.fil.pickple.application.command.EditMemberCommand;
import jakarta.validation.constraints.AssertTrue;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record EditMemberRequest(
        String nickname,
        LocalDate birthDate,
        ProfileVisibility profileVisibility,
        @ImageFile(message = "아바타가 이미지 파일이 아닙니다")
        MultipartFile avatarImage,
        @ImageFile(message = "배경이 이미지 파일이 아닙니다")
        MultipartFile backgroundImage,
        Boolean removeAvatarImage,
        Boolean removeBackgroundImage
) {
    public EditMemberCommand toCommand() {
        return EditMemberCommand.builder()
                .nickname(nickname)
                .birthDate(birthDate)
                .avatarImage(avatarImage)
                .backgroundImage(backgroundImage)
                .profileVisibility(profileVisibility)
                .removeAvatarImage(removeAvatarImage)
                .removeBackgroundImage(removeBackgroundImage)
                .build();
    }

    @AssertTrue(message = "닉네임은 빈 문자열일 수 없습니다")
    public boolean isValidNickname() {
        return nickname == null || !nickname.isBlank();
    }

    @AssertTrue(message = "생년월일은 미래일 수 없습니다")
    public boolean isValidBirthDate() {
        return birthDate == null || !birthDate.isAfter(LocalDate.now());
    }
}
