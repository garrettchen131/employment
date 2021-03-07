# DROP DATABASE IF EXISTS `db_employment`;
# CREATE DATABASE IF NOT EXISTS `db_employment`;
# USE `db_employment`;

DROP TABLE IF EXISTS `t_com_info`;
CREATE TABLE `t_com_info`(
                              `com_id` BIGINT (20) NOT NULL COMMENT '企业ID即主键',
                              `com_name` VARCHAR (50) DEFAULT '' COMMENT '公司名称',
                              `run_time` VARCHAR(128) DEFAULT NULL COMMENT '上市时间',
                              `address` VARCHAR (128) DEFAULT '' COMMENT '所在地址',
                              `manager` varchar(128) default '' comment '公司负责人',
                              `status` int(1) DEFAULT 1 COMMENT '公司状态(1-已上市)',
                              `type` varchar(255) default '' comment '公司类型',
                              `website` varchar(255) default '' comment '公司网址',
                              `detail` VARCHAR(255) DEFAULT '' COMMENT '公司简介',
                              PRIMARY KEY (`com_id`)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '企业信息表';

DROP TABLE IF EXISTS `t_com_recruit`;
CREATE TABLE `t_com_recruit`(
                             `id` BIGINT(20) NOT NULL NOT NULL COMMENT '记录id',
                             `manager` Varchar(255) DEFAULT '' COMMENT '负责人',
                             `manager_status` VARCHAR(255) DEFAULT '' COMMENT '负责人职位',
                             `com_id` BIGINT (20) NOT NULL COMMENT '企业ID',
                             `position` VARCHAR(255) DEFAULT '' COMMENT '招聘职位',
                             `money` VARCHAR(255) DEFAULT '' COMMENT '职位薪水',
                             `description` VARCHAR(255) DEFAULT '' COMMENT '岗位描述',
                             `work_content` VARCHAR(255) DEFAULT '' COMMENT '工作内容',
                             `requirement` VARCHAR(255) DEFAULT '' COMMENT '认知要求',
                             `work_place` VARCHAR(255) DEFAULT '' COMMENT '工作地点',
                             `work_time` VARCHAR(255) DEFAULT '' COMMENT '工作时间',
                             `status` TINYINT(1) DEFAULT 0 COMMENT '招聘信息状态（0-未发布；1-已发布）'
)ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT '企业招聘信息';

