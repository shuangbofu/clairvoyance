CREATE TABLE "chart" (
  "id" int(11) unsigned NOT NULL AUTO_INCREMENT,
  "gmt_create" bigint(13) unsigned NOT NULL,
  "gmt_modified" bigint(13) unsigned NOT NULL,
  "status" tinyint(1) unsigned NOT NULL,
  "name" varchar(64) NOT NULL DEFAULT '',
  "work_sheet_id" bigint(13) unsigned NOT NULL,
  "dashboard_id" bigint(13) NOT NULL,
  "layout_config" varchar(2048) NOT NULL DEFAULT '' COMMENT '图表布局配置',
  "sql_config" varchar(2024) NOT NULL DEFAULT '' COMMENT 'SQL配置',
  "alarm_config" varchar(2048) NOT NULL DEFAULT '' COMMENT '告警配置',
  "chart_type" int(4) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY ("id")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE "dashboard" (
  "id" int(11) unsigned NOT NULL AUTO_INCREMENT,
  "gmt_create" bigint(13) unsigned NOT NULL,
  "gmt_modified" bigint(13) unsigned NOT NULL,
  "status" tinyint(1) unsigned NOT NULL,
  "name" varchar(32) NOT NULL DEFAULT '',
  "tags" varchar(128) NOT NULL DEFAULT '',
  "remarks" varchar(128) NOT NULL DEFAULT '',
  "layout_config" varchar(2048) NOT NULL DEFAULT '' COMMENT '仪表盘布局配置',
  "filter_config" varchar(2048) NOT NULL DEFAULT '' COMMENT '全局过滤器配置',
  PRIMARY KEY ("id")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE "node" (
  "id" bigint(13) unsigned NOT NULL AUTO_INCREMENT,
  "name" varchar(32) NOT NULL DEFAULT '',
  "node_type" tinyint(4) unsigned NOT NULL,
  "ref_id" bigint(13) unsigned NOT NULL DEFAULT '0',
  "parent_id" bigint(13) unsigned NOT NULL DEFAULT '0',
  "gmt_create" bigint(13) unsigned NOT NULL,
  "gmt_modified" bigint(13) unsigned NOT NULL,
  "status" tinyint(1) unsigned NOT NULL,
  PRIMARY KEY ("id")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE "work_sheet" (
  "id" bigint(13) unsigned NOT NULL AUTO_INCREMENT,
  "gmt_create" bigint(13) unsigned NOT NULL,
  "gmt_modified" bigint(13) unsigned NOT NULL,
  "status" tinyint(1) unsigned NOT NULL,
  "sheet_type" tinyint(4) unsigned NOT NULL,
  "table_name" varchar(64) NOT NULL DEFAULT '',
  "title" varchar(64) NOT NULL DEFAULT '',
  "description" varchar(128) NOT NULL DEFAULT '',
  "field_infos" varchar(2048) NOT NULL DEFAULT '' COMMENT '字段元数据信息',
  "source_config" varchar(2024) NOT NULL DEFAULT '' COMMENT '数据源配置',
  "last_sync_time" bigint(13) unsigned NOT NULL COMMENT '最近数据同步时间',
  PRIMARY KEY ("id")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
