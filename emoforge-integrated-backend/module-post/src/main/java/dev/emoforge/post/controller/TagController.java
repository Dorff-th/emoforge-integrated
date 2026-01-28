package dev.emoforge.post.controller;


import dev.emoforge.post.service.internal.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 태그 자동완성 기능을 제공하는 컨트롤러.
 *
 * 이 API는 게시글 작성/수정 화면에서 사용자가 태그를 입력할 때,
 * 프론트엔드가 KeyDown 이벤트마다 호출하여 자동완성 후보를 가져오는 용도이다.
 *
 * ✔ Post-Frontend가 직접 호출하는 API
 * ✔ 사용자가 입력한 문자열을 기반으로 태그 이름 부분 일치 검색 수행
 * ✔ 실시간 검색 특성상 가볍고 빠른 응답이 특징
 *
 * 사용 흐름 예시:
 * Post-Frontend (태그 입력창)
 *    → onKeyDown 이벤트 발생
 *    → /api/posts/tags/suggest?query=xxx 호출
 *    → 태그 명 문자열 리스트 반환
 *
 * 반환 예:
 * ["spring", "springboot", "spring-security"]
 */
@Tag(
    name = "Tag API",
    description = "게시글 작성·수정 시 사용되는 태그 자동완성 기능 API"
)
@RestController
@RequestMapping("/api/posts/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * 자동완성 태그 목록 조회
     * @param query 사용자가 입력한 문자열
     * @return 태그 이름 목록(JSON 배열)
     */
    @Operation(
        summary = "태그 자동완성 목록 조회",
        description = """
                    사용자가 태그 입력창에 문자를 입력할 때,
                    해당 문자열과 부분 일치하는 태그 후보 목록을 반환합니다.

                    ✔ 프론트엔드 입력창(KeyDown 이벤트)에서 실시간 호출
                    ✔ 단순 문자열 리스트(JSON 배열)로 응답

                    호출 흐름:
                    Post-Frontend → TagController → TagService

                    반환 예:
                    [
                      "java",
                      "javascript",
                      "jpa"
                    ]
                    """
    )
    @GetMapping("/suggest")
    public List<String> suggestTags(@RequestParam("query") String query) {
        return tagService.getTagSuggestions(query);
    }
}
