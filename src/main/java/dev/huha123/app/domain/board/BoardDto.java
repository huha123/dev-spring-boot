package dev.huha123.app.domain.board;

import java.util.List;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record BoardDto(
    Long id,
    String title,
    String content,
    String writer,
    String categoryId,
    Integer viewCount,
    boolean isVisible,
    boolean isNotice,
    boolean isSecret,
    long likeCount,
    boolean liked,
    List<BoardCommentDto> comments
) {

    public static BoardDto fromEntity(BoardEntity entity) {
        return BoardDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .categoryId(entity.getCategory().getId())
                .viewCount(entity.getViewCount())
                .isVisible(entity.isVisible())
                .isNotice(entity.isNotice())
                .isSecret(entity.isSecret())
                .likeCount(entity.getLikeCount())
                .liked(entity.isLiked())
                .build();
    }

    public BoardEntity toEntity() {
        return BoardEntity.builder()
                .id(id())
                .title(title())
                .content(content())
                .writer(writer())
                .viewCount(viewCount())
                .isVisible(isVisible())
                .isNotice(isNotice())
                .isSecret(isSecret())
                .build();
    }
}
