package com.fil.pickple.exception;

import com.fil.pickple.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class PickpleException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String detailMessage;

    public PickpleException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.detailMessage = null;
    }

    public PickpleException(ErrorCode errorCode, String detailMessage) {
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

    public HttpStatus getHttpStatus() {
        return this.errorCode.getHttpStatus();
    }

    public String getMessage() {
        return detailMessage == null || detailMessage.isBlank() ? errorCode.getMessage() : detailMessage;
    }
}
