package cn.iocoder.yudao.module.maas.controller.admin.apikey;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.maas.controller.admin.apikey.vo.MaasApiKeyCreateReqVO;
import cn.iocoder.yudao.module.maas.controller.admin.apikey.vo.MaasApiKeyPageReqVO;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasApiKeyDO;
import cn.iocoder.yudao.module.maas.service.apikey.MaasApiKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - MaaS 开发者 API Key")
@RestController
@RequestMapping("/maas/api-key")
@Validated
public class MaasApiKeyController {
    @Resource private MaasApiKeyService maasApiKeyService;

    @PostMapping("/create")
    @Operation(summary = "创建 API Key（返回完整 key，仅此一次可见）")
    @PreAuthorize("@ss.hasPermission('maas:api-key:create')")
    public CommonResult<String> createApiKey(@Valid @RequestBody MaasApiKeyCreateReqVO reqVO) {
        return success(maasApiKeyService.createApiKey(reqVO));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除 API Key")
    @PreAuthorize("@ss.hasPermission('maas:api-key:delete')")
    public CommonResult<Boolean> deleteApiKey(@RequestParam("id") Long id) {
        maasApiKeyService.deleteApiKey(id);
        return success(true);
    }

    @PutMapping("/disable")
    @Operation(summary = "禁用 API Key")
    @PreAuthorize("@ss.hasPermission('maas:api-key:update')")
    public CommonResult<Boolean> disableApiKey(@RequestParam("id") Long id) {
        maasApiKeyService.disableApiKey(id);
        return success(true);
    }

    @PutMapping("/enable")
    @Operation(summary = "启用 API Key")
    @PreAuthorize("@ss.hasPermission('maas:api-key:update')")
    public CommonResult<Boolean> enableApiKey(@RequestParam("id") Long id) {
        maasApiKeyService.enableApiKey(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获取 API Key 详情")
    @PreAuthorize("@ss.hasPermission('maas:api-key:query')")
    public CommonResult<MaasApiKeyDO> getApiKey(@RequestParam("id") Long id) {
        return success(maasApiKeyService.getApiKey(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获取 API Key 分页")
    @PreAuthorize("@ss.hasPermission('maas:api-key:query')")
    public CommonResult<PageResult<MaasApiKeyDO>> getApiKeyPage(@Valid MaasApiKeyPageReqVO reqVO) {
        return success(maasApiKeyService.getApiKeyPage(reqVO));
    }
}