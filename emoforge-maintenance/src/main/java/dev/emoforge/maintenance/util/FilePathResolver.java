package dev.emoforge.maintenance.util;

import dev.emoforge.maintenance.config.FileUploadProperties;
import dev.emoforge.maintenance.domain.Attachment;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;

import static dev.emoforge.maintenance.domain.UploadType.*;

@Component
public class FilePathResolver {

    private final FileUploadProperties props;

    public FilePathResolver(FileUploadProperties props) {
        this.props = props;
    }

    public Path resolve(Attachment attachment) {
        String fileName = attachment.getFileUrl(); // 실제 DB 필드명은 fileUrl

        return switch (attachment.getUploadType()) {
            case EDITOR_IMAGE ->
                    Paths.get(props.getEditorImages().getBaseDir(), fileName);
            case ATTACHMENT ->
                    Paths.get(props.getAttachments().getBaseDir(), fileName);
            case PROFILE_IMAGE ->
                    Paths.get(props.getProfileImage().getBaseDir(), fileName);

        };
    }
}