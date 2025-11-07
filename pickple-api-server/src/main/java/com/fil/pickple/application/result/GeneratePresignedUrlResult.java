package com.fil.pickple.application.result;

public record GeneratePresignedUrlResult(String url) {
    public static GeneratePresignedUrlResult of(String url) {
        return new GeneratePresignedUrlResult(url);
    }
}
