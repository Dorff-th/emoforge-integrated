package dev.emoforge.diary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
사용자가 입력한 회고를 GPT가 피드백 응답을 받는 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GptFeedbackResponse {
    private String feedback; // GPT가 생성한 피드백 문장
}

