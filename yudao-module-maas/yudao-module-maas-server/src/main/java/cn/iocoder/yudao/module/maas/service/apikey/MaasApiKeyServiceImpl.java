package cn.iocoder.yudao.module.maas.service.apikey;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.maas.controller.admin.apikey.vo.MaasApiKeyCreateReqVO;
import cn.iocoder.yudao.module.maas.controller.admin.apikey.vo.MaasApiKeyPageReqVO;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasApiKeyDO;
import cn.iocoder.yudao.module.maas.dal.mysql.MaasApiKeyMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.maas.enums.ErrorCodeConstants.MAAS_MODEL_NOT_EXISTS;

@Service
@Validated
public class MaasApiKeyServiceImpl implements MaasApiKeyService {

    private static final String KEY_PREFIX = "maas-";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Resource
    private MaasApiKeyMapper maasApiKeyMapper;

    @Override
    public String createApiKey(MaasApiKeyCreateReqVO reqVO) {
        // 生成 API Key: maas-{32 random base64url chars}
        byte[] randomBytes = new byte[24];
        SECURE_RANDOM.nextBytes(randomBytes);
        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        String fullKey = KEY_PREFIX + randomPart;
        String keyPrefix = fullKey.substring(0, 12); // maas-xxxxxxx
        String keyHash = sha256Hex(fullKey);

        MaasApiKeyDO apiKey = MaasApiKeyDO.builder()
                .tenantId(reqVO.getTenantId())
                .userId(reqVO.getUserId())
                .name(reqVO.getName())
                .keyPrefix(keyPrefix)
                .keyHash(keyHash)
                .modelWhitelist(reqVO.getModelWhitelist())
                .monthlyQuotaLimit(reqVO.getMonthlyQuotaLimit())
                .usedQuota(0)
                .expiresAt(reqVO.getExpiresAt())
                .status(0) // 启用
                .build();
        maasApiKeyMapper.insert(apiKey);

        return fullKey; // 仅此一次返回完整 key
    }

    @Override
    public void deleteApiKey(Long id) {
        validateApiKeyExists(id);
        maasApiKeyMapper.deleteById(id);
    }

    @Override
    public void disableApiKey(Long id) {
        MaasApiKeyDO apiKey = validateApiKeyExists(id);
        apiKey.setStatus(1);
        maasApiKeyMapper.updateById(apiKey);
    }

    @Override
    public void enableApiKey(Long id) {
        MaasApiKeyDO apiKey = validateApiKeyExists(id);
        apiKey.setStatus(0);
        maasApiKeyMapper.updateById(apiKey);
    }

    @Override
    public MaasApiKeyDO getApiKey(Long id) {
        return maasApiKeyMapper.selectById(id);
    }

    @Override
    public PageResult<MaasApiKeyDO> getApiKeyPage(MaasApiKeyPageReqVO reqVO) {
        return maasApiKeyMapper.selectPage(reqVO);
    }

    @Override
    public MaasApiKeyDO getApiKeyByPrefix(String keyPrefix) {
        return maasApiKeyMapper.selectByKeyPrefix(keyPrefix);
    }

    private MaasApiKeyDO validateApiKeyExists(Long id) {
        MaasApiKeyDO apiKey = maasApiKeyMapper.selectById(id);
        if (apiKey == null) throw exception(MAAS_MODEL_NOT_EXISTS);
        return apiKey;
    }

    private static String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 hashing failed", e);
        }
    }

}