package dev.emoforge.post.controller;

import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.internal.PostSearchFilter;
import dev.emoforge.post.dto.internal.PostSearchRequest;
import dev.emoforge.post.dto.legacy.bff.PageResponseDTO;
import dev.emoforge.post.dto.query.PostSearchResultDTO;
import dev.emoforge.post.service.query.PostSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/posts/search")
public class PostSearchController {

    final private PostSearchService postSearchService;

    @GetMapping
    public PageResponseDTO<PostSearchResultDTO> getPostSearch(PageRequestDTO requestDTO, PostSearchFilter filter) {

        PostSearchRequest request = new PostSearchRequest(filter, requestDTO);

        PageResponseDTO<PostSearchResultDTO> result =
                postSearchService.search(request, filter);

        return result;

    }
}
