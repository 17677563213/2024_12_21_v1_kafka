package com.example.kafka.consumer;

import com.example.kafka.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Kafka消息消费者
 */
@Slf4j
@Component
public class KafkaConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String CONSUMER_RECORD_PREFIX = "kafka:consumer:record:";
    private static final long MESSAGE_TTL = 24 * 60 * 60; // 24 hours in seconds

    /**
     * Kafka消息消费方法
     * @KafkaListener 注解用于监听指定的topic消息
     *   - topics: 指定要监听的主题名称为"test-topic"
     *   - groupId: 指定消费者组ID为"test-consumer-group"
     *
     * @param messageJson 消息的JSON字符串内容
     * @param key 消息的key（通过Kafka消息头获取）
     * @param partition 消息所在的分区ID（通过Kafka消息头获取）
     * @param offset 消息在分区中的偏移量（通过Kafka消息头获取）
     * @param ack 手动确认对象，用于手动提交消费位移
     */
    @KafkaListener(topics = "test-topic-01", groupId = "test-consumer-group")
    public void consume(String messageJson,
                       @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                       @Header(KafkaHeaders.OFFSET) long offset,
                       Acknowledgment ack) {
        String messageId = CONSUMER_RECORD_PREFIX + key;
        
        try {
            // 幂等性检查
            Boolean isFirstProcess = redisTemplate.opsForValue().setIfAbsent(messageId, "1", MESSAGE_TTL, TimeUnit.SECONDS);
            if (Boolean.FALSE.equals(isFirstProcess)) {
                log.info("Message already processed, skipping: [{}]", messageJson);
                ack.acknowledge();
                return;
            }

            // 处理消息
            Message message = objectMapper.readValue(messageJson, Message.class);
            processMessageWithRetry(message);
            
            // 确认消息
            ack.acknowledge();
            log.info("Message processed successfully: [{}], partition: {}, offset: {}", 
                    messageJson, partition, offset);
            
        } catch (Exception e) {
            // 处理失败，删除Redis中的记录，允许重试
            redisTemplate.delete(messageId);
            log.error("Error processing message: [{}]", messageJson, e);
            // 这里不确认消息，让Kafka重新投递
        }
    }

    /**
     * 处理消息的方法，支持重试
     * @Retryable 注解用于指定异常重试策略
     *   - value: 指定异常类型为Exception
     *   - maxAttempts: 指定最大重试次数为3
     *   - backoff: 指定重试延迟策略
     *
     * @param message 消息对象
     * @throws Exception 处理消息时抛出的异常
     */
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void processMessageWithRetry(Message message) throws Exception {
        // 这里实现具体的业务逻辑
        log.info("Processing message: {}", message);
        // 模拟业务处理
        Thread.sleep(100);
    }
}
