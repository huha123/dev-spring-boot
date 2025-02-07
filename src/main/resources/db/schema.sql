CREATE TABLE IF NOT EXISTS `member`
(
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `phone_number` VARCHAR(255) NOT NULL,
    `reg_date` TIMESTAMP DEFAULT NOW(),
    `mod_date` TIMESTAMP DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX (`email`)
);

CREATE TABLE IF NOT EXISTS `board`
(
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `member_id` INTEGER DEFAULT NULL ,
    `title` VARCHAR(255) NOT NULL,
    `content` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `reg_date` TIMESTAMP DEFAULT NOW(),
    `mod_date` TIMESTAMP DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `role` (
    `id` BIGINT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `privilege` (
    `id` BIGINT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `member_role` (
    `member_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`member_id`, `role_id`)
);

CREATE TABLE IF NOT EXISTS `role_privilege` (
    `role_id` BIGINT NOT NULL,
    `privilege_id` BIGINT NOT NULL,
    PRIMARY KEY (`role_id`, `privilege_id`)
);