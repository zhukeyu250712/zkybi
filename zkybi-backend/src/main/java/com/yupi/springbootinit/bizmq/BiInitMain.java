package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-18 00:58
 * 创建测试测序用到的交换机和队列 (仅执行一次)
 **/

public class BiInitMain {
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            // 设置 rabbitmq 对应的信息
            factory.setHost(BiMqConstant.BI_HOST);            // 创建连接到 RabbitMQ 服务器的连接工厂

            Connection connection = factory.newConnection();
            // 用于与 RabbitMQ 服务器的通信
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = BiMqConstant.BI_EXCHANGE_NAME;
            // 定义交换机的名称
            channel.exchangeDeclare(EXCHANGE_NAME, BiMqConstant.BI_DIRECT_EXCHANGE);

            // 创建队列，分配一个队列名称
            String queueName = BiMqConstant.BI_QUEUE_NAME;
            // 定义队列的名称
            channel.queueDeclare(queueName, true, false, false, null);
            // 将队列绑定到交换机
            channel.queueBind(queueName, EXCHANGE_NAME, BiMqConstant.BI_ROUTING_KEY);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
