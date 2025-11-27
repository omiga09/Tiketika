package com.tiketika.Tiketika.common.auth.dto.command;

import com.tiketika.Tiketika.common.auth.enums.EntityType;

import java.util.Set;

public record CreateRoleRequest(
        String name,
        EntityType entityType,
        Set<Long> permissionIds
) {}
