package dev.huha123.app.service;

import dev.huha123.app.config.JwtUtil;
import dev.huha123.app.dto.UserDto;
import dev.huha123.app.entity.UserEntity;
import dev.huha123.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * 새로운 사용자를 생성합니다.
     * @param userDto 생성할 사용자 정보
     * @return 생성된 사용자 정보
     */
    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.username()).isPresent()) {
            // 실무에서는 Custom Exception을 사용하는 것이 좋습니다.
            throw new RuntimeException("이미 존재하는 사용자 이름입니다.");
        }

        UserEntity userEntity = UserEntity.builder()
                .username(userDto.username())
                .password(passwordEncoder.encode(userDto.password()))
                .email(userDto.email())
                .role(userDto.role())
                .build();

        UserEntity savedUser = userRepository.save(userEntity);
        return UserDto.fromEntity(savedUser);
    }

    /**
     * ID로 사용자를 조회합니다.
     * @param id 사용자 ID
     * @return Optional<UserDto>
     */
    public Optional<UserDto> findUserById(Long id) {
        return userRepository.findById(id).map(UserDto::fromEntity);
    }

    /**
     * 사용자 이름으로 사용자를 조회합니다.
     * @param username 사용자 이름
     * @return Optional<UserDto>
     */
    public Optional<UserDto> findUserByUsername(String username) {
        return userRepository.findByUsername(username).map(UserDto::fromEntity);
    }

    /**
     * 모든 사용자를 조회합니다.
     * @return List<UserDto>
     */
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 정보를 수정합니다. (email, role만 수정 가능)
     * @param id 사용자 ID
     * @param userDto 수정할 사용자 정보
     * @return 수정된 사용자 정보
     */
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자 이름과 비밀번호는 수정하지 않는다고 가정합니다.
        // 비밀번호 변경은 별도의 API로 처리하는 것이 안전합니다.
        UserEntity updatedEntity = UserEntity.builder()
                .id(existingUser.getId())
                .username(existingUser.getUsername())
                .password(existingUser.getPassword())
                .email(userDto.email()) // email 변경
                .role(userDto.role())     // role 변경
                .build();

        UserEntity savedUser = userRepository.save(updatedEntity);
        return UserDto.fromEntity(savedUser);
    }

    /**
     * 사용자를 삭제합니다.
     * @param id 삭제할 사용자 ID
     */
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * 로그인 처리를 하고 JWT 토큰을 반환합니다.
     */
    public String login(String username, String password) {
        // AuthenticationManager를 통해 인증 시도 (비밀번호 체크, 계정 활성화 여부 등 자동 처리)
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // 인증 성공 시 사용자 정보 조회 (여기서는 DB를 다시 조회하여 최신 Role 정보를 가져옴)
        UserEntity user = userRepository.findByUsername(username).get();
        return jwtUtil.createToken(user.getUsername(), user.getRole());
    }
}
