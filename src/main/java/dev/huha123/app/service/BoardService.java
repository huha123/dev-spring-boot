package dev.huha123.app.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import dev.huha123.app.entity.Board;
import dev.huha123.app.repository.BoardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository repository;

    public ResponseEntity<?> list(Pageable pageable) {
        return ResponseEntity.ok(repository.findAll(pageable));
    }

    public ResponseEntity<?> insert(@NonNull Board board) {
        return ResponseEntity.ok(repository.save(board));
    }

    public ResponseEntity<?> update(@NonNull Board board) {
        if (repository.findById(board.getId()).isPresent()) {
            return ResponseEntity.ok(repository.save(board));

        } else {
            return ResponseEntity.ok("no data");
        }
    }

    public ResponseEntity<?> delete(@NonNull Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok(id);
    }
}