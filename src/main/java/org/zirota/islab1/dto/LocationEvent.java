package org.zirota.islab1.dto;

public record LocationEvent(
        String type,
        Long locationId
) {}