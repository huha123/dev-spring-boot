package dev.huha123.app.controller;

import dev.huha123.app.dto.BoardCommentDto;
import dev.huha123.app.service.BoardCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<BoardCommentDto> createComment(@PathVariable("boardId") Long boardId, @RequestBody BoardCommentDto commentDto) {
        commentDto.setBoardId(boardId);
        return ResponseEntity.ok(boardCommentService.createComment(commentDto));
    }

    @GetMapping("/boards/{boardId}/comments")
    public ResponseEntity<List<BoardCommentDto>> getCommentsByBoardId(@PathVariable("boardId") Long boardId, java.security.Principal principal) {
        return ResponseEntity.ok(boardCommentService.getCommentsByBoardId(boardId, principal));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<BoardCommentDto> updateComment(@PathVariable("id") Long id, @RequestBody BoardCommentDto commentDto) {
        return ResponseEntity.ok(boardCommentService.updateComment(id, commentDto));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id) {
        boardCommentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
