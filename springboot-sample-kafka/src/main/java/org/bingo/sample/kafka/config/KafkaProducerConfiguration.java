/**
 * @Author: Bin GU
 * @Date: 2019/12/13 17:55
 */
package org.bingo.sample.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaProducerConfiguration {
    @Bean
    // 创建生产者工厂
    public ProducerFactory<String, String> producerFactory() {
        DefaultKafkaProducerFactory factory = new DefaultKafkaProducerFactory<>(senderProps());
        // 如果需要支持TX, 需要增加如下配置
        factory.transactionCapable();
        factory.setTransactionIdPrefix("tx-");
        return factory;
    }
    @Bean
    // 如果需要使用@Transactional注解, 则必须配置如下TX Manager. 使用本地事务kafkaTemplate.executeInTransaction则不需要配置
    public KafkaTransactionManager transactionManager(ProducerFactory<?,?> producerFactory) {
        KafkaTransactionManager tm = new KafkaTransactionManager(producerFactory);
        return  tm;
    }
    @Bean
    // KafkaTemplate实现了Kafka发送消息功能, 构造函数中有一个参数autoFlush表明是否每次生产消息后都flush, 开启时会显著影响性能
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<?,?> producerFactory) {
        KafkaTemplate<String, String> template = new KafkaTemplate(producerFactory);
        return template;
    }
    //生产者配置
    private Map<String, Object> senderProps (){
        Map<String, Object> props = new HashMap<>();
        // 连接地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // 失败重试，0为不启用重试机制
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        // 序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 其他配置 ...
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        props.put(ProducerConfig.RETRIES_CONFIG, "发送失败时的重试次数");
        return props;
    }
}
