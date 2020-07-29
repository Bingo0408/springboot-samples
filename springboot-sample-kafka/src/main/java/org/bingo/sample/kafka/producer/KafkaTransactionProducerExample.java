package org.bingo.sample.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AfterRollbackProcessor;
import org.springframework.kafka.listener.DefaultAfterRollbackProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.ServiceMode;
import java.time.LocalDateTime;

/**
 * @Author: Bin GU
 * @Date: 2019/12/30 18:09
 */
@Service
public class KafkaTransactionProducerExample {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    /** 使用方式2: 使用@Transactional注解
     *  前提是配置factory.transactionCapable(); 以及 factory.setTransactionIdPrefix("tx-"); @EnableTransactionManagement; 同时配置TX Manager
     *  @Bean
     *  public KafkaTransactionManager transactionManager(ProducerFactory producerFactory) {
     *    return new KafkaTransactionManager(producerFactory);
     *  }
     *  确认方式: KafkaException: Failing batch since transaction was aborted
     *  注意: 1.开启Spring-Kafka的TransactionManager支持后，所有`kafkaTemplate.send(...)`方法都强制要求加上@Transactional注解
     *  注意: 2.不能直接在UT Case中使用@Transactional测试事务 */
    public void transactionExample(){
        kafkaTemplate.send("bingo.topic.tx", "send success " + LocalDateTime.now());
        throw new RuntimeException();
        // 确认: KafkaException: Failing batch since transaction was aborted, 且消息未被集群持久化(使用Kafka-Tool查看)
    }
}
