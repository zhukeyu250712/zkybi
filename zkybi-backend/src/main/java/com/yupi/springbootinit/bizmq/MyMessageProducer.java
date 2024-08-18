package com.yupi.springbootinit.bizmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-18 00:52
 **/
@Component
public class MyMessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;
    /**
     * 发送消息
     * @param exchange
     * @param routingKey
     * @param message
     */
    public void sendMessage(String exchange, String routingKey, String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
