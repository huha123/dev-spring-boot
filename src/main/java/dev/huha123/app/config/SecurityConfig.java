package dev.huha123.app.config;

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

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/login/*", "/oauth2/**", "/api/login", "/api/users", "/h2-console/**").permitAll()
            .requestMatchers("/api/test/user").hasRole("USER")
            .requestMatchers("/api/test/manager").hasRole("MANAGER")
            .requestMatchers("/api/test/admin").hasRole("ADMIN")
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        // .oauth2Login(oauth2 -> oauth2.successHandler(oAuth2SuccessHandler))  // OAuth2 로그인 사용 시 주석 해제
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    @Bean
    public static RoleHierarchy roleHierarchy() {
        // 계층 구조 설정
        // ADMIN > MANAGER > USER
        // 만약 manager1, 2, 3이 병렬이라면 아래와 같이 설정 가능:
        // "ROLE_ADMIN > ROLE_MANAGER1\nROLE_ADMIN > ROLE_MANAGER2\nROLE_MANAGER1 > ROLE_USER\nROLE_MANAGER2 > ROLE_USER"
        return RoleHierarchyImpl.fromHierarchy("ADMIN > MANAGER\nMANAGER > USER");
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // 접두사 "ROLE_" 제거
        return new GrantedAuthorityDefaults("");
    }

}