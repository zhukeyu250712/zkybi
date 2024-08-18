package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-18 00:54
 **/
@Component
@Slf4j
public class MyMessageConsumer {
    /**
     * 指定程序监听的消息队列和确认机制
     * @param message
     * @param channel
     * @param deliveryTag
     */
    @RabbitListener(queues = {"demo_queue"}, ackMode = "MANUAL")
    private void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        log.info("receiveMessage = {}",message);
        try {
            // 手动确认消息
            channel.basicAck(deliveryTag,false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
