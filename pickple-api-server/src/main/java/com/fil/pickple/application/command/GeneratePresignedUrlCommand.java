package com.fil.pickple.application.command;

public record GeneratePresignedUrlCommand(String key, long ttlMinutes) {
}
