package com.wbigelow.ancillary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PermissionLevel {
    ANY (0),
    MEETUPS_PLUS(1),
    MOD (2),
    ADMIN (3);
    @Getter
    private final int level;
}
