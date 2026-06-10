package cn.iocoder.yudao.module.maas.controller.admin.plan;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.maas.controller.admin.plan.vo.MaasPlanPageReqVO;
import cn.iocoder.yudao.module.maas.controller.admin.plan.vo.MaasPlanSaveReqVO;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasPlanDO;
import cn.iocoder.yudao.module.maas.service.plan.MaasPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - MaaS 套餐")
@RestController
@RequestMapping("/maas/plan")
@Validated
public class MaasPlanController {
    @Resource private MaasPlanService maasPlanService;

    @PostMapping("/create")
    @Operation(summary = "创建套餐")
    @PreAuthorize("@ss.hasPermission('maas:plan:create')")
    public CommonResult<Long> createPlan(@Valid @RequestBody MaasPlanSaveReqVO reqVO) { return success(maasPlanService.createPlan(reqVO)); }

    @PutMapping("/update")
    @Operation(summary = "更新套餐")
    @PreAuthorize("@ss.hasPermission('maas:plan:update')")
    public CommonResult<Boolean> updatePlan(@Valid @RequestBody MaasPlanSaveReqVO reqVO) { maasPlanService.updatePlan(reqVO); return success(true); }

    @DeleteMapping("/delete")
    @Operation(summary = "删除套餐")
    @PreAuthorize("@ss.hasPermission('maas:plan:delete')")
    public CommonResult<Boolean> deletePlan(@RequestParam("id") Long id) { maasPlanService.deletePlan(id); return success(true); }

    @GetMapping("/get")
    @Operation(summary = "获取套餐详情")
    @PreAuthorize("@ss.hasPermission('maas:plan:query')")
    public CommonResult<MaasPlanDO> getPlan(@RequestParam("id") Long id) { return success(maasPlanService.getPlan(id)); }

    @GetMapping("/page")
    @Operation(summary = "获取套餐分页")
    @PreAuthorize("@ss.hasPermission('maas:plan:query')")
    public CommonResult<PageResult<MaasPlanDO>> getPlanPage(@Valid MaasPlanPageReqVO reqVO) { return success(maasPlanService.getPlanPage(reqVO)); }

    @GetMapping("/list")
    @Operation(summary = "获取套餐列表")
    @PreAuthorize("@ss.hasPermission('maas:plan:query')")
    public CommonResult<List<MaasPlanDO>> getPlanList() { return success(maasPlanService.getPlanList()); }
}