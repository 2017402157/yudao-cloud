package cn.iocoder.yudao.gateway.filter.maas;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * OIDC Discovery Endpoint Filter
 *
 * 提供 .well-known/openid-configuration 端点，供子系统 OIDC 发现使用。
 * 返回 yudao-cloud OAuth2/OIDC 的标准配置信息。
 *
 * @author MaaS
 */
@Slf4j
@Component
public class OidcDiscoveryFilter implements GlobalFilter, Ordered {

    private final String discoveryJson;

    public OidcDiscoveryFilter(Environment env) {
        String issuer = env.getProperty("maas.oidc.issuer", "http://localhost:48080");

        Map<String, Object> config = new HashMap<>();
        config.put("issuer", issuer);
        config.put("authorization_endpoint", issuer + "/admin-api/system/oauth2/authorize");
        config.put("token_endpoint", issuer + "/admin-api/system/oauth2/token");
        config.put("introspection_endpoint", issuer + "/admin-api/system/oauth2/check-token");
        config.put("revocation_endpoint", issuer + "/admin-api/system/oauth2/token");
        config.put("jwks_uri", issuer + "/admin-api/system/oauth2/jwks");
        config.put("response_types_supported", new String[]{"code", "token"});
        config.put("subject_types_supported", new String[]{"public"});
        config.put("id_token_signing_alg_values_supported", new String[]{"RS256", "HS256"});
        config.put("token_endpoint_auth_methods_supported", new String[]{"client_secret_basic", "client_secret_post"});
        config.put("claims_supported", new String[]{"sub", "user_id", "tenant_id", "dept_id", "data_scope"});
        config.put("scopes_supported", new String[]{"openid", "profile", "user_info"});
        this.discoveryJson = JsonUtils.toJsonString(config);

        log.info("[OidcDiscoveryFilter] OIDC Discovery endpoint 已配置，issuer={}", issuer);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // 仅处理 .well-known/openid-configuration 路径
        if (!path.endsWith("/.well-known/openid-configuration")) {
            return chain.filter(exchange);
        }

        // 返回 OIDC Discovery 配置
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Mono.just(
                exchange.getResponse().bufferFactory().wrap(discoveryJson.getBytes())
        ));
    }

    @Override
    public int getOrder() {
        return -80; // 在 MaasJwtSignFilter (-90) 之后执行，优先级较低
    }
}