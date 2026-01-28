package dev.emoforge.post.service.internal;

import dev.emoforge.post.dto.internal.PostStatsResponse;
import dev.emoforge.post.repository.CommentRepository;
import dev.emoforge.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 사용자별 게시글·댓글 통계를 제공하는 서비스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostStatisticsService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /**
     * 사용자가 작성한 게시글(Post), 댓글(comment) 개수를 조회한다.
     */
    public PostStatsResponse getPostStatistics(String memberUuid) {
        int postStat = postRepository.countByMemberUuid(memberUuid);
        int commentStat = commentRepository.countByMemberUuid(memberUuid);

        PostStatsResponse response = PostStatsResponse.builder()
            .postCount(postStat)
            .commentCount(commentStat)
            .build();

        return response;
    }
}
