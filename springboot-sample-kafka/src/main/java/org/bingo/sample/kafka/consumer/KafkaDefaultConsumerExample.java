package org.bingo.sample.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaDefaultConsumerExample {
    // 必须: Spring-Kafka默认注入bean="kafkaListenerContainerFactory"的工厂类, 也可以在@KafkaListener中显示指定containerFactory属性
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        // 本例中, 虽然使用了ConcurrenXXXContainerFactory, 但是由于没有指定Concurrency, 因此默认只有单线程工作
        // 注意: 单线程并不等价于一次只能消费一条数据。但是由于设定中也没有设定batch mode, 因此本例中确实是单次request消费1条数据
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(defaultConsumerFactory());
        return factory;
    }
    @Bean
    // 根据consumerProps填写的参数创建消费者工厂
    public ConsumerFactory<String, String> defaultConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }
    //消费者配置参数
    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        // 连接地址
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091, localhost:9092");
        // 序列化方式
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 其他配置 ...
        return props;
    }
    /** 运行前先发送测试数据 {@link org.bingo.sample.kafka.producer.KafkaTemplateTest#test00()} */
    @KafkaListener(id = "consumer_id_default", groupId = "bingo", topics = "bingo.topic.default")
    public void example01(ConsumerRecord<String, String> record) {
        System.out.println("=========KafkaDefaultBatchConsumer start===========");
        System.out.println(record);
        System.out.println("=========KafkaDefaultBatchConsumer end=============");
    }
}
