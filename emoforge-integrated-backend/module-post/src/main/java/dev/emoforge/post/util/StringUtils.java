package dev.emoforge.post.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {
    /**
     * 문자열을 쉼표(,) 기준으로 Long 리스트로 변환
     * @param input 변환할 문자열 (예: "12,14,16")
     * @return Long 리스트 (null·빈문자면 빈 리스트 반환)
     */
    public static List<Long> toLongList(String input) {
        if (input == null || input.isBlank()) {
            return Collections.emptyList();
        }

        return Arrays.stream(input.split(","))
            .map(String::trim)                 // 앞뒤 공백 제거
            .filter(s -> !s.isEmpty())         // 빈 문자열 제외
            .map(Long::parseLong)              // Long 변환
            .collect(Collectors.toList());     // Java 8+ 호환
    }
}
