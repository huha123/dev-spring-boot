-- This file is executed on startup by Spring Boot.
-- The password for all users is '1234'.
-- It has been pre-hashed using BCrypt.
-- BCrypt hash for '1234': $2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQiy38J

INSERT INTO users (username, password, email, role) VALUES
('admin', '$2a$10$gPcAI0QFM1zvQFe2MXoEEuOuFFtk18tEsw3SkA4ZRl8cxkKE3vSlq', 'admin@example.com', 'ADMIN'),
('user', '$2a$10$gPcAI0QFM1zvQFe2MXoEEuOuFFtk18tEsw3SkA4ZRl8cxkKE3vSlq', 'user@example.com', 'USER'),
('manager', '$2a$10$gPcAI0QFM1zvQFe2MXoEEuOuFFtk18tEsw3SkA4ZRl8cxkKE3vSlq', 'manager@example.com', 'MANAGER'),
('manager1', '$2a$10$gPcAI0QFM1zvQFe2MXoEEuOuFFtk18tEsw3SkA4ZRl8cxkKE3vSlq', 'manager1@example.com', 'MANAGER_1');



INSERT INTO roles (id, role_name, parent_id) VALUES (1, 'ADMIN', NULL);
INSERT INTO roles (id, role_name, parent_id) VALUES (2, 'MANAGER', 1);
INSERT INTO roles (id, role_name, parent_id) VALUES (3, 'MANAGER_1', 2);
INSERT INTO roles (id, role_name, parent_id) VALUES (4, 'ETC', 1);
-- INSERT INTO roles (id, role_name, parent_id) VALUES (5, 'USER', 2);
--