package cn.iocoder.yudao.module.maas.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * MaaS 账单 DO
 */
@TableName(value = "maas_bill")
@KeySequence("maas_bill_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaasBillDO extends BaseDO {

    private Long id;
    /**
     * 租户编号
     */
    private Long tenantId;
    /**
     * 账单周期（yyyy-MM）
     */
    private String billingPeriod;
    /**
     * 套餐费用
     */
    private BigDecimal planFee;
    /**
     * 超额用量费用
     */
    private BigDecimal overageFee;
    /**
     * 总费用
     */
    private BigDecimal totalFee;
    /**
     * 请求总数
     */
    private Long totalRequests;
    /**
     * 总 token 数
     */
    private Long totalTokens;
    /**
     * 已用配额
     */
    private Integer usedQuota;
    /**
     * 账单状态（0=待支付 1=已支付 2=已取消）
     */
    private Integer status;
    /**
     * 支付时间
     */
    private LocalDateTime paidTime;

}
