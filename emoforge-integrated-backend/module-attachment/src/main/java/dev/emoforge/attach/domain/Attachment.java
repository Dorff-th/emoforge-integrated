package dev.emoforge.attach.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "attachment",
        indexes = {
                @Index(name = "idx_attachment_post_id", columnList = "post_id"),
                @Index(name = "idx_attachment_member_uuid", columnList = "member_uuid"),
                @Index(name = "idx_attachment_upload_type", columnList = "upload_type")
        }
)
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글 첨부(에디터/일반)일 때만 사용
    @Column(name = "post_id")
    private Long postId;

    // 업로더(또는 프로필 이미지 소유자) 식별
    @Column(name = "member_uuid", length = 255)
    private String memberUuid;

    // 서버(스토리지)에 저장된 실제 파일명 (UUID_원본명)
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    // 사용자가 업로드한 원본 파일명
    @Column(name = "origin_file_name", nullable = false, length = 255)
    private String originFileName;

    // 실제 저장 경로 혹은 내부 접근 경로
    @Column(name = "file_url", length = 255)
    private String fileUrl;

    // 외부 공개 접근용 URL (이미지/다운로드 링크)
    @Column(name = "public_url", length = 255)
    private String publicUrl;

    // MIME 타입 (예: image/png, application/pdf)
    @Column(name = "file_type", length = 100)
    private String fileType;

    // 바이트 단위 크기
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    // PROFILE_IMAGE / EDITOR_IMAGE / ATTACHMENT
    @Enumerated(EnumType.STRING)
    @Column(name = "upload_type", nullable = false, length = 20)
    private UploadType uploadType;

    // 업로드 시각 (DB default CURRENT_TIMESTAMP와 호환)
    @CreationTimestamp
    @Column(name = "uploaded_at", updatable = false)
    private LocalDateTime uploadedAt;

    // 소프트 삭제 플래그 (tinyint(1) ↔ boolean)
    @Column(name = "deleted", nullable = false, columnDefinition = "TINYINT(1) default 0")
    private boolean deleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttachmentStatus attachmentStatus;

    @Column(name = "temp_key")
    private String tempKey;

}
