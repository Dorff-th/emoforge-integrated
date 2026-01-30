package dev.emoforge.post.repository;

import dev.emoforge.post.dto.query.PostListItemProjection;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import dev.emoforge.app.EmoforgeApplication;

@SpringBootTest(classes = EmoforgeApplication.class)
@Transactional
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Test
    void findPostList_basic() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<PostListItemProjection> page =
                postRepository.findPostList(null, pageable);

        System.out.println("totalElements = " + page.getTotalElements());

        page.getContent().forEach(p -> {
            System.out.println(
                    "id=" + p.getPostId()
                    + ", title=" + p.getTitle()
                    + ", category=" + p.getCategoryName()
                    + ", nickname=" + p.getNickname()
                    + ", commentCount=" + p.getCommentCount()
                    + ", attachmentCount=" + p.getAttachmentCount()
            );
        });
    }

    @Test
    void findPostList_withTag() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<PostListItemProjection> page =
                postRepository.findPostList("Gemini", pageable);

        System.out.println("totalElements(tag=Gemini) = " + page.getTotalElements());

        page.getContent().forEach(p ->
                System.out.println("id=" + p.getPostId() + ", title=" + p.getTitle())
        );
    }
}
