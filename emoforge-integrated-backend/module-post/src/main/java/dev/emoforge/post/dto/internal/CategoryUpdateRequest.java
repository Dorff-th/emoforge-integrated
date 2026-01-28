package dev.emoforge.post.dto.internal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(
    description = """
                관리자용 카테고리 수정 요청 DTO.

                사용 API:
                - PUT /api/posts/admin/categories/{id}

                카테고리 이름 변경 시 사용되며,
                name 필드는 필수 값이다.
                중복된 이름 또는 수정 불가 조건이 발생하면 400 Bad Request가 반환된다.
                """
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequest {

    @Schema(description = "수정할 카테고리 이름", example = "Java Framework")
    @NotBlank(message = "수정할 카테고리 이름은 필수입니다")
    private String name;
}
