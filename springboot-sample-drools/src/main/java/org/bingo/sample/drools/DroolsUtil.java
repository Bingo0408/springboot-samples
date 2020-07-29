package org.bingo.sample.drools;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

@Slf4j
public class DroolsUtil {
    /** 线程安全单例 */
    private static volatile KieServices kieServices = KieServices.Factory.get();
    /** KieBase容器，线程安全单例 */
    private static volatile KieContainer kieContainer;
    /** 加载KieContainer容器 */
    public static KieContainer loadKieContainer() throws RuntimeException {
        if (kieContainer == null) {
            // 设置drools的日期格式
            System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
            synchronized (DroolsUtil.class) {
                if (kieContainer == null) {
                    kieContainer = kieServices.getKieClasspathContainer();
                    Results results = kieContainer.verify();
                    if (results.hasMessages(Message.Level.ERROR)) {
                        StringBuffer sb = new StringBuffer();
                        for (Message mes : results.getMessages()) {
                            sb.append("解析错误的规则：").append(mes.getPath()).append(" 错误位置：").append(mes.getLine()).append(";");
                        }
                        throw new RuntimeException(sb.toString());
                    }
                }
            }
        }
        return kieContainer;
    }

    /**根据kiesession 名称创建KieSession, 每次调用都是一个新的KieSession
     * @param name kieSession的名称
     * @return 新的KieSession，每次使用后要销毁 .destroy() */
    public static KieSession getKieSessionByName(String name) {
        if (kieContainer == null) {
            kieContainer = loadKieContainer();
        }
        KieSession kieSession;
        try {
            kieSession = kieContainer.newKieSession(name);
        } catch (Exception e) {
            log.error("根据名称：" + name + " 创建kieSession异常");
            throw new RuntimeException(e);
        }
        return kieSession;
    }
}
