package com.fil.pickple.exception.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SeasonErrorCode implements ErrorCode {
    
    NOT_FOUND(HttpStatus.NOT_FOUND, "현재 시즌을 찾을 수 없습니다");
    
    private final HttpStatus httpStatus;
    private final String message;
}
