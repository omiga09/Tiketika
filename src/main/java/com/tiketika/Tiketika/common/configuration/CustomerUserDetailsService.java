package com.tiketika.Tiketika.common.configuration;

import com.tiketika.Tiketika.common.auth.entities.User;
import com.tiketika.Tiketika.common.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return buildUserDetails(user);
    }

    // 🔹 Helper method to convert User entity to UserDetails
    public UserDetails buildUserDetails(User user) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserType().name()));
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getEmail(); // or phone if needed
            }

            @Override
            public boolean isAccountNonExpired() {
                return !user.getIsPasswordExpired();
            }

            @Override
            public boolean isAccountNonLocked() {
                return !user.getIs_account_locked();
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return !user.getIsPasswordExpired();
            }

            @Override
            public boolean isEnabled() {
                return user.getIsAccountActive();
            }
        };
    }
}
