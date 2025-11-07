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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DoneQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picker_id", nullable = false)
    private Participant picker;

    @Column(nullable = false)
    private Boolean isRefreshed;

    public DoneQuestion(Question question, Participant picker) {
        this.question = question;
        this.picker = picker;
        this.isRefreshed = false;
    }

    public void refresh(Participant requester) {
        if (!picker.getId().equals(requester.getId())) {
            throw new PickpleException(QuestionErrorCode.FORBIDDEN);
        }
        this.isRefreshed = true;
    }

    public void repick(Participant requester) {
        if (!picker.getId().equals(requester.getId())) {
            throw new PickpleException(QuestionErrorCode.FORBIDDEN);
        }
        this.isRefreshed = false;
    }
}
