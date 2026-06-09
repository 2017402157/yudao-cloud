package cn.iocoder.yudao.gateway.filter.security;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.gateway.util.SecurityFrameworkUtils;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

/**
 * MaaS JWT 签发过滤器
 *
 * 在 TokenAuthenticationFilter (order=-100) 解析完 MD5 token / 获得 LoginUser 之后，
 * 本过滤器 (order=-90) 将 LoginUser 信息签发为 HS256 JWT，
 * 通过 X-Maas-JWT header 传递给下游子系统（new-api、lobehub 等）。
 *
 * JWT Claims 包含:
 * - user_id:     用户编号
 * - tenant_id:   租户编号
 * - dept_id:     部门编号（从 LoginUser.info 中获取）
 * - data_scope:  数据权限范围（从 LoginUser.info 中获取）
 * - permissions: 权限列表（从 LoginUser.scopes 中获取）
 * - source:      认证来源（"md5_token" 或 "api_key"）
 * - exp / iat:   过期时间 / 签发时间
 *
 * @author MaaS
 */
@Slf4j
@Component
public class MaasJwtSignFilter implements GlobalFilter, Ordered {

    /** JWT 签发 header 名 */
    public static final String HEADER_MAAS_JWT = "X-Maas-JWT";

    /** LoginUser.info 中存储 deptId 的 key */
    private static final String INFO_KEY_DEPT_ID = "deptId";

    /** LoginUser.info 中存储 dataScope 的 key */
    private static final String INFO_KEY_DATA_SCOPE = "dataScope";

    /** LoginUser.info 中存储 deptScopeIds 的 key */
    private static final String INFO_KEY_DEPT_SCOPE_IDS = "deptScopeIds";

    /** 默认 JWT 过期时间（秒），2 小时 */
    private static final long DEFAULT_JWT_EXPIRY_SECONDS = 7200L;

    private final MACSigner signer;
    private final long expirySeconds;

    public MaasJwtSignFilter(Environment env) {
        String secret = env.getProperty("maas.jwt.secret", "maas-default-secret-change-in-production-32chars!!");
        // HMAC-SHA256 要求密钥至少 256 位 (32 bytes)
        if (secret.length() < 32) {
            secret = secret + "XXXXXXXXXXXXXXXXXXXXXXXX"; // 补齐到 32 字符
            log.warn("[MaasJwtSignFilter] JWT secret 过短，已自动补齐至 32 字符。生产环境请配置足够长的密钥！");
        }
        try {
            this.signer = new MACSigner(secret.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new IllegalStateException("初始化 MaaS JWT signer 失败", e);
        }
        this.expirySeconds = env.getProperty("maas.jwt.expiry-seconds", Long.class, DEFAULT_JWT_EXPIRY_SECONDS);
        log.info("[MaasJwtSignFilter] 初始化完成，JWT 过期时间={}秒", expirySeconds);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 移除可能伪造的 X-Maas-JWT header
        ServerWebExchange cleanedExchange = exchange.mutate()
                .request(builder -> builder.headers(headers -> headers.remove(HEADER_MAAS_JWT)))
                .build();

        // 从 exchange attributes 中获取 LoginUser（由 TokenAuthenticationFilter 设置）
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser(cleanedExchange);
        if (loginUser == null || loginUser.getId() == null) {
            // 无登录用户，直接继续（不签发 JWT）
            return chain.filter(cleanedExchange);
        }

        // 签发 JWT
        try {
            String jwt = signJwt(loginUser);
            ServerWebExchange newExchange = cleanedExchange.mutate()
                    .request(builder -> builder.header(HEADER_MAAS_JWT, jwt))
                    .build();
            log.debug("[MaasJwtSignFilter] 为用户 id={} tenantId={} 签发 JWT", loginUser.getId(), loginUser.getTenantId());
            return chain.filter(newExchange);
        } catch (Exception e) {
            log.error("[MaasJwtSignFilter] JWT 签发失败，用户 id={}", loginUser.getId(), e);
            // JWT 签发失败不阻断请求，继续转发（子系统可能用其他认证方式）
            return chain.filter(cleanedExchange);
        }
    }

    /**
     * 签发 JWT
     */
    private String signJwt(LoginUser loginUser) throws Exception {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis + expirySeconds * 1000);

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .issuer("maas-gateway")
                .subject(String.valueOf(loginUser.getId()))
                .issueTime(now)
                .expirationTime(expiry)
                .claim("user_id", loginUser.getId())
                .claim("tenant_id", loginUser.getTenantId())
                .claim("user_type", loginUser.getUserType())
                .claim("source", "md5_token"); // 当前只支持 MD5 token 路径

        // 从 LoginUser.info 中提取部门信息和数据权限
        if (loginUser.getInfo() != null) {
            String deptId = loginUser.getInfo().get(INFO_KEY_DEPT_ID);
            if (StrUtil.isNotEmpty(deptId)) {
                claimsBuilder.claim("dept_id", Long.valueOf(deptId));
            }
            String dataScope = loginUser.getInfo().get(INFO_KEY_DATA_SCOPE);
            if (StrUtil.isNotEmpty(dataScope)) {
                claimsBuilder.claim("data_scope", dataScope);
            }
            String deptScopeIds = loginUser.getInfo().get(INFO_KEY_DEPT_SCOPE_IDS);
            if (StrUtil.isNotEmpty(deptScopeIds)) {
                claimsBuilder.claim("dept_scope_ids", deptScopeIds);
            }
        }

        // 权限列表
        if (loginUser.getScopes() != null) {
            claimsBuilder.claim("permissions", loginUser.getScopes());
        }

        // TODO: Path B (API Key) 路径时需要添加 model_whitelist claim

        JWTClaimsSet claims = claimsBuilder.build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256, JOSEObjectType.JWT, null, null, null, null, null, null, null, null, null, null, null),
                claims);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    @Override
    public int getOrder() {
        return -90; // 在 TokenAuthenticationFilter (-100) 之后执行
    }

}