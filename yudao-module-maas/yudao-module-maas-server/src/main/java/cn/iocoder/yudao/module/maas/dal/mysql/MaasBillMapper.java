package cn.iocoder.yudao.module.maas.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasBillDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MaasBillMapper extends BaseMapperX<MaasBillDO> {

    default MaasBillDO selectByTenantIdAndPeriod(Long tenantId, String billingPeriod) {
        return selectOne(new cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX<MaasBillDO>()
                .eq(MaasBillDO::getTenantId, tenantId)
                .eq(MaasBillDO::getBillingPeriod, billingPeriod));
    }

}
