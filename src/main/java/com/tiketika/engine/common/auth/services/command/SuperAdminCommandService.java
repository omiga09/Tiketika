package com.tiketika.engine.common.auth.services.command;

import com.tiketika.engine.common.auth.dto.command.CreateUserEntityRequest;
import com.tiketika.engine.common.auth.entities.Role;
import com.tiketika.engine.common.auth.entities.UserEntity;


import java.util.Set;


public interface SuperAdminCommandService {
    UserEntity createUserEntity(CreateUserEntityRequest request);
    Role assignPermissionsToRole(Long roleId, Set<String> permissionNames);
    UserEntity assignRolesToUserEntity(Long entityId, Set<String> roleNames);
}
