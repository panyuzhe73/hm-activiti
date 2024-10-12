package com.pp;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootTest
@Slf4j
public class ProcessSaveTest {

    @Autowired
    RepositoryService repositoryService;

    /**
     * 流程保存测试
     */
    @ParameterizedTest
    @ValueSource(strings = {"D:\\Desktop\\bpmn-01.xml"})
    public void testSaveFromFile(String file){
        String xml = FileUtil.readUtf8String(file);
        Model model = repositoryService.newModel();
        model.setName("单人审批请假流程"); // 设置名称
        model.setCategory("学习"); // 设置分类
        repositoryService.saveModel(model);// 保存基本信息
        repositoryService.addModelEditorSource(model.getId(),xml.getBytes());// 保存xml信息
        assertNotNull(model.getId()); // 断言插入成功
        log.info("new model id : {}", model.getId());
    }

}
