/**
 * 이미지 리사이징 유틸리티
 * 이미지를 최대 크기 제한에 맞게 압축합니다.
 */

/**
 * 이미지를 리사이징하고 압축합니다.
 * @param {File} file - 원본 이미지 파일
 * @param {number} maxSizeInBytes - 최대 크기 (바이트 단위, 기본값: 1MB)
 * @returns {Promise<string>} Base64로 인코딩된 압축 이미지
 */
export const resizeImage = (file, maxSizeInBytes = 1024 * 1024) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = (e) => {
      const img = new Image();
      img.onload = () => {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');

        let width = img.width;
        let height = img.height;
        let quality = 0.9;

        // 초기 캔버스 크기 설정
        canvas.width = width;
        canvas.height = height;
        ctx.drawImage(img, 0, 0, width, height);

        // 이미지를 Base64로 변환하고 크기 확인
        const compress = (currentQuality) => {
          const dataUrl = canvas.toDataURL(file.type || 'image/jpeg', currentQuality);
          const base64Length = dataUrl.length - `data:${file.type || 'image/jpeg'};base64,`.length;
          const sizeInBytes = (base64Length * 3) / 4;

          console.log(`압축 시도 - quality: ${currentQuality.toFixed(2)}, size: ${(sizeInBytes / 1024).toFixed(2)}KB`);

          if (sizeInBytes <= maxSizeInBytes || currentQuality <= 0.1) {
            console.log(`최종 이미지 크기: ${(sizeInBytes / 1024).toFixed(2)}KB`);
            resolve(dataUrl);
          } else {
            // 크기가 크면 해상도 줄이기
            if (currentQuality <= 0.5 && (width > 800 || height > 800)) {
              const scale = Math.min(800 / width, 800 / height);
              width = Math.floor(width * scale);
              height = Math.floor(height * scale);
              canvas.width = width;
              canvas.height = height;
              ctx.drawImage(img, 0, 0, width, height);
              console.log(`해상도 조정: ${width}x${height}`);
            }
            // 품질을 점진적으로 낮춤
            compress(currentQuality - 0.1);
          }
        };

        compress(quality);
      };
      img.onerror = reject;
      img.src = e.target.result;
    };
    reader.onerror = reject;
    reader.readAsDataURL(file);
  });
};
