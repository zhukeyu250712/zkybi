package com.yupi.springbootinit.mq;

import com.rabbitmq.client.*;

public class DirectConsumer {

    private static final String DIRECT_EXCHANGE = "direct-exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(DIRECT_EXCHANGE, "direct");

        // 创建队列，分配一个队列名称：小紫
        String queueName = "zky11_queue";
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, DIRECT_EXCHANGE, "zky1");

        // 创建队列，分配一个队列名称：小黑
        String queueName2 = "zky2_queue";
        channel.queueDeclare(queueName2, true, false, false, null);
        channel.queueBind(queueName2, DIRECT_EXCHANGE, "zky2");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // 小紫队列监听机制
        DeliverCallback xiaoziDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [xiaozi] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        // 小黑队列监听机制
        DeliverCallback xiaoheiDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [xiaohei] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, xiaoziDeliverCallback, consumerTag -> {
        });
        channel.basicConsume(queueName2, true, xiaoheiDeliverCallback, consumerTag -> {
        });
    }
}