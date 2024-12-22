package com.example.kafka.producer;

import com.example.kafka.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class KafkaProducer {

    private static final String TOPIC = "test-topic-01";
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage(Message message) {
        try {
            String messageJson = objectMapper.writeValueAsString(message);
            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, message.getId(), messageJson);
            
            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onSuccess(SendResult<String, String> result) {
                    log.info("Message sent successfully: [{}]", messageJson);
                    log.info("Partition: {}, Offset: {}", 
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                }

                @Override
                public void onFailure(Throwable ex) {
                    log.error("Unable to send message: [{}]", messageJson, ex);
                }
            });
        } catch (JsonProcessingException e) {
            log.error("Error converting message to JSON", e);
        }
    }
}
