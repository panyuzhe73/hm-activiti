package com.pp;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootTest
@Slf4j
public class ProcessRunTest {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    /**
     * 流程部署测试
     */
    @Test
    public void testDeployment(){
        // 通过名称查询出之前保存的流程图
        Model model = repositoryService.createModelQuery().modelName("单人审批请假流程").singleResult();
        assertNotNull(model); // 断言查询成功
        byte[] xml = repositoryService.getModelEditorSource(model.getId());// 获取内容
        String xmlName = "itcast.bpmn20.xml";
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                .key(model.getKey())
                .name(model.getName())
                .category(model.getCategory())
                .enableDuplicateFiltering()// 自动去重
                .tenantId(model.getTenantId())
                .addBytes(xmlName, xml);// 设置部署的xml名称、内容
        Deployment deploy = deploymentBuilder.deploy(); // 部署
        assertNotNull(deploy.getId()); // 断言插入成功
        model.setDeploymentId(deploy.getId());
        repositoryService.saveModel(model); // 部署的id更新回Model对象
        log.info("new deployment id : {}", model.getId());
    }

    /**
     * 运行测试
     */
    @ParameterizedTest
//    @ValueSource(strings = {"流程定义key"})
    @ValueSource(strings = {"hiss_process_12_1692440641431"})
    public void testRun(String key){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);
        assertNotNull(processInstance.getId()); // 断言插入成功
        log.info("new processInstance id : {}", processInstance.getId());
    }

}
