package dev.emoforge.post.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_tag")
@Getter
@Setter
@IdClass(PostTag.class)
@NoArgsConstructor
public class PostTag {

    @Id
    @ManyToOne
    @JoinColumn(name = "postId", insertable = false, updatable = false)
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name = "tagId", insertable = false, updatable = false)
    private Tag tag;

    public PostTag(Post post, Tag tag) {
        this.post = post;
        this.tag = tag;
    }
}
