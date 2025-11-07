package com.fil.pickple.security.util;

import com.fil.pickple.exception.PickpleException;
import com.fil.pickple.exception.code.AuthErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring Security Context에서 현재 인증된 사용자 정보를 추출하는 유틸리티 클래스
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

    /**
     * 현재 인증된 사용자의 Member ID를 반환
     * @return 현재 사용자의 Member ID
     * @throws PickpleException 인증되지 않은 경우
     */
    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new PickpleException(AuthErrorCode.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Long) {
            return (Long) principal;
        }

        throw new PickpleException(AuthErrorCode.UNAUTHORIZED);
    }

    /**
     * 현재 인증된 사용자가 있는지 확인
     * @return 인증된 사용자가 있으면 true, 없으면 false
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof Long;
    }

    /**
     * 현재 인증된 사용자의 Member ID를 반환 (인증되지 않은 경우 null 반환)
     * @return 현재 사용자의 Member ID 또는 null
     */
    public static Long getCurrentMemberIdOrNull() {
        try {
            return getCurrentMemberId();
        } catch (PickpleException e) {
            return null;
        }
    }
}