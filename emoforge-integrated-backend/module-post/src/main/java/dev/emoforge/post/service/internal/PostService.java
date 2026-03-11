package dev.emoforge.post.service.internal;

import dev.emoforge.attach.service.AttachmentService;
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
    private final CommentRepository commentRepository;
    private final AttachmentService attachmentService;

    @Transactional
    public Post createPost(PostRequestDTO dto, String memberUuid) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 없습니다."));

        Post post = Post.create(dto.title(), dto.content(), category.getId(), memberUuid);
        Post savedPost = postRepository.save(post);

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

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public Post editPost(PostUpdateDTO dto) {
        Post post = postRepository.findById(dto.id())
                .orElseThrow(() -> new IllegalArgumentException("해당 post가 없음!"));

        List<Long> deleteTagIds = StringUtils.toLongList(dto.deleteTagIds());
        if (deleteTagIds != null && !deleteTagIds.isEmpty()) {
            postTagRepository.deleteByPostIdAndTagIdIn(post.getId(), deleteTagIds);
        }

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

    @Transactional
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }

        postTagRepository.deleteByPostId(postId);
        commentRepository.deleteByPostId(postId);
        attachmentService.deleteByPostId(postId);
        postRepository.deleteById(postId);
    }
}
