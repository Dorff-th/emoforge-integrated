package dev.emoforge.auth.dto;

import dev.emoforge.auth.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Schema(
        description = """
                현재 로그인한 사용자의 정보를 제공하는 응답 DTO.

                사용되는 API:
                - AuthController.getCurrentUser()
                  → GET /api/auth/me

                로그인 후 사용자 기본 정보(UI 표시용)를 제공하며,
                권한(role)에 따라 화면 분기(USER/ADMIN)에도 활용된다.
                """
)
@Getter
@ToString
public class MemberDTO {

    @Schema(description = "사용자 UUID", example = "a12f81ab-9cd1-4cbb-983a-2b52a918e9b4")
    private final String uuid;

    @Schema(description = "로그인 아이디(username)", example = "tiger123")
    private final String username;

    @Schema(description = "이메일 주소", example = "tiger@example.com")
    private final String email;

    @Schema(description = "닉네임", example = "행복한호랑이")
    private final String nickname;

    @Schema(description = "사용자 역할(권한)", example = "USER")
    private final String role;

    @Schema(description = "계정 상태", example = "ACTIVE")
    private final String status;

    @Schema(description = "사용자 정보 생성일", example = "2025-09-16 19:19:33")
    private String createdAt;

    @Schema(description = "사용자 정보 변경일", example = "2025-10-29 13:19:15")
    private String updatedAt;

    @Schema(description = "사용자 탈퇴 신청 여부", example = "true")
    private boolean deleted;

    @Schema(description = "사용자 탈퇴 신청일", example = "2025-10-29 13:19:15")
    private String deletedAt;


    public MemberDTO(Member member) {
        this.uuid = member.getUuid();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.role = member.getRole().name();
        this.status = member.getStatus().name();
        this.createdAt = formatSafe(member.getCreatedAt());
        this.updatedAt = formatSafe(member.getUpdatedAt());
        this.deleted = member.isDeleted();
        this.deletedAt = formatSafe(member.getDeletedAt());
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String formatSafe(LocalDateTime time) {
        return (time == null) ? "" : time.format(FORMATTER);
    }
}
