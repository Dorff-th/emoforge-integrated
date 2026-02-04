import { useEffect, useState } from "react";

/**
 * value가 변경된 뒤 delay(ms) 동안 추가 변경이 없으면 debouncedValue를 업데이트한다.
 * - 검색어 입력, 필터 변경 등 API 호출을 늦추는 데 사용
 */
export function useDebounce<T>(value: T, delay: number = 300): T {
  const [debouncedValue, setDebouncedValue] = useState<T>(value);

  useEffect(() => {
    const timerId = window.setTimeout(() => {
      setDebouncedValue(value);
    }, delay);

    return () => {
      window.clearTimeout(timerId);
    };
  }, [value, delay]);

  return debouncedValue;
}
