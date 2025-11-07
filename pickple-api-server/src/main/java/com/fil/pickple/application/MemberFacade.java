package com.fil.pickple.application;

import com.fil.pickple.security.provider.JwtTokenProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberFacade {
    private final MemberService memberService;
    private final TokenBlackListService tokenBlackListService;

    @Transactional
    public void withdrawMember(String accessToken, String refreshToken) {
        memberService.withdraw();
        tokenBlackListService.addAccessTokenToBlacklist(accessToken);
        tokenBlackListService.addRefreshTokenToBlacklist(refreshToken);
    }
}
