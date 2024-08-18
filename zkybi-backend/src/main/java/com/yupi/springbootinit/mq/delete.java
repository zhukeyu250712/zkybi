package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;
/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-16 01:50
 **/

public class delete {
    private static final String TASK_QUEUE_NAME = "multi_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             // 获取RabbitMQ的Channel
             Channel channel = connection.createChannel()) {
            // 需要删除的队列名
            channel.queueDelete(TASK_QUEUE_NAME);
        }
    }
}
