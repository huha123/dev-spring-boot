package dev.huha123.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.huha123.app.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndPassword(String email, String password);
}
