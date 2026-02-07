package dev.huha123.app.config;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.huha123.app.domain.user.UserEntity;
import dev.huha123.app.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleHierarchy roleHierarchy;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + username));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(roleHierarchy.getReachableGrantedAuthorities(
                        AuthorityUtils.createAuthorityList(
                                user.getRole() != null ? user.getRole() : "USER")))
                .build();
    }
}