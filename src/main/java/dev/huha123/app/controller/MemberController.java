package dev.huha123.app.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.huha123.app.entity.Member;
import dev.huha123.app.service.MemberService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(path = { "member" })
@RequiredArgsConstructor
public class MemberController {
    private final MemberService service;

    @GetMapping
    public ResponseEntity<?> list(
            @PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "id") Pageable pageable) {
        return service.list(pageable);
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> insert(@RequestBody @NonNull Member member) {
        return service.insert(member);
    }

    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> update(@RequestBody @NonNull Member member) {
        return service.update(member);
    }

    @DeleteMapping(path = { "{id}" })
    public ResponseEntity<?> delete(@PathVariable(name = "id") @NonNull Long id) {
        return service.delete(id);
    }

    @PostMapping(path = { "login" })
    public ResponseEntity<?> login(@RequestBody @NonNull Member member) {
        return service.login(member);
    }
}
