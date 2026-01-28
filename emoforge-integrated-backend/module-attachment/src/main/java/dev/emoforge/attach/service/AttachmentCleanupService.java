package dev.emoforge.attach.service;

import dev.emoforge.attach.domain.UploadType;
import dev.emoforge.attach.repository.AttachmentRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ToastUI 에디터에서 삭제된 임시 이미지 파일을 정리하는 Controller.
 * 에디터에 최종적으로 남아있는 이미지 목록을 기반으로 사용되지 않은 파일을 DB 에서 제거한다.
 */
@Tag(
        name = "Attachment Cleanup API",
        description = "ToastUI 에디터에서 삭제된 임시 이미지 정리 API"
)
@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentCleanupService {

    private final AttachmentRepository attachmentRepository;

    @Transactional
    public void cleanupEditorImages(Long postId, List<String> fileUrls) {

        if (fileUrls == null || fileUrls.isEmpty()) {
            // 본문에 이미지가 하나도 없으면 전부 삭제
            attachmentRepository.deleteAll(attachmentRepository.findByPostId(postId, UploadType.EDITOR_IMAGE));
            return;
        }

        // 본문에 남아있는 이미지 → CONFIRMED
        attachmentRepository.confirmEditorImages(postId, UploadType.EDITOR_IMAGE, fileUrls);

        // 본문에 없는 이미지 → DELETE
        attachmentRepository.deleteUnusedEditorImages(postId, UploadType.EDITOR_IMAGE, fileUrls );
    }
}
