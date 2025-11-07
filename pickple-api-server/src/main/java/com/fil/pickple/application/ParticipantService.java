package com.fil.pickple.application;

import com.fil.pickple.application.command.LeaveTeamCommand;
import com.fil.pickple.application.command.JoinTeamCommand;
import com.fil.pickple.application.command.SearchParticipantDetailCommand;
import com.fil.pickple.application.command.SearchParticipantProfileCommand;
import com.fil.pickple.application.command.EditParticipantCommand;
import com.fil.pickple.application.result.SearchParticipantDetailResult;
import com.fil.pickple.application.result.SearchParticipantProfileResult;
import com.fil.pickple.application.result.EditParticipantResult;
import com.fil.pickple.application.result.SearchTeamParticipantsResult;
import com.fil.pickple.domain.BaseProfileImage;
import com.fil.pickple.domain.Member;
import com.fil.pickple.domain.Participant;
import com.fil.pickple.domain.Question;
import com.fil.pickple.domain.Team;
import com.fil.pickple.domain.TeamSnapshot;
import com.fil.pickple.exception.PickpleException;
import com.fil.pickple.exception.code.MemberErrorCode;
import com.fil.pickple.exception.code.TeamErrorCode;
import com.fil.pickple.persistence.BaseProfileImageRepository;
import com.fil.pickple.persistence.MemberRepository;
import com.fil.pickple.persistence.ParticipantRepository;
import com.fil.pickple.persistence.QuestionRepository;
import com.fil.pickple.persistence.TeamRepository;
import com.fil.pickple.persistence.TeamSnapshotRepository;
import com.fil.pickple.security.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final BaseProfileImageRepository baseProfileImageRepository;
    private final QuestionRepository questionRepository;
    private final TeamSnapshotRepository teamSnapshotRepository;

    @Transactional
    public void joinTeam(JoinTeamCommand command){
        Long requesterId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.getReferenceById(requesterId);

        Team team = teamRepository.findByInvitationCode(command.invitationCode())
                .orElseThrow(() -> new PickpleException(TeamErrorCode.NOT_FOUND_INVITATION_CODE));

        if (participantRepository.existsByTeamIdAndMemberIdAndIsLeftFalse(team.getId(), requesterId)){
            throw new PickpleException(TeamErrorCode.ALREADY_JOINED_TEAM);
        }

        if (participantRepository.existsByTeamIdAndNicknameAndIsLeftFalse(team.getId(), command.nickname())){
            throw new PickpleException(TeamErrorCode.ALREADY_EXISTS_NICKNAME);
        }

        String avatarImage = uploadImage(command.avatarImage());
        String backgroundImage = uploadImage(command.backgroundImage());

        Optional<Participant> maybeLeft = participantRepository
                .findByTeamIdAndMemberIdAndIsLeftTrue(team.getId(), requesterId);

        if (maybeLeft.isPresent()) {
            maybeLeft.get().reJoin(
                    command.nickname(),
                    avatarImage,
                    backgroundImage
            );
        } else {
            Participant participant = Participant.builder()
                    .team(team)
                    .member(member)
                    .nickname(command.nickname())
                    .avatarImage(avatarImage)
                    .backgroundImage(backgroundImage)
                    .isCreator(false)
                    .build();
            participantRepository.save(participant);
        }
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@teamService.isTeamMember(#teamId)")
    public SearchTeamParticipantsResult searchTeamParticipants(Long teamId) {

        List<Participant> participants = participantRepository.findAllByTeam_Id(teamId);

        return SearchTeamParticipantsResult.of(participants);
    }

    @Transactional(readOnly = true)
    public SearchParticipantProfileResult searchParticipantProfile(SearchParticipantProfileCommand command) {
        Participant participant = participantRepository.findByIdAndNotLeftWithMember(command.participantId())
                .orElseThrow(() -> new PickpleException(TeamErrorCode.NOT_FOUND_PARTICIPANT));

        Long requesterId = SecurityUtil.getCurrentMemberId();
        Member requester = memberRepository.getReferenceById(requesterId);
        boolean isRequesterTeamMember = participantRepository.existsByMemberAndTeamAndIsLeftIsFalse(requester, participant.getTeam());
        if (!isRequesterTeamMember) {
            throw new PickpleException(TeamErrorCode.FORBIDDEN, "팀에 속하지 않았습니다");
        }

        BaseProfileImage baseProfileImage = baseProfileImageRepository.findAll().getFirst();

        List<Question> seasonTopQuestions = questionRepository.findTopQuestionInLastSeasonByParticipant(participant.getId());
        List<Question> totalTopQuestions = questionRepository.findTopQuestionByParticipant(participant.getId());

        return SearchParticipantProfileResult.of(participant, seasonTopQuestions, totalTopQuestions, baseProfileImage);
    }

    @Transactional(readOnly = true)
    public SearchParticipantDetailResult searchParticipantDetail(SearchParticipantDetailCommand command) {
        Long requesterId = SecurityUtil.getCurrentMemberId();

        Participant participant = participantRepository.findByIdAndNotLeftWithMember(command.participantId())
                .orElseThrow(() -> new PickpleException(TeamErrorCode.NOT_FOUND_PARTICIPANT));

        if (!participant.getMember().getId().equals(requesterId)) {
            throw new PickpleException(TeamErrorCode.FORBIDDEN, "본인의 상세 정보만 조회할 수 있습니다");
        }

        BaseProfileImage baseProfileImage = baseProfileImageRepository.findAll().getFirst();

        return SearchParticipantDetailResult.of(participant, baseProfileImage.getPersonalAvatarImage(), baseProfileImage.getPersonalBackgroundImage());
    }

    @Transactional
    public EditParticipantResult editParticipant(EditParticipantCommand command) {
        Long requesterId = SecurityUtil.getCurrentMemberId();

        Participant participant = participantRepository.findByIdAndNotLeftWithMember(command.participantId())
                .orElseThrow(() -> new PickpleException(TeamErrorCode.NOT_FOUND_PARTICIPANT));

        if (participant.getMember().getId().equals(requesterId)) {
            throw new PickpleException(TeamErrorCode.FORBIDDEN, "본인의 상세 정보만 수정할 수 있습니다");
        }

        if (command.nickname() != null) {
            checkDuplicationEditedNickname(participant, command.nickname());
            participant.editNickname(command.nickname());
        }

        if(command.removeAvatarImage()){
            participant.editAvatarImage(null);
        }

        if(command.removeBackgroundImage()){
            participant.editBackgroundImage(null);
        }

        if(command.participantAvatarImage() != null){
            String avatarImage = uploadImage(command.participantAvatarImage());
            participant.editAvatarImage(avatarImage);
        }

        if(command.participantBackgroundImage() != null){
            String backgroundImage = uploadImage(command.participantBackgroundImage());
            participant.editBackgroundImage(backgroundImage);
        }

        return EditParticipantResult.from(participant);
    }

    @Transactional
    public void leaveTeam(LeaveTeamCommand command) {
        Long requesterId = SecurityUtil.getCurrentMemberId();

        Member requester = memberRepository.findByIdAndIsDeletedIsFalse(requesterId)
                .orElseThrow(() -> new PickpleException(MemberErrorCode.NOT_FOUND));

        Participant participant = participantRepository.findByIdAndNotLeftWithMember(command.participantId())
                .orElseThrow(() -> new PickpleException(TeamErrorCode.NOT_FOUND_PARTICIPANT));

        if(!participant.getMember().getId().equals(requester.getId())){
            throw new PickpleException(TeamErrorCode.FORBIDDEN_PARTICIPANT_MANIPULATION);
        }

        TeamSnapshot teamSnapshot = TeamSnapshot.builder()
                .name(participant.getTeam().getName())
                .avatarImage(participant.getTeam().getAvatarImage())
                .backgroundImage(participant.getTeam().getBackgroundImage())
                .build();
        participant.leave(teamSnapshot);

        teamSnapshotRepository.save(teamSnapshot);
    }

    private void checkDuplicationEditedNickname(Participant participant, String nickname) {
        if (nickname == null) {
            return;
        }

        boolean isSameNickname = nickname.equals(participant.getNickname());
        boolean isExistNicknameInTeam = participantRepository.existsByTeamIdAndNicknameAndIsLeftFalse(participant.getId(), nickname);

        if (isExistNicknameInTeam && !isSameNickname) {
            throw new PickpleException(TeamErrorCode.ALREADY_EXISTS_NICKNAME);
        }
    }

    private String uploadImage(MultipartFile imageFile) {
        // TODO: S3에 파일 업로드
        return "uploaded image";
    }
}
