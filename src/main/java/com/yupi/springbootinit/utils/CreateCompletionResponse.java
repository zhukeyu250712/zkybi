package com.yupi.springbootinit.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class CreateCompletionResponse {
    private Integer created;

    private Usage usage;

    private String model;

    private String id;

    private String system_fingerprint;


    /**
     * 回答列表
     */
    private List<ChoicesItem> choices;

    private String object;

    @Data
    public static class ChoicesItem {

        private String finishReason;

        private Integer index;
        private Message message;
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Message{
            private String role;
            private String content;
        }

        private Integer logprobs;
    }

    @Data
    public static class Usage {

        private Integer completionTokens;

        private Integer promptTokens;

        private Integer totalTokens;
    }
}
