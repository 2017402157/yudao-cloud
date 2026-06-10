package cn.iocoder.yudao.module.maas.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * MaaS 开发者 API Key DO
 */
@TableName(value = "maas_api_key")
@KeySequence("maas_api_key_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaasApiKeyDO extends BaseDO {

    private Long id;
    /**
     * 租户编号
     */
    private Long tenantId;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * Key 名称
     */
    private String name;
    /**
     * API Key 前缀（用于识别，如 maas-xxxx）
     */
    private String keyPrefix;
    /**
     * API Key 完整值（SHA256 加密存储）
     */
    private String keyHash;
    /**
     * 模型白名单（JSON 数组，空=全部可用）
     */
    private String modelWhitelist;
    /**
     * 月配额限制（0=无限）
     */
    private Integer monthlyQuotaLimit;
    /**
     * 已用配额
     */
    private Integer usedQuota;
    /**
     * 过期时间
     */
    private LocalDateTime expiresAt;
    /**
     * 状态（0=启用 1=禁用）
     */
    private Integer status;

}