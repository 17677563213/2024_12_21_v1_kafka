package com.example.kafka.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String content;
    private LocalDateTime timestamp;
    
    public Message() {
    }
    
    public Message(String id, String content) {
        this.id = id;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
}
