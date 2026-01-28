package dev.emoforge.post.dto.bff;

import dev.emoforge.post.dto.external.AttachmentResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시글 상세 조회 응답 DTO (BFF).
 *
 * PostController.getPost()에서 반환되는 최종 상세 데이터 구조이며,
 * 단순히 Post 엔티티 정보만 제공하는 것이 아니라,
 * 여러 서비스(BFF)에서 데이터를 취합하여 생성되는 종합 응답 모델이다.
 *
 * 🔎 사용되는 Controller API:
 * 1) PostController.getPost()
 *    → GET /api/posts/{id}
 *
 * 데이터 구성 (BFF 구조):
 * 1) PostService(PostDetailDTO)
 *    - id, title, content, memberUuid, createdAt, updatedAt
 *    - categoryId, categoryName
 *
 * 2) Auth-Service (Feign)
 *    - nickname, profileImageUrl
 *
 * 3) Attach-Service (Feign)
 *    - editorImages (에디터 본문 이미지)
 *    - attachments (일반 첨부파일)
 *
 * 주요 용도:
 * - 게시글 상세 화면(PostDetailPage)에서 필요한 **모든 데이터**를 단일 DTO로 제공
 * - 프론트는 별도의 API 호출 없이 한 번에 전체 상세 정보를 받을 수 있음
 */
@Schema(
    description = """
                게시글 상세 조회 응답 DTO (BFF).

                사용 API:
                - GET /api/posts/{id}

                구성 요소:
                - 게시글 기본정보
                - 작성자 정보(Auth-Service 연동)
                - 첨부 이미지 / 첨부파일 목록(Attach-Service 연동)

                PostDetailFacadeService에서 여러 서비스 데이터를 조합하여 생성되는 BFF 전용 구조이다.
                """
)
@Builder
public record PostDetailResponse(

        @Schema(description = "게시글 ID", example = "42")
        Long id,

        @Schema(description = "게시글 제목", example = "Spring Boot로 MSA 구축하기")
        String title,

        @Schema(description = "게시글 내용 (HTML 기준)",
            example = "<p>MSA 구성은 다음과 같습니다...</p>")
        String content,

        @Schema(description = "게시글 작성자 UUID",
            example = "5afc34da-bc92-41ca-9d9e-79d23e8dcf12")
        String memberUuid,

        @Schema(description = "게시글 작성 시각", example = "2025-11-18T08:55:32")
        LocalDateTime createdAt,

        @Schema(description = "게시글 수정 시각", example = "2025-11-18T12:22:11")
        LocalDateTime updatedAt,

        @Schema(description = "카테고리 ID", example = "3")
        Long categoryId,

        @Schema(description = "카테고리 이름", example = "Spring Boot")
        String categoryName,

        // ---------- Auth-Service BFF 합성 영역 ----------
        @Schema(description = "작성자 닉네임(Auth-Service)", example = "행복한호랑이")
        String nickname,

        @Schema(description = "작성자 프로필 이미지 URL(Auth-Service)",
            example = "https://cdn.emoforge.dev/profile/abcd1234.png")
        String profileImageUrl,

        // ---------- Attach-Service BFF 합성 영역 ----------
        @Schema(
            description = """
                        에디터 본문에서 업로드된 이미지 리스트.
                        이미지 URL, 파일명, 파일 크기 등이 포함된다.
                        """,
            example = "[{ \"publicUrl\": \"https://cdn/emoforge/editor/1.png\" }]"
        )
        List<AttachmentResponse> editorImages,

        @Schema(
            description = """
                        일반 첨부파일 목록.
                        에디터 이미지가 아닌 파일 첨부(예: zip, pdf 등)가 있을 경우 제공된다.
                        """,
            example = "[{ \"publicUrl\": \"https://www/emoforge/attach/file1.zip\" }]"
        )
        List<AttachmentResponse> attachments

) {
}
