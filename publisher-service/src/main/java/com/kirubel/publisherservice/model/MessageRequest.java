package com.kirubel.publisherservice.model;

import lombok.Data;

@Data
public class MessageRequest {

    private String routingKey;

    private String message;
}
