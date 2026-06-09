package cn.iocoder.yudao.module.maas.controller.admin.model;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.maas.controller.admin.model.vo.MaasModelPageReqVO;
import cn.iocoder.yudao.module.maas.controller.admin.model.vo.MaasModelRespVO;
import cn.iocoder.yudao.module.maas.controller.admin.model.vo.MaasModelSaveReqVO;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasModelDO;
import cn.iocoder.yudao.module.maas.service.model.MaasModelService;
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

@Tag(name = "管理后台 - MaaS 模型目录")
@RestController
@RequestMapping("/maas/model")
@Validated
public class MaasModelController {

    @Resource
    private MaasModelService maasModelService;

    @PostMapping("/create")
    @Operation(summary = "创建模型")
    @PreAuthorize("@ss.hasPermission('maas:model:create')")
    public CommonResult<Long> createModel(@Valid @RequestBody MaasModelSaveReqVO createReqVO) {
        return success(maasModelService.createModel(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新模型")
    @PreAuthorize("@ss.hasPermission('maas:model:update')")
    public CommonResult<Boolean> updateModel(@Valid @RequestBody MaasModelSaveReqVO updateReqVO) {
        maasModelService.updateModel(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除模型")
    @Parameter(name = "id", description = "模型编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('maas:model:delete')")
    public CommonResult<Boolean> deleteModel(@RequestParam("id") Long id) {
        maasModelService.deleteModel(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获取模型详情")
    @Parameter(name = "id", description = "模型编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('maas:model:query')")
    public CommonResult<MaasModelRespVO> getModel(@RequestParam("id") Long id) {
        MaasModelDO model = maasModelService.getModel(id);
        return success(BeanUtils.toBean(model, MaasModelRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获取模型分页")
    @PreAuthorize("@ss.hasPermission('maas:model:query')")
    public CommonResult<PageResult<MaasModelRespVO>> getModelPage(@Valid MaasModelPageReqVO pageReqVO) {
        PageResult<MaasModelDO> pageResult = maasModelService.getModelPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaasModelRespVO.class));
    }

    @GetMapping("/available-list")
    @Operation(summary = "获取可用模型列表（供下拉选择）")
    @PreAuthorize("@ss.hasPermission('maas:model:query')")
    public CommonResult<List<MaasModelRespVO>> getAvailableModelList() {
        List<MaasModelDO> list = maasModelService.getAvailableModelList();
        return success(BeanUtils.toBean(list, MaasModelRespVO.class));
    }

}