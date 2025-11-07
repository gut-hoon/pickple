import axios from 'axios';
import { useMeStore } from '@/features/member/meStore';

export const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  withCredentials: true,
});

let router = null;

/**
 * Vue Router를 주입하는 함수
 * main.js에서 호출되어야 함
 */
export const setRouter = (routerInstance) => {
  router = routerInstance;
};

// ==========================================
// 토큰 갱신 큐 시스템
// ==========================================

let isRefreshing = false;
let failedQueue = [];

/**
 * 대기 중인 모든 요청을 처리하는 함수
 * @param {Error|null} error - 에러가 있으면 모든 요청 거부
 * @param {string|null} token - 성공 시 토큰 (현재는 쿠키 사용으로 null)
 */
const processQueue = (error, token = null) => {
  failedQueue.forEach((promise) => {
    if (error) {
      promise.reject(error);
    } else {
      promise.resolve(token);
    }
  });

  // 큐 초기화
  failedQueue = [];
};

// ==========================================
// 유틸리티 함수
// ==========================================

/**
 * 페이지 이동 (router 우선, 없으면 window.location)
 * @param {string} path - 이동할 경로
 */
const navigateTo = (path) => {
  if (router) {
    router.push(path);
  } else {
    window.location.href = path;
  }
};

// ==========================================
// 에러 처리 함수들
// ==========================================

/**
 * 401 에러 처리 (인증 실패)
 * @param {Object} error - axios 에러 객체
 * @param {Object} originalRequest - 원래 요청 객체
 * @returns {Promise} 재시도 Promise 또는 거부된 Promise
 */
const handle401Error = async (error, originalRequest) => {
  const message = error.response?.data?.message || '';

  // 1) refresh API 자체가 실패한 경우
  if (originalRequest.url === '/auth/refresh') {
    isRefreshing = false;
    processQueue(error, null);

    const meStore = useMeStore();
    meStore.clearAuth();

    navigateTo('/login');
    return Promise.reject(error);
  }

  // 2) 로그인 페이지에서는 토큰 갱신 시도하지 않음 (무한 루프 방지)
  const currentPath = router?.currentRoute?.value?.path || window.location.pathname;
  if (currentPath === '/login') {
    const meStore = useMeStore();
    meStore.clearAuth();
    return Promise.reject(error);
  }

  // 3) 토큰 관련 에러인 경우
  if (
    message.includes('만료') ||
    message.includes('토큰') ||
    message.includes('인증')
  ) {
    // 이미 재시도한 요청이면 로그인으로 이동
    if (originalRequest._retry) {
      const meStore = useMeStore();
      meStore.clearAuth();

      navigateTo('/login');
      return Promise.reject(error);
    }

    // 토큰 갱신이 이미 진행 중이면 대기열에 추가
    if (isRefreshing) {
      return new Promise((resolve, reject) => {
        failedQueue.push({ resolve, reject });
      })
        .then(() => {
          // 토큰 갱신 성공 후 원래 요청 재시도
          return axiosInstance.request(originalRequest);
        })
        .catch((err) => {
          return Promise.reject(err);
        });
    }

    // 토큰 갱신 시작
    originalRequest._retry = true;
    isRefreshing = true;

    try {
      // refresh API 호출
      await axiosInstance.post('/auth/refresh');

      // 갱신 성공
      isRefreshing = false;
      processQueue(null, null);

      // 원래 요청 재시도
      return axiosInstance.request(originalRequest);
    } catch (refreshError) {
      // 갱신 실패
      isRefreshing = false;
      processQueue(refreshError, null);

      const meStore = useMeStore();
      meStore.clearAuth();

      navigateTo('/login');
      return Promise.reject(refreshError);
    }
  }

  // 4) 그 외 401 에러 → 로그인 페이지로
  const meStore = useMeStore();
  meStore.clearAuth();

  navigateTo('/login');
  return Promise.reject(error);
};

/**
 * 403 에러 처리 (권한 없음)
 * @param {Object} error - axios 에러 객체
 */
const handle403Error = (error) => {
  // 403 에러는 라우터 가드나 컴포넌트에서 처리하도록 에러만 전파
  // 자동 리다이렉트하지 않음 (중복 처리 방지)
};

/**
 * 404 에러 처리 (리소스를 찾을 수 없음)
 */
const handle404Error = () => {
  navigateTo('/error/404');
};

/**
 * 500 에러 처리 (서버 오류)
 */
const handle500Error = () => {
  navigateTo('/error/500');
};

// ==========================================
// 응답 인터셉터
// ==========================================

axiosInstance.interceptors.response.use(
  // 성공 응답은 그대로 반환
  (response) => response,

  // 에러 응답 처리
  async (error) => {
    const originalRequest = error.config;
    const status = error.response?.status;

    // 401: 인증 실패
    if (status === 401) {
      return handle401Error(error, originalRequest);
    }

    // 403: 권한 없음
    if (status === 403) {
      handle403Error(error);
    }

    // 404: 리소스를 찾을 수 없음
    if (status === 404) {
      handle404Error();
    }

    // 500: 서버 오류
    if (status >= 500) {
      handle500Error();
    }

    return Promise.reject(error);
  }
);
