package cn.iocoder.yudao.module.maas.service.model;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.maas.controller.admin.model.vo.MaasModelPageReqVO;
import cn.iocoder.yudao.module.maas.controller.admin.model.vo.MaasModelSaveReqVO;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasModelDO;
import cn.iocoder.yudao.module.maas.dal.mysql.MaasModelMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.maas.enums.ErrorCodeConstants.*;

/**
 * MaaS 模型 Service 实现类
 */
@Service
@Validated
@Slf4j
public class MaasModelServiceImpl implements MaasModelService {

    @Resource
    private MaasModelMapper maasModelMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Long createModel(MaasModelSaveReqVO createReqVO) {
        // 校验名称唯一
        MaasModelDO existing = maasModelMapper.selectByName(createReqVO.getName());
        if (existing != null) {
            throw exception(MAAS_MODEL_NAME_DUPLICATE);
        }
        // 插入
        MaasModelDO model = MaasModelDO.builder()
                .name(createReqVO.getName())
                .provider(createReqVO.getProvider())
                .description(createReqVO.getDescription())
                .inputPricePer1k(createReqVO.getInputPricePer1k())
                .outputPricePer1k(createReqVO.getOutputPricePer1k())
                .contextLength(createReqVO.getContextLength())
                .capabilities(createReqVO.getCapabilities())
                .available(createReqVO.getAvailable() != null ? createReqVO.getAvailable() : true)
                .channelCount(0)
                .status(createReqVO.getStatus() != null ? createReqVO.getStatus() : CommonStatusEnum.ENABLE.getStatus())
                .build();
        maasModelMapper.insert(model);
        return model.getId();
    }

    @Override
    public void updateModel(MaasModelSaveReqVO updateReqVO) {
        // 校验存在
        MaasModelDO model = validateModelExists(updateReqVO.getId());
        // 更新
        MaasModelDO updateObj = MaasModelDO.builder()
                .id(updateReqVO.getId())
                .name(updateReqVO.getName())
                .provider(updateReqVO.getProvider())
                .description(updateReqVO.getDescription())
                .inputPricePer1k(updateReqVO.getInputPricePer1k())
                .outputPricePer1k(updateReqVO.getOutputPricePer1k())
                .contextLength(updateReqVO.getContextLength())
                .capabilities(updateReqVO.getCapabilities())
                .available(updateReqVO.getAvailable())
                .status(updateReqVO.getStatus())
                .build();
        maasModelMapper.updateById(updateObj);
    }

    @Override
    public void deleteModel(Long id) {
        validateModelExists(id);
        maasModelMapper.deleteById(id);
    }

    @Override
    public MaasModelDO getModel(Long id) {
        return maasModelMapper.selectById(id);
    }

    @Override
    public PageResult<MaasModelDO> getModelPage(MaasModelPageReqVO pageReqVO) {
        return maasModelMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MaasModelDO> getAvailableModelList() {
        return maasModelMapper.selectList(MaasModelDO::getAvailable, true);
    }

    @Override
    public void syncModelFromWebhook(String event, String payload) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            JsonNode channelNode = root.get("channel");
            if (channelNode == null) {
                log.warn("Webhook payload 缺少 channel 信息: {}", payload);
                return;
            }

            String modelName = channelNode.path("model").asText("");
            String provider = channelNode.path("provider").asText("");
            if (modelName.isEmpty()) {
                log.warn("Webhook payload 缺少 model 名称");
                return;
            }

            switch (event) {
                case "channel_create", "channel_update" -> {
                    MaasModelDO existing = maasModelMapper.selectByName(modelName);
                    if (existing != null) {
                        // 更新 channelCount 和 lastSyncTime
                        existing.setChannelCount(existing.getChannelCount() + 1);
                        existing.setLastSyncTime(LocalDateTime.now());
                        existing.setAvailable(true);
                        if (!provider.isEmpty()) {
                            existing.setProvider(provider);
                        }
                        maasModelMapper.updateById(existing);
                    } else {
                        // 创建新模型
                        MaasModelDO newModel = MaasModelDO.builder()
                                .name(modelName)
                                .provider(provider)
                                .inputPricePer1k(null)
                                .outputPricePer1k(null)
                                .channelCount(1)
                                .available(true)
                                .lastSyncTime(LocalDateTime.now())
                                .status(CommonStatusEnum.ENABLE.getStatus())
                                .build();
                        maasModelMapper.insert(newModel);
                    }
                    log.info("Webhook 同步模型成功: event={}, model={}", event, modelName);
                }
                case "channel_delete" -> {
                    MaasModelDO existing = maasModelMapper.selectByName(modelName);
                    if (existing != null) {
                        int newCount = Math.max(0, existing.getChannelCount() - 1);
                        existing.setChannelCount(newCount);
                        existing.setAvailable(newCount > 0);
                        existing.setLastSyncTime(LocalDateTime.now());
                        maasModelMapper.updateById(existing);
                    }
                    log.info("Webhook 同步模型删除: model={}, remaining channels={}", modelName,
                            existing != null ? existing.getChannelCount() : 0);
                }
                default -> log.warn("不支持的 Webhook 事件: {}", event);
            }
        } catch (JsonProcessingException e) {
            log.error("解析 Webhook payload 失败", e);
        }
    }

    private MaasModelDO validateModelExists(Long id) {
        MaasModelDO model = maasModelMapper.selectById(id);
        if (model == null) {
            throw exception(MAAS_MODEL_NOT_EXISTS);
        }
        return model;
    }

}