package dev.emoforge.diary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
사용자가 선택한(입력한) GTP 회고 피드백 스타일과
사용자가 입력한 회고 내용을 받는 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GptFeedbackRequest {
    private String content;       // 회고 내용
    private String feedbackType;  // ENCOURAGE, SCOLD, COACH, RANDOM
}
