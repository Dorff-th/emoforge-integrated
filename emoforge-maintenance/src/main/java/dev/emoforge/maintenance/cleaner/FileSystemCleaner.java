package dev.emoforge.maintenance.cleaner;

import dev.emoforge.maintenance.config.CleanupProperties;
import dev.emoforge.maintenance.config.FileUploadProperties;
import dev.emoforge.maintenance.repository.AttachmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileSystemCleaner implements Cleaner {



    private final AttachmentRepository attachmentRepository;
    private final CleanupProperties props;
    private final FileUploadProperties uploadProps;

    public FileSystemCleaner(AttachmentRepository attachmentRepository,
                             CleanupProperties props,
                             FileUploadProperties uploadProps) {
        this.attachmentRepository = attachmentRepository;
        this.props = props;
        this.uploadProps = uploadProps;
    }

    @Override
    public String name() {
        return "filesystem";
    }

    @Override
    public CleanupResult run() {

        CleanupResult result = new CleanupResult();

        Set<String> dbFiles = new HashSet<>(attachmentRepository.findAllFileUrls());

        log.info("[filesystem] db file count={}", dbFiles.size());

        List<Path> dirs = List.of(
                Paths.get(uploadProps.getEditorImages().getBaseDir()),
                Paths.get(uploadProps.getAttachments().getBaseDir()),
                Paths.get(uploadProps.getProfileImage().getBaseDir())
        );

        LocalDateTime threshold = LocalDateTime.now().minus(props.getGracePeriod());

        for (Path dir : dirs) {

            try (Stream<Path> files = Files.list(dir)) {

                files.filter(Files::isRegularFile)
                        .forEach(path -> {

                            result.incProcessed();

                            String fileName = path.getFileName().toString();

                            if (dbFiles.contains(fileName)) {
                                result.incSkipped();
                                return;
                            }

                            try {

                                FileTime lastModified =
                                        Files.getLastModifiedTime(path);

                                LocalDateTime fileTime =
                                        LocalDateTime.ofInstant(
                                                lastModified.toInstant(),
                                                ZoneId.systemDefault());

                                if (fileTime.isAfter(threshold)) {
                                    result.incSkipped();
                                    return;
                                }

                                if (props.isDryRun()) {
                                    log.debug("[DRY-RUN][filesystem] delete {}", path);
                                    result.incSkipped();
                                    return;
                                }

                                boolean deleted = Files.deleteIfExists(path);

                                if (deleted) {
                                    result.incDeleted();
                                    log.debug("[filesystem] deleted {}", path);
                                } else {
                                    result.incFailed();
                                }

                            } catch (Exception e) {
                                result.incFailed();
                                log.warn("[filesystem] exception {} {}", path, e.toString());
                            }
                        });

            } catch (Exception e) {
                log.warn("[filesystem] scan error {}", dir);
            }
        }

        log.info("[filesystem] finish | {}", result);

        return result;
    }
}