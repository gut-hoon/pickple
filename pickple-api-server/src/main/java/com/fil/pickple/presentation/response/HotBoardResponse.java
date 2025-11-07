package com.fil.pickple.presentation.response;

import com.fil.pickple.application.result.HotBoardResult;

import java.util.List;

public record HotBoardResponse(
        boolean success,
        List<Member> topMembers,
        List<Question> topQuestions,
        List<Team> topTeams
) {

    public record Member(
            long id,
            String nickname,
            String avatarImage,
            long count
    ) {
        static Member from(HotBoardResult.MemberResult r) {
            return new Member(r.id(), r.nickname(), r.avatarImage(), r.count());
        }
    }

    public record Question(
            long id,
            String content,
            long count
    ) {
        static Question from(HotBoardResult.QuestionResult r) {
            return new Question(r.id(), r.content(), r.count());
        }
    }

    public record Team(
            long id,
            String name,
            String avatarImage,
            long count
    ) {
        static Team from(HotBoardResult.TeamResult r) {
            return new Team(r.id(), r.name(), r.avatarImage(), r.count());
        }
    }

    public static HotBoardResponse from(HotBoardResult result) {
        return new HotBoardResponse(
                true,
                result.topMembers().stream().map(Member::from).toList(),
                result.topQuestions().stream().map(Question::from).toList(),
                result.topTeams().stream().map(Team::from).toList()
        );
    }
}