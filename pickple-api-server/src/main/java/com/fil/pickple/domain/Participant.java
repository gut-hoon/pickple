package com.fil.pickple.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_snapshot_id", nullable = true)
    private TeamSnapshot teamSnapshot;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true)
    private String avatarImage;

    @Column(nullable = true)
    private String backgroundImage;

    @Column(nullable = false)
    private Boolean isCreator;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    @Column(nullable = false)
    private Boolean isLeft;

    @Builder
    private Participant(Team team, Member member, String nickname, String avatarImage, String backgroundImage, Boolean isCreator) {
        this.team = team;
        this.member = member;
        this.teamSnapshot = null;
        this.isCreator = isCreator;
        this.nickname = nickname;
        this.avatarImage = avatarImage;
        this.backgroundImage = backgroundImage;
        this.joinedAt = LocalDateTime.now();
        this.isLeft = false;
    }

    public void leave(TeamSnapshot teamSnapshot) {
        this.teamSnapshot = teamSnapshot;
        this.isLeft = true;
    }

    public void reJoin(String nickname, String avatarImage, String backgroundImage) {
        this.nickname = nickname;
        this.avatarImage = avatarImage;
        this.backgroundImage = backgroundImage;
        this.isLeft = false;
    }

    public void editNickname(String nickname) {
        if (nickname != null) {
            this.nickname = nickname;
        }
    }

    public void editAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public void editBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

}
