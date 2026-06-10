package cn.iocoder.yudao.module.maas.controller.admin.plan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - MaaS 套餐创建/更新 Request VO")
@Data
public class MaasPlanSaveReqVO {
    @Schema(description = "套餐编号（更新时必传）")
    private Long id;
    @Schema(description = "套餐名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    private String description;
    private BigDecimal monthlyPrice;
    private BigDecimal yearlyPrice;
    private String modelWhitelist;
    private Integer monthlyQuota;
    private Integer rpmLimit;
    private Integer dailyRequestLimit;
    private Integer maxContextLength;
    private Integer sort;
    private Integer status;
}