package dev.huha123.app.domain.file;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    void deleteByBoardId(Long boardId);
    List<FileEntity> findByBoardId(Long boardId);
}
