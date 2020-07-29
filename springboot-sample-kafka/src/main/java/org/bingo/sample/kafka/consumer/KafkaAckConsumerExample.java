package org.bingo.sample.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.bingo.sample.kafka.config.KafkaConsumerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Bin GU
 * @Date: 2018/12/16 10:55
 */
@Component
/** @KafkaListener
 *  1.知识点: <p>吞吐量及并行度</p> 见{@link KafkaAckConsumerExample#batchKafkaContainerFactory()}
 *  *  - 单线程还是多线程消费: 取决于ConcurrentKafkaListenerContainerFactory.setConcurrency(5)设置的并发线程数
 *  *  - 批量消费还是单条消费: 取决于ConcurrentKafkaListenerContainerFactory.setBatchListener(true), 并支持设置max.poll.records单次最大拉取数, max.poll.interval.ms设置最大间隔时间
 *  2.主要参数
 *  - 如果不指定group_id, 默认使用id作为group_id(idIsGroup属性可以设置禁用该功能)
 *  - topicPartitions: 配置消费的分区以及offset
 *  - containerFactory: 配置消费工厂(定义消费者并发线程数, 单次拉取消费量), 不配置的话默认查找kafkaListenerContainerFactoryBean
 *  - concurrency/errorHandler/autoStartup: overwrite工厂类中的定义 */
public class KafkaAckConsumerExample {
    @Bean
    /**Kafka监听器容器的工程类
     * - .setConcurrency(Integer): Consumer并行线程数
     * - .setBatchListener(boolean): 每次是否拉取批量数据, 取true时应配置 max.poll.records(最大拉取数), max.poll.interval.ms(最大间隔时间)
     * - .setAutoStartup(boolean): 禁止KafkaListener自启动, 可以在其它时间段再手工启动消费者
     * - .setErrorHandler(): 异常处理类 */
    public ConcurrentKafkaListenerContainerFactory<String, String> batchAckKafkaContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(5);
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        // 设置ACK的作用机制
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }
    @Bean
    // 根据consumerProps填写的参数创建消费者工厂
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }
    //消费者配置参数
    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091, localhost:9092");
        // 序列化方式
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 其他配置(设置单次最大拉取消息数量及禁止自动提交offset)
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return props;
    }
    /** Kafka中的Topic消费一般使用消息监听器实现(大多继承自GenericMessageListener), 大致分为
     *  - 单条消费: MessageListener
     *  - 批量消费: BatchMessageListener
     *  - ACK支持: AcknowledgingMessageListener 和 BatchAcknowledgingMessageListener
     *  ...
     *  @KafkaListener会根据入参类型自动封装
     *  举例如下:
     *  单次拉取多条数据: List<ConsumerRecord<String, String>>
     *  单次拉取数据的条数: ConsumerConfig.MAX_POLL_RECORDS_CONFIG - 5条
     *  同时工作的工作线程数量: .setConcurrency(5) 设置batch处理模式.setBatchListener(true) --不设置时只支持每次拉取1条数据
     *
     *  如何开启使用ACK机制确认消费
     *  1.设置ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG=false禁止自动提交
     *  2.ConcurrentKafkaListenerContainerFactory.ContainerProperties.AckMode.MANUAL_IMMEDIATE
     *  3.@KafkaListener监听方法的入参加入Acknowledgment ack参数
     *  4.设定@KafkaListener(.., containerFactory=你定义的containerFactoryBean)
     *  拒绝消息时, 直接不要调用ack.acknowledge()即可
     *
     *  重新消费未被ACK的消息, 可以
     *  1.将消息重新发送回消费队列中(实际使用中, 可以通过设定HEADER的自定义头来限制循环消费的最大次数)
     *  2.consumer.seek方法可以重新回到该未ack的消息偏移量的位置重新消费 */
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @KafkaListener(id = "consumer_id_ack", groupId = "bingo", topics = "bingo.topic.ack", containerFactory = "batchAckKafkaContainerFactory")
    public void example01(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        System.out.println("=========KafkaAckConsumer start===========");
        records.forEach(recode -> {
            if(recode.offset() % 2 == 0) {
                System.out.println("ack - " + recode);
                // 只消费偏移量为偶数的消息
                ack.acknowledge();
            } else {
                System.out.println("nack - " + recode);
                kafkaTemplate.send("bingo.topic.nack", recode.value());
            }
        });
        System.out.println("=========KafkaAckConsumer end=============");
    }
}
