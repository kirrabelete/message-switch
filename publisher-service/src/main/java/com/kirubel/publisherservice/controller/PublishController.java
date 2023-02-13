package com.kirubel.publisherservice.controller;

import com.kirubel.publisherservice.model.MessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/publish")
public class PublishController {

    @Value("${topic.exchange.name}")
    String topicExchangeName;

    private final RabbitTemplate rabbitTemplate;

    public PublishController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<String> publishMessage(@RequestBody MessageRequest messageRequest) {

        rabbitTemplate.convertAndSend(topicExchangeName, messageRequest.getRoutingKey(), messageRequest.getMessage());
        log.info("publishing message: " + messageRequest.getMessage());
        return new ResponseEntity<String>("Message submitted successfully to RabbitMQ", HttpStatus.OK);
    }
}
