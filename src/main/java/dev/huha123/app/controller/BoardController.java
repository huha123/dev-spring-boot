package dev.huha123.app.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.huha123.app.entity.Board;
import dev.huha123.app.service.BoardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = { "board" })
@RequiredArgsConstructor
// @PreAuthorize("hasRole('ADMIN')")
// @PreAuthorize("hasRole('GUEST')")
@PreAuthorize("hasAuthority('ROLE_USER')")
// @PreAuthorize("hasAuthority('ROLE_TEMPORARY_USER')")
// @PreAuthorize("hasAuthority('AUTH_UPDATE')")
public class BoardController {
    private final BoardService service;

    @GetMapping
    public ResponseEntity<?> list(
            @PageableDefault(page = 0, size = 100, direction = Direction.DESC) Pageable pageable) {
        return service.list(pageable);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insert(@RequestBody @NonNull Board board) {
        return service.insert(board);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody @NonNull Board board) {
        return service.update(board);
    }

    @DeleteMapping(path = { "{id}" })
    public ResponseEntity<?> delete(@PathVariable(name = "id") @NonNull Long id) {
        return service.delete(id);
    }
}
