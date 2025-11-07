package com.fil.pickple.presentation.request;


import com.fil.pickple.application.command.CreateQuestionCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateQuestionRequest(
        @NotNull(message = "질문을 등록할 팀 ID는 필수입니다")
        Long teamId,
        @NotBlank(message = "질문의 내용은 필수 입니다")
        String content
) {
        public CreateQuestionCommand toCommand() {
                return new CreateQuestionCommand(
                        teamId,
                        content
                );
        }
}
