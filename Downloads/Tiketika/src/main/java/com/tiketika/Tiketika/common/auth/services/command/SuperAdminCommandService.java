package com.tiketika.Tiketika.common.auth.services.command;

import com.tiketika.Tiketika.common.auth.dto.command.CreateUserEntityRequest;
import com.tiketika.Tiketika.common.auth.entities.Permission;
import com.tiketika.Tiketika.common.auth.entities.Role;
import com.tiketika.Tiketika.common.auth.entities.User;
import com.tiketika.Tiketika.common.auth.entities.UserEntity;


import java.util.Set;


public interface SuperAdminCommandService {
    UserEntity createUserEntity(CreateUserEntityRequest request);
    Role assignPermissionsToRole(Long roleId, Set<String> permissionNames);
    UserEntity assignRolesToUserEntity(Long entityId, Set<String> roleNames);
}
