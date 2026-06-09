package cn.iocoder.yudao.module.maas.dal.mysql;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.maas.controller.admin.model.vo.MaasModelPageReqVO;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasModelDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * MaaS 模型 Mapper
 */
@Mapper
public interface MaasModelMapper extends BaseMapperX<MaasModelDO> {

    default PageResult<MaasModelDO> selectPage(MaasModelPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MaasModelDO>()
                .likeIfPresent(MaasModelDO::getName, reqVO.getName())
                .eqIfPresent(MaasModelDO::getProvider, reqVO.getProvider())
                .eqIfPresent(MaasModelDO::getAvailable, reqVO.getAvailable())
                .eqIfPresent(MaasModelDO::getStatus, reqVO.getStatus())
                .eqIfPresent(MaasModelDO::getTenantId, reqVO.getTenantId())
                .orderByDesc(MaasModelDO::getId));
    }

    default MaasModelDO selectByName(String name) {
        return selectOne(MaasModelDO::getName, name);
    }

}
