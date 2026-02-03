package dev.huha123.app.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    @Transactional
    public void like(String username, LikeType likeType, Long contentId) {
        likeRepository.findByUserIdAndLikeTypeAndContentId(username, likeType, contentId)
                .ifPresentOrElse(
                        likeRepository::delete,
                        () -> {
                            LikeEntity like = LikeEntity.builder()
                                    .userId(username)
                                    .likeType(likeType)
                                    .contentId(contentId)
                                    .build();
                            likeRepository.save(like);
                        });
    }

    @Transactional(readOnly = true)
    public long getLikeCount(LikeType likeType, Long contentId) {
        return likeRepository.countByLikeTypeAndContentId(likeType, contentId);
    }

    @Transactional(readOnly = true)
    public boolean isLiked(String username, LikeType likeType, Long contentId) {
        if (username == null) {
            return false;
        }
        return likeRepository.findByUserIdAndLikeTypeAndContentId(username, likeType, contentId).isPresent();
    }
}
