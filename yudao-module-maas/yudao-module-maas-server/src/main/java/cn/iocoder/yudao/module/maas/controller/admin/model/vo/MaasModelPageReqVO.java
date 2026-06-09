package cn.iocoder.yudao.module.maas.controller.admin.model.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - MaaS 模型分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class MaasModelPageReqVO extends PageParam {

    @Schema(description = "模型名称（模糊）", example = "gpt")
    private String name;

    @Schema(description = "模型提供商", example = "openai")
    private String provider;

    @Schema(description = "是否可用", example = "true")
    private Boolean available;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "租户编号", example = "1")
    private Long tenantId;

}
