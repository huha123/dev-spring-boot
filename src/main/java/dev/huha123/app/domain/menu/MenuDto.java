package dev.huha123.app.domain.menu;

import java.util.List;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record MenuDto(
        Long id,
        String menuName,
        int sortOrder,
        Long parentId,
        String role,
        String pathUrl,
        boolean visible,
        String menuType,
        List<MenuDto> children) {

    public static MenuDto fromEntity(MenuEntity entity) {
        return MenuDto.builder()
                .id(entity.getId())
                .menuName(entity.getMenuName())
                .sortOrder(entity.getSortOrder())
                .parentId(entity.getParent() != null ? entity.getParent().getId() : null)
                .role(entity.getRole())
                .pathUrl(entity.getPathUrl())
                .visible(entity.isVisible())
                .menuType(entity.getMenuType())
                .build();
    }

    public MenuEntity toEntity() {
        return MenuEntity.builder()
                .id(id())
                .menuName(menuName())
                .sortOrder(sortOrder())
                .role(role())
                .pathUrl(pathUrl())
                .visible(visible())
                .menuType(menuType())
                .build();
    }
}
