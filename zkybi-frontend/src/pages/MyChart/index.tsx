import { useModel } from '@@/exports';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import {Avatar, Button, Card, Col, Divider, List, message, Modal, Result, Row} from 'antd';
import Search from 'antd/es/input/Search';
import ReactECharts from 'echarts-for-react';
import React, { useEffect, useState } from 'react';
import {deleteChartUsingPost, listMyChartByPageUsingPost} from "@/services/yubi/chartController";

const MyChartPage: React.FC = () => {
    /**
     * 初始值
     */
    const initSearchParams = {
        current: 1,
        pageSize: 6,
        sortField: 'createTime',
        sortOrder: 'desc',
    };
    // 当前的搜索参数
    const [searchParams, setSearchParams] = useState<API.ChartQueryRequest>({
        ...initSearchParams,
    });

    // const [selectedChartId, setSelectedChartId] = useState<number | undefined>(undefined);

    /**
     * 分页获取图表
     */
    // 存储返回的图表列表
    const [chartList, setChartList] = useState<API.Chart[]>();
    // 图表总数
    const [chartTotal, setChartTotal] = useState<number>(0);
    // 加载状态
    const [loading, setLoading] = useState<boolean>(true);
    /**
     * 获取当前用户
     */
    const { initialState } = useModel('@@initialState');
    const { currentUser } = initialState || {};

    /**
     * 删除图表
     * @param chartId
     */
    const handleDelete = (chartId: any) => {
        Modal.confirm({
            title: '确认删除',
            icon: <ExclamationCircleOutlined />,
            content: '确定要删除这个图表吗？',
            okText: '确认',
            cancelText: '取消',
            onOk: async () => {
                try {
                    // const res = await deleteChartUsingPost({ id: chartId });
                    const res = await deleteChartUsingPost({ id: chartId });
                    // 后端返回 boolean值
                    console.log("res:",res.data);
                    if (res.data) {
                        message.success('删除成功');
                        // 删除成功后重新加载图表数据
                        loadData();
                    } else {
                        message.error('删除失败');
                    }
                } catch (e: any) {
                    message.error('删除失败' + e.message);
                }
            },
        });
    };

    /**
     * 加载图表数据
     */
    const loadData = async () => {
        setLoading(loading);
        try {
            // let res = await listMyChartByPageUsingPOST(searchParams);
            let res = await listMyChartByPageUsingPost(searchParams);
            if (res.data) {
                setChartList(res.data.records ?? []);
                setChartTotal(res.data.total ?? 0);
                // 隐藏title
                if (res.data.records) {
                    res.data.records.forEach((data) => {
                        // 如果状态为succeed，才会解析图表代码
                        if(data.status === 'succeed'){
                          let jsonString = data.genChart ?? '{}';
                          // 替换单引号为双引号
                          jsonString = jsonString.replace(/'/g, '"');
                          // 处理不符合 JSON 规范的内容
                          // 比如替换可能的错误格式 (key: value) 为 ("key": "value")
                          jsonString = jsonString.replace(/([{,]\s*)(\w+)\s*:/g, '$1"$2":');
                          jsonString = jsonString.replace(/: (\w+)/g, ': "$1"');

                          // const chartOption = JSON.parse(data.genChart ?? '{}');
                          const chartOption = JSON.parse(jsonString);
                          // 取出title并且设置为 undefined
                          chartOption.title = undefined;
                          data.genChart = JSON.stringify(chartOption);
                        }
                    });
                }
            } else {
                message.error('获取我的图表失败');
            }
        } catch (e: any) {
            message.error('获取我的图表失败' + e.message);
        }
        setLoading(false);
    };
    /**
     * 变化时执行此处
     */
    useEffect(() => {
        loadData();
    }, [searchParams]);

    return (
        <div className="my-chart-page">
            <div className="margin-20">
                <Search
                    placeholder="请输入图标名称搜索"
                    loading={loading}
                    enterButton
                    onSearch={(value) => {
                        setSearchParams({
                            ...initSearchParams,
                            name: value,
                        });
                    }}
                />
            </div>
            <div className="margin-16"></div>
            <List
                grid={{
                    gutter: 16,
                    xs: 1,
                    sm: 1,
                    md: 1,
                    lg: 2,
                    xl: 2,
                    xxl: 2,
                }}
                pagination={{
                    // 设置分页
                    showTotal: () => `共 ${chartTotal} 条记录`,
                    showSizeChanger: true,
                    showQuickJumper: true,
                    pageSizeOptions: ['6', '10', '14', '20'],
                    onChange: (page, pageSize) => {
                        setSearchParams({
                            ...searchParams,
                            current: page,
                            pageSize,
                        });
                    },
                    current: searchParams.current, // 当前页
                    pageSize: searchParams.pageSize, // 每页条数
                    total: chartTotal, // 总数
                }}
                loading={loading}
                dataSource={chartList}
                renderItem={(item) => (
                    <List.Item key={item.id}>
                        <Card style={{ width: '100%' }}>
                            <List.Item.Meta
                                avatar={<Avatar src={currentUser?.userAvatar} />}
                                // title={currentUser?.userName}
                                title={item.name}
                                description={item.chartType ? '图表类型：' + item.chartType : undefined}
                            />
                            <>
                              {
                                item.status === 'wait' && <>
                                  <Result
                                    status="warning"
                                    title="待生成"
                                    subTitle={item.execMessage ?? '当前如表生成队列繁忙，请耐心等候'}
                                  />
                                </>
                              }
                              {
                                item.status === 'running' && <>
                                  <Result
                                    status="info"
                                    title="图表生成中"
                                    subTitle={item.execMessage}
                                  />
                                </>
                              }
                              {
                                item.status === 'succeed' && <>
                                  {/*<p*/}
                                  {/*    style={{*/}
                                  {/*        textAlign: 'left',*/}
                                  {/*        fontWeight: 'bold',*/}
                                  {/*        color: 'black',*/}
                                  {/*        fontSize: '16px',*/}
                                  {/*    }}*/}
                                  {/*>*/}
                                  {/*    {'分析目标：' + item.goal}*/}
                                  {/*</p>*/}
                                  {/*<List.Item.Meta*/}
                                  {/*    style={{ textAlign: 'left' }}*/}
                                  {/*    description={item.chartType ? '图表类型：' + item.chartType : undefined}*/}
                                  {/*/>*/}
                                  <ReactECharts option={item.genChart && JSON.parse(item.genChart)} />
                                  {/*<p*/}
                                  {/*    style={{*/}
                                  {/*        textAlign: 'center',*/}
                                  {/*        fontWeight: 'bold',*/}
                                  {/*        color: '#e617ff',*/}
                                  {/*        fontSize: '16px',*/}
                                  {/*    }}*/}
                                  {/*>*/}
                                  {/*    {'图表名称：' + item.name}*/}
                                  {/*</p>*/}
                                  <Divider style={{ fontWeight: 'bold', color: 'blue', fontSize: '16px' }}>
                                    智能分析结果
                                  </Divider>
                                  <p style={{ fontWeight: 'bold', color: '#0b93a1' }}>{item.genResult}</p>
                                  <Row justify="end">
                                    <Col>
                                      <Button danger onClick={() => handleDelete(item.id)}>
                                        删除
                                      </Button>
                                    </Col>
                                  </Row>
                                </>
                              }
                              {
                                item.status === 'failed' && <>
                                  <Result
                                    status="error"
                                    title="图表生成失败"
                                    subTitle={item.execMessage}
                                  />
                                </>
                              }
                            </>
                        </Card>
                    </List.Item>
                )}
            />
        </div>
    );
};
export default MyChartPage;
