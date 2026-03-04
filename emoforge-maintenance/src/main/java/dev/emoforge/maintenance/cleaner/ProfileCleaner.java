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
public class ProfileCleaner implements Cleaner {

    private static final Logger log = LoggerFactory.getLogger(ProfileCleaner.class);

    private final AttachmentRepository attachmentRepository;
    private final CleanupProperties props;
    private final FilePathResolver resolver;

    public ProfileCleaner(AttachmentRepository attachmentRepository,
                          CleanupProperties props,
                          FilePathResolver resolver) {
        this.attachmentRepository = attachmentRepository;
        this.props = props;
        this.resolver = resolver;
    }

    @Override
    public String name() {
        return "profile";
    }

    @Override
    public CleanupResult run() {

        CleanupResult result = new CleanupResult();
        LocalDateTime threshold = LocalDateTime.now().minus(props.getGracePeriod());

        long lastId = 0L;
        int batchSize = props.getBatchSize();

        log.info("[profile] start | threshold={} | batchSize={} | dryRun={}",
                threshold, batchSize, props.isDryRun());

        while (true) {

            List<Attachment> batch =
                    attachmentRepository.findOldProfileImagesBatch(threshold, lastId, batchSize);

            if (batch.isEmpty()) break;

            for (Attachment a : batch) {

                lastId = Math.max(lastId, a.getId());
                result.incProcessed();

                try {

                    if (props.isDryRun()) {
                        log.debug("[DRY-RUN][profile] would delete: {}", a.getFileUrl());
                        result.incSkipped();
                        continue;
                    }

                    boolean fileDeleted = deleteFileSafely(a);

                    /*if (!fileDeleted) {
                        result.incFailed();
                        continue;
                    }*/

                    if (!fileDeleted) {
                        log.warn("[profile] file missing but DB cleanup continues: {}", a.getFileUrl());
                    }

                    int affected =
                            attachmentRepository.deleteOldAttachment(a.getId());

                    if (affected == 1) {
                        result.incDeleted();
                        log.debug("[profile] deleted: {}", a.getFileUrl());
                    } else {
                        result.incSkipped();
                        log.info("[profile] skip (guard not matched): id={}", a.getId());
                    }

                } catch (Exception e) {
                    result.incFailed();
                    log.warn("[profile] exception: id={}, path={} | {}",
                            a.getId(), a.getFileUrl(), e.toString());
                }
            }
        }

        log.info("[profile] finish | {}", result);
        return result;
    }

    private boolean deleteFileSafely(Attachment a) {

        try {
            Path path = resolver.resolve(a);

            if (!Files.exists(path)) {
                log.warn("[profile] file not found: {}", path);
                return false;
            }

            boolean deleted = Files.deleteIfExists(path);

            if (!deleted) {
                log.warn("[profile] deleteIfExists returned false: {}", path);
                return false;
            }

            log.debug("[profile] file deleted: {}", path);
            return true;

        } catch (Exception e) {
            log.warn("[profile] file delete exception: id={}, path={}, error={}",
                    a.getId(), a.getFileUrl(), e.toString());
            return false;
        }
    }
}
