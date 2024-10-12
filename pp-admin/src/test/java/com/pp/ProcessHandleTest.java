package com.pp;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Slf4j
public class ProcessHandleTest {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    /**
     * 完成填写清单
     */
    @ParameterizedTest
//    @ValueSource(strings = {"这里替换成act_ru_task表中的id_"})
    @ValueSource(strings = {"50005"})
    public void testCompleteNode2(String taskId){
// 把张三填写的请假单中的数据，作为流程变量，设置到流程中
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName","张三");
        variables.put("startDate","2024-1-1");
        variables.put("days","1");
        variables.put("reason","元旦回家");
        // 标记任务完成
        taskService.complete(taskId, variables);
    }

    /**
     * 同意
     */
    @ParameterizedTest
//   @ValueSource(strings = {"这里替换成act_ru_task表中的id_"})
    @ValueSource(strings = {"35010"})
    public void testAgree(String taskId){
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("approvalStatus","同意");
        variables.put("approvalNote","");
        // 标记任务完成
        taskService.complete(taskId, variables);
    }

    /**
     * 不同意，既拒绝
     */
    @ParameterizedTest
//   @ValueSource(strings = {"这里替换成act_ru_execution表中的流程实例的id_"})
    @ValueSource(strings = {"50002"})
    public void testReject(String processInstanceId){
        String reason = "我不同意！！！";
        //把【同意】看做是填写的【审批表单（包括：审批结果、审批意见）】中的approvalStatus字段
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("approvalStatus", "不同意");
        variables.put("approvalNote", reason);
        //记录流程变量
        runtimeService.setVariables(processInstanceId, variables);
        runtimeService.deleteProcessInstance(processInstanceId, reason);
    }

}
