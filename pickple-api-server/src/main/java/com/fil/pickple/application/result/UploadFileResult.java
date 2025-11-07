package com.fil.pickple.application.result;

public record UploadFileResult(String key) {
    public static UploadFileResult of(String key) {
        return new UploadFileResult(key);
    }
}
