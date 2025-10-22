package com.fil.pickple.exception;

public record ErrorResponse(
        Boolean success,
        String message
) {
}
