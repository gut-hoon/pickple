package com.fil.pickple.application.command;

public record FindQuestionsCommand(
        Long teamId,
        Long memberId
) {}
