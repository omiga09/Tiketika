package com.tiketika.Tiketika.common.auth.controller.command;


import com.tiketika.Tiketika.common.auth.dto.command.CreateUserEntityRequest;
import com.tiketika.Tiketika.common.auth.entities.Role;
import com.tiketika.Tiketika.common.auth.entities.UserEntity;
import com.tiketika.Tiketika.common.auth.services.command.SuperAdminCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/user-entities")
@RequiredArgsConstructor
public class SuperAdminCommandController {

    private final SuperAdminCommandService superAdminService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> createUserEntity(@RequestBody @Valid CreateUserEntityRequest request) {
        UserEntity entity = superAdminService.createUserEntity(request);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/{roleId}/permissions")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> assignPermissionsToRole(
            @PathVariable Long roleId,
            @RequestBody Set<String> permissionNames) {

        Role role = superAdminService.assignPermissionsToRole(roleId, permissionNames);
        return ResponseEntity.ok(role);
    }

    @PutMapping("/{entityId}/roles")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> assignRoleToUserEntity(
            @PathVariable Long entityId,
            @RequestBody Set<String> roleNames) {

        UserEntity userEntity = superAdminService.assignRolesToUserEntity(entityId, roleNames);
        return ResponseEntity.ok(userEntity);
    }
}


