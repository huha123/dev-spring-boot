package dev.huha123.app.dto;

import dev.huha123.app.entity.CategoryEntity;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record CategoryDto(
        String id,
        String name,
        boolean isUse) {

    public static CategoryDto fromEntity(CategoryEntity entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .isUse(entity.isUse())
                .build();
    }

    public CategoryEntity toEntity() {
        return CategoryEntity.builder()
                .id(id())
                .name(name())
                .isUse(isUse())
                .build();
    }
}
