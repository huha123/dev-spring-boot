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

    @Transactional
    public BoardDto createBoard(BoardDto boardDto) {
        CategoryEntity category = categoryRepository.findById(boardDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        BoardEntity board = BoardEntity.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .writer(boardDto.getWriter())
                .category(category)
                .viewCount(boardDto.getViewCount())
                .isVisible(boardDto.isVisible())
                .isNotice(boardDto.isNotice())
                .isSecret(boardDto.isSecret())
                .build();

        return BoardDto.fromEntity(boardRepository.save(board));
    }

    public List<BoardDto> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(BoardDto::fromEntity)
                .collect(Collectors.toList());
    }

    public BoardDto getBoardById(Long id) {
        BoardDto boardDto = boardRepository.findById(id)
                .map(BoardDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        boardDto.setComments(boardCommentService.getCommentsByBoardId(id));
        return boardDto;
    }

    @Transactional
    public BoardDto updateBoard(Long id, BoardDto boardDto) {
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        CategoryEntity category = categoryRepository.findById(boardDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        board = board.toBuilder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
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
