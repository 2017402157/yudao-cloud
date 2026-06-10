package cn.iocoder.yudao.module.maas.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasApiKeyDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MaasApiKeyMapper extends BaseMapperX<MaasApiKeyDO> {

    default MaasApiKeyDO selectByKeyPrefix(String keyPrefix) {
        return selectOne(MaasApiKeyDO::getKeyPrefix, keyPrefix);
    }

    default cn.iocoder.yudao.framework.common.pojo.PageResult<MaasApiKeyDO> selectPage(
            cn.iocoder.yudao.module.maas.controller.admin.apikey.vo.MaasApiKeyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MaasApiKeyDO>()
                .eqIfPresent(MaasApiKeyDO::getTenantId, reqVO.getTenantId())
                .eqIfPresent(MaasApiKeyDO::getUserId, reqVO.getUserId())
                .likeIfPresent(MaasApiKeyDO::getName, reqVO.getName())
                .eqIfPresent(MaasApiKeyDO::getStatus, reqVO.getStatus())
                .orderByDesc(MaasApiKeyDO::getId));
    }

}