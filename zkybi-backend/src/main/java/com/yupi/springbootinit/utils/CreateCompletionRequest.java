package com.yupi.springbootinit.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
public class CreateCompletionRequest {
    /**
     * 模型
     */
    private String model;
    private List<Message> messages;
    /**
     * 提示词
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message{
        private String role = "";
        private String content = "";
    }

    private Integer max_tokens;

    private Integer temperature;

    private Integer top_p;

    private Integer n;

    private Boolean stream;

    private Integer logprobs;

    private String stop;
}
