package cn.iocoder.yudao.module.maas.controller.admin.plan.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - MaaS 套餐分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class MaasPlanPageReqVO extends PageParam {
    @Schema(description = "套餐名称（模糊）", example = "专业版")
    private String name;
    @Schema(description = "状态", example = "0")
    private Integer status;
}