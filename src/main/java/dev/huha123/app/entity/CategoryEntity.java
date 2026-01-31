package dev.huha123.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryEntity {

    @Id
    @Column(name = "category_id", length = 20)
    private String id; // 예: "NOTICE", "QNA" (직관적인 문자열 PK 추천)

    @Column(nullable = false)
    private String name; // 예: "공지사항"

    private boolean isUse; // 사용 여부


}
