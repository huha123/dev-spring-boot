package dev.huha123.app.dto;

import java.util.List;

import dev.huha123.app.entity.MenuEntity;
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
public class MenuDto {

    private Long id;
    private String menuName;
    private int sortOrder;
    private Long parentId;
    private String role;
    private String pathUrl;
    private boolean visible;
    private String menuType;
    private List<MenuDto> children;

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
                .id(this.id)
                .menuName(this.menuName)
                .sortOrder(this.sortOrder)
                .role(this.role)
                .pathUrl(this.pathUrl)
                .visible(this.visible)
                .menuType(this.menuType)
                .build();
    }
}
