package com.fil.pickple.domain;

import com.fil.pickple.exception.PickpleException;
import com.fil.pickple.exception.code.QuestionErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Participant author;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    private Question(Participant author, String content) {
        this.author = author;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    public void delete(Member requester) {
        if (!author.getMember().getId().equals(requester.getId())) {
            throw new PickpleException(QuestionErrorCode.FORBIDDEN);
        }
        this.isDeleted = true;
    }
}
