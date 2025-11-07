package com.fil.pickple.application;

import com.fil.pickple.application.result.CreateMemberResult;
import com.fil.pickple.domain.Member;
import com.fil.pickple.exception.PickpleException;
import com.fil.pickple.exception.code.AuthErrorCode;
import com.fil.pickple.persistence.MemberRepository;
import com.fil.pickple.security.provider.JwtTokenProvider;
import com.fil.pickple.application.result.OAuthMemberResult;
import com.fil.pickple.application.result.RefreshTokenResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlackListService tokenBlackListService;

    /**
     * OAuth 로그인 처리 메서드
     *
     * @param email           이메일 (구글 이메일)
     * @param name            이름 (구글 닉네임)
     * @param profileImageUrl (구글 프로필 이미지 링크, 없으면 null)
     * @return OAuthMemberResult
     */
    @Transactional
    public OAuthMemberResult processOAuthLogin(String email, String name, String profileImageUrl) {
        Optional<Member> existingMember = memberRepository.findByEmail(email);

        Long memberId;
        boolean isNewMember;
        boolean isRegisteredMember;

        if (existingMember.isPresent()) {
            Member member = existingMember.get();
            memberId = member.getId();
            isNewMember = false;
            isRegisteredMember = !(member.getFullName() == null || member.getBirthDate() == null);
        } else {
            CreateMemberResult createResult = memberService.createNewMember(email, name, profileImageUrl);
            memberId = createResult.memberId();
            isNewMember = true;
            isRegisteredMember = false;
        }

        return OAuthMemberResult.of(memberId, email, isNewMember, isRegisteredMember);
    }

    /**
     * Refresh Token을 사용해서 새로운 Access Token 발급
     *
     * @param refreshToken (쿠키에서 가져온 Refresh Token)
     * @return RefreshTokenResult (memberId, 새로운 accessToken)
     */
    @Transactional(readOnly = true)
    public RefreshTokenResult refreshAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new PickpleException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new PickpleException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (tokenBlackListService.isBlackListed(refreshToken)) {
            throw new PickpleException(AuthErrorCode.BLACKLISTED_TOKEN);
        }

        Long memberId = jwtTokenProvider.getMemberIdFromToken(refreshToken);

        Member member = memberRepository.findByIdAndIsDeletedIsFalse(memberId)
                .orElseThrow(() -> new PickpleException(AuthErrorCode.UNAUTHORIZED));

        long remainingTime = jwtTokenProvider.getRemainingTimeMillis(refreshToken);
        tokenBlackListService.addToBlackList(refreshToken, remainingTime);
        boolean isRegistered = (member.getFullName() != null && member.getBirthDate() != null);

        String newAccessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getEmail(), isRegistered);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(member.getId());

        return RefreshTokenResult.of(member.getId(), newAccessToken, newRefreshToken);
    }

    public void logout(String accessToken, String refreshToken) {
        tokenBlackListService.addAccessTokenToBlacklist(accessToken);
        tokenBlackListService.addRefreshTokenToBlacklist(refreshToken);
    }
}