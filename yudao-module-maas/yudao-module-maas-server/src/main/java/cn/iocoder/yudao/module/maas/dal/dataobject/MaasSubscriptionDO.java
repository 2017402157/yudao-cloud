package cn.iocoder.yudao.module.maas.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * MaaS 租户订阅 DO
 */
@TableName(value = "maas_subscription")
@KeySequence("maas_subscription_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaasSubscriptionDO extends BaseDO {

    private Long id;
    /**
     * 租户编号
     */
    private Long tenantId;
    /**
     * 套餐编号
     */
    private Long planId;
    /**
     * 订阅状态（0=有效 1=过期 2=取消）
     */
    private Integer status;
    /**
     * 当前周期开始时间
     */
    private LocalDateTime periodStart;
    /**
     * 当前周期结束时间
     */
    private LocalDateTime periodEnd;
    /**
     * 已用配额
     */
    private Integer usedQuota;
    /**
     * 配额上限（0=无限）
     */
    private Integer quotaLimit;
    /**
     * 模型白名单（覆盖套餐默认值，空=使用套餐默认）
     */
    private String modelWhitelist;
    /**
     * 自动续费
     */
    private Boolean autoRenew;

}
