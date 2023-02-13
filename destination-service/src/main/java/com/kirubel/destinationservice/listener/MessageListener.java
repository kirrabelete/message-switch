package com.kirubel.destinationservice.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class MessageListener {

    @RabbitListener(queues = "${message.queue.name}")
    public void receiveMessage(String messageContent, Channel channel, Message message) throws IOException {

        log.info("received filtered queue message: " + messageContent);

        try {
            //notify rabbitmq message has been received successfully
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("acknowledged getting message successfully");

        } catch (Exception e) {
            log.error("received incorrect message info" + e.getMessage());
            //reject and discard the message
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}
