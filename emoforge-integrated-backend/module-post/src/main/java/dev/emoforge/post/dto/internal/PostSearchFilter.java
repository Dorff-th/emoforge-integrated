package dev.emoforge.post.dto.internal;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Hidden
@Getter
@Setter
@NoArgsConstructor
public class PostSearchFilter {
        private String keyword;

        private Boolean titleChecked;
        private Boolean contentChecked;
        private Boolean commentChecked;
        private Boolean categoryChecked;
        private Boolean authorChecked;

        private Long categoryId;

        private LocalDateTime startDate;
        private LocalDateTime endDate;

}
