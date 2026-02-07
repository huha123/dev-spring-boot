package dev.huha123.app.domain.role;

import java.util.List;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record RoleDto(
        Long id,
        String name,
        Long parentId,
        List<RoleDto> children) {

    public static RoleDto fromEntity(RoleEntity role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getRoleName())
                .parentId(role.getParent() != null ? role.getParent().getId() : null)
                .build();
    }

    public RoleEntity toEntity() {
        return RoleEntity.builder()
                .id(id())
                .roleName(name())
                .build();
    }
}