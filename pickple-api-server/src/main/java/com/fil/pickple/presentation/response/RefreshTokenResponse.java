package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.RefreshTokenResult;

public record RefreshTokenResponse(
        Boolean success,
        Long memberId
) {
    public static RefreshTokenResponse from(RefreshTokenResult result) {
        return new RefreshTokenResponse(
                true,
                result.memberId()
        );
    }
}