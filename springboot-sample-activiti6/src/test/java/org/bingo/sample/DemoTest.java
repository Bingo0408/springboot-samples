package org.bingo.sample;

import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoTest {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private RepositoryService repositoryService;
    /**整体流程。
     * 0.需求说明
     * 1.发起人提起流程
     * 2.流程走到im处, 需要全体im组成员通过后(或者1min定时器触发)才算完成
     * 3.流程走到kam处, 任意1个kam组成员通过后即可
     * 4.流程走到SH处, 由于属于外部客户, 因此定时器触发流程(这里是1min), 进入下一个流程
     * 5.流程重新回到im处, 需要全体im组成员通过后(或者1min定时器触发)才算完成
     * 6.流程再次走到kam处, 任意1个kam组成员通过后即可
     * 7.流程结束
     *
     * 1.部署activiti-app.war应用, 推荐MySQL为存储数据库
     * 1-1.在/Kickstart App中导入CPFR_Process.bpmn20.xml流程配置, 同时在Apps标签中发布该应用。
     * 1-2.在/Identity Management中添加用户:im_01, im_02, kam_01, kam_02。添加组:im, kam。同时关联用户和组。
     *     - 或者运行initUsersAndGroup()
     * 2.运行测试用例
     * 2-1.运行test01测试用例, 启动流程。
     * 2-2.登录im_01, complete。确认流程没有进入下一个环节。再登录im_02, complete。确认流程进入下一个环节。
     * 2-3.登录任意一个kam_01或者kam_02, complete。确认流程进入SH节点。
     * 2-4.一分钟后自动触发complete, 确认流程再次进入im环节。
     * 2-5.以此类推, 完成kam流程后, 流程完成。
     */
    /** 查看所有活动工作流 */
//    @Test
    public void displayDetails() {
        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery().active().list();
        System.out.println("============当前已激活实例============");
        for (ProcessInstance instance: instances){
            System.out.println(instance.getId() + " - " + instance.getProcessDefinitionKey());
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getId()).list();
            for (Task task : tasks) {
                System.out.println("- " + task.getExecutionId() + " - " + task.getAssignee() + " - " + task.getName());
            }
        }
    }
    /** 插入用户及组 */
    @Test
    public void initUsersAndGroup() {
        String[] im_user_list = new String[]{"im_01", "im_02"};
        String[] kam_user_list = new String[]{"kam_01", "kam_02"};

        Group g = identityService.newGroup("im");
        g.setName("im");
        identityService.saveGroup(g);
        g = identityService.newGroup("kam");
        g.setName("kam");
        identityService.saveGroup(g);

        for (String user : im_user_list) {
            User u = identityService.newUser(user);
            u.setPassword("test");
            u.setEmail(user + "@test.com");
            u.setFirstName(user);
            u.setLastName("U");
            identityService.saveUser(u);
            identityService.createMembership(user, "im");
        }

        for (String user : kam_user_list) {
            User u = identityService.newUser(user);
            u.setPassword("test");
            u.setEmail(user + "@test.com");
            u.setFirstName(user);
            u.setLastName("U");
            identityService.createMembership(user, "kam");
        }
    }
    @Test
    /** 开始工作流 */
    public void test01() {
        // 将系统中定义的用户按组检索
        List<User> imUsers = identityService.createUserQuery().memberOfGroup("im").list();
        List<User> kamUsers = identityService.createUserQuery().memberOfGroup("kam").list();
        // 初始化环境变量
        Map variables = new HashMap<>();
        variables.put("im_list", imUsers.stream().map(u->u.getId()).collect(Collectors.toList()));
        variables.put("kam_list", kamUsers.stream().map(u->u.getId()).collect(Collectors.toList()));
        variables.put("supplierHasCheckedFlg", 0);
        runtimeService.startProcessInstanceByKey("cpfr_process", variables);
        displayDetails();
    }
    @Test
    /** 按用户id完成工作流: 应该按照task_id完成, 此处默认将该用户所有的task完成 */
    public void test02() {
        String uid = "im_01";
        String task_id = ""; // come from page
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(uid).list();
        if(tasks.size() == 0)
            System.out.println("No task for " + uid);
        for (Task task : tasks) {
            System.out.println(uid + " has completed task:" + task.getId());
            taskService.complete(task.getId());
        }
        displayDetails();
    }
}
