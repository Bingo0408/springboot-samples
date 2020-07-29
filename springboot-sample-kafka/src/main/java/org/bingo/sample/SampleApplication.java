package org.bingo.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableKafka
@EnableTransactionManagement
/** ProducerConfig - 定义了生产者的配置参数
 *  ConsumerConfig - 定义了消费者的配置参数
 *  1. KafkaTemplate提供了消息生产者的高级API
 *  2. @SendTo提供了消息转发(监听方法的返回值)的功能, 例如, 处理完订单信息后自动将消息发到仓库系统, 实现方式如下
 *  - A.配置ConcurrentKafkaListenerContainerFactory.setReplyTemplate(...)方法
 *  - B.监听的方法上添加@SendTo("topic.xx.xx")注解
 *  3. 使用@KafkaListener监听器实现各种消费者
 *  4. 禁止消费者自启动(例如, 可以在业务空闲时间启动消费者任务)
 *  5. 消息过滤器, 只需要在容器工厂配置一个RecordFilterStrategy即可 */

/** 6. 设置@KafkaListener的errorHandler
 *  - 默认注册BeanName, 见public ConsumerAwareListenerErrorHandler consumerAwareErrorHandler(), 单条消息与多条消息区别见于被覆写的方法
 *  - 关于事务处理中Rollback后续处理器:
 *    1.默认情况下, DefaultAfterRollbackProcessor会执行seek操作在下一个poll中重新拉取数据。如果是batch mode, 会重新处理所有批次数据而不仅仅出错的那条(因为不知道是哪一条)
 *    2.如果想要改变这一行为, 可以使用AfterRollbackProcessor, 使用record-based的监听器可以重试到指定次数后将信息推往dead-letter topic */
public class SampleApplication {
	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}
}

