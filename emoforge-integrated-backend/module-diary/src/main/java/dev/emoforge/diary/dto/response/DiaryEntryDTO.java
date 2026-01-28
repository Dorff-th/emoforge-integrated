package dev.emoforge.diary.dto.response;


import dev.emoforge.diary.domain.DiaryEntry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DiaryEntryDTO
 *
 * 개별 회고(다이어리) 엔트리를 나타내는 응답 DTO.
 * 날짜별 그룹 응답(DiaryGroupResponseDTO)의 구성 요소로 포함된다.
 *
 * 사용 위치:
 *  - DiaryEntryController.getDiaryList()
 *  - DiaryEntryController.getDiaryListMonthly()
 *
 * 제공 필드:
 *  ✔ id          : 회고 ID
 *  ✔ content     : 회고 본문
 *  ✔ emotion     : 감정 점수 (1~5)
 *  ✔ feedback    : GPT 피드백 문장
 *  ✔ feelingKo   : 한글 감정 문장
 *  ✔ feelingEn   : GPT 영어 감정 문장
 *  ✔ habitTags   : 완료한 습관 태그 리스트
 *  ✔ createdAt   : 작성 시각
 *
 * ※ Entity → DTO 변환을 위한 fromEntity() 제공
 */
@Schema(description = "단일 회고(다이어리 엔트리) 응답 DTO")
@Data
@Builder
@ToString
public class DiaryEntryDTO {

    @Schema(description = "회고 고유 ID", example = "101")
    private Long id;

    @Schema(
            description = "회고 본문 내용",
            example = "아침에 운동하고 독서하며 차분하게 보냈다."
    )
    private String content;

    @Schema(
            description = "감정 점수 (1~5)",
            example = "4"
    )
    private int emotion;

    @Schema(
            description = "GPT가 생성한 피드백 문장",
            example = "오늘처럼 꾸준한 루틴을 유지하면 감정 흐름이 더 안정됩니다."
    )
    private String feedback;

    @Schema(
            description = "한글 감정 문장",
            example = "차분하고 안정적인 하루였다."
    )
    private String feelingKo;

    @Schema(
            description = "GPT가 생성한 영어 감정 문장",
            example = "It was a calm and stable day overall."
    )
    private String feelingEn;

    @Schema(
            description = "오늘 완료한 습관 태그 리스트",
            example = "[\"운동\", \"독서\", \"명상\"]"
    )
    private List<String> habitTags;

    @Schema(
            description = "작성 시각 (LocalDateTime)",
            example = "2025-01-12T09:30:21"
    )
    private LocalDateTime createdAt;

    // 👉 Entity → DTO 변환용 static 메서드
    public static DiaryEntryDTO fromEntity(DiaryEntry entity) {
        return DiaryEntryDTO.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .emotion(entity.getEmotion())
                .feedback(entity.getFeedback())
                .feelingKo(entity.getFeelingKo())
                .feelingEn(entity.getFeelingEn())
                .habitTags(convertHabitTags(entity.getHabitTags()))
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private static List<String> convertHabitTags(String habitTagsRaw) {
        if (habitTagsRaw == null || habitTagsRaw.isBlank()) return List.of();
        return List.of(habitTagsRaw.split(","));
    }
}
