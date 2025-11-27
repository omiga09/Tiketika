package com.tiketika.Tiketika.common.auth.dto.command;
;

public record PermissionUpdateRequest(
        String name,
        String method,
        String endpoint,
        String description
) {}
