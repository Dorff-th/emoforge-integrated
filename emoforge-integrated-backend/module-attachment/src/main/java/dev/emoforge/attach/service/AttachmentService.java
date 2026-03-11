package dev.emoforge.attach.service;

import dev.emoforge.attach.domain.Attachment;
import dev.emoforge.attach.domain.AttachmentStatus;
import dev.emoforge.attach.domain.UploadType;
import dev.emoforge.attach.dto.AttachmentResponse;
import dev.emoforge.attach.dto.UploadedFileResult;
import dev.emoforge.attach.policy.UploadPolicy;
import dev.emoforge.attach.policy.UploadPolicyRegistry;
import dev.emoforge.attach.repository.AttachmentRepository;
import dev.emoforge.attach.util.FileUploadUtil;
import dev.emoforge.attach.util.FormatFileSize;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AttachmentService {

    private final FileUploadUtil fileUploadUtil;
    private final UploadPolicyRegistry policyRegistry;
    private final AttachmentRepository attachmentRepository;
    private final FileStorageService fileStorageService;

    public Attachment uploadFile(MultipartFile file,
                                 UploadType uploadType,
                                 AttachmentStatus attachmentStatus,
                                 Long postId,
                                 String memberUuid,
                                 String tempKey) throws IOException {

        UploadPolicy policy = policyRegistry.getPolicy(uploadType);
        validateExtension(file, policy);

        if (file.getSize() > policy.getMaxSize()) {
            throw new IllegalArgumentException("FILE_SIZE_EXCEEDED");
        }

        UploadedFileResult result = fileUploadUtil.saveFile(file, policy);

        Attachment attachment = Attachment.builder()
                .postId(postId)
                .memberUuid(memberUuid)
                .fileName(result.getFileName())
                .originFileName(result.getOriginFileName())
                .fileUrl(result.getFileName())
                .publicUrl(result.getPublicUrl())
                .fileType(result.getFileType())
                .fileSize(result.getFileSize())
                .uploadType(uploadType)
                .attachmentStatus(attachmentStatus)
                .tempKey(tempKey)
                .deleted(false)
                .build();

        return attachmentRepository.save(attachment);
    }

    public void deleteFile(Long id) {
        deleteAttachment(id);
    }

    public void deleteAttachment(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found: " + id));

        fileStorageService.deleteFile(attachment);
        attachmentRepository.delete(attachment);
    }

    public void deleteFileByTempKey(String tempKey) {
    }

    @Transactional(readOnly = true)
    public List<Attachment> getAttachmentsByPost(Long postId) {
        return attachmentRepository.findByPostIdAndDeletedFalse(postId);
    }

    @Transactional(readOnly = true)
    public Optional<Attachment> getProfileImage(String memberUuid) {
        return attachmentRepository.findTopByMemberUuidAndUploadTypeAndDeletedFalseOrderByUploadedAtDesc(
                memberUuid, UploadType.PROFILE_IMAGE
        );
    }

    @Transactional(readOnly = true)
    public boolean canUploadAttachment(Long postId) {
        long count = attachmentRepository.countByPostIdAndUploadTypeAndDeletedFalse(
                postId, UploadType.ATTACHMENT
        );
        return count < 3;
    }

    private void validateExtension(MultipartFile file, UploadPolicy policy) {
        String originalName = file.getOriginalFilename();
        if (originalName == null) {
            throw new IllegalArgumentException("INVALID_FILE_NAME");
        }

        String ext = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
        if (!policy.allowedExtensions().contains(ext)) {
            throw new IllegalArgumentException("INVALID_FILE_EXTENSION");
        }
    }

    public Map<Long, Integer> countByPostIds(List<Long> postIds) {
        return attachmentRepository.countByPostIds(postIds, UploadType.ATTACHMENT).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Number) row[1]).intValue()
                ));
    }

    public List<AttachmentResponse> findByPostId(Long postId, UploadType uploadType) {
        return attachmentRepository.findByPostId(postId, uploadType).stream()
                .map(attachment -> AttachmentResponse.builder()
                        .id(attachment.getId())
                        .postId(attachment.getId())
                        .memberUuid(attachment.getMemberUuid())
                        .originFileName(attachment.getOriginFileName())
                        .fileName(attachment.getFileName())
                        .fileType(attachment.getFileType())
                        .fileSize(attachment.getFileSize())
                        .fileSizeText(FormatFileSize.formatFileSize(attachment.getFileSize()))
                        .publicUrl(attachment.getPublicUrl())
                        .uploadType(attachment.getUploadType())
                        .uploadedAt(attachment.getUploadedAt())
                        .build())
                .toList();
    }

    public int confirmAttachments(Long postId, String tempKey) {
        return attachmentRepository.updatePostIdAndConfirmByTempKey(postId, AttachmentStatus.CONFIRMED, tempKey);
    }

    public Optional<Attachment> getById(Long id) {
        return attachmentRepository.findById(id);
    }

    @Transactional
    public void deleteBatch(List<Long> attachmentIds) {
        log.debug("\n\n\n=====attachmentIds : {}", attachmentIds);
        if (attachmentIds == null || attachmentIds.isEmpty()) {
            return;
        }

        attachmentIds.forEach(this::deleteAttachment);
    }

    public void deleteByPostId(Long postId) {
        attachmentRepository.findAllByPostId(postId)
                .forEach(attachment -> deleteAttachment(attachment.getId()));
    }

    // 2026-03-11: Added member profile image deletion for admin member purge.
    public void deleteProfileImages(String memberUuid) {
        attachmentRepository.findAllByMemberUuidAndUploadType(memberUuid, UploadType.PROFILE_IMAGE)
                .forEach(attachment -> deleteAttachment(attachment.getId()));
    }
}
