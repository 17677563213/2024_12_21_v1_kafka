package com.example.kafka.controller;

import com.example.kafka.model.Message;
import com.example.kafka.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping
    public String sendMessage(@RequestBody String content) {
        Message message = new Message(UUID.randomUUID().toString(), content);
        kafkaProducer.sendMessage(message);
        return "Message sent successfully";
    }
}
