package dev.huha123.app.domain.board;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.huha123.app.domain.like.LikeService;
import dev.huha123.app.domain.like.LikeType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;
    private final LikeService likeService;

    @Transactional
    public BoardCommentDto createComment(BoardCommentDto commentDto) {
        BoardEntity board = boardRepository.findById(commentDto.boardId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        BoardCommentEntity comment = BoardCommentEntity.builder()
                .content(commentDto.content())
                .writer(commentDto.writer())
                .board(board)
                .build();
        return BoardCommentDto.fromEntity(boardCommentRepository.save(comment));
    }

    public List<BoardCommentDto> getCommentsByBoardId(Long boardId, java.security.Principal principal) {
        return boardCommentRepository.findByBoardId(boardId).stream()
                .map(commentEntity -> {
                    commentEntity.setLikeCount(likeService.getLikeCount(LikeType.BOARD_COMMENT, commentEntity.getId()));
                    if (principal != null) {
                        commentEntity.setLiked(likeService.isLiked(principal.getName(), LikeType.BOARD_COMMENT, commentEntity.getId()));
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
                .content(commentDto.content())
                .build();

        return BoardCommentDto.fromEntity(boardCommentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Long id) {
        boardCommentRepository.deleteById(id);
    }
}
