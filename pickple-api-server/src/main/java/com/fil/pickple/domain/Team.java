package com.fil.pickple.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false)
    private String backgroundImage;

    @Column(nullable = false)
    private String invitationCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private Team(String name, String profileImage, String backgroundImage, String invitationCode) {
        this.name = name;
        this.profileImage = profileImage;
        this.backgroundImage = backgroundImage;
        this.invitationCode = invitationCode;
        this.createdAt = LocalDateTime.now();
    }
}
