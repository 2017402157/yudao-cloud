package cn.iocoder.yudao.module.maas.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * MaaS 错误码枚举类
 *
 * MaaS 系统，使用 1-020-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 模型目录 1-020-001-000 ==========
    ErrorCode MAAS_MODEL_NOT_EXISTS = new ErrorCode(1_020_001_000, "模型不存在");
    ErrorCode MAAS_MODEL_NAME_DUPLICATE = new ErrorCode(1_020_001_001, "模型名称已存在");
    ErrorCode MAAS_MODEL_PROVIDER_NOT_EXISTS = new ErrorCode(1_020_001_002, "模型提供商不存在");

    // ========== Webhook 1-020-002-000 ==========
    ErrorCode MAAS_WEBHOOK_SIGN_INVALID = new ErrorCode(1_020_002_000, "Webhook 签名验证失败");
    ErrorCode MAAS_WEBHOOK_EVENT_UNSUPPORTED = new ErrorCode(1_020_002_001, "不支持的 Webhook 事件类型");

}
