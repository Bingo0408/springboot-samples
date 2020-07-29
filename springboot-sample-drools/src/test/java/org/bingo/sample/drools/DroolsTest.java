package org.bingo.sample.drools;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DroolsTest {

    @Autowired
    private KieContainer kieContainer;

    @Test
    public void test01() {
        Product product = new Product("diamond", 0);

        KieSession kieSession = kieContainer.newKieSession("rule-v2");
        kieSession.insert(product);
        int ruleFiredCount = kieSession.fireAllRules();
        kieSession.dispose(); // 清理KieSession的维护状态, 确保规则引擎无状态

        System.out.println("共触发了"+ruleFiredCount+"条规则。");
        System.out.println(product);
    }

}
