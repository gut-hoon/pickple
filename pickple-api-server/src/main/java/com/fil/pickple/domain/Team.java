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

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = true)
    private String avatarImage;

    @Column(nullable = true)
    private String backgroundImage;

    @Column(nullable = false, unique = true)
    private String invitationCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private Team(String name, String avatarImage, String backgroundImage, String invitationCode) {
        this.name = name;
        this.avatarImage = avatarImage;
        this.backgroundImage = backgroundImage;
        this.invitationCode = invitationCode;
        this.createdAt = LocalDateTime.now();
    }

    public void editTeamName(String teamName) {
        if (name != null) {
            this.name = teamName;
        }
    }

    public void editAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public void editBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
}
