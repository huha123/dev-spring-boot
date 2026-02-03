package dev.huha123.app.service;

import dev.huha123.app.dto.BoardCommentDto;
import dev.huha123.app.entity.BoardCommentEntity;
import dev.huha123.app.entity.BoardEntity;
import dev.huha123.app.repository.BoardCommentRepository;
import dev.huha123.app.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;
    private final dev.huha123.app.like.LikeService likeService;

    @Transactional
    public BoardCommentDto createComment(BoardCommentDto commentDto) {
        BoardEntity board = boardRepository.findById(commentDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        BoardCommentEntity comment = BoardCommentEntity.builder()
                .content(commentDto.getContent())
                .writer(commentDto.getWriter())
                .board(board)
                .build();

        return BoardCommentDto.fromEntity(boardCommentRepository.save(comment));
    }

    public List<BoardCommentDto> getCommentsByBoardId(Long boardId, java.security.Principal principal) {
        return boardCommentRepository.findByBoardId(boardId).stream()
                .map(commentEntity -> {
                    commentEntity.setLikeCount(likeService.getLikeCount(dev.huha123.app.like.LikeType.BOARD_COMMENT, commentEntity.getId()));
                    if (principal != null) {
                        commentEntity.setLiked(likeService.isLiked(principal.getName(), dev.huha123.app.like.LikeType.BOARD_COMMENT, commentEntity.getId()));
                    }
                    return BoardCommentDto.fromEntity(commentEntity);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardCommentDto updateComment(Long id, BoardCommentDto commentDto) {
        BoardCommentEntity comment = boardCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        comment = comment.toBuilder()
                .content(commentDto.getContent())
                .build();

        return BoardCommentDto.fromEntity(boardCommentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Long id) {
        boardCommentRepository.deleteById(id);
    }
}
