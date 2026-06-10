package cn.iocoder.yudao.module.maas.service.subscription;

import cn.iocoder.yudao.module.maas.controller.admin.subscription.vo.MaasSubscriptionSaveReqVO;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasSubscriptionDO;

public interface MaasSubscriptionService {
    Long createSubscription(MaasSubscriptionSaveReqVO reqVO);
    void updateSubscription(MaasSubscriptionSaveReqVO reqVO);
    MaasSubscriptionDO getSubscription(Long id);
    MaasSubscriptionDO getSubscriptionByTenantId(Long tenantId);
    void renewSubscription(Long id, int months);
}