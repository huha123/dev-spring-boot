package dev.huha123.app.domain.board;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCommentRepository extends JpaRepository<BoardCommentEntity, Long> {

    List<BoardCommentEntity> findByBoardId(Long boardId);
}
