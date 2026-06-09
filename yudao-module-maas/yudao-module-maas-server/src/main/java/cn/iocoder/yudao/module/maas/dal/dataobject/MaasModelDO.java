package cn.iocoder.yudao.module.maas.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * MaaS 模型 DO
 *
 * 存储从 new-api 同步过来的模型目录信息及定价
 */
@TableName(value = "maas_model")
@KeySequence("maas_model_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaasModelDO extends BaseDO {

    /**
     * 模型编号，自增
     */
    private Long id;
    /**
     * 模型名称（如 gpt-4o, claude-3-opus）
     */
    private String name;
    /**
     * 模型提供商（如 openai, anthropic, google）
     */
    private String provider;
    /**
     * 模型描述
     */
    private String description;
    /**
     * 输入价格（每 1K tokens，单位：元）
     */
    private BigDecimal inputPricePer1k;
    /**
     * 输出价格（每 1K tokens，单位：元）
     */
    private BigDecimal outputPricePer1k;
    /**
     * 上下文长度
     */
    private Integer contextLength;
    /**
     * 模型能力标签（JSON 数组，如 ["chat","vision","function_calling"]）
     */
    private String capabilities;
    /**
     * 是否可用
     */
    private Boolean available;
    /**
     * 关联的 new-api channel 数量
     */
    private Integer channelCount;
    /**
     * 最后从 new-api 同步时间
     */
    private LocalDateTime lastSyncTime;
    /**
     * 状态（0=开启 1=关闭）
     *
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;
    /**
     * 租户编号
     */
    private Long tenantId;

}
