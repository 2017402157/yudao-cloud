package cn.iocoder.yudao.module.maas.service.plan;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.maas.controller.admin.plan.vo.MaasPlanPageReqVO;
import cn.iocoder.yudao.module.maas.controller.admin.plan.vo.MaasPlanSaveReqVO;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasPlanDO;
import cn.iocoder.yudao.module.maas.dal.mysql.MaasPlanMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.maas.enums.ErrorCodeConstants.MAAS_MODEL_NOT_EXISTS;

@Service
@Validated
public class MaasPlanServiceImpl implements MaasPlanService {

    @Resource
    private MaasPlanMapper maasPlanMapper;

    @Override
    public Long createPlan(MaasPlanSaveReqVO reqVO) {
        MaasPlanDO plan = MaasPlanDO.builder()
                .name(reqVO.getName()).description(reqVO.getDescription())
                .monthlyPrice(reqVO.getMonthlyPrice()).yearlyPrice(reqVO.getYearlyPrice())
                .modelWhitelist(reqVO.getModelWhitelist()).monthlyQuota(reqVO.getMonthlyQuota())
                .rpmLimit(reqVO.getRpmLimit()).dailyRequestLimit(reqVO.getDailyRequestLimit())
                .maxContextLength(reqVO.getMaxContextLength()).sort(reqVO.getSort())
                .status(reqVO.getStatus()).build();
        maasPlanMapper.insert(plan);
        return plan.getId();
    }

    @Override
    public void updatePlan(MaasPlanSaveReqVO reqVO) {
        validatePlanExists(reqVO.getId());
        MaasPlanDO updateObj = MaasPlanDO.builder()
                .id(reqVO.getId()).name(reqVO.getName()).description(reqVO.getDescription())
                .monthlyPrice(reqVO.getMonthlyPrice()).yearlyPrice(reqVO.getYearlyPrice())
                .modelWhitelist(reqVO.getModelWhitelist()).monthlyQuota(reqVO.getMonthlyQuota())
                .rpmLimit(reqVO.getRpmLimit()).dailyRequestLimit(reqVO.getDailyRequestLimit())
                .maxContextLength(reqVO.getMaxContextLength()).sort(reqVO.getSort())
                .status(reqVO.getStatus()).build();
        maasPlanMapper.updateById(updateObj);
    }

    @Override
    public void deletePlan(Long id) {
        validatePlanExists(id);
        maasPlanMapper.deleteById(id);
    }

    @Override
    public MaasPlanDO getPlan(Long id) { return maasPlanMapper.selectById(id); }

    @Override
    public PageResult<MaasPlanDO> getPlanPage(MaasPlanPageReqVO reqVO) { return maasPlanMapper.selectPage(reqVO); }

    @Override
    public List<MaasPlanDO> getPlanList() { return maasPlanMapper.selectList(); }

    private MaasPlanDO validatePlanExists(Long id) {
        MaasPlanDO plan = maasPlanMapper.selectById(id);
        if (plan == null) throw exception(MAAS_MODEL_NOT_EXISTS);
        return plan;
    }
}