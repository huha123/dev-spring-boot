package dev.huha123.app.like;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/board/{boardId}")
    public ResponseEntity<Void> likeBoard(@PathVariable("boardId") Long boardId, Principal principal) {
        likeService.like(principal.getName(), LikeType.BOARD, boardId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/board-comment/{commentId}")
    public ResponseEntity<Void> likeBoardComment(@PathVariable("commentId") Long commentId, Principal principal) {
        likeService.like(principal.getName(), LikeType.BOARD_COMMENT, commentId);
        return ResponseEntity.ok().build();
    }
}
