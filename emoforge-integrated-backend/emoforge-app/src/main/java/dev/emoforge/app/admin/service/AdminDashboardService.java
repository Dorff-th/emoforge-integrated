package dev.emoforge.app.admin.service;

import dev.emoforge.app.admin.dto.DashboardDTO;
import dev.emoforge.attach.domain.UploadType;
import dev.emoforge.attach.repository.AttachmentRepository;
import dev.emoforge.auth.repository.MemberRepository;
import dev.emoforge.post.repository.CommentRepository;
import dev.emoforge.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AttachmentRepository attachmentRepository;

    public DashboardDTO getDashboardStatistics() {
        return new DashboardDTO(
                memberRepository.count(),
                postRepository.count(),
                commentRepository.count(),
                attachmentRepository.countByUploadTypeAndDeletedFalse(UploadType.PROFILE_IMAGE),
                attachmentRepository.countByUploadTypeAndDeletedFalse(UploadType.EDITOR_IMAGE),
                attachmentRepository.countByUploadTypeAndDeletedFalse(UploadType.ATTACHMENT)
        );
    }
}
