import { UploadOutlined } from '@ant-design/icons';
import {Button, Card, Form, Input, message, Select, Space, Upload} from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React, { useState } from 'react';
import {
    genChartByAiAsyncMqUsingPost,
    genChartByAiAsyncUsingPost,
    genChartByAiUsingPost
} from "@/services/yubi/chartController";
import {useForm} from "antd/es/form/Form";

/**
 * 添加图表页面（异步）
 * @constructor
 */
const AddChartAsync: React.FC = () => {
    const [form, serForm] = useForm()
    const [submitting, setSubmitting] = useState<boolean>(false);

    /**
     * 提交
     * @param values
     */
    const onFinish = async (values: any) => {
        // 避免重复提交
        if (submitting) {
            return;
        }
        setSubmitting(true);
        // 对接后端，上传数据
        const params = {
            ...values,
            file: undefined,
        };
        try {
            // const res = await genChartByAiAsyncUsingPost(params, {}, values.file.file.originFileObj);
            const res = await genChartByAiAsyncMqUsingPost(params, {}, values.file.file.originFileObj);
            if (!res?.data) {
                message.error('分析失败');
            } else {
                message.success('分析任务提交成功，稍后请在我的图表页面查看');
                // 重置所有字段
                form.resetFields();
            }
        } catch (e: any) {
            message.error('分析失败，' + e.message);
        }
        setSubmitting(false);
    };

    return (
        <div className="add-chart-async">
            <Card title="智能分析">
                <Form form={form} name="addChart" labelAlign="left" labelCol={{ span: 4 }}
                      wrapperCol={{ span: 16 }} onFinish={onFinish} initialValues={{}}>
                    <Form.Item
                        name="goal"
                        label="分析目标"
                        rules={[{ required: true, message: '请输入分析目标' }]}
                    >
                        <TextArea placeholder="请输入你的分析需求，比如：分析网站用户的增长情况" />
                    </Form.Item>
                    <Form.Item name="name" label="图表名称">
                        <Input placeholder="请输入图表名称" />
                    </Form.Item>
                    <Form.Item name="chartType" label="图表类型">
                        <Select
                            options={[
                                { value: '饼图', label: '饼图' },
                                { value: '地图', label: '地图' },
                                { value: '树图', label: '树图' },
                                { value: '折线图', label: '折线图' },
                                { value: '柱状图', label: '柱状图' },
                                { value: '雷达图', label: '雷达图' },
                                { value: '条形图', label: '条形图' },
                                { value: '热力图', label: '热力图' },
                                { value: '散点图', label: '散点图' },
                                { value: '仪表盘', label: '仪表盘' },
                                { value: 'K线图', label: 'K线图' },
                                { value: '长图表', label: '长图表' },
                                { value: '区域图', label: '区域图' },
                                { value: '面积热力图', label: '面积热力图' },
                                { value: '三维散点图', label: '三维散点图' },
                            ]}
                        />
                    </Form.Item>
                    <Form.Item name="file" label="原始数据">
                        <Upload name="file" maxCount={1}>
                            <Button icon={<UploadOutlined />}>上传 CSV 文件</Button>
                        </Upload>
                    </Form.Item>

                    <Form.Item wrapperCol={{ span: 16, offset: 4 }}>
                        <Space>
                            <Button type="primary" htmlType="submit" loading={submitting} disabled={submitting}>
                                提交
                            </Button>
                            <Button htmlType="reset">重置</Button>
                        </Space>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    );
};
export default AddChartAsync;
