package dev.emoforge.post.controller;


import dev.emoforge.post.domain.Category;
import dev.emoforge.post.service.internal.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 게시글 작성 시 필요한 카테고리 목록을 제공하는 공개 API.
 *
 * 이 컨트롤러는 Post-Frontend에서 직접 호출되며,
 * 게시글 작성/수정 화면의 카테고리 콤보박스 데이터를 위한 엔드포인트이다.
 *
 * ✔ DB에 저장된 전체 카테고리 목록을 조회하되,
 *    "미분류(Uncategorized, is_default=1)" 유형은 선택 목록에서 제외되어 반환된다.
 *
 * ✔ 별도의 BFF 흐름 없이 Post-Service 단독으로 동작한다.
 */
@Tag(
    name = "Category API",
    description = "게시글 작성/수정 화면에서 사용하는 카테고리 조회 API"
)
// 카테고리 API
@RestController
@RequestMapping("/api/posts/categories")
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;

    @Operation(
        summary = "카테고리 목록 조회",
        description = """
                    게시글 작성/수정 화면에서 사용할 카테고리 목록을 조회합니다.

                    ✔ Post-Frontend가 직접 호출하는 API
                    ✔ '미분류(Uncategorized)' 카테고리는 자동으로 제외됩니다.

                    호출 흐름:
                    Post-Frontend → Post-Service(CategoryApiController)

                    반환 형태:
                    - 카테고리 ID
                    - 카테고리명
                    - 정렬 순서 등의 기본 정보
                    """
    )
    @GetMapping("")
    public List<Category> getCategories() {
        return categoryService.findAllCategory();
    }
}
