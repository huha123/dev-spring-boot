package dev.huha123.app.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.huha123.app.repository.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivilegeService {
    private final PrivilegeRepository repository;

    public ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseEntity.ok(repository.findAll(pageable));
    }

    public ResponseEntity<?> findById(Long id) {
        return ResponseEntity.ok(repository.findById(id).get());
    }

}
