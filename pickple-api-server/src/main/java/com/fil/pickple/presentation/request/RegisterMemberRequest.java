package com.fil.pickple.presentation.request;

import com.fil.pickple.application.command.RegisterMemberCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterMemberRequest(
        @NotBlank(message = "실명은 필수입니다")
        String fullName,
        @NotNull(message = "생년월일은 필수입니다")
        LocalDate birthDate
) {
    public RegisterMemberCommand toCommand() {
        return new RegisterMemberCommand(
                fullName,
                birthDate
        );
    }
}
