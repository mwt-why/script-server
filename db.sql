--- 中文编码问题解决：
1.
show full columns from table_name;
alter table table_name convert to character set utf8 collate utf8_general_ci;
2.
show variable like 'collation%';
set collation_connection=utf8_general_ci
3.
show variable like 'character%';
set character_set_database=utf8;
CREATE TABLE `mwt`.`pas_device_tb` (
  `serial` INT UNSIGNED NOT NULL,
  `brand` VARCHAR(45) NULL,
  `mac` VARCHAR(45) NULL,
  `ip` VARCHAR(45) NULL,
  `memory` BIGINT NULL,
  `height` INT NULL,
  `width` INT NULL,
  `type` VARCHAR(32) NULL,
  `status` TINYINT NULL,
  `create_time` DATETIME NULL);

create table pas_device_snapshot_tb as select * from pas_device_tb where 1=2;

CREATE TABLE `mwt`.`pas_account_tb` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `account` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `type` SMALLINT NULL,
  `status` SMALLINT NULL,
  `last_update_time` DATETIME NULL,
  `create_time` DATETIME NULL,
  PRIMARY KEY (`id`));


CREATE TABLE `mwt`.`pas_task_tb` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `script_name` VARCHAR(45) NULL,
  `device_num` INT NULL,
  `account_num` INT NULL,
  `status` SMALLINT NULL,
  `config` JSON NULL,
  `create_time` DATETIME NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `mwt`.`pas_sub_task_tb` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `script_name` VARCHAR(45) NULL,
  `brand` VARCHAR(45) NULL,
  `serial` VARCHAR(45) NULL,
  `ip` VARCHAR(45) NULL,
  `mac` VARCHAR(45) NULL,
  `account` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `config` JSON NULL,
  `status` SMALLINT NULL,
  `create_time` DATETIME NULL,
  `over_time` DATETIME NULL,
  PRIMARY KEY (`id`));


CREATE TABLE `mwt`.`pas_script_tb` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `status` SMALLINT NULL,
  `config` JSON NULL,
  `create_time` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));


CREATE TABLE `mwt`.`pas_role_tb` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(45) NULL,
  `account` VARCHAR(45) NULL,
  `area` VARCHAR(45) NULL,
  `create_time` DATETIME NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `mwt`.`pas_temp_tb` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `key` VARCHAR(45) NULL,
  `value` VARCHAR(45) NULL,
  `remark` VARCHAR(45) NULL,
  `create_time` DATETIME NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `mwt`.`pas_remark_tb` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `service` VARCHAR(45) NULL,
  `remark` VARCHAR(45) NULL,
  `update_time` DATETIME NULL,
  PRIMARY KEY (`id`));