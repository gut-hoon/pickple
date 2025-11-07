package com.fil.pickple.application;

import com.fil.pickple.application.command.SearchTeamInviteCodeCommand;
import com.fil.pickple.application.command.SearchTeamProfileCommand;
import com.fil.pickple.application.command.UpdateTeamProfileCommand;
import com.fil.pickple.application.result.CreateTeamResult;
import com.fil.pickple.application.result.SearchTeamInviteCodeResult;
import com.fil.pickple.application.result.FindTeamsPickedMeResult;
import com.fil.pickple.application.result.SearchTeamProfileResult;
import com.fil.pickple.application.result.FindTeamsResult;
import com.fil.pickple.application.result.UpdateTeamProfileResult;
import com.fil.pickple.domain.Member;
import com.fil.pickple.domain.Participant;
import com.fil.pickple.domain.Question;
import com.fil.pickple.domain.Team;
import com.fil.pickple.exception.PickpleException;
import com.fil.pickple.exception.code.MemberErrorCode;
import com.fil.pickple.exception.code.TeamErrorCode;
import com.fil.pickple.persistence.MemberRepository;
import com.fil.pickple.persistence.ParticipantRepository;
import com.fil.pickple.persistence.QuestionRepository;
import com.fil.pickple.persistence.TeamRepository;
import com.fil.pickple.application.command.CreateTeamCommand;
import com.fil.pickple.persistence.po.TeamViewPo;
import com.fil.pickple.security.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Service("teamService")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamService {

    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public CreateTeamResult createTeam(CreateTeamCommand command) {
        Long requesterId = SecurityUtil.getCurrentMemberId();

        if (teamRepository.findByName(command.teamName()).isPresent()) {
            throw new PickpleException(TeamErrorCode.ALREADY_EXISTS_TEAMNAME);
        }

        Member creator = memberRepository.findById(requesterId)
                .orElseThrow(() -> new PickpleException(MemberErrorCode.NOT_FOUND));

        String avatarImage = uploadImage(command.avatarImage());
        String backgroundImage = uploadImage(command.backgroundImage());

        Team newTeam = Team.builder()
                .name(command.teamName())
                .avatarImage(avatarImage)
                .backgroundImage(backgroundImage)
                .invitationCode(createUniqueTeamCode())
                .build();
        Team savedTeam = teamRepository.save(newTeam);

        Participant creatorParticipant = Participant.builder()
                .team(savedTeam)
                .member(creator)
                .nickname(creator.getNickname())
                .avatarImage(creator.getAvatarImage())
                .backgroundImage(creator.getBackgroundImage())
                .isCreator(true)
                .build();
        participantRepository.save(creatorParticipant);

        return CreateTeamResult.of(savedTeam, creatorParticipant.getId());
    }

    private String createUniqueTeamCode() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int TEAM_CODE_LENGTH = 8;
        Random random = new SecureRandom();
        StringBuilder teamCodeBuilder;

        do {
            teamCodeBuilder = new StringBuilder(TEAM_CODE_LENGTH);
            for (int i = 0; i < TEAM_CODE_LENGTH; i++) {
                int randomIdx = random.nextInt(CHARACTERS.length());
                teamCodeBuilder.append(CHARACTERS.charAt(randomIdx));
            }
        } while (
                teamRepository.existsByInvitationCode(teamCodeBuilder.toString())
        );

        return teamCodeBuilder.toString();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@teamService.isTeamMember(#command.teamId())")
    public SearchTeamProfileResult searchTeamProfile(SearchTeamProfileCommand command) {
        Team team = teamRepository.findById(command.teamId())
                .orElseThrow(() -> new PickpleException(TeamErrorCode.NOT_FOUND));

        List<Question> seasonTopQuestions = questionRepository.findLatestSeasonTopQuestionsByTeamId(team.getId(), PageRequest.of(0, 3));
        List<Question> currentTopQuestions = questionRepository.findCurrentSeasonTopQuestionsByTeamId(team.getId(), PageRequest.of(0, 3));

        return SearchTeamProfileResult.of(team, seasonTopQuestions, currentTopQuestions);
    }

    @Transactional
    @PreAuthorize("@teamService.isTeamMember(#command.teamId())")
    public UpdateTeamProfileResult updateTeamProfile(UpdateTeamProfileCommand command) {
        Team team = teamRepository.findById(command.teamId())
                .orElseThrow(() -> new PickpleException(TeamErrorCode.NOT_FOUND));

        if (command.teamName() != null) {
            String newName = command.teamName().trim();
            if (!newName.equals(team.getName()) &&
                    teamRepository.findByName(newName).isPresent()) {
                throw new PickpleException(TeamErrorCode.ALREADY_EXISTS_TEAMNAME);
            }
            team.editTeamName(newName);
        }

        if(command.removeAvatarImage()){
            team.editAvatarImage(null);
        }

        if(command.removeBackgroundImage()){
            team.editBackgroundImage(null);
        }

        if(command.teamAvatarImage() != null){
            String avatarImage = uploadImage(command.teamAvatarImage());
            team.editAvatarImage(avatarImage);
        }

        if(command.teamBackgroundImage() != null){
            String backgroundImage = uploadImage(command.teamAvatarImage());
            team.editBackgroundImage(backgroundImage);
        }


        return UpdateTeamProfileResult.from(team);
    }

    @Transactional(readOnly = true)
    public FindTeamsResult findTeams() {
        Long memberId = SecurityUtil.getCurrentMemberId();

        List<Participant> teams = teamRepository.findAllByMemberId(memberId);

        return FindTeamsResult.of(teams);
    }

    @Transactional(readOnly = true)
    public FindTeamsPickedMeResult findTeamsPickedMe() {
        Long memberId = SecurityUtil.getCurrentMemberId();

        List<TeamViewPo> teams = teamRepository.findPickedMeWithMember(memberId);

        return FindTeamsPickedMeResult.of(teams);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@teamService.isTeamMember(#command.teamId())")
    public SearchTeamInviteCodeResult searchTeamInviteCode(SearchTeamInviteCodeCommand command) {
        Team team = teamRepository.findById(command.teamId())
                .orElseThrow(() -> new PickpleException(TeamErrorCode.NOT_FOUND));

        return SearchTeamInviteCodeResult.of(team);
    }

    /**
     * 현재 사용자가 해당 팀의 멤버인지 확인하는 권한 검사 메서드.
     * @PreAuthorize에서 호출하기 위해 public으로 선언되어야 합니다.
     * @param teamId 검사할 팀 ID
     * @return 팀 멤버이면 true
     */
    public boolean isTeamMember(Long teamId) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return participantRepository.existsByTeamIdAndMemberIdAndIsLeftFalse(teamId, currentMemberId);
    }

    private String uploadImage(MultipartFile imageFile) {
        // TODO: S3에 파일 업로드
        return "uploaded image";
    }
}