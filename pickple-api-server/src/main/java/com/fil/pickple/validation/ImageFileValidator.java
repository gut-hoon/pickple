package com.fil.pickple.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class ImageFileValidator implements ConstraintValidator<ImageFile, MultipartFile> {
    private static final Set<String> ALLOWED_EXTENSION = Set.of(
            ".jpg", ".jpeg", ".png", ".gif", ".webp", ".svg"
    );

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        return isValidContentType(file) && isValidFileExtension(file);
    }

    private boolean isValidContentType(MultipartFile file) {
        String contentType = file.getContentType();

        return contentType != null && contentType.startsWith("image/");
    }

    private boolean isValidFileExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            return false;
        }

        String lowerFileName = filename.toLowerCase();
        return ALLOWED_EXTENSION.stream().anyMatch(lowerFileName::endsWith);
    }
}
