package com.tiketika.engine.common.auth.config;

import com.tiketika.engine.common.auth.entities.Permission;
import com.tiketika.engine.common.auth.entities.Role;
import com.tiketika.engine.common.auth.entities.User;
import com.tiketika.engine.common.auth.entities.UserEntity;
import com.tiketika.engine.common.auth.enums.EntityType;
import com.tiketika.engine.common.auth.enums.UserType;
import com.tiketika.engine.common.auth.repositories.PermissionRepository;
import com.tiketika.engine.common.auth.repositories.RoleRepository;
import com.tiketika.engine.common.auth.repositories.UserEntityRepository;
import com.tiketika.engine.common.auth.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserDataInitializer {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        // 1. Initialize permissions
        List<String> permissionNames = List.of(
                "USER_MANAGEMENT", "ROLE_MANAGEMENT", "PERMISSION_MANAGEMENT",
                "PRODUCT_CREATE", "PRODUCT_VIEW", "PRODUCT_UPDATE", "PRODUCT_DELETE",
                "CLIENT_CREATE", "CLIENT_VIEW", "CLIENT_UPDATE", "CLIENT_VERIFY", "CLIENT_ACTIVATE",
                "LOAN_INITIATE", "LOAN_APPROVE", "LOAN_DISBURSE", "LOAN_RESTRUCTURE", "LOAN_CLOSE",
                "VIEW_ALL_LOANS", "VIEW_DASHBOARD", "GENERATE_REPORTS",
                // ✅ Added missing permissions for owners
                "EVENT_CREATE", "EVENT_VIEW", "EVENT_UPDATE",
                "BUS_CREATE", "BUS_VIEW", "BUS_UPDATE",
                "TOUR_CREATE", "TOUR_VIEW", "TOUR_UPDATE",
                "HOTSPOT_CREATE", "HOTSPOT_VIEW", "HOTSPOT_UPDATE"
        );

        for (String permName : permissionNames) {
            permissionRepository.findFirstByName(permName)
                    .orElseGet(() -> {
                        Permission p = new Permission();
                        p.setName(permName);
                        p.setMethod("AUTO_" + permName);
                        p.setEndpoint("/" + permName.toLowerCase().replace("_", "-"));
                        p.setDescription("Auto-created permission: " + permName);
                        return permissionRepository.save(p);
                    });
        }

        // 2. SUPER_ADMIN role
        Role superAdminRole = roleRepository.findFirstByName("SUPER_ADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("SUPER_ADMIN");
                    r.setEntityType(EntityType.EVENTS);
                    r.setPermissions(new HashSet<>(permissionRepository.findAll()));
                    return roleRepository.save(r);
                });

        // 3. ENTITY role
        roleRepository.findFirstByName("ENTITY")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ENTITY");
                    r.setEntityType(EntityType.EVENTS);
                    r.setPermissions(new HashSet<>(Set.of(
                            permissionRepository.findFirstByName("PRODUCT_CREATE").orElseThrow(),
                            permissionRepository.findFirstByName("PRODUCT_VIEW").orElseThrow(),
                            permissionRepository.findFirstByName("PRODUCT_UPDATE").orElseThrow()
                    )));
                    return roleRepository.save(r);
                });

        // 4. CUSTOMER role
        roleRepository.findFirstByName("CUSTOMER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("CUSTOMER");
                    r.setEntityType(EntityType.EVENTS);
                    r.setPermissions(new HashSet<>(Set.of(
                            permissionRepository.findFirstByName("PRODUCT_VIEW").orElseThrow()
                    )));
                    return roleRepository.save(r);
                });

        // 5. SUPER ADMIN user
        List<User> admins = userRepository.findAllByEmail("super@kuza.com");
        if (admins.isEmpty()) {
            User superAdmin = new User();
            superAdmin.setFirstName("Super");
            superAdmin.setLastName("Admin");
            superAdmin.setEmail("super@kuza.com");
            superAdmin.setPhone("255700000001");
            superAdmin.setUserType(UserType.SUPER_ADMIN);
            superAdmin.setPassword(passwordEncoder.encode("123456"));
            superAdmin.setIsAccountActive(true);
            superAdmin.setIsEmailVerified(true);

            User savedUser = userRepository.save(superAdmin);

            UserEntity entity = new UserEntity();
            entity.setEntityType(EntityType.EVENTS);
            entity.setUser(savedUser);
            entity.setRoles(new HashSet<>(Set.of(superAdminRole)));
            userEntityRepository.save(entity);
        } else {
            User superAdmin = admins.get(0);
            superAdmin.setIsAccountActive(true);
            superAdmin.setIsEmailVerified(true);
            userRepository.save(superAdmin);
        }

        // Event Owner
        roleRepository.findFirstByName("EVENT_OWNER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("EVENT_OWNER");
                    r.setEntityType(EntityType.EVENTS);
                    r.setPermissions(new HashSet<>(Set.of(
                            permissionRepository.findFirstByName("EVENT_CREATE").orElseThrow(),
                            permissionRepository.findFirstByName("EVENT_VIEW").orElseThrow(),
                            permissionRepository.findFirstByName("EVENT_UPDATE").orElseThrow()
                    )));
                    return roleRepository.save(r);
                });

        // Bus Owner
        roleRepository.findFirstByName("BUS_OWNER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("BUS_OWNER");
                    r.setEntityType(EntityType.BUSES);
                    r.setPermissions(new HashSet<>(Set.of(
                            permissionRepository.findFirstByName("BUS_CREATE").orElseThrow(),
                            permissionRepository.findFirstByName("BUS_VIEW").orElseThrow(),
                            permissionRepository.findFirstByName("BUS_UPDATE").orElseThrow()
                    )));
                    return roleRepository.save(r);
                });

        // Tour Owner
        roleRepository.findFirstByName("TOUR_OWNER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("TOUR_OWNER");
                    r.setEntityType(EntityType.TOURS);
                    r.setPermissions(new HashSet<>(Set.of(
                            permissionRepository.findFirstByName("TOUR_CREATE").orElseThrow(),
                            permissionRepository.findFirstByName("TOUR_VIEW").orElseThrow(),
                            permissionRepository.findFirstByName("TOUR_UPDATE").orElseThrow()
                    )));
                    return roleRepository.save(r);
                });

        // Hotspot Owner
        roleRepository.findFirstByName("HOTSPOT_OWNER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("HOTSPOT_OWNER");
                    r.setEntityType(EntityType.HOTSPOTS);
                    r.setPermissions(new HashSet<>(Set.of(
                            permissionRepository.findFirstByName("HOTSPOT_CREATE").orElseThrow(),
                            permissionRepository.findFirstByName("HOTSPOT_VIEW").orElseThrow(),
                            permissionRepository.findFirstByName("HOTSPOT_UPDATE").orElseThrow()
                    )));
                    return roleRepository.save(r);
                });
    }
}
