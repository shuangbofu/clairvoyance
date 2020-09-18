CREATE TABLE `chart` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` bigint(13) unsigned NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(13) unsigned NOT NULL COMMENT '修改时间',
  `status` tinyint(1) unsigned NOT NULL COMMENT '逻辑删除',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '名称',
  `remarks` varchar(128) NOT NULL DEFAULT '' COMMENT '备注',
  `chart_type` int(4) unsigned NOT NULL DEFAULT '0' COMMENT '图表类型',
  `work_sheet_id` bigint(13) unsigned NOT NULL COMMENT '工作表ID',
  `dashboard_id` bigint(13) NOT NULL COMMENT '仪表盘ID',
  `layout_config` varchar(2048) NOT NULL DEFAULT '' COMMENT '图表布局配置',
  `sql_config` text NOT NULL COMMENT 'SQL配置',
  `alarm_config` varchar(2048) NOT NULL DEFAULT '' COMMENT '告警配置',
  `create_user` varchar(64) NOT NULL DEFAULT '' COMMENT '创建用户',
  `modify_user` varchar(64) NOT NULL DEFAULT '' COMMENT '修改用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `dashboard` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` bigint(13) unsigned NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(13) unsigned NOT NULL COMMENT '修改时间',
  `status` tinyint(1) unsigned NOT NULL COMMENT '逻辑删除',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '仪表盘名称',
  `tags` varchar(128) NOT NULL DEFAULT '' COMMENT '标签',
  `remarks` varchar(128) NOT NULL DEFAULT '' COMMENT '备注描述',
  `layout_config` varchar(2048) NOT NULL DEFAULT '' COMMENT '仪表盘布局配置',
  `filter_config` varchar(2048) NOT NULL DEFAULT '' COMMENT '全局过滤器配置',
  `create_user` varchar(64) NOT NULL DEFAULT '' COMMENT '创建用户',
  `modify_user` varchar(64) NOT NULL DEFAULT '' COMMENT '修改用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `datasource` (
  `id` bigint(13) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` bigint(13) unsigned NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(13) unsigned NOT NULL COMMENT '修改时间',
  `status` tinyint(1) unsigned NOT NULL COMMENT '逻辑删除',
  `config` varchar(1024) NOT NULL DEFAULT '' COMMENT '数据源配置',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '数据源名称',
  `type` int(4) unsigned NOT NULL COMMENT '数据源类型',
  `description` varchar(256) NOT NULL DEFAULT '' COMMENT '数据源描述',
  `create_user` varchar(64) NOT NULL DEFAULT '' COMMENT '创建用户',
  `modify_user` varchar(64) NOT NULL DEFAULT '' COMMENT '修改用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `node` (
  `id` bigint(13) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '名称',
  `node_type` tinyint(4) unsigned NOT NULL COMMENT '节点类型',
  `ref_id` bigint(13) unsigned NOT NULL DEFAULT '0' COMMENT '引用ID',
  `parent_id` bigint(13) unsigned NOT NULL DEFAULT '0' COMMENT '父节点ID',
  `gmt_create` bigint(13) unsigned NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(13) unsigned NOT NULL COMMENT '修改时间',
  `status` tinyint(1) unsigned NOT NULL COMMENT '逻辑删除',
  `create_user` varchar(64) NOT NULL DEFAULT '' COMMENT '创建用户',
  `modify_user` varchar(64) NOT NULL DEFAULT '' COMMENT '修改用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `work_sheet` (
  `id` bigint(13) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` bigint(13) unsigned NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(13) unsigned NOT NULL COMMENT '修改时间',
  `status` tinyint(1) unsigned NOT NULL COMMENT '逻辑删除',
  `datasource_id` bigint(20) unsigned NOT NULL COMMENT '数据源ID',
  `sheet_type` tinyint(4) unsigned NOT NULL COMMENT '工作表类型',
  `table_name` varchar(64) NOT NULL DEFAULT '' COMMENT '表名',
  `title` varchar(64) NOT NULL DEFAULT '' COMMENT '表名称',
  `description` varchar(128) NOT NULL DEFAULT '' COMMENT '表描述',
  `source_config` varchar(2024) NOT NULL DEFAULT '' COMMENT '数据源配置',
  `last_sync_time` bigint(13) unsigned NOT NULL COMMENT '最近数据同步时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `work_sheet_field` (
  `id` bigint(13) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '字段名',
  `work_sheet_id` bigint(13) unsigned NOT NULL COMMENT '工作表ID',
  `field_type` int(4) unsigned NOT NULL COMMENT '工作表字段类型',
  `title` varchar(64) NOT NULL DEFAULT '' COMMENT '字段名称',
  `column_type` int(4) unsigned NOT NULL COMMENT '原字段类型',
  `remarks` varchar(128) NOT NULL DEFAULT '' COMMENT '字段描述',
  `seq_no` int(4) unsigned NOT NULL COMMENT '顺序',
  `link_url` varchar(512) NOT NULL DEFAULT '' COMMENT '链接',
  `config` varchar(1024) NOT NULL DEFAULT '' COMMENT '其他配置',
  `gmt_create` bigint(13) unsigned NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(13) unsigned NOT NULL COMMENT '修改时间',
  `status` int(4) unsigned NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

