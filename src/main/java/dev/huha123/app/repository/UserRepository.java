package dev.huha123.app.repository;

import dev.huha123.app.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * 사용자 이름으로 사용자를 조회합니다.
     * @param username 사용자 이름
     * @return Optional<UserEntity>
     */
    Optional<UserEntity> findByUsername(String username);
}
