package org.zirota.islab1.dto;

import org.zirota.islab1.entity.ImportStatus;

import java.time.ZonedDateTime;

public record ImportOperationResponse(
        Long id,
        ImportStatus status,
        String username,
        Integer addedCount,
        ZonedDateTime createdAt
) {
}
