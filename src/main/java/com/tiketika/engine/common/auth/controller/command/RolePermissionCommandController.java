package com.tiketika.engine.common.auth.controller.command;

import com.tiketika.engine.common.auth.dto.command.CreatePermissionRequest;
import com.tiketika.engine.common.auth.dto.command.CreateRoleRequest;
import com.tiketika.engine.common.auth.dto.command.PermissionUpdateRequest;
import com.tiketika.engine.common.auth.dto.command.UpdateRoleRequest;
import com.tiketika.engine.common.auth.entities.Permission;
import com.tiketika.engine.common.auth.entities.Role;
import com.tiketika.engine.common.auth.services.command.RolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class RolePermissionCommandController {

    private final RolePermissionService rolePermissionService;

    @PostMapping("/roles")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Role> createRole(@Valid @RequestBody CreateRoleRequest request) {
        Role role = rolePermissionService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @PutMapping("/roles/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Role> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequest request) {

        Role updated = rolePermissionService.updateRole(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/roles/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        rolePermissionService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/permissions")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody CreatePermissionRequest request) {
        Permission permission = rolePermissionService.createPermission(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(permission);
    }

    @PutMapping("/permissions/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Permission> updatePermission(
            @PathVariable Long id,
            @Valid @RequestBody PermissionUpdateRequest request) {

        Permission updated = rolePermissionService.updatePermission(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/permissions/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        rolePermissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

}