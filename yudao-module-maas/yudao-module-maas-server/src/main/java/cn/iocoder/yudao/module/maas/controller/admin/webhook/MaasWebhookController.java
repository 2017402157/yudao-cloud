package cn.iocoder.yudao.module.maas.controller.admin.webhook;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.maas.service.model.MaasModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * MaaS Webhook 回调 Controller
 *
 * 接收 new-api 的渠道变更通知，用于同步模型目录信息。
 * new-api 在渠道创建/更新/删除时调用此接口，携带 HMAC-SHA256 签名。
 */
@Tag(name = "管理后台 - MaaS Webhook 回调")
@RestController
@RequestMapping("/maas/webhook")
@Validated
@Slf4j
public class MaasWebhookController {

    @Resource
    private MaasModelService maasModelService;

    @Value("${maas.webhook.secret:}")
    private String webhookSecret;

    @PostMapping("/new-api")
    @Operation(summary = "接收 new-api 渠道变更通知")
    @PermitAll
    public CommonResult<Boolean> handleNewApiWebhook(
            @RequestHeader("X-Newapi-Signature") String signature,
            @RequestHeader("X-Newapi-Event") String event,
            @RequestBody String payload) {
        // 验证 HMAC-SHA256 签名
        if (webhookSecret != null && !webhookSecret.isEmpty()) {
            String expectedSignature = HmacUtils.hmacSha256Hex(webhookSecret, payload);
            if (!expectedSignature.equals(signature)) {
                log.warn("Webhook 签名验证失败: expected={}, actual={}", expectedSignature, signature);
                return error(1_020_002_000, "Webhook 签名验证失败");
            }
        }

        log.info("收到 new-api Webhook: event={}, payload={}", event, payload);
        maasModelService.syncModelFromWebhook(event, payload);
        return success(true);
    }

}