package dev.emoforge.auth.service.admin;

import dev.emoforge.attach.domain.UploadType;
import dev.emoforge.attach.repository.AttachmentRepository;
import dev.emoforge.attach.service.AttachmentService;
import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.repository.MemberRepository;
import dev.emoforge.auth.repository.RefreshTokenRepository;
import dev.emoforge.diary.domain.GptSummary;
import dev.emoforge.diary.domain.MemberArtistPreference;
import dev.emoforge.diary.domain.MusicRecommendHistory;
import dev.emoforge.diary.domain.MusicRecommendSong;
import dev.emoforge.diary.repository.DiaryEntryRepository;
import dev.emoforge.diary.repository.GptSummaryRepository;
import dev.emoforge.diary.repository.MemberArtistPreferenceRepository;
import dev.emoforge.diary.repository.MusicRecommendHistoryRepository;
import dev.emoforge.diary.repository.MusicRecommendSongRepository;
import dev.emoforge.post.domain.Post;
import dev.emoforge.post.repository.CommentRepository;
import dev.emoforge.post.repository.PostRepository;
import dev.emoforge.post.service.internal.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberDeletionService {

    private static final Logger AUDIT_LOG = LoggerFactory.getLogger("AUDIT");
    private static final DateTimeFormatter AUDIT_TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final DiaryEntryRepository diaryEntryRepository;
    private final GptSummaryRepository gptSummaryRepository;
    private final MusicRecommendHistoryRepository musicRecommendHistoryRepository;
    private final MusicRecommendSongRepository musicRecommendSongRepository;
    private final MemberArtistPreferenceRepository memberArtistPreferenceRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentService attachmentService;

    // 2026-03-11: Added admin member deletion with related data counts and AUDIT logging.
    @Transactional
    public void deleteMember(UUID memberUuid, UUID adminUuid) {
        String targetUuid = memberUuid.toString();
        String actorUuid = adminUuid.toString();

        Member member = memberRepository.findByUuid(targetUuid)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + targetUuid));

        Map<UploadType, Long> attachmentCounts = new EnumMap<>(UploadType.class);
        attachmentRepository.countByMemberAndUploadTypes(targetUuid, List.of(
                UploadType.ATTACHMENT,
                UploadType.EDITOR_IMAGE,
                UploadType.PROFILE_IMAGE
        )).forEach(row -> attachmentCounts.put(row.uploadType(), row.count()));

        int postCount = postRepository.countByMemberUuid(targetUuid);
        int commentCount = commentRepository.countByMemberUuid(targetUuid);
        long attachmentCount = attachmentCounts.getOrDefault(UploadType.ATTACHMENT, 0L);
        long editorImageCount = attachmentCounts.getOrDefault(UploadType.EDITOR_IMAGE, 0L);
        long profileImageCount = attachmentCounts.getOrDefault(UploadType.PROFILE_IMAGE, 0L);
        int diaryEntryCount = diaryEntryRepository.countByMemberUuid(targetUuid);

        List<Post> posts = postRepository.findAllByMemberUuid(targetUuid, Pageable.unpaged()).getContent();
        posts.forEach(post -> postService.deletePost(post.getId()));

        commentRepository.deleteByMemberUuid(targetUuid);

        List<GptSummary> gptSummaries = gptSummaryRepository.findAllByMemberUuid(targetUuid);
        gptSummaryRepository.deleteAll(gptSummaries);

        List<MusicRecommendHistory> histories = musicRecommendHistoryRepository.findByMemberUuidOrderByCreatedAtDesc(targetUuid);
        List<Long> historyIds = histories.stream()
                .map(MusicRecommendHistory::getId)
                .toList();
        List<MusicRecommendSong> songs = historyIds.isEmpty()
                ? List.of()
                : musicRecommendSongRepository.findAllByHistoryIdIn(historyIds);
        musicRecommendSongRepository.deleteAll(songs);
        musicRecommendHistoryRepository.deleteAll(histories);

        List<MemberArtistPreference> preferences =
                memberArtistPreferenceRepository.findByMemberUuidOrderByPreferenceScoreDesc(targetUuid);
        memberArtistPreferenceRepository.deleteAll(preferences);

        diaryEntryRepository.deleteAll(diaryEntryRepository.findByMemberUuid(targetUuid, Pageable.unpaged()).getContent());

        attachmentService.deleteProfileImages(targetUuid);
        refreshTokenRepository.deleteByMemberUuid(targetUuid);
        memberRepository.deleteByUuid(member.getUuid());

        AUDIT_LOG.info(
                "ADMIN_MEMBER_DELETE{}admin={}{}target={}{}posts={}{}comments={}{}attachments={}{}editorImages={}{}profileImages={}{}diaryEntries={}{}timestamp={}",
                System.lineSeparator(),
                actorUuid,
                System.lineSeparator(),
                targetUuid,
                System.lineSeparator(),
                postCount,
                System.lineSeparator(),
                commentCount,
                System.lineSeparator(),
                attachmentCount,
                System.lineSeparator(),
                editorImageCount,
                System.lineSeparator(),
                profileImageCount,
                System.lineSeparator(),
                diaryEntryCount,
                System.lineSeparator(),
                LocalDateTime.now().format(AUDIT_TIMESTAMP_FORMAT)
        );
    }
}
