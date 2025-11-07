package com.fil.pickple.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fil.pickple.exception.ErrorResponse;
import com.fil.pickple.exception.code.AuthErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * 인증되지 않은 사용자가 리소스에 접근할 때 호출
 * JSON 응답 반환
 */
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        AuthErrorCode errorCode = (AuthErrorCode) request.getAttribute("exception");

        if (errorCode == null) {
            errorCode = AuthErrorCode.UNAUTHORIZED;
        }

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(
                false,
                errorCode.getMessage()
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
