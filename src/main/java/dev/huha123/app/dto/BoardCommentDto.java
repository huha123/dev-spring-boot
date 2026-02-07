package dev.huha123.app.dto;

import dev.huha123.app.entity.BoardCommentEntity;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record BoardCommentDto(
        Long id,
        String content,
        String writer,
        Long boardId,
        long likeCount,
        boolean liked) {

    public static BoardCommentDto fromEntity(BoardCommentEntity entity) {
        return BoardCommentDto.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .boardId(entity.getBoard().getId())
                .likeCount(entity.getLikeCount())
                .liked(entity.isLiked())
                .build();
    }

    public BoardCommentEntity toEntity() {
        return BoardCommentEntity.builder()
                .id(id())
                .content(content())
                .writer(writer())
                .likeCount(likeCount())
                .build();
    }

}
