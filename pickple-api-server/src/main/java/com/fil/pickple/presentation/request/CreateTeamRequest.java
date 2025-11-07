package com.fil.pickple.presentation.request;

import com.fil.pickple.application.command.CreateTeamCommand;
import com.fil.pickple.validation.ImageFile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

public record CreateTeamRequest(

        @NotBlank(message = "팀 이름은 필수입니다")
        @Length(max = 10)
        String teamName,

        @ImageFile(message = "아바타가 이미지 파일이 아닙니다")
        MultipartFile avatarImage,

        @ImageFile(message = "배경이 이미지 파일이 아닙니다")
        MultipartFile backgroundImage
) {
    public CreateTeamCommand toCommand(){
        return CreateTeamCommand.builder()
                .teamName(teamName)
                .avatarImage(avatarImage)
                .backgroundImage(backgroundImage)
                .build();
    }
}
