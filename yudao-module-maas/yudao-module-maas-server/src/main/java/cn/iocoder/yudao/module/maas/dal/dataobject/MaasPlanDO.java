package cn.iocoder.yudao.module.maas.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * MaaS 套餐 DO
 */
@TableName(value = "maas_plan")
@KeySequence("maas_plan_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaasPlanDO extends BaseDO {

    private Long id;
    /**
     * 套餐名称
     */
    private String name;
    /**
     * 套餐描述
     */
    private String description;
    /**
     * 月价格（元）
     */
    private BigDecimal monthlyPrice;
    /**
     * 年价格（元）
     */
    private BigDecimal yearlyPrice;
    /**
     * 模型白名单（JSON 数组，空=全部可用）
     */
    private String modelWhitelist;
    /**
     * 月配额（0=无限）
     */
    private Integer monthlyQuota;
    /**
     * 每分钟请求限制（0=无限）
     */
    private Integer rpmLimit;
    /**
     * 每天请求限制（0=无限）
     */
    private Integer dailyRequestLimit;
    /**
     * 最大上下文长度限制
     */
    private Integer maxContextLength;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态（0=开启 1=关闭）
     */
    private Integer status;

}
