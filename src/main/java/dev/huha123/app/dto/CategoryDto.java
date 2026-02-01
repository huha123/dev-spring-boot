package dev.huha123.app.dto;

import dev.huha123.app.entity.CategoryEntity;
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
public class CategoryDto {

    private String id;
    private String name;
    private boolean isUse;

    public static CategoryDto fromEntity(CategoryEntity entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .isUse(entity.isUse())
                .build();
    }

    public CategoryEntity toEntity() {
        return CategoryEntity.builder()
                .id(this.id)
                .name(this.name)
                .isUse(this.isUse)
                .build();
    }
}
