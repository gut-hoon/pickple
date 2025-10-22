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

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false)
    private String backgroundImage;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private ProfileVisibility visibility;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    private Member(String email, String nickname, String profileImage, String backgroundImage, LocalDate birthDate, ProfileVisibility visibility) {
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.backgroundImage = backgroundImage;
        this.birthDate = birthDate;
        this.visibility = visibility;
        this.registeredAt = LocalDateTime.now();
        this.isDeleted = false;
    }
}
