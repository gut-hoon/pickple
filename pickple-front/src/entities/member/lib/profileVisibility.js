/**
 * profileVisibility와 hotboardVisible 간의 변환 유틸리티
 */

/**
 * profileVisibility 값을 hotboardVisible boolean으로 변환
 * @param {string} profileVisibility - 'VISIBLE' 또는 'INVISIBLE_HOTBOARD'
 * @returns {boolean} 핫보드 노출 여부
 */
export const toHotboardVisible = (profileVisibility) => {
  return profileVisibility === 'VISIBLE';
};

/**
 * hotboardVisible boolean을 profileVisibility 값으로 변환
 * @param {boolean} hotboardVisible - 핫보드 노출 여부
 * @returns {string} 'VISIBLE' 또는 'INVISIBLE_HOTBOARD'
 */
export const toProfileVisibility = (hotboardVisible) => {
  return hotboardVisible ? 'VISIBLE' : 'INVISIBLE_HOTBOARD';
};
