package dev.huha123.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.huha123.app.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String username;

    // JsonProperty.Access.WRITE_ONLY: 이 필드는 오직 쓰기(요청)시에만 사용됩니다.
    // 즉, DTO를 JSON으로 변환하여 응답할 때는 이 필드가 제외됩니다.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String email;

    private String role;

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
                .username(this.username)
                .password(this.password) // 비밀번호 암호화는 서비스 계층에서 처리됩니다.
                .email(this.email)
                .role(this.role)
                .build();
    }
}
