import { resizeImage } from './imageResizer';

/**
 * 이미지 파일 업로드 유틸리티
 */

/**
 * 이미지 파일 선택 다이얼로그를 열고 선택된 이미지를 리사이징하여 반환합니다.
 * @param {Object} options - 업로드 옵션
 * @param {string} options.accept - 허용할 파일 타입 (기본값: 'image/*')
 * @param {number} options.maxSize - 최대 파일 크기 (바이트, 기본값: 1MB)
 * @param {function} options.onSuccess - 성공 콜백 (resizedImageBase64) => void
 * @param {function} options.onError - 에러 콜백 (error) => void
 * @param {boolean} options.validateImageType - 이미지 타입 검증 여부 (기본값: true)
 */
export const openImageUploader = (options = {}) => {
  const {
    accept = 'image/*',
    maxSize = 1024 * 1024, // 1MB
    onSuccess,
    onError,
    validateImageType = true
  } = options;

  const input = document.createElement('input');
  input.type = 'file';
  input.accept = accept;

  input.onchange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    try {
      // 이미지 파일 타입 검증
      if (validateImageType && !file.type.startsWith('image/')) {
        const error = new Error('이미지 파일만 업로드할 수 있습니다.');
        error.code = 'INVALID_FILE_TYPE';
        throw error;
      }

      console.log(`원본 파일 크기: ${(file.size / 1024).toFixed(2)}KB`);

      // 이미지 리사이징
      const resizedImage = await resizeImage(file, maxSize);

      if (onSuccess) {
        onSuccess(resizedImage);
      }
    } catch (error) {
      console.error('이미지 업로드 중 오류:', error);
      if (onError) {
        onError(error);
      } else {
        // 기본 에러 핸들링
        alert(error.message || '이미지 처리 중 오류가 발생했습니다.');
      }
    }
  };

  input.click();
};

/**
 * Promise 기반 이미지 업로더
 * @param {Object} options - 업로드 옵션 (openImageUploader와 동일)
 * @returns {Promise<string>} 리사이징된 이미지의 Base64 문자열
 */
export const uploadImage = (options = {}) => {
  return new Promise((resolve, reject) => {
    openImageUploader({
      ...options,
      onSuccess: resolve,
      onError: reject
    });
  });
};
