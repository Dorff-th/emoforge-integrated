package dev.emoforge.post.repository;

import dev.emoforge.app.EmoforgeApplication;
import dev.emoforge.post.admin.dto.AdminCommentListItemResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EmoforgeApplication.class)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("관리자 화면에 노출할 댓글 목록 조회에 성공한다")
    void adminCommentSearchTest() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<AdminCommentListItemResponse> result =
                commentRepository.findAdminComments("ALL", "", pageable);

        System.out.println("총 개수: " + result.getTotalElements());

        result.getContent().forEach(comment -> {
            System.out.println("댓글ID: " + comment.getCommentId());
            System.out.println("내용: " + comment.getContent());
            System.out.println("작성자: " + comment.getAuthor());
            System.out.println("게시글: " + comment.getPostTitle());
            System.out.println("작성일: " + comment.getCreatedAt());
            System.out.println("---------------------------");
        });
    }

    @Test
    void searchAllTest() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<AdminCommentListItemResponse> result =
                commentRepository.findAdminComments("ALL", "댓글", pageable);

        result.getContent().forEach(c ->
                System.out.println(c.getPostTitle() + " | " + c.getContent())
        );
    }

    @Test
    void searchPostTitleTest() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<AdminCommentListItemResponse> result =
                commentRepository.findAdminComments("POST", "번째", pageable);

        result.getContent().forEach(c ->
                System.out.println(c.getPostTitle())
        );
    }

    @Test
    void searchCommentContentTest() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<AdminCommentListItemResponse> result =
                commentRepository.findAdminComments("COMMENT", "굿굿", pageable);

        result.getContent().forEach(c ->
                System.out.println(c.getContent())
        );
    }

    @Test
    void searchNicknameTest() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<AdminCommentListItemResponse> result =
                commentRepository.findAdminComments("NICKNAME", "user", pageable);

        result.getContent().forEach(c ->
                System.out.println(c.getAuthor())
        );
    }

}