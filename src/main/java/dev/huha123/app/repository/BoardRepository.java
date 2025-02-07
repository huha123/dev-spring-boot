package dev.huha123.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.huha123.app.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @EntityGraph(attributePaths = { "member" })
    Page<Board> findAll(Pageable pageable);
}
