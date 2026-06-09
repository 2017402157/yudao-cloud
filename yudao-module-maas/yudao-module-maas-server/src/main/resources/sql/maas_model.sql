-- MaaS 模型目录迁移脚本

-- MaaS 模型目录表
CREATE TABLE IF NOT EXISTS maas_model (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '模型编号',
    name VARCHAR(128) NOT NULL COMMENT '模型名称（如 gpt-4o, claude-3-opus）',
    provider VARCHAR(64) NOT NULL COMMENT '模型提供商（如 openai, anthropic）',
    description VARCHAR(512) DEFAULT '' COMMENT '模型描述',
    input_price_per_1k DECIMAL(10,6) DEFAULT NULL COMMENT '输入价格（每1K tokens，元）',
    output_price_per_1k DECIMAL(10,6) DEFAULT NULL COMMENT '输出价格（每1K tokens，元）',
    context_length INT DEFAULT NULL COMMENT '上下文长度',
    capabilities VARCHAR(512) DEFAULT NULL COMMENT '模型能力标签（JSON数组）',
    available BIT(1) DEFAULT b'1' COMMENT '是否可用',
    channel_count INT DEFAULT 0 COMMENT '关联的 new-api channel 数量',
    last_sync_time DATETIME DEFAULT NULL COMMENT '最后从 new-api 同步时间',
    status TINYINT DEFAULT 0 COMMENT '状态（0=开启 1=关闭）',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户编号',
    creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT(1) DEFAULT b'0' COMMENT '是否删除',
    INDEX idx_maas_model_provider (provider),
    INDEX idx_maas_model_available (available),
    INDEX idx_maas_model_status (status),
    INDEX idx_maas_model_tenant_id (tenant_id),
    UNIQUE INDEX uk_maas_model_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='MaaS 模型目录';