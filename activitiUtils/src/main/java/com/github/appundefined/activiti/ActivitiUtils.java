package com.github.appundefined.activiti;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitiUtils {
    public static ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
    public static HistoryService historyService = defaultProcessEngine.getHistoryService();
    public static TaskService taskService = defaultProcessEngine.getTaskService();
    public static RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
    public static RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

    /**
     * 根据流程定义id返回流程name
     * @param procdefId
     * @return
     */
    public static String getProcdefName(String procdefId){
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procdefId).singleResult();
        return processDefinition.getResourceName().replace(".bpmn", "");
    }
    /**
     * 根据任务id返回任务执行人
     * @param taskId
     * @return
     */
    public static HistoricTaskInstance getHistoryTask(String taskId){
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        return historicTaskInstance;
    }
    /**
     * 根据Assignee查询所有关联的流程实例
     * @param assignee
     * @return
     */
    public static Map getHistoryByAssignee(String assignee){
        //key:流程实例  value：流程实例对应的所有任务(value：(key:任务,value:comment))
        HashMap<HistoricActivityInstance,List<HashMap<HistoricActivityInstance,List<Comment>>>> result = new HashMap<>();
        List<HistoricActivityInstance> hais = historyService.createHistoricActivityInstanceQuery().taskAssignee(assignee).list();
        for (HistoricActivityInstance hai : hais) {
            List<HistoricActivityInstance> tasks = historyService.createHistoricActivityInstanceQuery().processInstanceId(hai.getProcessInstanceId()).list();
            //key:任务,value:comment
            List<HashMap<HistoricActivityInstance,List<Comment>>>  taskList = new ArrayList<>();
            for (HistoricActivityInstance t : tasks) {
                HashMap<HistoricActivityInstance, List<Comment>> task = new HashMap<>();
                if(t.getTaskId()==null){
                    continue;
                }
                List<Comment> taskComments = taskService.getTaskComments(t.getTaskId());
                task.put(t,taskComments);
                taskList.add(task);
            }
            //添加所有的流程实例
            result.put(hai,taskList);
        }
        return result;
    }
    /**
     * 根据processInstanceId查询详情
     * @param processInstanceId
     * @return
     */
    public static List getHistoryProcessInstanceDetail(String processInstanceId){
            List<HistoricActivityInstance> tasks = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
            //key:任务,value:comment
            List<HashMap<HistoricActivityInstance,List<Comment>>>  taskList = new ArrayList<>();
            for (HistoricActivityInstance t : tasks) {
                HashMap<HistoricActivityInstance, List<Comment>> task = new HashMap<>();
                if(t.getTaskId()==null){
                    continue;
                }
                List<Comment> taskComments = taskService.getTaskComments(t.getTaskId());
                task.put(t,taskComments);
                taskList.add(task);
            }
        return taskList;
    }
    /**
     * 根据key查询流程实例
     * @param key
     * @return
     */
    public static HashMap<String, Object> getProcessInstanceByKey(String key) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("currentPI", runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(key).list());
        result.put("historyPI", historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(key).list());
        return result;
    }
}
