package com.yupi.springbootinit.manager;

import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.utils.CreateCompletionRequest;
import com.yupi.springbootinit.utils.CreateCompletionResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
@Slf4j
public class OpenaiManager {
    // 0、类和静态变量定义
//    final static String url = "https://api.openai.com/v1/models";
    final static String url = "https://api.openai.com/v1/chat/completions";
    final static String apiKey = "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";
//    final static String apiKey = "sk-proj-AkmSM3fEvYLEt2kESxhhT3BlbkFJoGxwjukMwmTV7v3OmRjn";

    // wjm
//    final static String apiKey = "sk-proj-OZNubzf4ZDC00l4C0Ul6T3BlbkFJPLzYEmgvv7LgZ47uCy55";
    static final  int TIMEOUT_MSEC = 5 * 1000; // 请求超时时间设置为5秒（5000毫秒）

    public String doPost(String prompt){   // 接收用户输入的提示信息
        // 1、设置代理主机和端口号
        String proxyHost = "127.0.0.1";
        int proxyPort = 7897; // 代理端口号

        // 2、创建代理对象
        HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        CloseableHttpClient httpClient = HttpClients.custom().setProxy(proxy).build();

        // 3、创建请求
        CreateCompletionRequest completionRequest = new CreateCompletionRequest();
        completionRequest.setModel("gpt-3.5-turbo");
        completionRequest.setTemperature(1);    // 用于控制生成的多样性
        completionRequest.setMax_tokens(1000);  // 响应中生成的最大令牌数
        // 创建一个消息列表并添加用户输入的提示信
        ArrayList<CreateCompletionRequest.Message> messages = new ArrayList<>();
        messages.add(new CreateCompletionRequest.Message("user",prompt));
        completionRequest.setMessages(messages);


        // 4、配置请求头和主体
        CloseableHttpResponse response = null;
        HttpPost post = new HttpPost(url);  // 创建POST请求对象
        post.addHeader("Content-Type","application/json");
        post.addHeader("Authorization", "Bearer " + apiKey);
        String jsonStr = JSONUtil.toJsonStr(completionRequest);  // 对象转换为JSON字符串
        StringEntity entity = new StringEntity(jsonStr,ContentType.APPLICATION_JSON);
        post.setEntity(entity);     //设置为请求主体
        post.setConfig(builderRequestConfig());  //设置请求配置

        // 5、执行请求和处理响应： 执行HTTP请求
        try {
            response = httpClient.execute(post);
            HttpEntity responseEntity = response.getEntity();
            // 解析响应字符串
            if (responseEntity != null) {
                // 解析响应
                String responseString = EntityUtils.toString(responseEntity);
                CreateCompletionResponse completionResponse = JSONUtil.toBean(responseString, CreateCompletionResponse.class);
                System.out.println(responseString);
                System.out.println("====================");
                System.out.println(completionResponse);
                // 如果响应中有结果，返回第一个结果的内容；否则返回空字符串
                if(completionResponse.getChoices()==null){
                    return "";
                }
                return completionResponse.getChoices().get(0).getMessage().getContent();
            }
        } catch (IOException e) {
            log.info("AI请求出错："+e.getMessage());
        }
        return "";
    }
    private static RequestConfig builderRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_MSEC)
                .setConnectionRequestTimeout(TIMEOUT_MSEC)
                .setSocketTimeout(TIMEOUT_MSEC).build();
    }
}
