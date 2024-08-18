package com.yupi.springbootinit.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.util.StringUtils;
import com.yupi.springbootinit.common.ErrorCode;
import org.springframework.stereotype.Service;

/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-06-11 15:48
 **/

//@Service
public class OpenAiApi {
//    /**
//     * AI 对话（需要自己创建请求响应对象）
//     *
//     * @param request
//     * @param openAiApiKey
//     * @return
//     */
//    public CreateChatCompletionResponse createChatCompletion(CreateChatCompletionRequest request, String openAiApiKey) {
//        if (StringUtils.isBlank(openAiApiKey)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未传 openAiApiKey");
//        }
//        String url = "https://api.openai.com/v1/chat/completions";
//        String json = JSONUtil.toJsonStr(request);
//        String result = HttpRequest.post(url)
//                .header("Authorization", "Bearer " + openAiApiKey)
//                .body(json)
//                .execute()
//                .body();
//        return JSONUtil.toBean(result, CreateChatCompletionResponse.class);
//    }
}
