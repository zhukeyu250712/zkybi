package com.yupi.springbootinit.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-12 17:51
 **/
@Data
@ConfigurationProperties(prefix = "spring.redis")
@Configuration
public class RedissonConfig {
    private Integer database;
    private String host;
    private Integer port;
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
//                .setDatabase(database)
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password);
//        config.useSingleServer().setAddress("redis://127.0.0.1:6380").setPassword("123456");
//        config.useSingleServer().setAddress("redis://192.168.1.100:6379").setPassword("123456");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}
