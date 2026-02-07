package dev.huha123.app.domain.board;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dev.huha123.app.domain.category.CategoryEntity;
import dev.huha123.app.domain.category.CategoryRepository;
import dev.huha123.app.domain.file.FileDto;
import dev.huha123.app.domain.file.FileEntity;
import dev.huha123.app.domain.file.FileService;
import dev.huha123.app.domain.like.LikeService;
import dev.huha123.app.domain.like.LikeType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final BoardCommentService boardCommentService;
    private final LikeService likeService;
    private final FileService fileService;

    @Transactional
    public BoardDto createBoard(BoardDto boardDto) {
        CategoryEntity category = categoryRepository.findById(boardDto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        BoardEntity board = boardDto.toEntity().toBuilder().category(category).build();
        return BoardDto.fromEntity(boardRepository.save(board));
    }

    public List<BoardDto> getAllBoards() {
        return boardRepository.findAll().stream().map(boardEntity -> {
            boardEntity.setLikeCount(likeService.getLikeCount(LikeType.BOARD, boardEntity.getId()));

            // 댓글도 함께 매핑
            List<BoardCommentDto> comments = boardEntity.getComments().stream()
                    .map(BoardCommentDto::fromEntity).collect(Collectors.toList());

            // 파일도 함께 매핑
            List<FileDto> files = boardEntity.getFiles().stream().map(FileDto::fromEntity)
                    .collect(Collectors.toList());

            return BoardDto.fromEntity(boardEntity).withComments(comments).withFiles(files);
        }).collect(Collectors.toList());
    }

    public BoardDto getBoardById(Long id, java.security.Principal principal) {
        BoardEntity boardEntity = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        boardEntity.setLikeCount(likeService.getLikeCount(LikeType.BOARD, boardEntity.getId()));
        if (principal != null) {
            boardEntity
                    .setLiked(likeService.isLiked(principal.getName(), LikeType.BOARD, boardEntity.getId()));
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

        board = boardDto.toEntity().toBuilder().id(id).category(category).build();

        return BoardDto.fromEntity(boardRepository.save(board));
    }

    @Transactional
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public BoardDto createBoardWithFiles(BoardDto boardDto, List<MultipartFile> files) {
        // 카테고리 조회
        CategoryEntity category = categoryRepository.findById(boardDto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // 파일 저장
        List<FileEntity> savedFiles = fileService.saveFiles(files);

        // 게시글 저장
        BoardEntity board = boardDto.toEntity().toBuilder().category(category).build();
        board.addFiles(savedFiles);
        BoardEntity savedBoard = boardRepository.save(board);

        return BoardDto.fromEntity(savedBoard).toBuilder().build();
    }

    @Transactional
    public BoardDto updateBoardWithFiles(Long id, BoardDto boardDto, List<MultipartFile> files) {
        // 기존 게시글 조회
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        // 카테고리 조회
        CategoryEntity category = categoryRepository.findById(boardDto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // 게시글 정보 업데이트
        board = boardDto.toEntity().toBuilder().id(id).category(category).build();
        BoardEntity savedBoard = boardRepository.save(board);

        // 기존 파일 삭제 후 새 파일 저장 (선택적으로 변경 가능)
        if (files != null && !files.isEmpty()) {
            fileService.deleteFilesByBoardId(id);
            fileService.saveFiles(files, savedBoard);
        }

        return BoardDto.fromEntity(savedBoard).toBuilder()
                .files(fileService.getFilesByBoardId(savedBoard.getId())).build();
    }

}
