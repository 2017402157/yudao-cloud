package cn.iocoder.yudao.module.maas.controller.admin.subscription;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.maas.controller.admin.subscription.vo.MaasSubscriptionSaveReqVO;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasSubscriptionDO;
import cn.iocoder.yudao.module.maas.service.subscription.MaasSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - MaaS 订阅")
@RestController
@RequestMapping("/maas/subscription")
@Validated
public class MaasSubscriptionController {
    @Resource private MaasSubscriptionService maasSubscriptionService;

    @PostMapping("/create")
    @Operation(summary = "创建订阅")
    @PreAuthorize("@ss.hasPermission('maas:subscription:create')")
    public CommonResult<Long> createSubscription(@Valid @RequestBody MaasSubscriptionSaveReqVO reqVO) { return success(maasSubscriptionService.createSubscription(reqVO)); }

    @PutMapping("/update")
    @Operation(summary = "更新订阅")
    @PreAuthorize("@ss.hasPermission('maas:subscription:update')")
    public CommonResult<Boolean> updateSubscription(@Valid @RequestBody MaasSubscriptionSaveReqVO reqVO) { maasSubscriptionService.updateSubscription(reqVO); return success(true); }

    @GetMapping("/get")
    @Operation(summary = "获取订阅详情")
    @PreAuthorize("@ss.hasPermission('maas:subscription:query')")
    public CommonResult<MaasSubscriptionDO> getSubscription(@RequestParam("id") Long id) { return success(maasSubscriptionService.getSubscription(id)); }

    @GetMapping("/get-by-tenant")
    @Operation(summary = "根据租户获取订阅")
    @Parameter(name = "tenantId", description = "租户编号", required = true)
    @PreAuthorize("@ss.hasPermission('maas:subscription:query')")
    public CommonResult<MaasSubscriptionDO> getSubscriptionByTenant(@RequestParam("tenantId") Long tenantId) { return success(maasSubscriptionService.getSubscriptionByTenantId(tenantId)); }

    @PutMapping("/renew")
    @Operation(summary = "续费订阅")
    @PreAuthorize("@ss.hasPermission('maas:subscription:renew')")
    public CommonResult<Boolean> renewSubscription(@RequestParam("id") Long id, @RequestParam("months") Integer months) { maasSubscriptionService.renewSubscription(id, months); return success(true); }
}