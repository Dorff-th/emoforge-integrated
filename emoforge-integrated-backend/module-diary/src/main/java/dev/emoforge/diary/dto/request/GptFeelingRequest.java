package dev.emoforge.diary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
사용자가 오늘의 기분을 간단히 한글로 입력받는 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GptFeelingRequest {
    private String feelingKo; // 사용자가 입력한 한글 감정 표현
}
