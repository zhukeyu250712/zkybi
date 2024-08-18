package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-15 15:43
 **/
public class SingleProducer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        // 创建链接工场
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
//        factory.setUsername("xxxx");
//        factory.setPassword("xxxx");
//        factory.setHost("xxxx");
        try (Connection connection = factory.newConnection();
             // 建立链接，常见频道
             Channel channel = connection.createChannel()) {
            // 创建消息队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}