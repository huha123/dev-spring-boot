package dev.huha123.app.dto;

import java.util.List;

import dev.huha123.app.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private Long id;
    private String name;
    private Long parentId;
    private List<RoleDto> children;

    public static RoleDto fromEntity(RoleEntity role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getRoleName())
                .parentId(role.getParent() != null ? role.getParent().getId() : null)
                .build();
    }
}