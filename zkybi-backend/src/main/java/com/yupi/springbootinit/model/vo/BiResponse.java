package com.yupi.springbootinit.model.vo;

import lombok.Data;

/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-06-11 16:42
 **/

/**
 * Bi 的返回结果
 */
@Data
public class BiResponse {
    private String genChart;

    private String genResult;
    // 新生成的图标id
    private Long chartId;
}
