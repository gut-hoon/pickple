package com.fil.pickple.application.command;

import java.time.LocalDate;

public record RegisterMemberCommand(
        String fullName,
        LocalDate birthDate
) {}