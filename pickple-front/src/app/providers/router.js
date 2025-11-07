import { routes } from "@/pages"
import { createRouter, createWebHistory } from "vue-router";
import { useMeStore } from '@/features/member/meStore';

export const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes,
})

// 전역 라우터 가드
router.beforeEach(async (to, from, next) => {
    const meStore = useMeStore();

    // 1. 에러 페이지는 항상 통과
    const errorPages = ['/error/404', '/error/500'];
    if (errorPages.includes(to.path)) {
        return next();
    }

    // 2. 로그인 페이지 접근 시
    if (to.path === '/login') {
        // 이미 store에 인증 정보가 있으면 체크
        if (meStore.isAuthenticated) {
            return meStore.isRegistered ? next('/') : next('/register');
        }
        // 인증 정보 없으면 그냥 통과 (로그인 페이지 표시)
        return next();
    }

    // 3. 보호된 페이지 접근 시 - 인증 체크
    if (!meStore.isAuthenticated) {
        try {
            await meStore.fetchMe();
        } catch (error) {
            // 403: 인증 성공했지만 미등록 상태
            if (error.response?.status === 403) {
                // /register가 아니면 리다이렉트
                if (to.path !== '/register') {
                    return next('/register');
                }
                // /register로 가는 중이면 통과
                return next();
            }

            // 401 또는 기타 에러: 로그인 페이지로
            meStore.clearAuth();
            return next('/login');
        }
    }

    // 4. 회원가입 페이지 처리
    if (to.path === '/register') {
        // 미등록 상태면 통과
        if (!meStore.isRegistered) {
            return next();
        }
        // 이미 등록 완료된 사용자는 메인으로
        return next('/');
    }

    // 5. 기타 보호된 페이지 - 등록 완료 여부 체크
    if (!meStore.isRegistered) {
        return next('/register');
    }

    next();

});
