package com.kirubel.publisherservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirubel.publisherservice.model.MessageRequest;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublishController.class)
public class PublishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void testPublishMessage() throws Exception{

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setRoutingKey("haud.address.one");
        messageRequest.setMessage("hello, sir");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestContent = objectMapper.writeValueAsString(messageRequest);

        mockMvc.perform(post("/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Message submitted successfully to RabbitMQ"));
    }
}
