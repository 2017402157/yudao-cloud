package cn.iocoder.yudao.gateway.filter.security;

import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * MaasJwtSignFilter 单元测试
 *
 * 验证 JWT 签发的正确性：
 * - claims 完整性（user_id, tenant_id, dept_id, data_scope, permissions, source）
 * - HS256 签名可验证
 * - 无 LoginUser 时不签发 JWT
 * - JWT 过期时间配置正确
 */
class MaasJwtSignFilterTest {

    private static final String TEST_SECRET = "test-secret-that-is-at-least-32-characters-long!!";

    private MaasJwtSignFilter filter;

    @BeforeEach
    void setUp() {
        Environment env = mock(Environment.class);
        when(env.getProperty("maas.jwt.secret", "maas-default-secret-change-in-production-32chars!!"))
                .thenReturn(TEST_SECRET);
        when(env.getProperty("maas.jwt.expiry-seconds", Long.class, 7200L))
                .thenReturn(7200L);
        filter = new MaasJwtSignFilter(env);
    }

    @Test
    void testSignJwt_completeClaims() throws Exception {
        // 准备 LoginUser
        LoginUser user = new LoginUser();
        user.setId(1L);
        user.setUserType(1);
        user.setTenantId(100L);
        user.setInfo(Map.of(
                "deptId", "10",
                "dataScope", "1",
                "deptScopeIds", "10,20,30"
        ));
        user.setScopes(List.of("model:read", "model:write"));

        // 通过反射调用 signJwt 方法
        var method = MaasJwtSignFilter.class.getDeclaredMethod("signJwt", LoginUser.class);
        method.setAccessible(true);
        String jwtStr = (String) method.invoke(filter, user);

        assertNotNull(jwtStr);

        // 验证 JWT 签名和 claims
        SignedJWT signedJWT = SignedJWT.parse(jwtStr);

        // 验证签名
        MACVerifier verifier = new MACVerifier(TEST_SECRET.getBytes("UTF-8"));
        assertTrue(signedJWT.verify(verifier));

        // 验证 claims
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        assertEquals("maas-gateway", claims.getIssuer());
        assertEquals("1", claims.getSubject());
        assertEquals(1L, claims.getLongClaim("user_id"));
        assertEquals(100L, claims.getLongClaim("tenant_id"));
        assertEquals(1, claims.getIntegerClaim("user_type"));
        assertEquals(10L, claims.getLongClaim("dept_id"));
        assertEquals("1", claims.getStringClaim("data_scope"));
        assertEquals("10,20,30", claims.getStringClaim("dept_scope_ids"));
        assertEquals("md5_token", claims.getStringClaim("source"));

        // 验证 permissions
        @SuppressWarnings("unchecked")
        List<String> permissions = (List<String>) claims.getClaim("permissions");
        assertNotNull(permissions);
        assertTrue(permissions.contains("model:read"));
        assertTrue(permissions.contains("model:write"));

        // 验证过期时间
        assertNotNull(claims.getExpirationTime());
        assertNotNull(claims.getIssueTime());
    }

    @Test
    void testSignJwt_minimalClaims() throws Exception {
        // 最小 claims：无 info、无 scopes
        LoginUser user = new LoginUser();
        user.setId(42L);
        user.setUserType(2);
        user.setTenantId(200L);

        var method = MaasJwtSignFilter.class.getDeclaredMethod("signJwt", LoginUser.class);
        method.setAccessible(true);
        String jwtStr = (String) method.invoke(filter, user);

        SignedJWT signedJWT = SignedJWT.parse(jwtStr);
        MACVerifier verifier = new MACVerifier(TEST_SECRET.getBytes("UTF-8"));
        assertTrue(signedJWT.verify(verifier));

        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        assertEquals(42L, claims.getLongClaim("user_id"));
        assertEquals(200L, claims.getLongClaim("tenant_id"));
        // dept_id 和 data_scope 不应存在
        assertNull(claims.getClaim("dept_id"));
        assertNull(claims.getClaim("data_scope"));
    }

    @Test
    void testFilterOrder() {
        // 确认过滤器顺序在 TokenAuthenticationFilter (-100) 之后
        assertEquals(-90, filter.getOrder());
    }

    @Test
    void testSignJwt_withEmptyDeptId() throws Exception {
        LoginUser user = new LoginUser();
        user.setId(1L);
        user.setTenantId(100L);
        user.setInfo(Map.of("deptId", "")); // 空字符串 deptId

        var method = MaasJwtSignFilter.class.getDeclaredMethod("signJwt", LoginUser.class);
        method.setAccessible(true);
        String jwtStr = (String) method.invoke(filter, user);

        SignedJWT signedJWT = SignedJWT.parse(jwtStr);
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        // 空 deptId 不应包含在 claims 中
        assertNull(claims.getClaim("dept_id"));
    }
}