package dev.huha123.app.service;

import dev.huha123.app.dto.BoardDto;
import dev.huha123.app.entity.BoardEntity;
import dev.huha123.app.entity.CategoryEntity;
import dev.huha123.app.repository.BoardRepository;
import dev.huha123.app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final BoardCommentService boardCommentService;
    private final dev.huha123.app.like.LikeService likeService;

    @Transactional
    public BoardDto createBoard(BoardDto boardDto) {
        CategoryEntity category = categoryRepository.findById(boardDto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        BoardEntity board = BoardEntity.builder()
                .title(boardDto.title())
                .content(boardDto.content())
                .writer(boardDto.writer())
                .category(category)
                .viewCount(boardDto.viewCount())
                .isVisible(boardDto.isVisible())
                .isNotice(boardDto.isNotice())
                .isSecret(boardDto.isSecret())
                .build();

        return BoardDto.fromEntity(boardRepository.save(board));
    }

    public List<BoardDto> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(boardEntity -> {
                    boardEntity.setLikeCount(likeService.getLikeCount(dev.huha123.app.like.LikeType.BOARD, boardEntity.getId()));
                    return BoardDto.fromEntity(boardEntity);
                })
                .collect(Collectors.toList());
    }

    public BoardDto getBoardById(Long id, java.security.Principal principal) {
        BoardEntity boardEntity = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        boardEntity.setLikeCount(likeService.getLikeCount(dev.huha123.app.like.LikeType.BOARD, boardEntity.getId()));
        if (principal != null) {
            boardEntity.setLiked(likeService.isLiked(principal.getName(), dev.huha123.app.like.LikeType.BOARD, boardEntity.getId()));
        }

        BoardDto boardDto = BoardDto.fromEntity(boardEntity);
        boardDto = boardDto.withComments(boardCommentService.getCommentsByBoardId(id, principal));
        return boardDto;
    }

    @Transactional
    public BoardDto updateBoard(Long id, BoardDto boardDto) {
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        CategoryEntity category = categoryRepository.findById(boardDto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        board = board.toBuilder()
                .title(boardDto.title())
                .content(boardDto.content())
                .category(category)
                .isVisible(boardDto.isVisible())
                .isNotice(boardDto.isNotice())
                .isSecret(boardDto.isSecret())
                .build();

        return BoardDto.fromEntity(boardRepository.save(board));
    }

    @Transactional
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }
}
