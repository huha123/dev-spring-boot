package dev.huha123.app.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.huha123.app.config.TokenProvider;
import dev.huha123.app.constant.Constant;
import dev.huha123.app.entity.Member;
import dev.huha123.app.entity.Role;
import dev.huha123.app.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository repository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public ResponseEntity<?> list(Pageable pageable) {
        return ResponseEntity.ok(repository.findAll(pageable).getContent());
    }

    public ResponseEntity<?> insert(@NonNull Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return ResponseEntity.ok(repository.save(member));
    }

    public ResponseEntity<?> update(@NonNull Member member) {
        if (repository.findById(member.getId()).isPresent()) {
            return ResponseEntity.ok(repository.save(member));

        } else {
            return ResponseEntity.ok("no data");
        }
    }

    public ResponseEntity<?> delete(@NonNull Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok(id);
    }

    public ResponseEntity<?> login(Member member) {
        Optional<Member> data = repository.findByEmail(member.getEmail());
        if (data.isPresent()) {
            /* spring security authentication 인증 객체로 담는다 */
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    member.getEmail(), member.getPassword());

            /* implements UserDetailService -> loadUserByUsername() 실행 */
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            /* Security Context Authentication에 보관 */
            SecurityContextHolder.getContext().setAuthentication(authentication);

            /* 인증후 JWT 토큰 생성 */
            String jwtToken = tokenProvider.createToken(authentication);

            /* 인증후 JWT REFRESH 토큰 생성 */
            String jwtRefreshToken = tokenProvider.createRefreshToken(authentication);

            Map<String, String> map = new HashMap<>();
            map.put("token", jwtToken);
            map.put("refreshToken", jwtRefreshToken);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(Constant.HEADER_NAME.getValue(), Constant.HEADER_VALUE.getValue() + jwtToken);
            return new ResponseEntity<>(map, httpHeaders, HttpStatus.OK);

        } else {
            return new ResponseEntity<>("LOGIN ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* spring security user detail service */
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        /* member data */
        Optional<Member> data = repository.findByEmail(userEmail);
        log.info("### loadUserByUsername data :{}", data.get());

        /* security settings role & authorize */
        data.get().setAuthorities(
                Stream.concat(
                        getRoles(data.get().getRoles()).stream(),
                        getPrivileges(data.get().getRoles()).stream()).collect(Collectors.toList()));

        return data.get();
    }

    private List<SimpleGrantedAuthority> getRoles(Collection<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .peek(v -> log.info("### peek roles: {}", v))
                .collect(Collectors.toList());
    }

    private List<SimpleGrantedAuthority> getPrivileges(Collection<Role> roles) {
        return roles.stream()
                .flatMap(role -> role.getPrivileges().stream()
                // .peek(v -> log.info("### peek role: {}", v))
                )
                .map(privilege -> new SimpleGrantedAuthority(privilege.getName()))
                .peek(v -> log.info("### peek privilege: {}", v))
                .collect(Collectors.toList());
    }

}
