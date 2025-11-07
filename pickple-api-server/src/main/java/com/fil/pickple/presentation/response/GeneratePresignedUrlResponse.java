package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.GeneratePresignedUrlResult;

public record GeneratePresignedUrlResponse(
        Boolean success,
        String url
) {
    public static GeneratePresignedUrlResponse from(GeneratePresignedUrlResult result){
        return new GeneratePresignedUrlResponse(true,result.url());
    }
}
