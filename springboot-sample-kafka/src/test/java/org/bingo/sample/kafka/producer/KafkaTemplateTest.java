/**
 * @Author: Bin GU
 * @Date: 2019/12/13 17:55
 */
package org.bingo.sample.kafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KafkaTemplateTest {
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Test
    /** 用于测试Consumer的不同配置 */
    public void test00() {
        while (true) {
            kafkaTemplate.send("bingo.topic.default", 0, System.currentTimeMillis(), "key", "this is a kafka message " + LocalDateTime.now());
            kafkaTemplate.send("bingo.topic.batch", 0, System.currentTimeMillis(), "key", "this is a kafka message " + LocalDateTime.now());
            kafkaTemplate.send("bingo.topic.ack", 0, System.currentTimeMillis(), "key", "this is a kafka message " + LocalDateTime.now());
        }
    }
    @Test
    /** sendDefault(...): 发送消息到默认Topic
     *  send(...) 带有timestamp的消息会将该时间戳记入消息 */
    public void test01() {
        // 使用方式1: 指定Topic
        kafkaTemplate.send("bingo.topic", 0, System.currentTimeMillis(), "key", "this is a kafka message " + LocalDateTime.now());
        // 使用方式2: 使用Default Topic
        kafkaTemplate.setDefaultTopic("bingo.topic.defaultTemplate");
        kafkaTemplate.sendDefault("this data is sent to default topic " + LocalDateTime.now());
    }
    @Test
    /** kafkaTemplate支持的3种消息发送方式
     *  使用方式1: 普通方式 send(...)
     *  使用方式2: 使用ProducerRecord
     *  使用方式3: Message */
    public void test02() {
        // 使用方式1: 普通方式
        kafkaTemplate.send("bingo.topic", "this is a kafka message " + LocalDateTime.now());
        // 使用方式2: 使用ProducerRecord
        ProducerRecord record = new ProducerRecord("bingo.topic.sent.by.producerRecord", "use ProducerRecord to send message " + LocalDateTime.now());
        kafkaTemplate.send(record);
        // 使用方式3: 使用Message
        Map map = new HashMap();
        map.put(KafkaHeaders.TOPIC, "bingo.topic.sent.by.message");
        map.put(KafkaHeaders.PARTITION_ID, 0);
        map.put(KafkaHeaders.MESSAGE_KEY, "key");
        Message message = new GenericMessage("this message is sent by using Message " + LocalDateTime.now(), new MessageHeaders(map));
        kafkaTemplate.send(message);
    }
    @Test
    /** 设置KafkaTemplate的ProducerListener回调方法 */
    public void test03() {
        class KafkaSendResultHandler implements ProducerListener<String, String> {
            @Override
            public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
                System.out.println("printed by ProducerListener: SUCCESS " + producerRecord.toString() + "--" + recordMetadata.toString());
            }
            @Override
            public void onError(ProducerRecord producerRecord, Exception exception) {
                System.out.println("printed by ProducerListener: ERROR " + producerRecord.toString());
            }
        }
        kafkaTemplate.setProducerListener(new KafkaSendResultHandler());
        System.out.println("==================================================");
        kafkaTemplate.send("bingo.topic", "this is a kafka message " + LocalDateTime.now());
    }
    @Test
    /** 1.同步发送消息: send(...).get()
     *  2.异步发送消息: 使用ListenableFuture接收send(...) */
    public void test04() throws InterruptedException {
        // 2.异步发送消息
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("bingo.topic", "this is a kafka message " + LocalDateTime.now());
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("this is Async Success Callback: " + result.getProducerRecord());
            }
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("this is Async Failure Callback: " + throwable);
            }
        });
        System.out.println("Sleeping ....");
        TimeUnit.SECONDS.sleep(1);
    }

    /** Kafka使用事务的2种方式
     *  - 前提: 配置factory.transactionCapable(); 以及 factory.setTransactionIdPrefix("tx-"); */

    @Test
    /** 使用方式1: 使用KafkaTemplate.executeInTransaction(...)
     *  确认方式: 抛出KafkaException: Failing batch since transaction was aborted, 且消息未被集群持久化(使用Kafka-Tool查看) */
    public void test05_01(){
        // 使用方式1: 使用KafkaTemplate.executeInTransaction(...)
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send("bingo.topic.tx", "send success " + LocalDateTime.now());
            throw new RuntimeException();
            // 确认: KafkaException: Failing batch since transaction was aborted, 且消息未被集群持久化(使用Kafka-Tool查看)
        });
    }

    @Autowired
    private KafkaTransactionProducerExample kafkaTransactionProducerExample;
    @Test
    /** 使用方式2: 使用@Transactional注解 */
    /** 在需要执行事务的方法上.transactionExample()添加@Transactional注解即可, 此外需要开启事务支持@EnableTransactionManagement
     *  此处, 没有在@Test方法内直接编写@Transactional测试用例而是另外重写Service是因为Spring事务在这种情况下不工作 */
    public void test05_02() {
        kafkaTransactionProducerExample.transactionExample();
    }

    @After
    public void afterAction() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
    }
}
