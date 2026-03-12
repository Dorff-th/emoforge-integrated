package dev.emoforge.auth.dto.admin;

import dev.emoforge.auth.entity.Member;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record AdminMemberListDTO(
        String uuid,
        String username,
        String nickname,
        String role,
        String status,
        boolean deleted,
        LocalDateTime deletedAt,
        LocalDateTime createdAt,
        LocalDateTime lastLoginAt
) {
    public static AdminMemberListDTO fromEntity(Member m) {
        return new AdminMemberListDTO(
            m.getUuid(),
            m.getUsername(),
            m.getNickname(),
            m.getRole().name(),
            m.getStatus().name(),
            m.isDeleted(),
            m.getDeletedAt(),
            m.getCreatedAt(),
            m.getLastLoginAt()
        );
    }
}

