DROP DATABASE IF EXISTS wuthering_waves_analyse;
CREATE DATABASE wuthering_waves_analyse;
USE wuthering_waves_analyse;

DROP TABLE IF EXISTS convene_item;
CREATE TABLE convene_item
(
    id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cardPoolType VARCHAR(255) NOT NULL,
    resourceId   BIGINT       NOT NULL,
    qualityLevel BIGINT       NOT NULL,
    resourceType VARCHAR(255) NOT NULL,
    name         VARCHAR(255) NOT NULL,
    count        BIGINT       NOT NULL,
    time         DATETIME     NOT NULL,
    timeKey      BIGINT       NOT NULL,
    playerId     BIGINT       NOT NULL,
    UNIQUE (timeKey, playerId)
);

DROP TABLE IF EXISTS rare_convene_item;
CREATE TABLE rare_convene_item
(
    id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cardPoolType VARCHAR(255) NOT NULL,
    resourceType VARCHAR(255) NOT NULL,
    name         VARCHAR(255) NOT NULL,
    cost         INT          NOT NULL,
    target       BOOLEAN      NOT NULL,
    time         DATETIME     NOT NULL,
    timeKey      BIGINT       NOT NULL,
    playerId     BIGINT       NOT NULL,
    UNIQUE (timeKey, playerId)
);

DROP TABLE IF EXISTS convene_summary;
CREATE TABLE convene_summary
(
    id               BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    playerId         BIGINT NOT NULL UNIQUE,
    cardPoolCost     JSON,
    cardPoolGet      JSON,
    cardPoolTarget   JSON,
    cardPoolPrepared JSON
);

DROP TABLE IF EXISTS card_pool_up_item;
CREATE TABLE card_pool_up_item
(
    id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    resourceType VARCHAR(255) NOT NULL,
    name         VARCHAR(255) NOT NULL,
    startTime    DATETIME     NOT NULL,
    endTime      DATETIME     NOT NULL

);
INSERT INTO card_pool_up_item(resourceType, name, startTime, endTime)
VALUES
#开服时间为2024-05-23 10:00:00
('角色', '忌炎', '2024-05-23 08:00:00', '2024-06-13 09:59:59'),
('武器', '苍鳞千嶂', '2024-05-23 08:00:00', '2024-06-13 09:59:59'),
('角色', '吟霖', '2024-06-13 10:00:00', '2024-07-02 11:59:59'),
('武器', '掣傀之手', '2024-06-13 10:00:00', '2024-07-02 11:59:59'),
#1.1版本更新自2024-06-28 06:00:00开始
('角色', '今汐', '2024-06-28 06:00:00', '2024-07-22 09:59:59'),
('武器', '时和岁稔', '2024-06-28 06:00:00', '2024-07-22 09:59:59'),
('角色', '长离', '2024-07-22 10:00:00', '2024-08-14 11:59:59'),
('武器', '赫奕流明', '2024-07-22 10:00:00', '2024-08-14 11:59:59'),
#1.2版本更新自2024-08-15 06:00:00开始
('角色', '折枝', '2024-08-15 06:00:00', '2024-09-07 09:59:59'),
('武器', '琼枝冰绡', '2024-08-15 06:00:00', '2024-09-07 09:59:59'),
('角色', '相里要', '2024-09-07 10:00:00', '2024-09-28 11:59:59'),
('武器', '诸方玄枢', '2024-09-07 10:00:00', '2024-09-28 11:59:59'),
#1.3版本更新自2024-09-29 04:00:00开始
('角色', '守岸人', '2024-09-29 04:00:00', '2024-10-24 09:59:59'),
('武器', '星序协响', '2024-09-29 04:00:00', '2024-10-24 09:59:59'),
('角色', '忌炎', '2024-10-24 10:00:00', '2024-11-13 11:59:59'),
('武器', '苍鳞千嶂', '2024-10-24 10:00:00', '2024-11-13 11:59:59'),
#1.4版本更新自2024-11-14 04:00:00开始
('角色', '椿', '2024-11-14 04:00:00', '2024-12-12 09:59:59'),
('武器', '裁春', '2024-11-14 04:00:00', '2024-12-12 09:59:59'),
('角色', '吟霖', '2024-12-12 10:00:00', '2025-01-01 11:59:59'),
('角色', '相里要', '2024-12-12 10:00:00', '2025-01-01 11:59:59'),
('武器', '掣傀之手', '2024-12-12 10:00:00', '2025-01-01 11:59:59'),
('武器', '诸方玄枢', '2024-12-12 10:00:00', '2025-01-01 11:59:59');

DROP TABLE IF EXISTS role;
CREATE TABLE role
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    playerId   BIGINT       NOT NULL UNIQUE,
    roleName   VARCHAR(255) NOT NULL,
    cardPoolId VARCHAR(255),
    serverId   VARCHAR(255),
    recordId   VARCHAR(255)
);

select *
from convene_item;

select *
from rare_convene_item;

select *
from convene_summary;