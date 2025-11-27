package com.tiketika.engine.common.auth.dto.command;
;

public record PermissionUpdateRequest(
        String name,
        String method,
        String endpoint,
        String description
) {}
