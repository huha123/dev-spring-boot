package dev.huha123.app.config;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.huha123.app.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final RoleService roleService;
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
        // 기본값만 반환 (DB 데이터는 ApplicationRunner에서 동적 설정)
        try {
                log.info("########## RoleHierarchy - All roles from DB: {}",
                        objectMapper.writeValueAsString(roleService.getAllRoles()));

                String hierarchy = roleService.getAllRoles().stream()
                        .filter(role -> role.getParentId() != null)
                        .map(role -> roleService.getRoleById(role.getParentId()).getName() + " > " + role.getName())
                        .collect(Collectors.joining("\n"));
                        log.info("########## Role Hierarchy Set:\n{}", hierarchy);
                        return RoleHierarchyImpl.fromHierarchy(hierarchy);
            } catch (JsonProcessingException e) {
                log.error("Could not serialize roles to JSON", e);
            }
        return RoleHierarchyImpl.fromHierarchy("ADMIN > USER");

    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // 접두사 "ROLE_" 제거
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public ApplicationRunner applicationRunner(RoleHierarchyImpl roleHierarchyImpl) {
        return args -> {
            try {
                log.info("########## ApplicationRunner - All roles from DB: {}",
                        objectMapper.writeValueAsString(roleService.getAllRoles()));

                String hierarchy = roleService.getAllRoles().stream()
                        .filter(role -> role.getParentId() != null)
                        .map(role -> roleService.getRoleById(role.getParentId()).getName() + " > " + role.getName())
                        .collect(Collectors.joining("\n"));
                roleHierarchyImpl.setHierarchy(hierarchy);
                log.info("########## Role Hierarchy Set:\n{}", hierarchy);
            } catch (JsonProcessingException e) {
                log.error("Could not serialize roles to JSON", e);
            }
        };
    }

}