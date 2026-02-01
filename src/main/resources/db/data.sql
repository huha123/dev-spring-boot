-- This file is executed on startup by Spring Boot.
-- The password for all users is '1234'.
-- It has been pre-hashed using BCrypt.
-- BCrypt hash for '1234': $2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQiy38J

INSERT INTO users (username, password, email, role) VALUES
('admin', '$2a$10$gPcAI0QFM1zvQFe2MXoEEuOuFFtk18tEsw3SkA4ZRl8cxkKE3vSlq', 'admin@example.com', 'ADMIN'),
('user', '$2a$10$gPcAI0QFM1zvQFe2MXoEEuOuFFtk18tEsw3SkA4ZRl8cxkKE3vSlq', 'user@example.com', 'USER'),
('manager', '$2a$10$gPcAI0QFM1zvQFe2MXoEEuOuFFtk18tEsw3SkA4ZRl8cxkKE3vSlq', 'manager@example.com', 'MANAGER'),
('manager1', '$2a$10$gPcAI0QFM1zvQFe2MXoEEuOuFFtk18tEsw3SkA4ZRl8cxkKE3vSlq', 'manager1@example.com', 'MANAGER_1');


-- 계층형 권한 DDL
INSERT INTO roles (id, role_name, parent_id) VALUES (1, 'ADMIN', NULL);
INSERT INTO roles (id, role_name, parent_id) VALUES (2, 'MANAGER', 1);
INSERT INTO roles (id, role_name, parent_id) VALUES (3, 'MANAGER_1', 2);
INSERT INTO roles (id, role_name, parent_id) VALUES (4, 'ETC', 1);

-- 계층형 메뉴 DDL (3개 메인 메뉴 + 하위 메뉴)
-- 1. 마이페이지 메뉴
INSERT INTO menu (id, menu_name, parent_id, role, path_url, visible, menu_type, sort_order) VALUES (10, '마이페이지', NULL, 'USER', '/mypage', true, 'board', 1);
INSERT INTO menu (id, menu_name, parent_id, role, path_url, visible, menu_type, sort_order) VALUES (11, '개인 정보', 10, 'USER', '/mypage/info', true, 'static', 1);
INSERT INTO menu (id, menu_name, parent_id, role, path_url, visible, menu_type, sort_order) VALUES (12, '비밀번호 변경', 10, 'USER', '/mypage/password', true, 'static', 2);

-- 2. 게시판 메뉴
INSERT INTO menu (id, menu_name, parent_id, role, path_url, visible, menu_type, sort_order) VALUES (20, '게시판', NULL, 'USER', '/board', true, 'board', 2);
INSERT INTO menu (id, menu_name, parent_id, role, path_url, visible, menu_type, sort_order) VALUES (21, '공지사항', 20, 'USER', '/board/notice', true, 'board', 1);
INSERT INTO menu (id, menu_name, parent_id, role, path_url, visible, menu_type, sort_order) VALUES (22, '자유게시판', 20, 'USER', '/board/free', false, 'board', 2);

-- 3. 설정 메뉴
INSERT INTO menu (id, menu_name, parent_id, role, path_url, visible, menu_type, sort_order) VALUES (30, '시스템 설정', NULL, 'ADMIN', '/system', false, 'static', 3);
INSERT INTO menu (id, menu_name, parent_id, role, path_url, visible, menu_type, sort_order) VALUES (31, '메뉴 관리', 30, 'ADMIN', '/system/menu', true, 'static', 1);
INSERT INTO menu (id, menu_name, parent_id, role, path_url, visible, menu_type, sort_order) VALUES (32, '권한 관리', 30, 'ADMIN', '/system/authority', false, 'static', 2);

-- categoryEntity 관련 DDL
INSERT INTO category (category_id, name, is_use) VALUES ('NOTICE', '공지사항', true);
INSERT INTO category (category_id, name, is_use) VALUES ('FREE', '자유게시판', true);
INSERT INTO category (category_id, name, is_use) VALUES ('QNA', 'Q&A', true);
INSERT INTO category (category_id, name, is_use) VALUES ('FAQ', 'FAQ', true);
INSERT INTO category (category_id, name, is_use) VALUES ('EVENT', '이벤트', false);
INSERT INTO category (category_id, name, is_use) VALUES ('HR', '인사관리', true);
