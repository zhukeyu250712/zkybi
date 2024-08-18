package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.manager.AiManager;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.service.ChartService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class BiMessageConsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AiManager aiManager;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receiveMessage message = {}", message);
        if(StringUtils.isBlank(message)) {
            // 消息拒绝：抛异常了，应该将当前消息拒绝了
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }
        long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if(chart == null) {
            // 抛异常了，应该将当前消息拒绝了
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图表为空");
        }
        // todo 等待太久了，抛异常，超时时间
        // 等待-->执行中--> 成功/失败
        //先修改图表任务状态为"执行中"。等执行成功后，修改为"已完成"、保存执行结果；
        // 执行失败后，状态修改为"失败"，记录任务失败信息。
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus("running");  // todo  设置枚举值
        boolean updateChartById = chartService.updateById(updateChart);
        if(!updateChartById) {
            // 消息拒绝
            channel.basicNack(deliveryTag, false, false);
//                throw new BusinessException(ErrorCode.OPERATION_ERROR, "图表状态更新失败"); // todo
            handleChartUpdateError(chart.getId(), "更新图表·执行中状态·失败");
            return;
        }
        // 调用鱼皮接口,拿到返回结果
        String result = aiManager.doChat(CommonConstant.BI_MODEL_ID ,buildUserInput(chart));
        //对返回结果做拆分,按照5个中括号进行拆分
        String[] splits = result.split("【【【【【");
        // 拆分之后还要进行校验
        if (splits.length < 3) {
            // 消息拒绝
            channel.basicNack(deliveryTag, false, false);
//                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 生成错误");
            handleChartUpdateError(chart.getId(), "AI生成错误");
            return;
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();

        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setGenResult(genResult);
        updateChartResult.setGenChart(genChart);
        // todo 建议定义为枚举值
        updateChartResult.setStatus("succeed");
        boolean b = chartService.updateById(updateChartResult);
        if(!b) {
            // 消息拒绝
            channel.basicNack(deliveryTag, false, false);
//                throw new BusinessException(ErrorCode.OPERATION_ERROR, "图表状态更新失败"); // todo
            handleChartUpdateError(chart.getId(), "更新图表·成功状态·失败");
        }
        // 消息确认
        channel.basicAck(deliveryTag, false);
    }

    private String buildUserInput(Chart chart) {
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();
        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");
        // 拼接分析目标
        String userGoal = goal;
        // 如果图表类型不为空
        if (StringUtils.isNotBlank(chartType)) {
            // 就将分析目标拼接上“请使用”+图表类型
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        // 压缩后的数据（把multipartFile传进来）
        userInput.append(csvData).append("\n");
        return userInput.toString();
    }

    /**
     * 图表更新错误
     *
     * @param chartId
     * @param execMessage
     */
    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage("execMessage！！");
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败状态失败" + chartId + "," + execMessage);
        }
    }
}
