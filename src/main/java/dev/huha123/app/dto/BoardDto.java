package dev.huha123.app.dto;

import dev.huha123.app.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private String categoryId;
    private Integer viewCount;
    private boolean isVisible;
    private boolean isNotice;
    private boolean isSecret;
    private long likeCount;
    private boolean liked;
    private List<BoardCommentDto> comments;

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
}
