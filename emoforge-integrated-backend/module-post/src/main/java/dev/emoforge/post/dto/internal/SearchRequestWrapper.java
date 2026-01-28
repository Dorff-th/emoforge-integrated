package dev.emoforge.post.dto.internal;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Hidden
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestWrapper {
    private SearchFilterDTO searchFilterDTO;
    private PageRequestDTO pageRequestDTO;
}
