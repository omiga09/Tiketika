package com.tiketika.engine.common.auth.services.command;

import com.tiketika.engine.common.auth.dto.command.CreateUserEntityRequest;
import com.tiketika.engine.common.auth.entities.Permission;
import com.tiketika.engine.common.auth.entities.Role;
import com.tiketika.engine.common.auth.entities.User;
import com.tiketika.engine.common.auth.entities.UserEntity;
import com.tiketika.engine.common.auth.repositories.PermissionRepository;
import com.tiketika.engine.common.auth.repositories.RoleRepository;
import com.tiketika.engine.common.auth.repositories.UserEntityRepository;
import com.tiketika.engine.common.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuperAdminCommandServiceImpl implements SuperAdminCommandService {

    private final UserRepository userRepository;
    private final UserEntityRepository userEntityRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public UserEntity createUserEntity(CreateUserEntityRequest request) {
        // Fetch roles
        Set<Role> roles = request.getRoles().stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found with ID " + roleId)))
                .collect(Collectors.toSet());

        User user;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID " + request.getUserId()));
        } else {
            String password = UUID.randomUUID().toString();

            user = User.builder()
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .phone(request.getPhone())
                    .password(password)
                    .isPasswordExpired(true)
                    .expiryDate(LocalDate.now())
                    .userType(request.getUserType())
                    .isAccountActive(true)
                    .build();

            userRepository.saveAndFlush(user);
        }

        UserEntity entity = new UserEntity();
        entity.setEntityType(request.getEntityType());
        entity.setUser(user);
        entity.setRoles(roles);

        return userEntityRepository.save(entity);
    }

    public Role assignPermissionsToRole(Long roleId, Set<String> permissionNames) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Set<Permission> permissions = permissionNames.stream()
                .map(name -> permissionRepository.findFirstByName(name) // ✅ badala ya findByName
                        .orElseThrow(() -> new RuntimeException("Permission not found: " + name)))
                .collect(Collectors.toSet());

        role.getPermissions().addAll(permissions);
        return roleRepository.save(role);
    }

    public UserEntity assignRolesToUserEntity(Long entityId, Set<String> roleNames) {
        UserEntity userEntity = userEntityRepository.findById(entityId)
                .orElseThrow(() -> new RuntimeException("UserEntity not found"));

        Set<Role> roles = roleNames.stream()
                .map(name -> roleRepository.findFirstByName(name) // ✅ badala ya findByName
                        .orElseThrow(() -> new RuntimeException("Role not found: " + name)))
                .collect(Collectors.toSet());

        userEntity.setRoles(roles);
        return userEntityRepository.save(userEntity);
    }
}
