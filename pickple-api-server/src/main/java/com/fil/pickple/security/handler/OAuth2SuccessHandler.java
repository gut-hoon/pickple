package com.fil.pickple.security.handler;

import com.fil.pickple.domain.Member;
import com.fil.pickple.security.provider.JwtTokenProvider;
import com.fil.pickple.application.result.OAuthMemberResult;
import com.fil.pickple.application.AuthService;
import com.fil.pickple.security.util.CookieUtil;
import com.fil.pickple.security.util.SecurityUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * OAuth2 로그인 성공 시 실행되는 핸들러
 * Google OAuth 로그인이 성공하면 Spring Security가 이 핸들러를 호출함
 */
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    private final String REDIRECT_URL;
    private final long ACCESS_TOKEN_EXPIRATION;
    private final long REFRESH_TOKEN_EXPIRATION;
    private final String ACCESS_TOKEN_NAME;
    private final String REFRESH_TOKEN_NAME;

    protected OAuth2SuccessHandler(
            AuthService authService,
            JwtTokenProvider jwtTokenProvider,
            @Value("${oauth.redirect-url}") String redirectUrl,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
            @Value("${jwt.cookie.access-token-name}") String accessTokenName,
            @Value("${jwt.cookie.refresh-token-name}") String refreshTokenName
    ) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.REDIRECT_URL = redirectUrl;
        this.ACCESS_TOKEN_EXPIRATION = accessTokenExpiration;
        this.REFRESH_TOKEN_EXPIRATION = refreshTokenExpiration;
        this.ACCESS_TOKEN_NAME = accessTokenName;
        this.REFRESH_TOKEN_NAME = refreshTokenName;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        OAuthMemberResult memberResult = authService.processOAuthLogin(email, name, picture);

        String accessToken = jwtTokenProvider.createAccessToken(
                memberResult.memberId(),
                memberResult.email(),
                memberResult.isRegisteredMember()
        );
        String refreshToken = jwtTokenProvider.createRefreshToken(memberResult.memberId());

        addTokenCookies(response, accessToken, refreshToken);

        String targetUrl;
        if (!memberResult.isNewMember() && memberResult.isRegisteredMember()) {
            targetUrl = REDIRECT_URL;
        }
        else {
            targetUrl = REDIRECT_URL + "/register";
        }
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * JWT 토큰을 HttpOnly 쿠키에 저장
     *
     * @param response (HTTP 응답)
     * @param accessToken
     * @param refreshToken
     */
    private void addTokenCookies(HttpServletResponse response, String accessToken, String refreshToken ) {
        Cookie accessTokenCookie = CookieUtil.createCookie(
                ACCESS_TOKEN_NAME,
                accessToken,
                (int) (ACCESS_TOKEN_EXPIRATION / 1000)

        );

        Cookie refreshTokenCookie = CookieUtil.createCookie(
                REFRESH_TOKEN_NAME,
                refreshToken,
                (int) (REFRESH_TOKEN_EXPIRATION / 1000)
        );
        CookieUtil.addCookies(response, accessTokenCookie, refreshTokenCookie);
    }
}
