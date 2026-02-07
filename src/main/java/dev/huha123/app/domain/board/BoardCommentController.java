package dev.huha123.app.domain.board;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<BoardCommentDto> createComment(@PathVariable("boardId") Long boardId, @RequestBody BoardCommentDto commentDto) {
        commentDto = commentDto.withBoardId(boardId);
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
