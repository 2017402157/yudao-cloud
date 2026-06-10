package cn.iocoder.yudao.module.maas.service.subscription;

import cn.iocoder.yudao.module.maas.controller.admin.subscription.vo.MaasSubscriptionSaveReqVO;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasSubscriptionDO;
import cn.iocoder.yudao.module.maas.dal.mysql.MaasSubscriptionMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.maas.enums.ErrorCodeConstants.MAAS_MODEL_NOT_EXISTS;

@Service
@Validated
public class MaasSubscriptionServiceImpl implements MaasSubscriptionService {

    @Resource
    private MaasSubscriptionMapper maasSubscriptionMapper;

    @Override
    public Long createSubscription(MaasSubscriptionSaveReqVO reqVO) {
        MaasSubscriptionDO sub = MaasSubscriptionDO.builder()
                .tenantId(reqVO.getTenantId()).planId(reqVO.getPlanId())
                .status(reqVO.getStatus()).periodStart(reqVO.getPeriodStart())
                .periodEnd(reqVO.getPeriodEnd()).usedQuota(0)
                .quotaLimit(reqVO.getQuotaLimit()).modelWhitelist(reqVO.getModelWhitelist())
                .autoRenew(reqVO.getAutoRenew()).build();
        maasSubscriptionMapper.insert(sub);
        return sub.getId();
    }

    @Override
    public void updateSubscription(MaasSubscriptionSaveReqVO reqVO) {
        validateSubscriptionExists(reqVO.getId());
        MaasSubscriptionDO updateObj = MaasSubscriptionDO.builder()
                .id(reqVO.getId()).planId(reqVO.getPlanId())
                .status(reqVO.getStatus()).periodStart(reqVO.getPeriodStart())
                .periodEnd(reqVO.getPeriodEnd()).quotaLimit(reqVO.getQuotaLimit())
                .modelWhitelist(reqVO.getModelWhitelist()).autoRenew(reqVO.getAutoRenew()).build();
        maasSubscriptionMapper.updateById(updateObj);
    }

    @Override
    public MaasSubscriptionDO getSubscription(Long id) { return maasSubscriptionMapper.selectById(id); }

    @Override
    public MaasSubscriptionDO getSubscriptionByTenantId(Long tenantId) { return maasSubscriptionMapper.selectByTenantId(tenantId); }

    @Override
    public void renewSubscription(Long id, int months) {
        MaasSubscriptionDO sub = validateSubscriptionExists(id);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentEnd = sub.getPeriodEnd();
        LocalDateTime base = currentEnd.isAfter(now) ? currentEnd : now;
        sub.setPeriodEnd(base.plusMonths(months));
        sub.setPeriodStart(currentEnd.isAfter(now) ? sub.getPeriodStart() : now);
        sub.setStatus(0); // 有效
        sub.setUsedQuota(0); // 重置配额
        maasSubscriptionMapper.updateById(sub);
    }

    private MaasSubscriptionDO validateSubscriptionExists(Long id) {
        MaasSubscriptionDO sub = maasSubscriptionMapper.selectById(id);
        if (sub == null) throw exception(MAAS_MODEL_NOT_EXISTS);
        return sub;
    }
}