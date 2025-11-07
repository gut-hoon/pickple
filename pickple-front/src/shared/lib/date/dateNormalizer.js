/**
 * 날짜 객체의 시간 부분을 제거하고 날짜만 비교할 수 있도록 정규화합니다.
 * @param date - Date 객체 또는 날짜 문자열
 * @returns 시간이 00:00:00.000으로 설정된 Date 객체
 */
export const getNormalizedDate = (date) => {
  const normalized = new Date(date);
  normalized.setHours(0, 0, 0, 0);
  return normalized;
};