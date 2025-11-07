package com.fil.pickple.application;

import com.fil.pickple.security.provider.JwtTokenProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenBlackListService {

    private final StringRedisTemplate redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void addToBlackList(String token, long ttlMillis) {
        if (token == null || token.isBlank()) {
            return;
        }

        if (ttlMillis <= 0) {
            return;
        }

        String key = BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(key, "", ttlMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isBlackListed(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        String key = BLACKLIST_PREFIX + token;
        return redisTemplate.hasKey(key);
    }

    public void addAccessTokenToBlacklist(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            return;
        }

        if (jwtTokenProvider.validateToken(accessToken) &&
                jwtTokenProvider.isAccessToken(accessToken) &&
                !isBlackListed(accessToken)
        ) {
            long accessTokenTTL = jwtTokenProvider.getRemainingTimeMillis(accessToken);
            addToBlackList(accessToken, accessTokenTTL);
        }
    }

    public void addRefreshTokenToBlacklist(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return;
        }

        if (jwtTokenProvider.validateToken(refreshToken) &&
                jwtTokenProvider.isRefreshToken(refreshToken) &&
                !isBlackListed(refreshToken)) {
            long refreshTokenTTL = jwtTokenProvider.getRemainingTimeMillis(refreshToken);
            addToBlackList(refreshToken, refreshTokenTTL);
        }
    }
}