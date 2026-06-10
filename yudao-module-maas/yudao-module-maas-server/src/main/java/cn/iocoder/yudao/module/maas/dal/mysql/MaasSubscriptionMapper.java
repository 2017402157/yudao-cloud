package cn.iocoder.yudao.module.maas.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasSubscriptionDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MaasSubscriptionMapper extends BaseMapperX<MaasSubscriptionDO> {

    default MaasSubscriptionDO selectByTenantId(Long tenantId) {
        return selectOne(MaasSubscriptionDO::getTenantId, tenantId);
    }

}
