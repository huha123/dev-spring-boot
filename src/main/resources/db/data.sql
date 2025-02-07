
INSERT INTO `member` VALUES (1, 'admin', 'admin@gmail.com', '{bcrypt}$2a$10$oDTdFtjc8EngFXk8zBrR4ecO17/PDz60C8XtkVczmfI.wN57e21Be', '01099998888', now(), null);
INSERT INTO `member` VALUES (2, 'user1', 'user1@gmail.com', '{bcrypt}$2a$10$oDTdFtjc8EngFXk8zBrR4ecO17/PDz60C8XtkVczmfI.wN57e21Be', '01099998888', now(), null);
INSERT INTO `member` VALUES (3, 'user2', 'user2@gmail.com', '{bcrypt}$2a$10$oDTdFtjc8EngFXk8zBrR4ecO17/PDz60C8XtkVczmfI.wN57e21Be', '01099998888', now(), null);
INSERT INTO `member` VALUES (4, 'user3', 'user3@gmail.com', '{bcrypt}$2a$10$oDTdFtjc8EngFXk8zBrR4ecO17/PDz60C8XtkVczmfI.wN57e21Be', '01099998888', now(), null);

-- INSERT INTO `member` VALUES (1, 'admin', 'admin@gmail.com', '1', '01099998888', now(), null);
-- INSERT INTO `member` VALUES (2, 'user1', 'user1@gmail.com', '1', '01099998888', now(), null);
-- INSERT INTO `member` VALUES (3, 'user2', 'user2@gmail.com', '1', '01099998888', now(), null);
-- INSERT INTO `member` VALUES (4, 'user3', 'user3@gmail.com', '1', '01099998888', now(), null);

INSERT INTO `board` values (1, 1, 'title admin', 'content admin', '1234', now(), null);
INSERT INTO `board` values (2, 2, 'title 1', 'content 1', '1234', now(), null);
INSERT INTO `board` values (3, 3, 'title 2', 'content 2', '1234', now(), null);
INSERT INTO `board` values (4, 4, 'title 3', 'content 3', '1234', now(), null);

INSERT INTO `role` VALUES (1, 'ROLE_ADMIN');
INSERT INTO `role` VALUES (2, 'ROLE_USER');
INSERT INTO `role` VALUES (3, 'ROLE_TEMPORARY_USER');
INSERT INTO `role` VALUES (4, 'ROLE_GUEST');

INSERT INTO `privilege` VALUES (1, 'AUTH_READ');
INSERT INTO `privilege` VALUES (2, 'AUTH_WRITE');
INSERT INTO `privilege` VALUES (3, 'AUTH_UPDATE');
INSERT INTO `privilege` VALUES (4, 'AUTH_DELETE');

INSERT INTO `member_role` VALUES (1, 1);
INSERT INTO `member_role` VALUES (2, 2);
INSERT INTO `member_role` VALUES (3, 3);
INSERT INTO `member_role` VALUES (4, 4);

INSERT INTO `role_privilege` VALUES (1, 1);
INSERT INTO `role_privilege` VALUES (1, 2);
INSERT INTO `role_privilege` VALUES (1, 3);
INSERT INTO `role_privilege` VALUES (1, 4);
INSERT INTO `role_privilege` VALUES (2, 1);
INSERT INTO `role_privilege` VALUES (2, 2);
INSERT INTO `role_privilege` VALUES (2, 3);
INSERT INTO `role_privilege` VALUES (3, 1);
INSERT INTO `role_privilege` VALUES (3, 2);
INSERT INTO `role_privilege` VALUES (4, 1);