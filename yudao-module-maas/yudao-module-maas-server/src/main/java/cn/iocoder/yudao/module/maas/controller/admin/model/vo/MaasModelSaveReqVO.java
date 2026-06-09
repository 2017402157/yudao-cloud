package cn.iocoder.yudao.module.maas.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Schema(description = "管理后台 - MaaS 模型创建/更新 Request VO")
@Data
public class MaasModelSaveReqVO {

    @Schema(description = "模型编号（更新时必传）", example = "1")
    private Long id;

    @Schema(description = "模型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "gpt-4o")
    private String name;

    @Schema(description = "模型提供商", requiredMode = Schema.RequiredMode.REQUIRED, example = "openai")
    private String provider;

    @Schema(description = "模型描述", example = "GPT-4o 多模态模型")
    private String description;

    @Schema(description = "输入价格（每 1K tokens，元）", example = "0.03")
    private BigDecimal inputPricePer1k;

    @Schema(description = "输出价格（每 1K tokens，元）", example = "0.06")
    private BigDecimal outputPricePer1k;

    @Schema(description = "上下文长度", example = "128000")
    private Integer contextLength;

    @Schema(description = "模型能力标签（JSON 数组）", example = "[\"chat\",\"vision\"]")
    private String capabilities;

    @Schema(description = "是否可用", example = "true")
    private Boolean available;

    @Schema(description = "状态（0=开启 1=关闭）", example = "0")
    private Integer status;

}
