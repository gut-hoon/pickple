package com.fil.pickple.exception.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum TeamErrorCode implements ErrorCode{

    NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다"),
    NOT_FOUND_PARTICIPANT(HttpStatus.NOT_FOUND, "참가자를 찾을 수 없습니다"),

    FORBIDDEN(HttpStatus.FORBIDDEN, "팀에 접근할 권한이 없습니다"),
    FORBIDDEN_PARTICIPANT_MANIPULATION(HttpStatus.FORBIDDEN, "참가자 정보 조작 금지"),

    ALREADY_EXISTS_TEAMNAME(HttpStatus.CONFLICT, "요청한 팀 이름을 이미 사용 중입니다"),
    ALREADY_EXISTS_NICKNAME(HttpStatus.CONFLICT, "요청한 사용자 이름을 이미 사용 중입니다"),
    NO_FIELD_TO_UPDATE(HttpStatus.BAD_REQUEST, "업데이트할 필드가 없습니다"),
    BLANK_INVITATION_CODE(HttpStatus.BAD_REQUEST, "초대 코드를 입력해주세요."),
    INVALID_INVITATION_CODE(HttpStatus.NOT_FOUND, "유효하지 않은 초대 코드입니다."),
    ALREADY_JOINED_TEAM(HttpStatus.CONFLICT, "이미 가입한 팀입니다"),
    NOT_FOUND_INVITATION_CODE(HttpStatus.NOT_FOUND, "초대 코드를 찾을 수 없습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
