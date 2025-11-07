/**
 * 날짜를 YYYY.MM.DD 형식으로 포맷팅합니다.
 * @param dateStr - ISO 8601 형식의 날짜 문자열 또는 Date 객체
 * @returns 포맷팅된 날짜 문자열 (예: 2025.10.28)
 */
export const formatDate = (dateStr) => {
  const date = typeof dateStr === 'string' ? new Date(dateStr) : dateStr;
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}.${month}.${day}`;
};

/**
 * 시간을 HH:MM 형식으로 포맷팅합니다.
 * @param dateStr - ISO 8601 형식의 날짜 문자열 또는 Date 객체
 * @returns 포맷팅된 시간 문자열 (예: 14:30)
 */
export const formatTime = (dateStr) => {
  const date = typeof dateStr === 'string' ? new Date(dateStr) : dateStr;
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  return `${hours}:${minutes}`;
};

/**
 * 날짜를 YYYY.MM.DD HH:MM 형식으로 포맷팅합니다.
 * @param dateStr - ISO 8601 형식의 날짜 문자열 또는 Date 객체
 * @returns 포맷팅된 날짜시간 문자열 (예: 2025.10.28 14:30)
 */
export const formatDateTime = (dateStr) => {
  return `${formatDate(dateStr)} ${formatTime(dateStr)}`;
};

/**
 * ISO 8601 날짜 문자열에서 날짜 부분만 추출합니다.
 * @param dateStr - ISO 8601 형식의 날짜 문자열
 * @returns YYYY-MM-DD 형식의 날짜 문자열
 */
export const extractDateString = (dateStr) => {
  return new Date(dateStr).toISOString().split('T')[0];
};


export const formetDateToMessege = (date) => {
  const now = new Date();
  const diff = now.getTime() - new Date(date).getTime();
  const minutes = Math.floor(diff / (1000 * 60));
  const hours = Math.floor(diff / (1000 * 60 * 60));
  const days = Math.floor(diff / (1000 * 60 * 60 * 24));
  if (minutes < 1) return "방금 전";
  if (minutes < 60) return `${minutes}분 전`;
  if (hours < 24) return `${hours}시간 전`;
  if (days < 7) return `${days}일 전`;
  return new Date(date).toLocaleDateString("ko-KR", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });
}