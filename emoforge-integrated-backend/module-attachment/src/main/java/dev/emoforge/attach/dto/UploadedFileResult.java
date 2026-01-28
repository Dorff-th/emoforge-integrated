package dev.emoforge.attach.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 파일 업로드 처리 과정에서 사용되는 내부 전용 DTO.
 *
 * - Service 및 파일 처리 유틸(FileUploadUtil 등)에서만 사용되며,
 *   컨트롤러의 요청/응답 바디에는 노출되지 않는다.
 * - 업로드된 파일의 기본 정보를 담아 Attachment 엔티티 생성 또는
 *   후속 처리 단계에서 활용된다.
 *
 * Swagger 문서화 대상이 아닌 내부 처리용 객체이다.
 */
@Data
@Builder
public class UploadedFileResult {
    private String fileName;
    private String originFileName;
    private String fileType;
    private long fileSize;
    private String publicUrl;
}
