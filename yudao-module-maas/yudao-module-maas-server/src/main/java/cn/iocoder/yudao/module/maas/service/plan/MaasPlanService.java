package cn.iocoder.yudao.module.maas.service.plan;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.maas.controller.admin.plan.vo.MaasPlanPageReqVO;
import cn.iocoder.yudao.module.maas.controller.admin.plan.vo.MaasPlanSaveReqVO;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasPlanDO;
import java.util.List;

public interface MaasPlanService {
    Long createPlan(MaasPlanSaveReqVO reqVO);
    void updatePlan(MaasPlanSaveReqVO reqVO);
    void deletePlan(Long id);
    MaasPlanDO getPlan(Long id);
    PageResult<MaasPlanDO> getPlanPage(MaasPlanPageReqVO reqVO);
    List<MaasPlanDO> getPlanList();
}