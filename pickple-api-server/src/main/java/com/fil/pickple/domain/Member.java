package com.fil.pickple.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String fullName;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true)
    private String avatarImage;

    @Column(nullable = true)
    private String backgroundImage;

    @Column(nullable = true)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private ProfileVisibility visibility;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    private Member(String email, String nickname, String avatarImage, String backgroundImage, ProfileVisibility visibility) {
        this.email = email;
        this.fullName = null;
        this.nickname = nickname;
        this.avatarImage = avatarImage;
        this.backgroundImage = backgroundImage;
        this.birthDate = null;
        this.visibility = visibility;
        this.registeredAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    public void register(String fullName, LocalDate birthDate) {
        this.fullName = fullName;
        this.birthDate = birthDate;
    }

    public void editNickname(String nickname) {
        this.nickname = nickname;
    }

    public void editAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public void editBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void editBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void editVisibility(ProfileVisibility visibility) {
        this.visibility = visibility;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
