package com.fil.pickple.service.command;

public record SearchBaseProfileCommand(
        Long memberId
) {
    public static SearchBaseProfileCommand of(Long memberId) {
        return new SearchBaseProfileCommand(memberId);
    }
}
