package com.yupi.springbootinit.manager;


import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiManagerTest {

    @Resource
    private AiManager aiManager;

    @Test
    void doChat() {
        String prompt = "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n" +
                "分析需求：\n" +
                "{数据分析的需求或者目标}\n" +
                "原始数据：\n" +
                "{csv格式的原始数据，用,作为分隔符}\n" +
                "请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\n" +
                "【【【【【\n" +
                "{前端 Echarts V5 的 option 配置对象的json格式代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释}\n" +
                "【【【【【\n" +
                "{明确的数据分析结论、越详细越好，不要生成多余的注释}";
//        https://www.yucongming.com/model/1654785040361893889?inviteUser=1800423670920454145
        String answer = aiManager.doChat(1821914479207112706L, prompt);
        System.out.println(answer);
    }

    @Test
    void openAiTest() {
//配置api keys
        OpenAiClient openAiClient = new OpenAiClient("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");
        CompletionResponse completions = openAiClient.completions("三体人是什么？");
        Arrays.stream(completions.getChoices()).forEach(System.out::println);
    }
}