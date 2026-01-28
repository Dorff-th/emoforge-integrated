package dev.emoforge.attach.service;

import dev.emoforge.attach.domain.UploadType;
import dev.emoforge.attach.dto.UploadTypeCountResponse;
import dev.emoforge.attach.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentStatsService {

    private final AttachmentRepository attachmentRepository;

    public Map<UploadType, Long> getUserAttachmentCounts(String memberUuid) {
        List<UploadType> types = List.of(
                UploadType.EDITOR_IMAGE,
                UploadType.ATTACHMENT
        );

        List<UploadTypeCountResponse> result =
                attachmentRepository.countByMemberAndUploadTypes(memberUuid, types);

        return result.stream()
                .collect(Collectors.toMap(
                        UploadTypeCountResponse::uploadType,
                        UploadTypeCountResponse::count
                ));
    }
}
