package com.fil.pickple.exception.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MemberErrorCode implements ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),

    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),

    ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다"),
    ALREADY_EXISTS_USERNAME(HttpStatus.CONFLICT, "요청한 이메일을 이미 사용 중입니다"),
    ALREADY_EXISTS_NICKNAME(HttpStatus.CONFLICT, "요청한 닉네임을 이미 사용 중입니다"),
    ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "이미 추가 정보 입력이 완료되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
