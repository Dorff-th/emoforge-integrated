package dev.emoforge.diary.dto.music;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * MusicRecommendRequest
 *
 * 감정 기반 음악 추천을 생성할 때 프론트엔드에서 전달되는 요청 DTO.
 *
 * 사용 위치:
 *  - MusicRecommendController.recommendMusic()
 *
 * 제공 필드:
 *  ✔ diaryEntryId        : 어떤 회고(DiaryEntry)에 기반하여 음악을 추천할지 지정
 *  ✔ artistPreferences   : 사용자가 선호하는 아티스트 목록 (선택 입력)
 *
 * 특징:
 *  - LangGraph-Service에 전달되며 감정 기반 + 선호 기반 음악 추천에 사용됨
 */
@Data
public class MusicRecommendRequest {

    @Schema(
            description = "음악 추천을 수행할 대상 DiaryEntry의 ID",
            example = "150"
    )
    private Long diaryEntryId;

    @Schema(
            description = """
                    사용자가 직접 입력한 선호 아티스트 목록.
                    추천 정확도를 높이기 위해 LangGraph가 참고함.
                    예: ["IU", "Coldplay", "잔나비"]
                    (선택 사항)
                    """,
            example = "[\"IU\", \"Coldplay\"]"
    )
    private List<String> artistPreferences;
}