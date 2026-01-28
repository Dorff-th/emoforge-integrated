// dev.emoforge.auth.controller.PublicProfileController.java
package dev.emoforge.auth.controller;

import dev.emoforge.auth.dto.PublicProfileResponse;
import dev.emoforge.auth.service.MemberPublicProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PublicProfileController
 *
 * 인증 없이 접근 가능한 "공개 회원 프로필" 조회 API를 제공하는 컨트롤러.
 *
 * - 회원의 전체 정보가 아닌, 공개 가능한 최소 정보만 제공한다.
 * - 제공되는 정보:
 *      • 닉네임
 *      • 프로필 이미지 URL (없을 경우 null)
 *
 * 외부 서비스(Post-Service, Diary-Service 등)에서
 * "작성자 정보"나 "공개 프로필"이 필요할 때 사용하는 엔드포인트이다.
 * 개인정보(이메일, 권한 등)는 절대 포함되지 않는다.
 */
@Tag(name = "PublicProfile", description = "공개 회원 프로필 조회 API (인증 불필요)")
@RestController
@RequestMapping("/api/auth/public/members")
@RequiredArgsConstructor
public class PublicProfileController {

    private final MemberPublicProfileService publicProfileService;

    @Operation(
            summary = "공개 회원 프로필 조회",
            description = """
                    회원 UUID를 기반으로 공개 가능한 최소 정보(닉네임, 프로필 이미지 URL)를 반환합니다.
                    회원 전체 정보는 제공하지 않으며, 인증도 필요 없습니다.
                    프로필 이미지가 존재하지 않을 경우 imageUrl은 null로 반환됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없음")
    })
    @GetMapping("/{uuid}/profile")
    public ResponseEntity<PublicProfileResponse> getPublicProfile(@PathVariable("uuid") String uuid) {
        return ResponseEntity.ok(publicProfileService.getPublicProfile(uuid));
    }
}
