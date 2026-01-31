package dev.huha123.app.entity;

import java.util.List;

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
    private String directUrl;
    private boolean visible;
    private List<MenuDto> children;

    public static MenuDto fromEntity(MenuEntity entity) {
        return MenuDto.builder()
                .id(entity.getId())
                .menuName(entity.getMenuName())
                .sortOrder(entity.getSortOrder())
                .parentId(entity.getParent() != null ? entity.getParent().getId() : null)
                .role(entity.getRole())
                .directUrl(entity.getDirectUrl())
                .visible(entity.isVisible())
                .build();
    }

    public MenuEntity toEntity() {
        return MenuEntity.builder()
                .id(this.id)
                .menuName(this.menuName)
                .sortOrder(this.sortOrder)
                .role(this.role)
                .directUrl(this.directUrl)
                .visible(this.visible)
                .build();
    }
}
