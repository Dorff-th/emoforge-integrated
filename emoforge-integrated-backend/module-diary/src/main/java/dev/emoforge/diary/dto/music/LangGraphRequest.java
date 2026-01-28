package dev.emoforge.diary.dto.music;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * LangGraph-Service로 전송할 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LangGraphRequest {
    private int emotionScore;
    private String feelingKo;
    private String content;
    private List<String> artistPreferences;
}
