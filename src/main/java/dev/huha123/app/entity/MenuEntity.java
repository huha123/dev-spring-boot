package dev.huha123.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuName; // 메뉴명

    private int sortOrder; // 순서

    // 1. 계층형 구조 (상위 메뉴)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private MenuEntity parent;

    // 3. 권한 (이 메뉴는 누구에게 보일까?)
    private String role; // "ROLE_ADMIN", "ROLE_USER"

    // 4. 게시판이 아닌 일반 링크일 경우를 대비
    private String directUrl;

    private boolean visible; // 메뉴 표시 여부

}