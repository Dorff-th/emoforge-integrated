package dev.emoforge.post.admin.controller;

import dev.emoforge.post.admin.service.AdminCategoryService;
import dev.emoforge.post.domain.Category;
import dev.emoforge.post.dto.internal.CategoryRequest;
import dev.emoforge.post.dto.internal.CategoryUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자(Admin) 전용 카테고리(Category) 관리 컨트롤러.
 *
 * 이 API는 관리자 화면(admin-frontend)에서만 사용되며,
 * Post-Service가 담당하는 카테고리 데이터를 생성/수정/삭제하기 위한 기능을 제공한다.
 *
 * ✔ 카테고리 추가
 * ✔ 카테고리 이름 수정
 * ✔ 카테고리 삭제
 * ✔ (조회 시) 기본 카테고리(예: '미분류')는 제외하여 반환
 *
 * 권한:
 *  - 관리자 전용 API (ROLE_ADMIN)
 *  - Spring Security의 관리자 전용 필터체인에서 접근 제어됨
 *
 * 사용 흐름:
 * admin-frontend
 *   → /api/posts/admin/categories
 *   → AdminCategoryService
 *
 * 비고:
 *  - 카테고리명 중복 여부는 AdminCategoryService에서 검증
 *  - 중복 또는 삭제 불가 상황 발생 시 400 Bad Request 반환
 */
@Tag(
    name = "Admin Category API",
    description = "관리자 전용 카테고리 생성/수정/삭제 기능 API"
)
@RestController
@RequestMapping("/api/posts/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @Operation(
        summary = "카테고리 목록 조회 (기본 카테고리 제외)",
        description = """
                    관리자 화면에서 사용할 카테고리 목록을 조회합니다.

                    ✔ 기본 카테고리(예: '미분류')는 목록에서 제외
                    ✔ 단순 Category 엔티티 리스트 반환

                    호출 흐름:
                    admin-frontend → AdminCategoryController → AdminCategoryService
                    """
    )
    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(adminCategoryService.getAllCategories());
    }

    @Operation(
        summary = "카테고리 생성",
        description = """
                    새로운 카테고리를 생성합니다.

                    ✔ 관리자 권한 필요
                    ✔ 카테고리 이름 중복 시 400 Bad Request 반환

                    요청 예:
                    {
                      "name": "Spring Boot"
                    }

                    성공 시:
                    - 201 Created
                    - 생성된 카테고리 정보 반환
                    """
    )
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest request) {
        try {
            Category saved = adminCategoryService.createCategory(request.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());    // 카테고리 추가시 카테고리 이름이 중복되면 400 Bad Request 발생
        }
    }

    @Operation(
        summary = "카테고리 이름 수정",
        description = """
                    기존 카테고리의 이름을 수정합니다.

                    ✔ 관리자 권한 필요
                    ✔ 이름 중복 또는 변경 불가 상황 발생 시 400 Bad Request 반환

                    요청 예:
                    {
                      "name": "Java"
                    }
                    """
    )
    //카테고리 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id,
                                            @RequestBody CategoryUpdateRequest request) {
        try {
            Category updated = adminCategoryService.updateCategory(id, request.getName());
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "카테고리 삭제",
        description = """
                    지정된 카테고리를 삭제합니다.

                    ✔ 관리자 권한 필요
                    ✔ 삭제 성공 시 204 No Content 반환

                    삭제 불가 케이스:
                    - 기본 카테고리 삭제 시도
                    - 다른 게시글이 참조 중인 경우(서비스에서 예외 처리)
                    """
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        adminCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
