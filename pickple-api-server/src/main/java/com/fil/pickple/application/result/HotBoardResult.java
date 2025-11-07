package com.fil.pickple.application.result;

import com.fil.pickple.persistence.po.MemberHotPo;
import com.fil.pickple.persistence.po.QuestionHotPo;
import com.fil.pickple.persistence.po.TeamHotPo;

import java.util.List;

public record HotBoardResult(
        List<MemberResult> topMembers,
        List<QuestionResult> topQuestions,
        List<TeamResult> topTeams
) {
    public record MemberResult(
            long id,
            String nickname,
            String avatarImage,
            long count
    ) {
        public static MemberResult from(MemberHotPo po) {
            return new MemberResult(po.id(), po.nickname(), po.avatarImage(), po.count());
        }
    }

    public record QuestionResult(
            long id,
            String content,
            long count
    ) {
        public static QuestionResult from(QuestionHotPo po) {
            return new QuestionResult(po.id(), po.content(), po.count());
        }
    }

    public record TeamResult(
            long id,
            String name,
            String avatarImage,
            long count
    ) {
        public static TeamResult from(TeamHotPo po) {
            return new TeamResult(po.id(), po.name(), po.avatarImage(), po.count());
        }
    }

    public static HotBoardResult of(
            List<MemberHotPo> memberPos,
            List<QuestionHotPo> questionPos,
            List<TeamHotPo> teamPos
    ) {
        return new HotBoardResult(
                memberPos.stream().map(MemberResult::from).toList(),
                questionPos.stream().map(QuestionResult::from).toList(),
                teamPos.stream().map(TeamResult::from).toList()
        );
    }
}