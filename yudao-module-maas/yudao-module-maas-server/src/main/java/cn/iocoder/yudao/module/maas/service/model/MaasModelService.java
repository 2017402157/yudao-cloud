package cn.iocoder.yudao.module.maas.service.model;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.maas.controller.admin.model.vo.MaasModelPageReqVO;
import cn.iocoder.yudao.module.maas.controller.admin.model.vo.MaasModelSaveReqVO;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasModelDO;

import java.util.List;

/**
 * MaaS 模型 Service
 */
public interface MaasModelService {

    /**
     * 创建模型
     */
    Long createModel(MaasModelSaveReqVO createReqVO);

    /**
     * 更新模型
     */
    void updateModel(MaasModelSaveReqVO updateReqVO);

    /**
     * 删除模型
     */
    void deleteModel(Long id);

    /**
     * 获取模型
     */
    MaasModelDO getModel(Long id);

    /**
     * 获取模型分页
     */
    PageResult<MaasModelDO> getModelPage(MaasModelPageReqVO pageReqVO);

    /**
     * 获取所有可用模型列表（供前端下拉选择）
     */
    List<MaasModelDO> getAvailableModelList();

    /**
     * 根据 new-api Webhook 通知同步模型信息
     *
     * @param event    事件类型（channel_create/channel_update/channel_delete）
     * @param payload  事件数据（包含 channel 和 model 信息）
     */
    void syncModelFromWebhook(String event, String payload);

}