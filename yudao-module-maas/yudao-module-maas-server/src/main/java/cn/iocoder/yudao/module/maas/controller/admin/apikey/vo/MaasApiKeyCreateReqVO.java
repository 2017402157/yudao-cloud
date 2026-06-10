package cn.iocoder.yudao.module.maas.controller.admin.apikey.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - MaaS API Key 创建 Request VO")
@Data
public class MaasApiKeyCreateReqVO {
    @Schema(description = "租户编号")
    private Long tenantId;
    @Schema(description = "用户编号")
    private Long userId;
    @Schema(description = "Key 名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "模型白名单（JSON数组）")
    private String modelWhitelist;
    @Schema(description = "月配额限制（0=无限）")
    private Integer monthlyQuotaLimit;
    @Schema(description = "过期时间")
    private LocalDateTime expiresAt;
}