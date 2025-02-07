package dev.huha123.app.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.huha123.app.service.RoleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = { "role" })
@RequiredArgsConstructor
public class RoleController {
    private final RoleService service;

    @GetMapping
    public ResponseEntity<?> list(
            @PageableDefault(size = 100, direction = Direction.ASC, sort = "id") Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping(path = { "{id}" })
    public ResponseEntity<?> findById(@PathVariable(name = "id") Long id) {
        return service.findById(id);
    }

}
