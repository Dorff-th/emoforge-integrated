package dev.emoforge.maintenance.cleaner;

import dev.emoforge.maintenance.config.CleanupProperties;
import dev.emoforge.maintenance.domain.Attachment;
import dev.emoforge.maintenance.repository.AttachmentRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import dev.emoforge.maintenance.util.FilePathResolver;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorCleaner implements Cleaner {

    private static final Logger log = LoggerFactory.getLogger(EditorCleaner.class);

    private final AttachmentRepository attachmentRepository;
    private final CleanupProperties props;

    private final FilePathResolver resolver;

    public EditorCleaner(AttachmentRepository attachmentRepository, CleanupProperties props, FilePathResolver resolver) {
        this.attachmentRepository = attachmentRepository;
        this.props = props;
        this.resolver = resolver;
    }

    @Override
    public String name() {
        return "editor";
    }

    @Override
    public CleanupResult run() {
        CleanupResult result = new CleanupResult();
        LocalDateTime threshold = LocalDateTime.now().minus(props.getGracePeriod());

        long lastId = 0L;
        int batchSize = props.getBatchSize();

        log.info("[{}] start | threshold={} | batchSize={} | dryRun={}",
                name(), threshold, batchSize, props.isDryRun());

        while (true) {
            List<Attachment> batch = attachmentRepository.findTempEditorBatch(threshold, lastId, batchSize);
            if (batch.isEmpty()) break;

            for (Attachment a : batch) {
                lastId = Math.max(lastId, a.getId());
                result.incProcessed();

                try {
                    // 1) 파일 삭제 (TODO)
                    if (props.isDryRun()) {
                        log.debug("[DRY-RUN][{}] would delete file: {}", name(), a.getFileUrl());
                        result.incSkipped();
                        continue;
                    }

                    boolean fileDeleted = deleteFileSafely(a);
                    /*if (!fileDeleted) {
                        log.warn("[{}] file delete failed: id={}, path={}", name(), a.getId(), a.getFileUrl());
                        result.incFailed();
                        continue;
                    }*/
                    if (!fileDeleted) {
                        log.warn("[editor] file missing but DB cleanup continues: {}", a.getFileUrl());
                    }

                    // 2) DB row 삭제 (조건 재검증)
                    int affected = deleteDbRowWithGuard(a.getId(), threshold);
                    if (affected == 1) {
                        result.incDeleted();
                        log.debug("[{}] deleted: id={}, path={}", name(), a.getId(), a.getFileUrl());
                    } else {
                        // 상태가 TEMP->CONFIRM 으로 바뀌었거나 post_id가 세팅된 케이스 등
                        result.incSkipped();
                        log.info("[{}] skip (guard not matched): id={}, path={}", name(), a.getId(), a.getFileUrl());
                    }

                } catch (Exception e) {
                    result.incFailed();
                    log.warn("[{}] exception: id={}, path={} | {}",
                            name(), a.getId(), a.getFileUrl(), e.toString());
                }
            }
        }

        log.info("[{}] finish | {}", name(), result);
        return result;
    }

    private boolean deleteFileSafely(Attachment a) {
        try {
            Path path = resolver.resolve(a);

            if (!Files.exists(path)) {
                log.warn("[editor] file not found: {}", path);
                return false;
            }

            boolean deleted = Files.deleteIfExists(path);

            if (!deleted) {
                log.warn("[editor] deleteIfExists returned false: {}", path);
                return false;
            }

            log.debug("[editor] file deleted: {}", path);
            return true;

        } catch (Exception e) {
            log.warn("[editor] file delete exception: id={}, path={}, error={}",
                    a.getId(), a.getFileUrl(), e.toString());
            return false;
        }
    }

    @Transactional
    protected int deleteDbRowWithGuard(long id, LocalDateTime threshold) {
        return attachmentRepository.deleteOldAttachment(id);
    }
}