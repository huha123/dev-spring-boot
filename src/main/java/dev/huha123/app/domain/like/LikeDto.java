package dev.huha123.app.domain.like;

import lombok.Builder;

@Builder(toBuilder = true)
public record LikeDto(
        Long id,
        String userId,
        LikeType likeType,
        Long contentId
) {
    public static LikeDto fromEntity(LikeEntity entity) {
        return LikeDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .likeType(entity.getLikeType())
                .contentId(entity.getContentId())
                .build();
    }

    public LikeEntity toEntity() {
        return LikeEntity.builder()
                .id(id())
                .userId(userId())
                .likeType(likeType())
                .contentId(contentId())
                .build();
    }
}
