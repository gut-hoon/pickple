package com.fil.pickple.application.command;

public record CreatePickCommand(
        Long questionId,
        Long pickpleId
) {
}
