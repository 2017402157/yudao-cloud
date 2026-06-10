-- MaaS 套餐订阅与计费迁移脚本

-- MaaS 套餐表
CREATE TABLE IF NOT EXISTS maas_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '套餐编号',
    name VARCHAR(128) NOT NULL COMMENT '套餐名称',
    description VARCHAR(512) DEFAULT '' COMMENT '套餐描述',
    monthly_price DECIMAL(10,2) DEFAULT 0 COMMENT '月价格（元）',
    yearly_price DECIMAL(10,2) DEFAULT 0 COMMENT '年价格（元）',
    model_whitelist VARCHAR(1024) DEFAULT NULL COMMENT '模型白名单（JSON数组）',
    monthly_quota INT DEFAULT 0 COMMENT '月配额（0=无限）',
    rpm_limit INT DEFAULT 0 COMMENT '每分钟请求限制（0=无限）',
    daily_request_limit INT DEFAULT 0 COMMENT '每天请求限制（0=无限）',
    max_context_length INT DEFAULT 0 COMMENT '最大上下文长度限制',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 0 COMMENT '状态（0=开启 1=关闭）',
    creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT(1) DEFAULT b'0' COMMENT '是否删除',
    INDEX idx_maas_plan_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='MaaS 套餐';

-- MaaS 租户订阅表
CREATE TABLE IF NOT EXISTS maas_subscription (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订阅编号',
    tenant_id BIGINT NOT NULL COMMENT '租户编号',
    plan_id BIGINT NOT NULL COMMENT '套餐编号',
    status TINYINT DEFAULT 0 COMMENT '订阅状态（0=有效 1=过期 2=取消）',
    period_start DATETIME DEFAULT NULL COMMENT '当前周期开始时间',
    period_end DATETIME DEFAULT NULL COMMENT '当前周期结束时间',
    used_quota INT DEFAULT 0 COMMENT '已用配额',
    quota_limit INT DEFAULT 0 COMMENT '配额上限（0=无限）',
    model_whitelist VARCHAR(1024) DEFAULT NULL COMMENT '模型白名单（覆盖套餐默认）',
    auto_renew BIT(1) DEFAULT b'0' COMMENT '自动续费',
    creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT(1) DEFAULT b'0' COMMENT '是否删除',
    INDEX idx_maas_sub_tenant (tenant_id),
    INDEX idx_maas_sub_plan (plan_id),
    INDEX idx_maas_sub_status (status),
    UNIQUE INDEX uk_maas_sub_tenant (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='MaaS 租户订阅';

-- MaaS 账单表
CREATE TABLE IF NOT EXISTS maas_bill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '账单编号',
    tenant_id BIGINT NOT NULL COMMENT '租户编号',
    billing_period VARCHAR(7) NOT NULL COMMENT '账单周期（yyyy-MM）',
    plan_fee DECIMAL(10,2) DEFAULT 0 COMMENT '套餐费用',
    overage_fee DECIMAL(10,2) DEFAULT 0 COMMENT '超额用量费用',
    total_fee DECIMAL(10,2) DEFAULT 0 COMMENT '总费用',
    total_requests BIGINT DEFAULT 0 COMMENT '请求总数',
    total_tokens BIGINT DEFAULT 0 COMMENT '总token数',
    used_quota INT DEFAULT 0 COMMENT '已用配额',
    status TINYINT DEFAULT 0 COMMENT '账单状态（0=待支付 1=已支付 2=已取消）',
    paid_time DATETIME DEFAULT NULL COMMENT '支付时间',
    creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT(1) DEFAULT b'0' COMMENT '是否删除',
    INDEX idx_maas_bill_tenant (tenant_id),
    INDEX idx_maas_bill_period (billing_period),
    INDEX idx_maas_bill_status (status),
    UNIQUE INDEX uk_maas_bill_tenant_period (tenant_id, billing_period)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='MaaS 账单';