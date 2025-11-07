package com.fil.pickple.exception;

import com.fil.pickple.exception.code.CommonErrorCode;
import com.fil.pickple.exception.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
@Slf4j
public class PickpleExceptionHandler {

    @ExceptionHandler(PickpleException.class)
    public ResponseEntity<ErrorResponse> handlePickpleException(PickpleException e) {
        log.error("PickpleException 발생: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse(false, e.getMessage());

        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException 발생: {}", e);

        StringBuilder messages = new StringBuilder();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            messages.append(error.getDefaultMessage()).append(", ");
        }
        messages.deleteCharAt(messages.length() - 1);
        messages.deleteCharAt(messages.length() - 1);

        ErrorCode code = CommonErrorCode.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(false, messages.toString());

        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException 발생: {}", e);

        ErrorCode code = CommonErrorCode.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(false, String.format("파라미터 %s가 누락되었습니다", e.getParameterName()));

        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException 발생: {}", e);

        ErrorCode code = CommonErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse response = new ErrorResponse(false, code.getMessage());

        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("AccessDeniedException: 접근이 거부되었습니다.", e);
        ErrorCode code = CommonErrorCode.FORBIDDEN;
        ErrorResponse response = new ErrorResponse(false, code.getMessage());

        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException e) {
        log.warn("MaxUploadSizeExceededException: 파일 최대 크기를 초과하였습니다.", e);
        ErrorCode code = CommonErrorCode.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(false, code.getMessage());

        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }
}
