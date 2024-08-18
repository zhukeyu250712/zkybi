package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author Shier
 */
public class FanoutConsumer {
    private static final String FANOUT_EXCHANGE_NAME = "fanout-exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 rabbitmq 对应的信息
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel1 = connection.createChannel();
        Channel channel2 = connection.createChannel();

        // 声明交换机
        channel1.exchangeDeclare(FANOUT_EXCHANGE_NAME, "fanout");
        //channel2.exchangeDeclare(FANOUT_EXCHANGE_NAME, "fanout");

        // 员工小红
        String queueName1 = "xiaohong_queue";
        channel1.queueDeclare(queueName1, true, false, false, null);
        channel1.queueBind(queueName1, FANOUT_EXCHANGE_NAME, "");
        // 员工小蓝
        String queueName2 = "xiaolan_queue";
        channel2.queueDeclare(queueName2, true, false, false, null);
        channel2.queueBind(queueName2, FANOUT_EXCHANGE_NAME, "");

        System.out.println(" [*] =========================================");

        DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [xiaohong] Received '" + message + "'");
        };

        DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [xiaolan] Received '" + message + "'");
        };

        // 监听消息
        channel1.basicConsume(queueName1, true, deliverCallback1, consumerTag -> {
        });
        channel2.basicConsume(queueName2, true, deliverCallback2, consumerTag -> {
        });
    }
}