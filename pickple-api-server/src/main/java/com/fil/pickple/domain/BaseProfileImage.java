package com.fil.pickple.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BaseProfileImage {
    @Id
    private Long id;

    @Column(nullable = false)
    private String personalAvatarImage;

    @Column(nullable = false)
    private String personalBackgroundImage;

    @Column(nullable = false)
    private String teamAvatarImage;

    @Column(nullable = false)
    private String teamBackgroundImage;
}
