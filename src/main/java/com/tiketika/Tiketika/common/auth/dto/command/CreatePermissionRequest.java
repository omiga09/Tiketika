package com.tiketika.Tiketika.common.auth.dto.command;

public record CreatePermissionRequest(
        String name,
        String method,
        String endpoint,
        String description
) {}