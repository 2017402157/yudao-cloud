package cn.iocoder.yudao.module.maas.controller.admin.apikey.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - MaaS API Key 分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class MaasApiKeyPageReqVO extends PageParam {
    @Schema(description = "租户编号")
    private Long tenantId;
    @Schema(description = "用户编号")
    private Long userId;
    @Schema(description = "Key 名称（模糊）")
    private String name;
    @Schema(description = "状态")
    private Integer status;
}