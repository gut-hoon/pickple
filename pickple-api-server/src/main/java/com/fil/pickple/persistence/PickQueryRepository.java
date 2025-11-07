package com.fil.pickple.persistence;

import com.fil.pickple.domain.Pick;
import com.fil.pickple.domain.QPick;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class PickQueryRepository {
    private final JPAQueryFactory queryFactory;

    private static final QPick pick = QPick.pick;

    public Slice<Pick> findReceivePicks(
            Long memberId,
            Long pickpleId,
            Integer pageSize,
            LocalDateTime lastCreatedAt
    ) {
        List<Pick> picks = queryFactory
                .select(pick)
                .from(pick)
                .join(pick.question).fetchJoin()
                .join(pick.pickple).fetchJoin()
                .leftJoin(pick.pickple.teamSnapshot).fetchJoin()
                .where(
                        filterPickple(memberId, pickpleId),
                        cursor(lastCreatedAt)
                )
                .orderBy(pick.createdAt.desc())
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = picks.size() > pageSize;
        if (hasNext) {
            picks.removeLast();
        }

        return new SliceImpl<>(picks, Pageable.ofSize(pageSize), hasNext);
    }

    // 검색 조건에 픽플이 있으면 픽플로, 없으면 해당 사용자가 받은 모든 픽 조회
    private BooleanExpression filterPickple(Long memberId, Long pickpleId) {
        return pickpleId != null
                ? filterTeam(pickpleId)
                : filterMember(memberId);
    }

    private BooleanExpression filterMember(Long memberId) {
        return memberId != null ? pick.pickple.member.id.eq(memberId) : null;
    }

    private BooleanExpression filterTeam(Long pickpleId) {
        return pickpleId != null ? pick.pickple.id.eq(pickpleId) : null;
    }

    private BooleanExpression cursor(LocalDateTime lastCreatedAt) {
        return lastCreatedAt != null ? pick.createdAt.before(lastCreatedAt) : null;
    }
}
