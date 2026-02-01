package dev.huha123.app.dto;

import dev.huha123.app.entity.BoardCommentEntity;
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
public class BoardCommentDto {

    private Long id;
    private String content;
    private String writer;
    private Long boardId;

    public static BoardCommentDto fromEntity(BoardCommentEntity entity) {
        return BoardCommentDto.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .boardId(entity.getBoard().getId())
                .build();
    }
}
