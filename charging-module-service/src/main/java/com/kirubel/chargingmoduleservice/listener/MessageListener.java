package com.kirubel.chargingmoduleservice.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageListener {

    @RabbitListener(queues = "${message.queue.name}")
    public void receiveMessage(String message) {
        log.info("Consuming message from all messages queue - " + message);
    }
}
