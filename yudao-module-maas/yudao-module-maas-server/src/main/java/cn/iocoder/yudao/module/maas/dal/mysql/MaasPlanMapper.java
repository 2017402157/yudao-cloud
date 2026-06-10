package cn.iocoder.yudao.module.maas.dal.mysql;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasPlanDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MaasPlanMapper extends BaseMapperX<MaasPlanDO> {

    default PageResult<MaasPlanDO> selectPage(cn.iocoder.yudao.module.maas.controller.admin.plan.vo.MaasPlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MaasPlanDO>()
                .likeIfPresent(MaasPlanDO::getName, reqVO.getName())
                .eqIfPresent(MaasPlanDO::getStatus, reqVO.getStatus())
                .orderByAsc(MaasPlanDO::getSort));
    }

}
