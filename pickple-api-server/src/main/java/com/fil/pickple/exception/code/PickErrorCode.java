package com.fil.pickple.exception.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum PickErrorCode implements ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),

    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다"),

    ALREADY_PICKED(HttpStatus.CONFLICT, "이미 픽 되었습니다"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "픽 정보를 찾을 수 없습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
