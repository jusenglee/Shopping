export function formatNumberWithComma(value) {
  // 1) 숫자가 아닌 문자 제거
  let numericValue = String(value).replace(/[^0-9]/g, '');
  // 2) 정수 변환
  numericValue = parseInt(numericValue, 10) || 0;
  // 3) 3자리마다 콤마 삽입
  return String(numericValue).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}
