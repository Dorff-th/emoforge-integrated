package dev.emoforge.maintenance.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;

    private String memberUuid;

    @Enumerated(EnumType.STRING)
    private UploadType uploadType;

    private String fileUrl;

    private LocalDateTime uploadedAt;

    private String status;
}
