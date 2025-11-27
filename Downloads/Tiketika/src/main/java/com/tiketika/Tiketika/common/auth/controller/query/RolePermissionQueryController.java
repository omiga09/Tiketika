package com.tiketika.Tiketika.common.auth.controller.query;


import com.tiketika.Tiketika.common.auth.entities.Permission;
import com.tiketika.Tiketika.common.auth.entities.Role;
import com.tiketika.Tiketika.common.auth.services.command.RolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class RolePermissionQueryController {


    private final RolePermissionService rolePermissionService;

    @GetMapping("/roles")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(rolePermissionService.getAllRoles());
    }

    @GetMapping("/permissions")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        return ResponseEntity.ok(rolePermissionService.getAllPermissions());
    }

}
