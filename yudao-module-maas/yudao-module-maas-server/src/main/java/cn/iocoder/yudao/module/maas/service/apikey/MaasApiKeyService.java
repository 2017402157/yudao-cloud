package cn.iocoder.yudao.module.maas.service.apikey;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.maas.controller.admin.apikey.vo.MaasApiKeyCreateReqVO;
import cn.iocoder.yudao.module.maas.controller.admin.apikey.vo.MaasApiKeyPageReqVO;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasApiKeyDO;

public interface MaasApiKeyService {
    /** 创建 API Key，返回完整的 key 字符串（仅创建时可见） */
    String createApiKey(MaasApiKeyCreateReqVO reqVO);
    void deleteApiKey(Long id);
    void disableApiKey(Long id);
    void enableApiKey(Long id);
    MaasApiKeyDO getApiKey(Long id);
    PageResult<MaasApiKeyDO> getApiKeyPage(MaasApiKeyPageReqVO reqVO);
    /** 根据 key 前缀查找 API Key */
    MaasApiKeyDO getApiKeyByPrefix(String keyPrefix);
}