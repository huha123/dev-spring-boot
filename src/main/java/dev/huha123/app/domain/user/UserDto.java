package dev.huha123.app.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record UserDto(
        Long id,
        String username,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,
        String email,
        String role) {

    /**
     * UserEntity를 UserDto로 변환하는 정적 팩토리 메서드
     * 응답용 DTO를 생성할 때 사용됩니다.
     */
    public static UserDto fromEntity(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .role(entity.getRole())
                // password는 응답에 포함하지 않습니다.
                .build();
    }

    /**
     * UserDto를 UserEntity로 변환하는 인스턴스 메서드
     * 요청 DTO를 엔티티로 변환할 때 사용됩니다.
     * @return UserEntity
     */
    public UserEntity toEntity() {
        return UserEntity.builder()
                .username(username())
                .password(password()) // 비밀번호 암호화는 서비스 계층에서 처리됩니다.
                .email(email())
                .role(role())
                .build();
    }
}
