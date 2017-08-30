
-- 日志信息
CREATE TABLE tf_log_info
(
	cid numeric(19) NOT NULL COMMENT 'cid',
	sys_type varchar(20) COMMENT '系统类型',
	class_name varchar(200) COMMENT '类名',
	log_code varchar(100) COMMENT '日志编码',
	log_type varchar(20) COMMENT '日志类型',
	-- 支持精确或模糊匹配
	-- tmp*表示后面都算
	log_info varchar(100) COMMENT '日志信息',
	-- 状态(if_active)0无效，1有效
	status int COMMENT '状态',
	-- 是否删除
	if_del int COMMENT '是否删除',
	-- 创建人
	create_user numeric(19) COMMENT '创建人',
	-- 创建时间
	create_date datetime COMMENT '创建时间',
	-- 修改人
	modify_user numeric(19) COMMENT '修改人',
	-- 修改时间
	modify_date datetime COMMENT '修改日期',
	-- 备注
	remark varchar(2000) COMMENT '备注',
	PRIMARY KEY (cid)
) COMMENT = '日志信息';
INSERT INTO tf_sequence (name, current_value, increment) VALUES ('_tf_logInfo_cid_seq', 10, 1);

-- 日志配置
CREATE TABLE tf_log_config
(
	cid numeric(19) NOT NULL COMMENT 'cid',
	sys_type varchar(20) COMMENT '系统类型',
	log_code varchar(100) COMMENT '日志编码',
	log_type varchar(20) COMMENT '日志类型',
	-- 状态(if_active)0无效，1有效
	status int COMMENT '状态',
	-- 是否删除
	if_del int COMMENT '是否删除',
	-- 创建人
	create_user numeric(19) COMMENT '创建人',
	-- 创建时间
	create_date datetime  COMMENT '创建时间',
	-- 修改人
	modify_user numeric(19) COMMENT '修改人',
	-- 修改时间
	modify_date datetime COMMENT '修改日期',
	-- 备注
	remark varchar(2000) COMMENT '备注',
	PRIMARY KEY (cid)
) COMMENT = '日志配置';
INSERT INTO tf_sequence (name, current_value, increment) VALUES ('_tf_logConfig_cid_seq', 10, 1);