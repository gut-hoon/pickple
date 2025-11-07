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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String avatarImage;

    @Column(nullable = true)
    private String backgroundImage;

    @Builder
    public TeamSnapshot(String name, String avatarImage, String backgroundImage) {
        this.name = name;
        this.avatarImage = avatarImage;
        this.backgroundImage = backgroundImage;
    }
}
