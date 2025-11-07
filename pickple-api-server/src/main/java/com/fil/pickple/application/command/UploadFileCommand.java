package com.fil.pickple.application.command;

import org.springframework.web.multipart.MultipartFile;

public record UploadFileCommand(MultipartFile file, String key) {
}