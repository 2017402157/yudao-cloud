-- MaaS SSO: 注册三个子系统为 yudao-cloud OAuth2 客户端
-- 插入到 system_oauth2_client 表
-- 注意：client_secret 需要在生产环境中替换为实际值
-- 幂等性：使用 INSERT IGNORE / ON DUPLICATE KEY UPDATE

-- lobehub OAuth2 客户端
INSERT INTO system_oauth2_client (client_id, secret, name, logo, description, status, access_token_validity_seconds, refresh_token_validity_seconds, redirect_uris, authorized_grant_types, scopes, auto_approve_scopes, authorities, resource_ids, additional_information, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES ('lobehub-maas', '{bcrypt}$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX', 'LobeHub AI 对话', '', 'LobeHub MaaS AI 对话平台', 0, 7200, 43200, '["http://localhost:3210/api/auth/callback/custom","http://lobehub:3210/api/auth/callback/custom"]', '["authorization_code","refresh_token"]', '["openid","profile","user_info"]', '["openid","profile"]', '', '', '{}', '1', NOW(), '1', NOW(), 0, 1)
ON DUPLICATE KEY UPDATE update_time = NOW();

-- ragflow OAuth2 客户端
INSERT INTO system_oauth2_client (client_id, secret, name, logo, description, status, access_token_validity_seconds, refresh_token_validity_seconds, redirect_uris, authorized_grant_types, scopes, auto_approve_scopes, authorities, resource_ids, additional_information, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES ('ragflow-maas', '{bcrypt}$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX', 'RAGFlow 知识库', '', 'RAGFlow MaaS 知识库引擎', 0, 7200, 43200, '["http://localhost:9382/oauth/callback/maas","http://ragflow:9382/oauth/callback/maas"]', '["authorization_code","refresh_token"]', '["openid","profile","user_info"]', '["openid","profile"]', '', '', '{}', '1', NOW(), '1', NOW(), 0, 1)
ON DUPLICATE KEY UPDATE update_time = NOW();

-- new-api OAuth2 客户端
INSERT INTO system_oauth2_client (client_id, secret, name, logo, description, status, access_token_validity_seconds, refresh_token_validity_seconds, redirect_uris, authorized_grant_types, scopes, auto_approve_scopes, authorities, resource_ids, additional_information, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES ('newapi-maas', '{bcrypt}$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX', 'New-API 模型网关', '', 'New-API MaaS 模型管理网关', 0, 7200, 43200, '["http://localhost:3000/oauth/callback","http://new-api:3000/oauth/callback"]', '["authorization_code","refresh_token"]', '["openid","profile","user_info"]', '["openid","profile"]', '', '', '{}', '1', NOW(), '1', NOW(), 0, 1)
ON DUPLICATE KEY UPDATE update_time = NOW();

-- 验证
SELECT client_id, name, status FROM system_oauth2_client WHERE client_id LIKE '%-maas';