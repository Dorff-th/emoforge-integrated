package dev.emoforge.post.dto.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.data.domain.Sort;

public enum SortDirection {
    ASC,
    DESC;

    @JsonCreator
    public static SortDirection from(String value) {
        try {
            return SortDirection.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return DESC;
        }
    }

    public Sort.Direction toSpringDirection() {
        return Sort.Direction.valueOf(this.name());
    }
}
