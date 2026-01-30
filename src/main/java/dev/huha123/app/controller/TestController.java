package dev.huha123.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/user")
    public ResponseEntity<String> userAccess() {
        return ResponseEntity.ok("User Access: 인증된 모든 사용자(USER, MANAGER, ADMIN) 접근 가능");
    }

    @GetMapping("/manager")
    public ResponseEntity<String> managerAccess() {
        return ResponseEntity.ok("Manager Access: MANAGER, ADMIN 권한 접근 가능");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminAccess() {
        return ResponseEntity.ok("Admin Access: ADMIN 권한만 접근 가능");
    }
}