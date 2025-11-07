package com.fil.pickple.security.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;
import java.util.Optional;

/**
 * HTTP Cookie 관리를 위한 유틸리티 클래스
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtil {
    /**
     * HTTPOnly Secure Cookie 생성
     * 
     * @param name (쿠키 이름)
     * @param value (쿠키 값)
     * @param maxAge (만료 시간)
     * @return Cookie
     */
    public static Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // TODO 배포환경에서 true로 변경
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setAttribute("SameSite", "Lax");

        return cookie;
    }

    /**
     * HTTP 요청에서 특정 이름의 Cookie 값 추출
     *
     * @param request (HTTP 요청)
     * @param cookieName (찾을 쿠키 이름)
     * @return Cookie의 값 (Optional)
     */
    public static Optional<String> getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    /**
     * HTTP 요청에서 특정 이름의 Cookie 객체 추출
     *
     * @param request (HTTP 요청)
     * @param cookieName (찾을 쿠키 이름)
     * @return Cookie 객체 (Optional)
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findFirst();
    }

    /**
     * 여러 Cookie를 응답에 추가
     *
     * @param response (HTTP 응답)
     * @param cookies (추가할 Cookie 배열)
     */
    public static void addCookies(HttpServletResponse response, Cookie... cookies) {
        for (Cookie cookie: cookies) {
            response.addCookie(cookie);
        }
    }

    /**
     * Cookie 값이 존재하는지 확인
     * 
     * @param request (HTTP 요청)
     * @param cookieName (확인할 쿠키 이름)
     * @return 존재하면 true, 없으면 false
     */
    public static boolean hasCookie(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName).isPresent();
    }
}
