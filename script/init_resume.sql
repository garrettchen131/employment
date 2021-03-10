# DROP DATABASE IF EXISTS `db_employment`;
# CREATE DATABASE IF NOT EXISTS `db_employment`;
# USE `db_employment`;

DROP TABLE IF EXISTS `t_user_resume`;
CREATE TABLE `t_user_resume`(
                              `id` BIGINT(20) NOT NULL COMMENT '简历id',
                              `user_id` BIGINT (20) NOT NULL COMMENT '绑定用户id',
                              `comments` VARCHAR(255) DEFAULT '' COMMENT '简历备注',
                              `create_time` VARCHAR(32) DEFAULT '' COMMENT '简历创建时间',
                              # 待补充
                              PRIMARY KEY (`id`)
)ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT '用户简历表';


DROP TABLE IF EXISTS `t_resume_recruit`;
CREATE TABLE `t_resume_recruit`(
                              `id` BIGINT(20) UNSIGNED NOT NULL COMMENT '记录id即主键',
                              `resume_id` BIGINT(20) NOT NULL COMMENT 'resume表主键',
                              `recruit_id` BIGINT(20) NOT NULL COMMENT 'recruit表主键',
                              `status` TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '简历状态（1-已投递；2-已查看；3-已收藏；4-待面试；5-不合适）',
                              `send_time` VARCHAR(32) DEFAULT '' COMMENT '投递时间',
                           #   `read_time` VARCHAR(32) DEFAULT '' COMMENT '查看时间',
                              `favo_time` VARCHAR(32) DEFAULT '' COMMENT '收藏时间',
                              `reject_time` VARCHAR(32) DEFAULT '' COMMENT '拒绝时间',
                              PRIMARY KEY (`id`),
                              CONSTRAINT `fk_t_resume_recruit_resume_id_t_user_resume_id` FOREIGN KEY (`resume_id`) REFERENCES `t_user_resume`(`id`),
                              CONSTRAINT `fk_t_resume_recruit_recruit_id_t_com_recruit_id` FOREIGN KEY (`recruit_id`) REFERENCES `t_com_recruit`(`id`)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '简历招聘关联表';