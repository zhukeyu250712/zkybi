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
public class OpenaiUtil {
    final static String url = "https://api.openai.com/v1/chat/completions";
    //final static String apiKey = "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";
    //final static String apiKey = "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";
    final static String apiKey = "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";
    static final  int TIMEOUT_MSEC = 5 * 1000;
    public String doPost(String tip){
        // 设置代理主机和端口号
        String proxyHost = "127.0.0.1";
        int proxyPort = 7890; // 代理端口号

        // 创建代理对象
        HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        CloseableHttpClient httpClient = HttpClients.custom().setProxy(proxy).build();

        CreateCompletionRequest completionRequest = new CreateCompletionRequest();
        completionRequest.setModel("gpt-3.5-turbo");
        completionRequest.setTemperature(1);
        completionRequest.setMax_tokens(1000);
        ArrayList<CreateCompletionRequest.Message> messages = new ArrayList<>();
        messages.add(new CreateCompletionRequest.Message("user",tip));
        completionRequest.setMessages(messages);


        CloseableHttpResponse response = null;

        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type","application/json");
        post.addHeader("Authorization", "Bearer " + apiKey);


        String jsonStr = JSONUtil.toJsonStr(completionRequest);
        StringEntity entity = new StringEntity(jsonStr,ContentType.APPLICATION_JSON);
        post.setEntity(entity);

        post.setConfig(builderRequestConfig());
        try {
            response = httpClient.execute(post);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                // 解析响应
                String responseString = EntityUtils.toString(responseEntity);

                CreateCompletionResponse completionResponse = JSONUtil.toBean(responseString, CreateCompletionResponse.class);
                //System.out.println(responseString);
                //System.out.println("====================");
                //System.out.println(completionResponse);
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
