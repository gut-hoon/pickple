package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.UploadFileResult;

public record UploadFileResponse(
        Boolean success,
        String key
) {
    public static UploadFileResponse from(UploadFileResult result){
        return new UploadFileResponse( true, result.key());
    }
}
