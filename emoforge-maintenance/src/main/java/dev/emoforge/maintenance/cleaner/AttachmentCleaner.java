package dev.emoforge.maintenance.cleaner;

import dev.emoforge.maintenance.config.CleanupProperties;
import dev.emoforge.maintenance.domain.Attachment;
import dev.emoforge.maintenance.repository.AttachmentRepository;
import dev.emoforge.maintenance.util.FilePathResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttachmentCleaner implements Cleaner {

    private static final Logger log = LoggerFactory.getLogger(AttachmentCleaner.class);

    private final AttachmentRepository attachmentRepository;
    private final CleanupProperties props;
    private final FilePathResolver resolver;

    public AttachmentCleaner(AttachmentRepository attachmentRepository,
                             CleanupProperties props,
                             FilePathResolver resolver) {
        this.attachmentRepository = attachmentRepository;
        this.props = props;
        this.resolver = resolver;
    }

    @Override
    public String name() {
        return "attachment";
    }

    @Override
    public CleanupResult run() {

        CleanupResult result = new CleanupResult();
        LocalDateTime threshold = LocalDateTime.now().minus(props.getGracePeriod());

        long lastId = 0L;
        int batchSize = props.getBatchSize();

        log.info("[attachment] start | threshold={} | batchSize={} | dryRun={}",
                threshold, batchSize, props.isDryRun());

        while (true) {

            List<Attachment> batch =
                    attachmentRepository.findOrphanAttachmentsBatch(threshold, lastId, batchSize);

            if (batch.isEmpty()) break;

            for (Attachment a : batch) {

                lastId = Math.max(lastId, a.getId());
                result.incProcessed();

                try {

                    if (props.isDryRun()) {
                        log.debug("[DRY-RUN][attachment] would delete: {}", a.getFileUrl());
                        result.incSkipped();
                        continue;
                    }

                    boolean fileDeleted = deleteFileSafely(a);

                    /*if (!fileDeleted) {
                        result.incFailed();
                        continue;
                    }*/
                    if (!fileDeleted) {
                        log.warn("[attachment] file missing but DB cleanup continues: {}", a.getFileUrl());
                    }

                    int affected =
                            attachmentRepository.deleteOldAttachment(a.getId());

                    if (affected == 1) {
                        result.incDeleted();
                        log.debug("[attachment] deleted: {}", a.getFileUrl());
                    } else {
                        result.incSkipped();
                        log.info("[attachment] skip guard: id={}", a.getId());
                    }

                } catch (Exception e) {
                    result.incFailed();
                    log.warn("[attachment] exception: id={}, path={} | {}",
                            a.getId(), a.getFileUrl(), e.toString());
                }
            }
        }

        log.info("[attachment] finish | {}", result);
        return result;
    }

    private boolean deleteFileSafely(Attachment a) {

        try {

            Path path = resolver.resolve(a);

            if (!Files.exists(path)) {
                log.warn("[attachment] file not found: {}", path);
                return false;
            }

            boolean deleted = Files.deleteIfExists(path);

            if (!deleted) {
                log.warn("[attachment] deleteIfExists returned false: {}", path);
                return false;
            }

            log.debug("[attachment] file deleted: {}", path);
            return true;

        } catch (Exception e) {
            log.warn("[attachment] file delete exception: id={}, path={}, error={}",
                    a.getId(), a.getFileUrl(), e.toString());
            return false;
        }
    }
}