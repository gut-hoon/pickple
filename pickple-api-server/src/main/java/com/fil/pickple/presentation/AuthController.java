package com.fil.pickple.presentation;

import com.fil.pickple.exception.PickpleException;
import com.fil.pickple.exception.code.AuthErrorCode;
import com.fil.pickple.presentation.response.RefreshTokenResponse;
import com.fil.pickple.application.AuthService;
import com.fil.pickple.application.result.RefreshTokenResult;
import com.fil.pickple.security.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 인증 관련 API Controller
 * 토큰 갱신, 로그아웃 등 담당
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    private final String REFRESH_TOKEN_NAME;
    private final String ACCESS_TOKEN_NAME;
    private final long ACCESS_TOKEN_EXPIRATION;
    private final long REFRESH_TOKEN_EXPIRATION;

    protected AuthController(
            AuthService authService,
            @Value("${jwt.cookie.refresh-token-name}") String refreshTokenName,
            @Value("${jwt.cookie.access-token-name}") String accessTokenName,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.authService = authService;
        this.REFRESH_TOKEN_NAME = refreshTokenName;
        this.ACCESS_TOKEN_NAME = accessTokenName;
        this.ACCESS_TOKEN_EXPIRATION = accessTokenExpiration;
        this.REFRESH_TOKEN_EXPIRATION = refreshTokenExpiration;
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshAccessToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
      String refreshToken = CookieUtil.getCookieValue(request, REFRESH_TOKEN_NAME)
              .orElseThrow(() -> new PickpleException(AuthErrorCode.INVALID_REFRESH_TOKEN));

        RefreshTokenResult result = authService.refreshAccessToken(refreshToken);

        Cookie accessTokenCookie = CookieUtil.createCookie(
                ACCESS_TOKEN_NAME,
                result.accessToken(),
                (int) (ACCESS_TOKEN_EXPIRATION / 1000)
        );
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = CookieUtil.createCookie(
                REFRESH_TOKEN_NAME,
                result.refreshToken(),
                (int) (REFRESH_TOKEN_EXPIRATION / 1000)
        );
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(RefreshTokenResponse.from(result));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String accessToken = CookieUtil.getCookieValue(request, ACCESS_TOKEN_NAME).orElse(null);
        String refreshToken = CookieUtil.getCookieValue(request, REFRESH_TOKEN_NAME).orElse(null);

        authService.logout(accessToken, refreshToken);

        Cookie accessTokenCookie = CookieUtil.createCookie(ACCESS_TOKEN_NAME,"",0);
        Cookie refreshTokenCookie = CookieUtil.createCookie(REFRESH_TOKEN_NAME, "", 0);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.noContent().build();
    }
}
