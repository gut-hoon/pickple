package com.fil.pickple.security.filter;

import com.fil.pickple.exception.code.AuthErrorCode;
import com.fil.pickple.security.provider.JwtTokenProvider;
import com.fil.pickple.security.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * 클라이언트 요청에서 JWT 토큰을 추출하고 유효성을 검사하는 필터
 * 쿠키에서 accessToken을 읽어서 인증 처리
 * 각 요청당 한 번만 실행되는 것을 보장하기 위해 OncePerRequestFilter를 상속
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final String ACCESS_TOKEN_NAME;

    protected JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            @Value("${jwt.cookie.access-token-name}") String accessTokenName
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        ACCESS_TOKEN_NAME = accessTokenName;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = CookieUtil.getCookieValue(request, ACCESS_TOKEN_NAME).orElse(null);

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
            boolean isRegistered = jwtTokenProvider.getIsRegisteredFromToken(token);

            String requestURI = request.getRequestURI();
            boolean isRegisterEndpoint = requestURI.equals("/members/register");

            if (!isRegistered && !isRegisterEndpoint) {
                request.setAttribute("exception", AuthErrorCode.REGISTRATION_REQUIRED);
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            memberId,                   // Principal (주체)
                            null,                       // credential (자격증명)
                            Collections.emptyList()     // authorities (권한)
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", AuthErrorCode.EXPIRED_TOKEN);
        } catch (SignatureException e) {
            request.setAttribute("exception", AuthErrorCode.INVALID_TOKEN_SIGNATURE);
        } catch (MalformedJwtException e) {
            request.setAttribute("exception", AuthErrorCode.INVALID_TOKEN);
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", AuthErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", AuthErrorCode.INVALID_TOKEN);
        } catch (Exception e) {
            request.setAttribute("exception", AuthErrorCode.INVALID_TOKEN);
        }

        filterChain.doFilter(request, response);
    }
}
