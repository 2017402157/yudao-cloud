package cn.iocoder.yudao.module.maas.service.model;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.maas.controller.admin.model.vo.MaasModelPageReqVO;
import cn.iocoder.yudao.module.maas.controller.admin.model.vo.MaasModelSaveReqVO;
import cn.iocoder.yudao.module.maas.dal.dataobject.MaasModelDO;
import cn.iocoder.yudao.module.maas.dal.mysql.MaasModelMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.maas.enums.ErrorCodeConstants.MAAS_MODEL_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

@Import(MaasModelServiceImpl.class)
public class MaasModelServiceImplTest extends BaseDbUnitTest {

    @Resource
    private MaasModelService maasModelService;

    @Resource
    private MaasModelMapper maasModelMapper;

    @Test
    public void testCreateModel_success() {
        MaasModelSaveReqVO reqVO = new MaasModelSaveReqVO();
        reqVO.setName("gpt-4o");
        reqVO.setProvider("openai");
        reqVO.setInputPricePer1k(new BigDecimal("0.03"));
        reqVO.setOutputPricePer1k(new BigDecimal("0.06"));

        Long id = maasModelService.createModel(reqVO);
        assertNotNull(id);

        MaasModelDO model = maasModelService.getModel(id);
        assertNotNull(model);
        assertEquals("gpt-4o", model.getName());
        assertEquals("openai", model.getProvider());
        assertEquals(0, new BigDecimal("0.03").compareTo(model.getInputPricePer1k()));
    }

    @Test
    public void testCreateModel_duplicateName_throwsException() {
        MaasModelSaveReqVO reqVO1 = new MaasModelSaveReqVO();
        reqVO1.setName("gpt-4o");
        reqVO1.setProvider("openai");
        maasModelService.createModel(reqVO1);

        MaasModelSaveReqVO reqVO2 = new MaasModelSaveReqVO();
        reqVO2.setName("gpt-4o");
        reqVO2.setProvider("anthropic");

        assertThrows(Exception.class, () -> maasModelService.createModel(reqVO2));
    }

    @Test
    public void testUpdateModel_success() {
        Long id = createTestModel("gpt-4o", "openai");

        MaasModelSaveReqVO updateReqVO = new MaasModelSaveReqVO();
        updateReqVO.setId(id);
        updateReqVO.setName("gpt-4o");
        updateReqVO.setProvider("openai");
        updateReqVO.setInputPricePer1k(new BigDecimal("0.05"));

        maasModelService.updateModel(updateReqVO);

        MaasModelDO model = maasModelService.getModel(id);
        assertEquals(0, new BigDecimal("0.05").compareTo(model.getInputPricePer1k()));
    }

    @Test
    public void testDeleteModel_success() {
        Long id = createTestModel("gpt-4o", "openai");

        maasModelService.deleteModel(id);

        assertNull(maasModelService.getModel(id));
    }

    @Test
    public void testDeleteModel_notExists_throwsException() {
        assertThrows(Exception.class, () -> maasModelService.deleteModel(99999L));
    }

    @Test
    public void testGetModelPage_success() {
        createTestModel("gpt-4o", "openai");
        createTestModel("claude-3-opus", "anthropic");

        MaasModelPageReqVO pageReqVO = new MaasModelPageReqVO();
        pageReqVO.setProvider("openai");

        PageResult<MaasModelDO> pageResult = maasModelService.getModelPage(pageReqVO);
        assertEquals(1, pageResult.getTotal());
        assertEquals("gpt-4o", pageResult.getList().get(0).getName());
    }

    @Test
    public void testGetAvailableModelList_success() {
        Long id = createTestModel("gpt-4o", "openai");

        var list = maasModelService.getAvailableModelList();
        assertFalse(list.isEmpty());
        assertEquals("gpt-4o", list.get(0).getName());
    }

    @Test
    public void testSyncModelFromWebhook_create() {
        String payload = """
                {"channel":{"model":"claude-3-opus","provider":"anthropic","id":1}}
                """;

        maasModelService.syncModelFromWebhook("channel_create", payload);

        var list = maasModelMapper.selectList();
        assertFalse(list.isEmpty());
        assertEquals("claude-3-opus", list.get(0).getName());
        assertEquals(1, list.get(0).getChannelCount());
    }

    @Test
    public void testSyncModelFromWebhook_delete() {
        Long id = createTestModel("gpt-4o", "openai");
        MaasModelDO model = maasModelMapper.selectById(id);
        model.setChannelCount(2);
        maasModelMapper.updateById(model);

        String payload = """
                {"channel":{"model":"gpt-4o","provider":"openai","id":1}}
                """;

        maasModelService.syncModelFromWebhook("channel_delete", payload);

        MaasModelDO updated = maasModelMapper.selectById(id);
        assertEquals(1, updated.getChannelCount());
        assertTrue(updated.getAvailable());
    }

    private Long createTestModel(String name, String provider) {
        MaasModelSaveReqVO reqVO = new MaasModelSaveReqVO();
        reqVO.setName(name);
        reqVO.setProvider(provider);
        return maasModelService.createModel(reqVO);
    }

}