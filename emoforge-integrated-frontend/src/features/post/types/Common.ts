// src/types/Common.ts
export interface PageResponse<T> {
  dtoList: T[]; // ✅ 실제 목록은 dtoList
  page: number; // 현재 페이지
  size: number;
  totalPages: number;
  startPage: number;
  endPage: number;
  prev: boolean;
  next: boolean;
  totalElements: number;
  currentPage: number;
}
