-- This file is executed on startup by Spring Boot.
-- The password for all users is '1234'.
-- It has been pre-hashed using BCrypt.
-- BCrypt hash for '1234': $2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQiy38J

INSERT INTO users (username, password, email, role) VALUES
('admin', '$2a$10$gPcAI0QFM1zvQFe2MXoEEuOuFFtk18tEsw3SkA4ZRl8cxkKE3vSlq', 'admin@example.com', 'ROLE_ADMIN'),
('user', '$2a$10$gPcAI0QFM1zvQFe2MXoEEuOuFFtk18tEsw3SkA4ZRl8cxkKE3vSlq', 'user@example.com', 'ROLE_USER'),
('manager', '$2a$10$gPcAI0QFM1zvQFe2MXoEEuOuFFtk18tEsw3SkA4ZRl8cxkKE3vSlq', 'manager@example.com', 'ROLE_MANAGER');
