package dev.emoforge.auth.dto.admin;

import java.time.LocalDate;

public record MemberSearchCondition(
        String nickname,
        Boolean deleted,
        LocalDate startDate,
        LocalDate endDate
) {}
