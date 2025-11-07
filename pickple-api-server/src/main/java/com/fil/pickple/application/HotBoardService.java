package com.fil.pickple.application;

import com.fil.pickple.application.command.HotBoardCommand;
import com.fil.pickple.application.result.HotBoardResult;
import com.fil.pickple.infrastructure.cache.HotBoardCacheKeyFactory;
import com.fil.pickple.persistence.MemberRepository;
import com.fil.pickple.persistence.QuestionRepository;
import com.fil.pickple.persistence.TeamRepository;
import com.fil.pickple.persistence.po.MemberHotPo;
import com.fil.pickple.persistence.po.QuestionHotPo;
import com.fil.pickple.persistence.po.TeamHotPo;
import com.fil.pickple.presentation.response.HotBoardResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class HotBoardService {
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final TeamRepository teamRepository;
    private final HotBoardCacheKeyFactory cacheKeyFactory;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional(readOnly = true)
    public HotBoardResult getHotBoardCached(HotBoardCommand command) {
        String cacheKey = cacheKeyFactory.keyFor(command);

        HotBoardResult cachedResult = (HotBoardResult) redisTemplate.opsForValue().get(cacheKey);

        if (cachedResult != null) {
            log.info("핫보드 캐시 적중: {}", cacheKey);
            return cachedResult;
        }
        log.info("핫보드 캐시 미스 - 집계 호출: {}", cacheKey);

        return getHotBoard(command);
    }

    @Transactional(readOnly = true)
    public HotBoardResult getHotBoard(HotBoardCommand command) {
        int limit = Math.max(1, command.limit());
        Pageable topN = PageRequest.of(0, limit);

        List<MemberHotPo> hotMembers = memberRepository.findHotMembersSince(command.since(), topN);
        List<QuestionHotPo> hotQuestions = questionRepository.findHotQuestionsSince(command.since(), topN);
        List<TeamHotPo> hotTeams = teamRepository.findHotTeamsSince(command.since(), topN);

        HotBoardResult result = HotBoardResult.of(hotMembers, hotQuestions, hotTeams);

        String cacheKey = cacheKeyFactory.keyFor(command);
        redisTemplate.opsForValue().set(cacheKey, result, Duration.ofMinutes(5));
        log.info("핫보드 캐시 저장 완료: {}", cacheKey);

        return result;
    }
}