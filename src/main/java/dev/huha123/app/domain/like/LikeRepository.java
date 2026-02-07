package dev.huha123.app.domain.like;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByUserIdAndLikeTypeAndContentId(String userId, LikeType likeType, Long contentId);

    long countByLikeTypeAndContentId(LikeType likeType, Long contentId);
}
