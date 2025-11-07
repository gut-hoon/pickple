package com.fil.pickple.exception.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthErrorCode implements ErrorCode {

    // 인증 관련 에러
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰 형식입니다."),
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "토큰 서명이 유효하지 않습니다."),

    // 권한 관련 에러
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    REGISTRATION_REQUIRED(HttpStatus.FORBIDDEN, "추가 정보 등록이 필요합니다."),

    // 토큰 갱신 관련 에러
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었니다."),
    
    // 블랙리스트 관련 에러
    BLACKLISTED_TOKEN(HttpStatus.UNAUTHORIZED, "무효화된 토큰입니다. 다시 로그인해주세요."),

    // OAuth 관련 에러
    OAUTH_LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth 로그인에 실패했습니다."),
    OAUTH_USER_INFO_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth 사용자 정보 조회에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}