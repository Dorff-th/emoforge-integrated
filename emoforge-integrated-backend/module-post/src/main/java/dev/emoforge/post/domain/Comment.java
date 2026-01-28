package dev.emoforge.post.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String content;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "post_id", nullable = false, updatable = false)
    private Long postId;

    @Column(name = "member_uuid", nullable = false, length = 36, updatable = false)
    private String memberUuid;

    public static Comment create(Long postId, String memberUuid, String content) {
        return Comment.builder()
                .postId(postId)
                .memberUuid(memberUuid)
                .content(content)
                .build();
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
