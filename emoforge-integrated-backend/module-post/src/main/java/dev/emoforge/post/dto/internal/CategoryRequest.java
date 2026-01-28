package dev.emoforge.post.dto.internal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(
    description = """
                관리자용 카테고리 생성 요청 DTO.

                사용 API:
                - POST /api/posts/admin/categories

                카테고리 생성 시 입력되는 name 필드 하나로 구성된 단순 구조이며,
                중복된 이름 등록 시 400 Bad Request가 발생한다.
                """
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @Schema(description = "등록할 카테고리 이름", example = "Spring Boot")
    @NotBlank(message = "카테고리 이름은 필수입니다")
    private String name;
}
