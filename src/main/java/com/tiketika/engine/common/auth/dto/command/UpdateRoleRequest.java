package com.tiketika.engine.common.auth.dto.command;

import com.tiketika.engine.common.auth.enums.EntityType;

import java.util.Set;

public record UpdateRoleRequest(
        String name,
        EntityType entityType,
        Set<Long> permissionIds
) {}
