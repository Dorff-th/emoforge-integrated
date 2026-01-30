package dev.emoforge.post.service.internal;


import dev.emoforge.attach.repository.AttachmentRepository;
import dev.emoforge.post.domain.Category;
import dev.emoforge.post.domain.Post;
import dev.emoforge.post.domain.PostTag;
import dev.emoforge.post.domain.Tag;
import dev.emoforge.post.dto.internal.PostRequestDTO;
import dev.emoforge.post.dto.internal.PostUpdateDTO;
import dev.emoforge.post.repository.CategoryRepository;
import dev.emoforge.post.repository.CommentRepository;
import dev.emoforge.post.repository.PostRepository;
import dev.emoforge.post.repository.PostTagRepository;
import dev.emoforge.post.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagService tagService;
    private final PostTagRepository postTagRepository;
    private CommentRepository commentRepository;
    private AttachmentRepository attachmentRepository;



    @Transactional
    public Post createPost(PostRequestDTO dto, String memberUuid) {

        log.debug("\n\n\n====debuging");
        log.debug("input category id " + dto.categoryId());

        Category category = categoryRepository.findById(dto.categoryId()).orElseThrow(()->new IllegalArgumentException("카테고리가 없습니다."));

        Post post = Post.create(dto.title(), dto.content(), category.getId(), memberUuid);

        Post savedPost = postRepository.save(post);


        //태그 & post_tag 관계 저장
        if (dto.tags() != null) {
            String[] tagArray = dto.tags().split(",");
            for (String tagName : tagArray) {
                                Tag tag = tagService.getOrCreateTag(tagName);
                PostTag postTag = new PostTag(savedPost, tag);
                postTagRepository.save(postTag);
            }
        }

        return savedPost;
    }

    //Post 수정요청할때 필요한 해당 post 작성자(Member) id값을 조회하기 위해 필요
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }


    @Transactional
    public Post editPost(PostUpdateDTO dto) {

        Post post = postRepository.findById(dto.id()).orElseThrow(()->new IllegalArgumentException("해당 post가 없음!"));

        //삭제 대상 tag id들을 List<Long> 타입으로 변환후 삭제 쿼리 실행
        List<Long> deleteTagIds = StringUtils.toLongList(dto.deleteTagIds());
        if(deleteTagIds != null && !deleteTagIds.isEmpty()) {
            postTagRepository.deleteByPostIdAndTagIdIn(post.getId(), deleteTagIds);
        }

        //태그 & post_tag 관계 저장
        if (dto.tags() != null) {
            String[] tagArray = dto.tags().split(",");
            for (String tagName : tagArray) {
                Tag tag = tagService.getOrCreateTag(tagName);
                PostTag postTag = new PostTag(post, tag);
                postTagRepository.save(postTag);
            }
        }

      postRepository.updatePostById(dto);

      return post;
    }


    //Post 삭제 (2026.01.30 Emoforge-integrated project)
    @Transactional
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }

        // 1️⃣ 연관 메타 먼저 정리
        postTagRepository.deleteByPostId(postId);
        commentRepository.deleteByPostId(postId);
        attachmentRepository.deleteByPostId(postId);

        // 2️⃣ 마지막에 post 삭제
        postRepository.deleteById(postId);
    }


}
