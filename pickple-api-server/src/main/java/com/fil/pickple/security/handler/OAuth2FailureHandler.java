package com.fil.pickple.security.handler;

import com.fil.pickple.exception.code.AuthErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * OAuth2 로그인 실패 핸들러
 * 로그인 페이지로 리다이렉트
 */
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final String FAILURE_REDIRECT_URL;

    protected OAuth2FailureHandler(
            @Value("${oauth.failure-redirect-url}")String failureRedirectUrl
    ) {
        this.FAILURE_REDIRECT_URL = failureRedirectUrl;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String redirectUrl = UriComponentsBuilder.fromUriString(FAILURE_REDIRECT_URL)
                .queryParam("error", "login_failed")
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}