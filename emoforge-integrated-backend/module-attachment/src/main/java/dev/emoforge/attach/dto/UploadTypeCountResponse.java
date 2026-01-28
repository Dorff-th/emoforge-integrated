package dev.emoforge.attach.dto;

import dev.emoforge.attach.domain.UploadType;

public record UploadTypeCountResponse(
        UploadType uploadType,
        long count
) {}
