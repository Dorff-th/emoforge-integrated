package dev.emoforge.diary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DailyDiaryResponse {

    private String date;
    private List<DiaryEntryDTO> entries;
    private String summary;
}
