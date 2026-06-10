package cn.iocoder.yudao.module.maas.controller.admin.usage;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * MaaS 用量统计 Controller
 *
 * 代理查询 new-api 的用量统计接口
 */
@Tag(name = "管理后台 - MaaS 用量统计")
@RestController
@RequestMapping("/maas/usage")
@Validated
public class MaasUsageController {

    @Value("${maas.new-api.url:http://new-api:3000}")
    private String newApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/stats")
    @Operation(summary = "获取租户用量统计")
    @Parameter(name = "tenantId", description = "租户编号", example = "1")
    @Parameter(name = "date", description = "日期（yyyy-MM-dd）", example = "2024-01-01")
    public CommonResult<Object> getUsageStats(
            @RequestParam(value = "tenantId", required = false) Long tenantId,
            @RequestParam(value = "date", required = false) String date) {
        // 代理请求到 new-api 的 maas usage 端点
        String url = newApiUrl + "/maas/usage/stats?";
        if (tenantId != null) {
            url += "tenant_id=" + tenantId + "&";
        }
        if (date != null) {
            url += "date=" + date;
        }

        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
            return success(response.getBody());
        } catch (Exception e) {
            return success(null);
        }
    }

    @PostMapping("/quota/set")
    @Operation(summary = "设置租户配额")
    public CommonResult<Boolean> setTenantQuota(@RequestBody Object quotaRequest) {
        String url = newApiUrl + "/maas/quota/set";
        try {
            restTemplate.postForEntity(url, quotaRequest, Object.class);
            return success(true);
        } catch (Exception e) {
            return success(false);
        }
    }

}
