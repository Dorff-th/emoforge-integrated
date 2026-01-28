package dev.emoforge.post.dto.internal;

import dev.emoforge.post.domain.PostTag;
import lombok.Data;

@Data
public class TagDTO {
    private PostTag postTag;
    private String name;

    public TagDTO(PostTag postTag, String name) {
        this.postTag = postTag;
        this.name = name;
    }
}
