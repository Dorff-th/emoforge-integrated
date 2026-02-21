package dev.emoforge.post.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchRequest {

    private PostSearchFilter filter;
    private PageRequestDTO page;

}