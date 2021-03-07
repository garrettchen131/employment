DROP DATABASE IF EXISTS `db_employment`;
CREATE DATABASE IF NOT EXISTS `db_employment`;
USE `db_employment`;
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`(
     `id` BIGINT(20) UNSIGNED AUTO_INCREMENT COMMENT '用户ID',
     `username` VARCHAR(256) NOT NULL COMMENT '用户名',
     `password` VARCHAR(256) NOT NULL COMMENT '密码',
     `mobile` VARCHAR (16) NOT NULL COMMENT '电话',
     `email` VARCHAR(32) NOT NULL COMMENT '用户邮箱',
     `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间',
     `status` TINYINT(1) DEFAULT 0 COMMENT '是否首次登陆，1：true、0：false',
     `account_non_expired` TINYINT(1) DEFAULT 1 COMMENT 'spring security验证用户是否过期，默认true未过期',
     `account_non_locked` TINYINT(1) DEFAULT 1 COMMENT 'spring security验证用户是否被锁，默认true未锁',
     `credentials_non_expired` TINYINT(1) DEFAULT 1 COMMENT 'spring security验证用户密码是否过期，默认true未过期',
     `enabled` TINYINT(1) DEFAULT 1 COMMENT 'spring security验证用户是否激活',
     PRIMARY KEY (`id`),
     UNIQUE KEY (`username`),
     UNIQUE KEY (`mobile`),
     UNIQUE KEY (`email`)
)ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT '用户权限表';

INSERT INTO `t_user`(`username`, `password`, `mobile`, `email`)
VALUES ('zxc', '$2a$10$SrgyJaiEzALT4ey4qf0Ws.NBGujT6Heamm287tMyo6XFobci9mtVa', '15388327166', 'zxc@qq.com');


DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`
(
    `id`        BIGINT(20) UNSIGNED AUTO_INCREMENT COMMENT '角色ID',
    `authority` VARCHAR(16) NOT NULL COMMENT '权限名称',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`authority`)
)ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT '角色名称表';

INSERT INTO `t_role`(`authority`)
VALUES ('ROLE_PERSON'),
       ('ROLE_ENTERPRISE');


DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`(
    `user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'user表主键',
    `role_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'role表主键',
    PRIMARY KEY (`user_id`, `role_id`),
    CONSTRAINT `fk_t_user_role_user_id_t_user_id` FOREIGN KEY (`user_id`) REFERENCES `t_user`(`id`),
    CONSTRAINT `fk_t_user_role_role_id_t_role_id` FOREIGN KEY (`role_id`) REFERENCES `t_role`(`id`)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '用户角色关联表';

INSERT INTO `t_user_role`
VALUES (1, 1),
       (1, 2);

DROP TABLE IF EXISTS `t_user_info`;
CREATE TABLE `t_user_info`(
    `user_id` BIGINT (20) NOT NULL COMMENT '用户ID即主键',
    `person_name` VARCHAR (50) DEFAULT '' COMMENT '姓名',
    `birth_day` DATE DEFAULT NULL COMMENT '出生年月',
    `address` VARCHAR (128) DEFAULT '' COMMENT '所在地址',
    `home_address` VARCHAR (128) DEFAULT '' COMMENT '户籍地址',
    `education` VARCHAR(50)	DEFAULT '' COMMENT '学历',
    `major` VARCHAR(128) DEFAULT '' COMMENT '专业',
    `certificate`   VARCHAR(256)    DEFAULT '' COMMENT '证书',
    `school` VARCHAR(128)   DEFAULT '' COMMENT '毕业院校',
    `start_year` VARCHAR (5) DEFAULT '' COMMENT '入学年份',
    `start_month` VARCHAR (2) DEFAULT '' COMMENT '入学月份',
    `end_year` VARCHAR (5) DEFAULT '' COMMENT '毕业年份',
    `end_month` VARCHAR (2) DEFAULT '' COMMENT '毕业月份',
    `training`	VARCHAR (255)	DEFAULT '' COMMENT '培训经历',
    `skill`	VARCHAR (255)	DEFAULT '' COMMENT '技能',
    `com_name`	VARCHAR (255)	DEFAULT '' COMMENT '所在公司',
    `position` VARCHAR (255)	DEFAULT '' COMMENT '职位',
    `work_start_year` VARCHAR (5)	DEFAULT '' COMMENT '入职年月',
    `work_start_month` VARCHAR (2)	DEFAULT '' COMMENT '入职月月',
    `work_end_year` VARCHAR (5)	DEFAULT '' COMMENT '离职年月',
    `work_end_month`	VARCHAR (2) DEFAULT '' COMMENT '离职月月',
    `work_content`	VARCHAR(255)	DEFAULT '' COMMENT '工作内容',
    `work_character` VARCHAR(255)	DEFAULT '' COMMENT '工作性质',
    `ex_status`	VARCHAR(128)	DEFAULT '' COMMENT '求职状态',
    `ex_salary`	INT(10)	DEFAULT 0 COMMENT '期望月薪',
    `ex_address` VARCHAR(64) DEFAULT '' COMMENT '期望地点',
    `ex_position` VARCHAR(256) DEFAULT '' COMMENT '期望职位',
    `project_ex` VARCHAR (256) DEFAULT '' COMMENT '项目经历',
    `gender` VARCHAR(4)	DEFAULT '' COMMENT '性别',
    `signature` VARCHAR(255) DEFAULT '' COMMENT '个性签名',
    PRIMARY KEY (`user_id`)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '用户信息表';

