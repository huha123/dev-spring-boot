package dev.huha123.app.config;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.huha123.app.entity.RoleEntity;
import dev.huha123.app.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login/*", "/oauth2/**", "/api/login", "/api/users", "/h2-console/**")
                        .permitAll()
                        .requestMatchers("/api/test/user").hasRole("USER")
                        .requestMatchers("/api/test/manager").hasRole("MANAGER")
                        .requestMatchers("/api/test/manager1").hasRole("MANAGER_1")
                        .requestMatchers("/api/test/admin").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                // .oauth2Login(oauth2 -> oauth2.successHandler(oAuth2SuccessHandler)) // OAuth2
                // 로그인 사용 시 주석 해제
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(buildRoleHierarchyString());
    }

    private String buildRoleHierarchyString() {
        List<RoleEntity> allRoles = roleRepository.findAll();

        // 1. 자식 노드를 가진 부모 ID 세트 생성 (Leaf Node 판별용)
        Set<Long> parentIds = allRoles.stream()
                .map(RoleEntity::getParent)
                .filter(Objects::nonNull)
                .map(RoleEntity::getId)
                .collect(Collectors.toSet());

        StringBuilder hierarchyBuilder = new StringBuilder();

        for (RoleEntity role : allRoles) {
            String currentRole = role.getRoleName();

            // 부모가 있다면 관계 추가 (예: ROLE_ADMIN > ROLE_MANAGER)
            if (role.getParent() != null) {
                String parentRole = role.getParent().getRoleName();
                hierarchyBuilder.append(parentRole)
                        .append(" > ")
                        .append(currentRole)
                        .append("\n");
            }

            // 2. 이 역할이 최하위 노드(Leaf)라면 ROLE_USER 연결
            if (!parentIds.contains(role.getId()) && !"USER".equals(role.getRoleName())) {
                hierarchyBuilder.append(currentRole)
                        .append(" > USER\n");
            }
        }
        return hierarchyBuilder.toString();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // 접두사 "ROLE_" 제거
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public ApplicationRunner applicationRunner(RoleHierarchyImpl roleHierarchyImpl) {
        return args -> {
            roleHierarchyImpl.setHierarchy(buildRoleHierarchyString());
            log.info("########## Role Hierarchy Set:\n{}", buildRoleHierarchyString());
        };
    }

}