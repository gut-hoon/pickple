package com.fil.pickple.security.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey SECRET_KEY;
    private final long ACCESS_TOKEN_EXPIRATION;
    private final long REFRESH_TOKEN_EXPIRATION;

    private final String TOKEN_TYPE_ACCESS;
    private final String TOKEN_TYPE_REFRESH;
    private final String CLAIM_TYPE;
    private final String CLAIM_EMAIL;
    private final String CLAIM_IS_REGISTERED;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
            @Value("${jwt.token-type.access}") String tokenTypeAccess,
            @Value("${jwt.token-type.refresh}") String tokenTypeRefresh,
            @Value("${jwt.claim.type}") String claimType,
            @Value("${jwt.claim.email}") String claimEmail,
            @Value("${jwt.claim.is-registered}") String claimIsRegistered
    ) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ACCESS_TOKEN_EXPIRATION = accessTokenExpiration;
        this.REFRESH_TOKEN_EXPIRATION = refreshTokenExpiration;
        this.TOKEN_TYPE_ACCESS = tokenTypeAccess;
        this.TOKEN_TYPE_REFRESH = tokenTypeRefresh;
        this.CLAIM_TYPE = claimType;
        this.CLAIM_EMAIL = claimEmail;
        this.CLAIM_IS_REGISTERED = claimIsRegistered;
    }

    /**
     * Access Token 생성
     */
    public String createAccessToken(Long memberId, String email, boolean isRegistered) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION);

        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .claim(CLAIM_EMAIL, email)
                .claim(CLAIM_IS_REGISTERED, isRegistered)
                .claim(CLAIM_TYPE, TOKEN_TYPE_ACCESS)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Refresh Token 생성
     */
    public String createRefreshToken(Long memberId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION);

        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .claim(CLAIM_TYPE, TOKEN_TYPE_REFRESH)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 토큰에서 회원 ID 추출
     */
    public Long getMemberIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.valueOf(claims.getSubject());
    }

    /**
     * 토큰에서 이메일 추출
     */
    public String getEmailFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get(CLAIM_EMAIL, String.class);
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException | SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    /**
     * 토큰 파싱
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 토큰 타입 확인 (access/refresh)
     */
    public String getTokenType(String token) {
        Claims claims = parseToken(token);
        return claims.get(CLAIM_TYPE, String.class);
    }

    /**
     * Refresh Token인지 확인
     */
    public boolean isRefreshToken(String token) {
        return TOKEN_TYPE_REFRESH.equals(getTokenType(token));
    }

    /**
     * Access Token인지 확인
     */
    public boolean isAccessToken(String token) {
        return TOKEN_TYPE_ACCESS.equals(getTokenType(token));
    }

    /**
     * 토큰 만료 시간 확인
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }

    /**
     * 토큰 만료까지 남은 시간 (밀리초)
     * 이미 만료된 경우 0을 반환
     */
    public long getRemainingTimeMillis(String token) {
        Date expiration = getExpirationFromToken(token);
        long now = System.currentTimeMillis();
        long expirationTime = expiration.getTime();

        long remainingTime = expirationTime - now;
        return Math.max(remainingTime, 0);
    }

    /**
     * 토큰에서 등록 여부 추출
     */
    public boolean getIsRegisteredFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get(CLAIM_IS_REGISTERED, Boolean.class);
    }
}