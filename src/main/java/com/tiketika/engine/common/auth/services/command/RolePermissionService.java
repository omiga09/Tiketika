package com.tiketika.engine.common.auth.services.command;

import com.tiketika.engine.common.auth.dto.command.CreatePermissionRequest;
import com.tiketika.engine.common.auth.dto.command.CreateRoleRequest;
import com.tiketika.engine.common.auth.dto.command.PermissionUpdateRequest;
import com.tiketika.engine.common.auth.dto.command.UpdateRoleRequest;
import com.tiketika.engine.common.auth.entities.Permission;
import com.tiketika.engine.common.auth.entities.Role;
import com.tiketika.engine.common.auth.repositories.PermissionRepository;
import com.tiketika.engine.common.auth.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RolePermissionService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public Role createRole(CreateRoleRequest request) {
        Optional<Role> existing = roleRepository.findFirstByNameAndEntityType(request.name(), request.entityType());
        if (existing.isPresent()) {
            throw new RuntimeException("Role already exists: " + request.name() + "-" + request.entityType());
        }

        Role role = new Role();
        role.setName(request.name());
        role.setEntityType(request.entityType());
        Role savedRole = roleRepository.save(role);

        if (request.permissionIds() != null && !request.permissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.permissionIds()));
            savedRole.setPermissions(permissions);
            savedRole = roleRepository.save(savedRole);
        }

        return savedRole;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role updateRole(Long id, UpdateRoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setName(request.name());
        role.setEntityType(request.entityType());

        if (request.permissionIds() != null && !request.permissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.permissionIds()));
            role.setPermissions(permissions);
        } else {
            role.setPermissions(new HashSet<>());
        }

        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found");
        }
        roleRepository.deleteById(id);

    }

    public Permission createPermission(CreatePermissionRequest request) {
        Permission p = new Permission();
        p.setName(request.name());
        p.setMethod(request.method());
        p.setEndpoint(request.endpoint());
        p.setDescription(request.description());
        return permissionRepository.save(p);
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Permission updatePermission(Long id, PermissionUpdateRequest request) {
        Permission existing = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        existing.setName(request.name());
        existing.setMethod(request.method());
        existing.setEndpoint(request.endpoint());
        existing.setDescription(request.description());

        return permissionRepository.save(existing);
    }

    public void deletePermission(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException("Permission not found");
        }
        permissionRepository.deleteById(id);
    }
}
