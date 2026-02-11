// src/types/Attachment.ts
export interface Attachment {
  id: number;
  fileName: string;
  originFileName: string;
  fileUrl: string; // S3나 로컬 저장소 URL
  fileSizeText: string;
  createdAt: string;
  publicUrl: string | null; // ✅ 서버 응답 기준
  fileSize: number;         // ✅ 숫자 그대로
}

export interface AttachmentItem {
  id: number;
  fileName?: string;
  originFileName?: string;
  publicUrl?: string;
  uploadType: "EDITOR_IMAGE" | "ATTACHMENT";
  status: "TEMP" | "CONFIRMED";
  fileSize?: number;
  isNew?: boolean;            // 신규 첨부 여부
  markedForDelete?: boolean;  // 삭제 예정 표시
}

// src/types/Attachment.ts

// 업로드 API 응답 DTO
export interface AttachmentUploadResponseDto {
  id: number;
  tempKey?: string;
  groupTempKey?: string;
  fileName?: string;
  originalName?: string;
  originFileName?: string;
  fileUrl?: string;
  publicUrl?: string;
  uploadType?: "EDITOR_IMAGE" | "ATTACHMENT";
  status?: "TEMP" | "CONFIRMED";
  fileSize?: number;
}

// 프론트에서 관리용으로 쓰는 확장 타입
export interface AttachmentItem extends AttachmentUploadResponseDto {
  isNew?: boolean;            // 신규 첨부 여부
  markedForDelete?: boolean;  // 삭제 예정 여부
}
