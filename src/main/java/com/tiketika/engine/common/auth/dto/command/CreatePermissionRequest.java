package com.tiketika.engine.common.auth.dto.command;

public record CreatePermissionRequest(
        String name,
        String method,
        String endpoint,
        String description
) {}