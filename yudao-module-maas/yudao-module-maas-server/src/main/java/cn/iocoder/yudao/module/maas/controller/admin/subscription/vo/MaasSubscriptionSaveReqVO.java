package cn.iocoder.yudao.module.maas.controller.admin.subscription.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - MaaS 订阅创建/更新 Request VO")
@Data
public class MaasSubscriptionSaveReqVO {
    @Schema(description = "订阅编号（更新时必传）")
    private Long id;
    @Schema(description = "租户编号")
    private Long tenantId;
    @Schema(description = "套餐编号")
    private Long planId;
    @Schema(description = "状态（0=有效 1=过期 2=取消）")
    private Integer status;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private Integer quotaLimit;
    private String modelWhitelist;
    private Boolean autoRenew;
}