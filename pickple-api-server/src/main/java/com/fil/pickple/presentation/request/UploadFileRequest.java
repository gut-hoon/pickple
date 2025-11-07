package com.fil.pickple.presentation.request;

import com.fil.pickple.application.command.UploadFileCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UploadFileRequest(
        @NotNull(message = "파일은 필수입니다.")
        MultipartFile file,
        @NotBlank(message = "키는 필수입니다.")
        String key
) {
    public UploadFileCommand toCommand() {
        return new UploadFileCommand(file, key);
    }
}
