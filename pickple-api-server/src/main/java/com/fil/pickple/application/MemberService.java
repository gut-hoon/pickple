package com.fil.pickple.application;

import com.fil.pickple.application.command.RegisterMemberCommand;
import com.fil.pickple.application.result.*;
import com.fil.pickple.domain.BaseProfileImage;
import com.fil.pickple.domain.ProfileVisibility;
import com.fil.pickple.domain.Question;
import com.fil.pickple.persistence.BaseProfileImageRepository;
import com.fil.pickple.persistence.MemberRepository;
import com.fil.pickple.persistence.QuestionRepository;
import com.fil.pickple.domain.Member;
import com.fil.pickple.exception.PickpleException;
import com.fil.pickple.exception.code.MemberErrorCode;
import com.fil.pickple.application.command.EditMemberCommand;
import com.fil.pickple.application.command.SearchBaseProfileCommand;
import com.fil.pickple.security.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final BaseProfileImageRepository baseProfileImageRepository;

    @Transactional(readOnly = true)
    public SearchBaseProfileResult searchBaseProfile(SearchBaseProfileCommand command) {
        Member member = memberRepository.findByIdAndIsDeletedIsFalse(command.memberId())
                .orElseThrow(() -> new PickpleException(MemberErrorCode.NOT_FOUND));

        List<Question> seasonTopQuestions = questionRepository.findTopQuestionInLastSeason(member.getId());
        List<Question> totalTopQuestions = questionRepository.findTopQuestion(member.getId());

        BaseProfileImage baseProfileImage = baseProfileImageRepository.findAll().getFirst();

        return SearchBaseProfileResult.of(member, seasonTopQuestions, totalTopQuestions, baseProfileImage);
    }

    @Transactional(readOnly = true)
    public SearchMeResult searchMe() {
        Long requesterId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.findByIdAndIsDeletedIsFalse(requesterId)
                .orElseThrow(() -> new PickpleException(MemberErrorCode.NOT_FOUND));

        BaseProfileImage baseProfileImage = baseProfileImageRepository.findAll().getFirst();

        return SearchMeResult.of(member, baseProfileImage.getPersonalAvatarImage(), baseProfileImage.getPersonalBackgroundImage());
    }

    @Transactional
    public void editMember(EditMemberCommand command) {
        Long requesterId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.findByIdAndIsDeletedIsFalse(requesterId)
                .orElseThrow(() -> new PickpleException(MemberErrorCode.NOT_FOUND));

        if (command.nickname() != null) {
            boolean exist = memberRepository.existsByNickname(command.nickname());
            if (exist) {
                throw new PickpleException(MemberErrorCode.ALREADY_EXISTS_NICKNAME);
            }
            member.editNickname(command.nickname());
        }

        if (command.removeAvatarImage() != null) {
            member.editAvatarImage(null);
        }

        if (command.removeBackgroundImage() != null) {
            member.editBackgroundImage(null);
        }

        if (command.avatarImage() != null) {
            String avatarImageUrl = uploadImage(command.avatarImage());
            member.editAvatarImage(avatarImageUrl);
        }

        if (command.backgroundImage() != null) {
            String backgroundImageUrl = uploadImage(command.backgroundImage());
            member.editBackgroundImage(backgroundImageUrl);
        }

        if (command.birthDate() != null) {
            member.editBirthDate(command.birthDate());
        }

        if (command.profileVisibility() != null) {
            member.editVisibility(command.profileVisibility());
            // TODO: 핫보드 재 집계
        }
    }

    @Transactional
    public void withdraw() {
        Long requesterId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(requesterId)
                .orElseThrow(() -> new PickpleException(MemberErrorCode.NOT_FOUND));

        member.delete();
    }

    private String uploadImage(MultipartFile imageFile) {
        // TODO: S3에 파일 업로드
        return "uploaded image";
    }

    @Transactional
    public CreateMemberResult createNewMember(String email, String name, String avatarImage) {
        Member newMember = Member.builder()
                .email(email)
                .nickname(name)
                .avatarImage(avatarImage)
                .backgroundImage(null)
                .visibility(ProfileVisibility.VISIBLE)
                .build();

        memberRepository.save(newMember);

        return CreateMemberResult.from(newMember);
    }

    @Transactional
    public RegisterMemberResult registerMember(RegisterMemberCommand command) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByIdAndIsDeletedIsFalse(memberId)
                .orElseThrow(() -> new PickpleException(MemberErrorCode.NOT_FOUND));

        if (member.getFullName() != null && member.getBirthDate() != null) {
            throw new PickpleException(MemberErrorCode.ALREADY_REGISTERED);
        }

        member.register(command.fullName(), command.birthDate());

        return RegisterMemberResult.from(member);
    }
}
