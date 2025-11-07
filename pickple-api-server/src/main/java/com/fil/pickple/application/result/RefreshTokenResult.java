package com.fil.pickple.application.result;

public record RefreshTokenResult(
        Long memberId,
        String accessToken,
        String refreshToken
) {
    public static RefreshTokenResult of(Long memberId, String accessToken, String refreshToken) {
        return new RefreshTokenResult(memberId, accessToken, refreshToken);
    }
}
