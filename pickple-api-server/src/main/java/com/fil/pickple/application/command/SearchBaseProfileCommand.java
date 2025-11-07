package com.fil.pickple.application.command;

public record SearchBaseProfileCommand(
        Long memberId
) {
    public static SearchBaseProfileCommand of(Long memberId) {
        return new SearchBaseProfileCommand(memberId);
    }
}
