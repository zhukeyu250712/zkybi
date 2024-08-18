# 智能BI项目
# 项目介绍
## 1、什么是BI？
BI是商业智能（Business Intelligence）的缩写。它是一种通过收集、分析和呈现数据来帮助企业做出更好决策的技术和过程。BI通常涉及使用各种数据仓库、数据挖掘和数据分析工具，以提取有关企业绩效、市场趋势、客户行为等方面的信息。这些数据可以被转化为可视化图表、报表和仪表盘，可协助企业管理层了解他们所运营的公司的情况，并根据这些信息制定更明智的商业决策。
类似于下图：
![](https://paperfly-blog.oss-cn-shenzhen.aliyuncs.com/blog/22957787-d7f446b2a08a616f.png#id=LdYwH&originHeight=938&originWidth=1500&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
主流BI平台：[帆软BI](https://www.finebi.com/)、[小马BI](https://bi.zhls.qq.com/#/)、[微软Power BI](https://powerbi.microsoft.com/zh-cn/)
[阿里云的BI平台](https://chartcube.alipay.com/)
传统的 BI 平台：

1. 手动上传数据
2. 手动选择分析所需的数据行和列（由数据分析师完成）
3. 需要手动选择所需的图表类型（由数据分析师完成）
4. 生成图表并保存配置
## 2、本项目的 BI 平台
[项目介绍学习](https://www.bilibili.com/video/BV1em4y117oZ/)
区别于传统的BI，用户（数据分析者）只需要导入最原始的数据集，输入想要进行分析的目标（比如帮我分析一下网站的增长趋势)，就能利用AI自动生成一个符合要求的图表以及分析结论。此外，还会有图表管理、异步生成等功能。
**优点：让不会数据分析的用户也可以通过输入目标快速完成数据分析，大幅节约人力成本，将会用到 AI 接口生成分析结果**
## 3、需求分析

1. 智能分析：用户输入目标和原始数据（图表类型），可以自动生成图表和分析结论
2. 图表管理
3. **图表生成的异步化（消息队列）**
4. 对接 AI 能力
## 4、项目架构图
### 4.1 基础流程
基础流程：客户端输入分析诉求和原始数据，向业务后端发送请求。业务后端利用AI服务处理客户端数据，保持到数据库，并生成图表。处理后的数据由业务后端发送给AI服务，AI服务生成结果并返回给后端，最终将结果返回给客户端展示。
> 要根据用户的输入生成图标，借助AI服务

![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230626133359830.png#id=szlbE&originHeight=548&originWidth=919&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
> 上图的流程会出现一个问题：
> 假设一个 AI 服务生成图表和分析结果要等50秒，如果有大量用户需要生成图表，每个人都需要等待50秒，那么 AI 服务可能无法受这种压力。为了解决这个问题，可以采用消息队列技术。
> 这类以于在餐厅点餐时，为了避免顾客排队等待，餐厅会给顾客一个取餐号码，上顾客可以先去坐下或做其他事情，等到餐厅叫到他们的号码时再去领取餐点，这样就能节省等待时间。
> 同样地，通过消息队列，用户可以提交生成图表的请求，这些请求会进入队列，AI 服务会依次处理队列中的请求，从而避免了同时处理大量请求造成的压力，同时也影更好地控制资源的使用。

### 4.2 优化流程（异步化）

![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230626134246728.png#id=eCguK&originHeight=604&originWidth=1214&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
优化流程（异步化）：客户端输入分析诉求和原始数据，向业务后端发送请求。业务后端将请求事件放入消息队列，并为客户端生成取餐号，让要生成图表的客户端去排队，消息队列根据AI服务负载情况，定期检查进度，如果AI服务还能处理更多的图表生成请求，就向任务处理模块发送消息。
任务处理模块调用AI服务处理客户端数据，AI 服务异步生成结果返回给后端并保存到数据库，当后端的AI工服务生成完毕后，可以通过向前端发送通知的方式，或者通过业务后端监控数据库中图表生成服务的状态，来确定生成结果是否可用。若生成结果可用，前端即可获取并处理相应的数据，最终将结果返回给客户端展示。在此期间，用户可以去做自己的事情。

## 5、技术栈
### 5.1 前端

1. React
2. Umi
3. Ant Design Pro
4. 可视化开发库：**Echarts** √ 、HighChairts + AntV
5. umi openapi 代码生成：自动生成后端调用代码
6. EChart 图表生成
### 5.2 后端

1. Spring Boot：万用模板
2. MySQL数据库
3. Redis：Redissson限流控制
4. MyBatis Plus 数据库访问结构
5. 消息队列：RabbitMQ
6. AI能力：Open AI接口开发
7. Excel上传和数据的解析：Easy Excel
8. Swagger + Knife4j 项目文档
9. Hutool 工具库
# 第一期直播 - 前后端初始化、前后端联调
## 1、前端初始化
[Nodejs安装](https://nodejs.org/en)版本建议：16.14 <  建议版本 < 18 ，避免乱七八糟的错误
[跟着官网步骤](https://pro.ant.design/zh-CN/docs/getting-started/)
```bash
npm i @ant-design/pro-cli -g

pro create 项目名称
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717413925533-2ffca4d4-1bb0-44f2-a5ab-0e7337ddf5e7.png#averageHue=%23222121&clientId=u73cb3262-daea-4&from=paste&height=858&id=u2b404992&originHeight=1030&originWidth=896&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=230764&status=done&style=none&taskId=u08de4fff-ace0-4912-90c0-eeb0151c25d&title=&width=746.6666369968002)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717414229213-76fa3727-ac59-47e1-b8e9-dbcb9d853317.png#averageHue=%231b1b1b&clientId=u73cb3262-daea-4&from=paste&height=507&id=u245d436f&originHeight=609&originWidth=941&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=86497&status=done&style=none&taskId=u5adf04bc-e0b9-42eb-bb1a-98178a39280&title=&width=784.1666355066842)
命令执行完毕，使用Webstorm 或 其他代码编译器打开项目
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717414430721-ba5e2051-21a2-428a-accf-5bffb6a1064a.png#averageHue=%232c2f33&clientId=u73cb3262-daea-4&from=paste&height=607&id=u8f6fa142&originHeight=728&originWidth=982&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=68552&status=done&style=none&taskId=u895dff8d-66ee-4063-8708-c0d1e2e2a7b&title=&width=818.3333008156895)
在 Webstorm 终端输入 npm i  或者yarn 或者yarn install（需要安装yarn）
启动项目
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717414669382-55d671b7-1b86-4372-a82b-75476e285d61.png#averageHue=%23232529&clientId=u73cb3262-daea-4&from=paste&height=860&id=u756cb834&originHeight=1032&originWidth=1920&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=233819&status=done&style=none&taskId=u5b072c14-6674-4e70-87c2-72c20a62166&title=&width=1599.9999364217147)
运行start命令
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717414871722-5d8ca9ab-7bd9-4b36-91e3-a0f9f44fb0c2.png#averageHue=%23232528&clientId=u73cb3262-daea-4&from=paste&height=860&id=ua85c9521&originHeight=1032&originWidth=1920&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=245101&status=done&style=none&taskId=u91c62117-fdba-495d-b81d-382cdeaeb4f&title=&width=1599.9999364217147)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717414957963-ab7f0d2b-a3a8-4f84-b973-4087b4578804.png#averageHue=%23f6f6f6&clientId=u73cb3262-daea-4&from=paste&height=822&id=u05adcf0d&originHeight=987&originWidth=1919&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=316590&status=done&style=none&taskId=ub3a0b5b5-49da-43d4-b0ee-87505d3fab2&title=&width=1599.1666031214952)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717415055928-f76d6ec7-fbd4-4fca-a116-0c97da24329a.png#averageHue=%23222529&clientId=u73cb3262-daea-4&from=paste&height=329&id=uf13a72e8&originHeight=395&originWidth=1227&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=67533&status=done&style=none&taskId=uc5aada2e-a5df-40da-a0b3-5b87c9202ef&title=&width=1022.4999593695021)
nvm可以管理node版本
### 1.1 移除国际化
运行 i18n-remove 再删除对应的国际化文件
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717415157457-cf7945e0-e099-4e6e-9cda-d3d85ef4d2be.png#averageHue=%23222427&clientId=u73cb3262-daea-4&from=paste&height=842&id=Wboqp&originHeight=1011&originWidth=1904&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=201267&status=done&style=none&taskId=u56b26f29-c966-4ed4-8492-67c66f2b63e&title=&width=1586.6666036182005)
#### 1.1.1 移除国际化报错

1. 找到开源地址：[https://github.com/ant-design/ant-design-pro](https://github.com/ant-design/ant-design-pro)
2. 找到错误原因：[https://github.com/ant-design/ant-design-pro/issues/10452](https://github.com/ant-design/ant-design-pro/issues/10452)
3. 前端运行：`yarn add eslint-config-prettier --dev yarn add eslint-plugin-unicorn --dev`
4. 然后修改node_modules/@umijs/lint/dist/config/eslint/index.js文件注释 `// es2022: true`可以解决问题
5. 左则导航栏不显示问题
移除国际化后，项目左侧导航栏菜单和右上角退出登陆按钮不显示了。
解决方法：给config/route.ts配置文件的每一个路由都加上对应的name属性，如果不添加name属性，则菜单默认展示的是path属性。

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717416118237-d31f26d1-f82f-48ef-b153-37d163c2fde2.png#averageHue=%2324262a&clientId=u73cb3262-daea-4&from=paste&height=597&id=udc057f3b&originHeight=717&originWidth=1673&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=129621&status=done&style=none&taskId=uaf3d7015-c865-44ef-b14e-99a9fcd26f3&title=&width=1394.166611267463)
这里一定要给每一个路由都添加对应的name属性，不然导航栏还是不显示
![](https://gitee.com/kcsen/img/raw/master/picGo/image-20230514162121366.png#id=AJKPW&originHeight=414&originWidth=949&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717768410598-c25d3324-2614-474d-93ee-62c28202b11d.png#averageHue=%23222428&clientId=u1f7bbf76-80a8-4&from=paste&height=662&id=u38a9ffda&originHeight=795&originWidth=1444&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=124793&status=done&style=none&taskId=u325f2556-d20f-4df9-a13c-698a4e9739d&title=&width=1203.3332855171645)
再次运行项目成功解决问题。
#### 其他错误：
> eslint版本过高报错
移除国际化后，再次运行i18n-remove命令，又报了一个错误：
TypeError:Failed to load plugin 'jest'declared in 'BaseConfig':Class exte
21885118212814
原因：这是因为eslint版本高于8，项目中eslint版本是8.34.0。
@otypescript--eslint/eslint-plugin和eslint版本7兼容，版本高于8会不兼容，不兼容的时候就会提示这个错误。
> [https://blog.csdn.net/qq_43436008/article/details/126056307](https://blog.csdn.net/qq_43436008/article/details/126056307)
> 解决办法：把eslint的版本改成7.32.0

## 2、后端初始化
首先拉取[springboot-ini万用模板](http://gitlab.code-nav.cn/root/springboot-init)
修改成配置文件，修改成属于你的配置，然后运行项目
修改登录账号密码，同时运行sql语句创建sql
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717418130370-db425aab-2217-466c-ae8d-77a4eea82d09.png#averageHue=%23303742&clientId=u73cb3262-daea-4&from=paste&height=792&id=u92c31936&originHeight=951&originWidth=1881&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=206472&status=done&style=none&taskId=uc20e6808-6b35-4e05-8142-f8082358a28&title=&width=1567.4999377131487)
[http://localhost:8080/api/doc.html](http://localhost:8080/api/doc.html)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717418088107-c117f4ad-1c5d-47ee-86a6-21b7caeb5f45.png#averageHue=%23c2b18b&clientId=u73cb3262-daea-4&from=paste&height=820&id=XsQFy&originHeight=984&originWidth=1928&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=117656&status=done&style=none&taskId=ud8fa96ca-32b6-48c5-9b38-919ae8cc89c&title=&width=1606.666602823472)
注册用户
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717418389474-b44264a7-2abe-4bb7-be7f-08c551a9a366.png#averageHue=%23c3b28a&clientId=u73cb3262-daea-4&from=paste&height=826&id=ue2647188&originHeight=991&originWidth=1925&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=147478&status=done&style=none&taskId=u65afbc55-dd47-466a-aaf9-4d2e8a63ea7&title=&width=1604.166602922813)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717418463210-e2d7064a-431a-48b8-8cf9-39101b9d06d1.png#averageHue=%23ac9971&clientId=u73cb3262-daea-4&from=paste&height=806&id=u030f0252&originHeight=967&originWidth=1943&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=156890&status=done&style=none&taskId=ua8b7e91e-d31e-456f-940d-25bc9770ca2&title=&width=1619.1666023267665)
### 后端启动项目端口冲突问题解决
原因：Windows Hyper-V 虚拟化平台占用端口
先使用：netsh interface ipv4 show excludedportrange protocol=tcp 查看被占用的端口范围，然后选择一个没被占用的端口使用，然后启动项目
[参考](https://blog.csdn.net/weixin_52297086/article/details/128845095)
## 3、库表设计
### 3.1 用户表
```java
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_userAccount(userAccount)
) comment '用户' collate = utf8mb4_unicode_ci;
```
### 3.2 图标信息表
```sql
-- 图表信息表
create table if not exists chart
(
    id         bigint auto_increment comment 'id' primary key,
    goal       text                               null comment '分析目标',
    chartData  text                               null comment '图表信息',
    chartType  varchar(256)                       null comment '图表类型',
    genChart   text                               null comment '生成的图表信息',
    getResult  text                               null comment '生成的分析结论',
    userId     bigint                             null comment '创建图标用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
) comment '图表信息表' collate = utf8mb4_unicode_ci;
```
## 4、后端开发（根据PDF步骤查看）

1. 使用MybatisX自动生成增删改查代码

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717419091295-afb1b346-ca98-40fd-a72b-002004d47b6e.png#averageHue=%23232830&clientId=u73cb3262-daea-4&from=paste&height=607&id=u2cefe38a&originHeight=728&originWidth=982&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=69396&status=done&style=none&taskId=u25a19b69-161c-4e9c-be38-5f741d99ee9&title=&width=818.3333008156895)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717419129875-fc576bcf-8316-4634-810d-047959bbd12f.png#averageHue=%2324282f&clientId=u73cb3262-daea-4&from=paste&height=557&id=uf5cb5b35&originHeight=669&originWidth=722&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=58022&status=done&style=none&taskId=u51f245f5-e611-4a38-aea9-88aab750ed5&title=&width=601.6666427585823)
control + shift + r
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717419355688-6eee5ad6-86f2-4e0e-9b2e-7a8331e5555f.png#averageHue=%23262b33&clientId=u73cb3262-daea-4&from=paste&height=777&id=u348bad8a&originHeight=932&originWidth=1473&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=133946&status=done&style=none&taskId=u6989ec05-ff58-4c1e-9465-8a914a1677f&title=&width=1227.4999512235343)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717419474890-ca07d591-4e9c-41b4-afb8-7440891a6ee0.png#averageHue=%23252a31&clientId=u73cb3262-daea-4&from=paste&height=424&id=u52da92ca&originHeight=509&originWidth=942&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=38022&status=done&style=none&taskId=uda07d66f-70c2-468b-9fd8-5661b1a6751&title=&width=784.9999688069038)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717419528225-c68f37e4-281a-49b4-813d-b49d8e3f9b71.png#averageHue=%23252a32&clientId=u73cb3262-daea-4&from=paste&height=422&id=u092e102d&originHeight=506&originWidth=942&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=59153&status=done&style=none&taskId=u7bb5b2a7-f87f-4e23-a029-b69cbeb99af&title=&width=784.9999688069038)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717419715105-25d4a867-b426-4f75-80af-2f382fb04b62.png#averageHue=%23252a31&clientId=u73cb3262-daea-4&from=paste&height=792&id=u7fdfed39&originHeight=951&originWidth=1223&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=159924&status=done&style=none&taskId=u84dcf443-a4c9-4a69-bcae-b5c1dfd6450&title=&width=1019.1666261686236)
修改chart 和 user属性的注解
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717419921403-c69a2346-7dd3-4c3b-a0c9-1fe148e21b5e.png#averageHue=%23262a32&clientId=u73cb3262-daea-4&from=paste&height=854&id=u006f82da&originHeight=1025&originWidth=1909&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=259170&status=done&style=none&taskId=uf74f9530-8d77-4325-b68b-14898e9dab2&title=&width=1590.8332701192987)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717423102701-b9211407-a56c-4dda-b31d-c4472a72ac16.png#averageHue=%23262d38&clientId=u73cb3262-daea-4&from=paste&height=221&id=uc91a989a&originHeight=265&originWidth=776&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=32929&status=done&style=none&taskId=u04897b21-c584-4a51-aa26-cf5fa6bd80f&title=&width=646.666640970443)
删除generate代码目录之后需要查看导包是否正常，之后需要查看resources下面得mapper的路径是否正常。

2. 将原来的User修改现在生成的User，运行，看报错，删除不要的微信登录代码
3. 再来走一遍登录流程

步骤如下：

1. 执行SQL语句来建表
2. mybatisX插件生成代码
3. 迁移生成的代码
4. 复制老的增册删改查模板，根据的表重构
5. 根据接口文档来测试

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717750116894-a253f976-ae25-40c3-b61c-bda57dbbb117.png#averageHue=%23fbfbfa&clientId=u1f7bbf76-80a8-4&from=paste&height=340&id=u223474d1&originHeight=408&originWidth=695&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=108264&status=done&style=none&taskId=u7516ee67-614d-4f9d-a05b-ce2e6636430&title=&width=579.1666436526519)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717750146412-e7447fba-a498-4253-b58d-5bffa10bb2a0.png#averageHue=%23f9f8f6&clientId=u1f7bbf76-80a8-4&from=paste&height=1079&id=u5469147a&originHeight=1295&originWidth=1692&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=919412&status=done&style=none&taskId=u068f0ca5-12d2-4fb5-840d-99224d83095&title=&width=1409.9999439716362)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717750161551-6960d0cb-e0d5-4ed2-9895-9c1389b39c89.png#averageHue=%23f9f8f6&clientId=u1f7bbf76-80a8-4&from=paste&height=931&id=u5b9eb1bd&originHeight=1117&originWidth=1791&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=1011685&status=done&style=none&taskId=u40b38691-d142-4481-a635-3b004d88785&title=&width=1492.4999406933807)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717750178609-d6b1866a-615f-4db5-bfd0-3d96eae90ca4.png#averageHue=%23fafafa&clientId=u1f7bbf76-80a8-4&from=paste&height=1045&id=u6f9f4a56&originHeight=1254&originWidth=2006&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=454337&status=done&style=none&taskId=ucdaf2a33-d24e-4ab5-a8fc-8848badd9e3&title=&width=1671.666600240604)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717750197668-22ceffe1-ed76-4c17-afe8-cd1549eca802.png#averageHue=%23fcfcfb&clientId=u1f7bbf76-80a8-4&from=paste&height=996&id=ue437ce7e&originHeight=1195&originWidth=1968&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=409828&status=done&style=none&taskId=u93825503-2fe2-4416-a063-b1dc8ef51bf&title=&width=1639.9999348322576)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717750236295-57eaec3e-e0da-4fb2-86f2-1de7a473d62c.png#averageHue=%23fcfcfc&clientId=u1f7bbf76-80a8-4&from=paste&height=999&id=ud5624510&originHeight=1199&originWidth=1900&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=369924&status=done&style=none&taskId=uc2015aaf-115a-4251-bcb1-2dd65ea5155&title=&width=1583.333270417322)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717750309114-507c6629-cbf0-4020-9c99-ef2321376330.png#averageHue=%23fcfcfb&clientId=u1f7bbf76-80a8-4&from=paste&height=1036&id=u168e2e08&originHeight=1243&originWidth=1959&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=419632&status=done&style=none&taskId=uac45f2f6-aec7-45f1-b50c-156ff57bb90&title=&width=1632.4999351302808)
```java
package com.yupi.springbootinit.model.dto.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class ChartAddRequest implements Serializable {

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表数据
     */
    private String chartData;

    /**
     * 图表类型
     */
    private String chartType;

    private static final long serialVersionUID = 1L;
}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717750326519-1df051e0-53d8-46a8-b626-c3b25d81ee0e.png#averageHue=%23fcfcfb&clientId=u1f7bbf76-80a8-4&from=paste&height=1049&id=u3de3beab&originHeight=1259&originWidth=1951&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=418815&status=done&style=none&taskId=ub6e88d27-245a-4051-ae91-9adcc0bfac0&title=&width=1625.8332687285238)
```java
package com.yupi.springbootinit.model.dto.chart;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 编辑请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class ChartEditRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表数据
     */
    private String chartData;

    /**
     * 图表类型
     */
    private String chartType;

    private static final long serialVersionUID = 1L;
}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717750337081-8f188a64-b9c3-40af-9fac-90293846040b.png#averageHue=%23fcfbfb&clientId=u1f7bbf76-80a8-4&from=paste&height=1103&id=u48d47b09&originHeight=1324&originWidth=1948&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=435442&status=done&style=none&taskId=u1b093a50-b6c0-4197-9eba-85e6594be37&title=&width=1623.3332688278647)
```java
package com.yupi.springbootinit.model.dto.chart;

import com.yupi.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChartQueryRequest extends PageRequest implements Serializable {

    private Long id;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表类型
     */
    private String chartType;

    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717750350531-14764b2a-733c-4265-a49d-0709196af63a.png#averageHue=%23fbfbfb&clientId=u1f7bbf76-80a8-4&from=paste&height=1087&id=uc91d0dad&originHeight=1304&originWidth=1922&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=583324&status=done&style=none&taskId=ufacd1cdc-6ca4-441e-baac-93b441f3a63&title=&width=1601.666603022154)
```java
package com.yupi.springbootinit.model.dto.chart;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 更新请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class ChartUpdateRequest implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表数据
     */
    private String chartData;

    /**
     * 图表类型
     */
    private String chartType;

    /**
     * 生成的图表数据
     */
    private String genChart;

    /**
     * 生成的分析结论
     */
    private String genResult;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717750363970-f3732347-93dd-4a23-b1c9-28f8678940d5.png#averageHue=%23f9f9f8&clientId=u1f7bbf76-80a8-4&from=paste&height=1117&id=u1c659d3f&originHeight=1341&originWidth=1981&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=582884&status=done&style=none&taskId=ub39f25a5-3acb-4676-8a7a-f8fd48a2c33&title=&width=1650.833267735113)
```java
package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.yupi.springbootinit.annotation.AuthCheck;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.DeleteRequest;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.constant.UserConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.chart.ChartAddRequest;
import com.yupi.springbootinit.model.dto.chart.ChartEditRequest;
import com.yupi.springbootinit.model.dto.chart.ChartQueryRequest;
import com.yupi.springbootinit.model.dto.chart.ChartUpdateRequest;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.service.UserService;
import com.yupi.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 *
 */
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private ChartService chartService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param chartAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addChart(@RequestBody ChartAddRequest chartAddRequest, HttpServletRequest request) {
        if (chartAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, chart);
        User loginUser = userService.getLoginUser(request);
        chart.setUserId(loginUser.getId());
        boolean result = chartService.save(chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newChartId = chart.getId();
        return ResultUtils.success(newChartId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldChart.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = chartService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param chartUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateChart(@RequestBody ChartUpdateRequest chartUpdateRequest) {
        if (chartUpdateRequest == null || chartUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartUpdateRequest, chart);
        long id = chartUpdateRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Chart> getChartById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(chart);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Chart>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
            HttpServletRequest request) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<Chart>> listMyChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
            HttpServletRequest request) {
        if (chartQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        chartQueryRequest.setUserId(loginUser.getId());
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }

    // endregion

    /**
     * 编辑（用户）
     *
     * @param chartEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editChart(@RequestBody ChartEditRequest chartEditRequest, HttpServletRequest request) {
        if (chartEditRequest == null || chartEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartEditRequest, chart);
        User loginUser = userService.getLoginUser(request);
        long id = chartEditRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldChart.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * 获取查询包装类
     *
     * @param chartQueryRequest
     * @return
     */
    private QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        if (chartQueryRequest == null) {
            return queryWrapper;
        }
        Long id = chartQueryRequest.getId();
        String goal = chartQueryRequest.getGoal();
        String chartType = chartQueryRequest.getChartType();
        Long userId = chartQueryRequest.getUserId();
        String sortField = chartQueryRequest.getSortField();
        String sortOrder = chartQueryRequest.getSortOrder();

        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chartType", chartType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717753690207-6beb96e7-8ce7-47fd-af8a-45af3ad140f9.png#averageHue=%23c3b18a&clientId=u1f7bbf76-80a8-4&from=paste&height=862&id=u19c6aa34&originHeight=1034&originWidth=1910&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=126650&status=done&style=none&taskId=uc4103943-f828-43c7-af83-5e08b1b40b4&title=&width=1591.6666034195184)
```java
{
  "chartData": "[{\"月份\":6}]",
  "chartType": "柱状图",
  "goal": "我想要分析网站近一年的用户增长趋势"
}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717753735676-6e475832-f801-4429-829d-54af5b49c14d.png#averageHue=%23c2ae86&clientId=u1f7bbf76-80a8-4&from=paste&height=812&id=ua688309d&originHeight=975&originWidth=1901&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=139608&status=done&style=none&taskId=uc66dc2e9-3c6b-4e22-af38-50250995d78&title=&width=1584.1666037175416)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717753791848-3f7c96b3-0a31-4431-9234-93563cf9c47e.png#averageHue=%23c3b28b&clientId=u1f7bbf76-80a8-4&from=paste&height=812&id=ub770a7aa&originHeight=974&originWidth=1910&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=120985&status=done&style=none&taskId=u3d0225e0-5300-4511-9c6b-7a0724b16ac&title=&width=1591.6666034195184)
## 4、前后端联调
### 4.1 前端

1. 在config.ts中的配置后端的接口文档地址
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717754197885-6f51fb85-87db-4aae-8b5c-61c816655147.png#averageHue=%23cbb791&clientId=u1f7bbf76-80a8-4&from=paste&height=813&id=u94c9aa2a&originHeight=976&originWidth=1910&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=85047&status=done&style=none&taskId=u902986ec-cf22-41b9-9ea2-dc9c1a0478d&title=&width=1591.6666034195184)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717754308464-24a1bb6d-52d7-4233-a4c9-a2e2e9981c30.png#averageHue=%23fdfdfd&clientId=u1f7bbf76-80a8-4&from=paste&height=847&id=ua826971d&originHeight=1016&originWidth=1905&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=71861&status=done&style=none&taskId=ub85381be-f0b9-488d-a603-fde6bb537c2&title=&width=1587.4999369184202)
```json
  openAPI: [
    {
      requestLibPath: "import { request } from '@umijs/max'",
      // 后端项目Swagger接口地址，拿到后端JSON格式的数据
      schemaPath: 'http://localhost:8080/api/v2/api-docs',
      // 项目名称
      projectName: 'yubi',  
      // 不生成模拟数据
      mock: false,
    },
  ],
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717754397351-3c560e9e-d15b-4f9e-92df-0ea36abe1424.png#averageHue=%23222327&clientId=u1f7bbf76-80a8-4&from=paste&height=884&id=u46b36dde&originHeight=1061&originWidth=1757&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=217174&status=done&style=none&taskId=uecbd81b2-03da-4d82-94e9-ee5f9b10b01&title=&width=1464.166608485913)

2. 然后点击openapi，运行之后，就会得到下面这个目录

![](https://gitee.com/kcsen/img/raw/master/picGo/image-20230515090702712.png#id=sG9RC&originHeight=318&originWidth=339&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
这个目录下面的就是对应后端的接口
然后将`requestErrorConfig.ts`改名 => `requestConfig.ts`，在里面添加请求后端的地址： 
```typescript
// 链接后端地址
baseURL: 'http://localhost:8080',
// 获取cookie
withCredentials: true,
```
![](https://gitee.com/kcsen/img/raw/master/picGo/image-20230515092108487.png#id=vGwUJ&originHeight=633&originWidth=1153&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
将app.tsx 中的request修改下如下：
```typescript
export const request = requestConfig
```
上面的修改有些麻烦，可以使用下面的方法：
使用ant design pro自带的openapi工具，根据后端的swagger接口文档数据自动生成对应的请求service代码。
注意：前端须更改对应的请求地址为你的后端地址，方法：在app.tsx里修改request.baseURL
![](https://gitee.com/kcsen/img/raw/master/picGo/image-20230515092547498.png#id=CHNj5&originHeight=574&originWidth=1026&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
完成请求后端地址：
![](https://gitee.com/kcsen/img/raw/master/picGo/image-20230515094844832.png#id=oaOf3&originHeight=238&originWidth=608&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)

# 第二期直播 - 前端登录、智能分析业务流程讲解
学习任务：

1. 前端模板优化
2. 前端登录功开发
3. 智能分析业务流程讲解
4. 智能分析接口开发(Excl数据处理、AI网站、玩法技巧分享)

## 1、前端
### 1.1 前端目录介绍
修改端口（第一次直播的时候遇到端口冲突）
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717833874283-659834c1-a34b-410a-b943-cc33ee548bca.png#averageHue=%23fafafa&clientId=ua095d19c-0831-4&from=paste&height=520&id=u0ef1a073&originHeight=624&originWidth=844&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=109597&status=done&style=none&taskId=ub7d0d665-86c6-4232-aeed-8e0b0bf2683&title=&width=703.3333053853788)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717833054428-e8114c0c-8ea5-426a-a938-ea6556bcaf4c.png#averageHue=%23bbcdc3&clientId=u35cba382-74c2-4&from=paste&height=722&id=Rma7x&originHeight=867&originWidth=1384&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=260263&status=done&style=none&taskId=u53e2a570-3e25-44c0-861e-e41d474d1e5&title=&width=1153.333287503986)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717833088732-14dfc658-0153-4d09-a88d-4be26cc8f2ed.png#averageHue=%23f9f9f9&clientId=u35cba382-74c2-4&from=paste&height=574&id=uc6b641aa&originHeight=689&originWidth=1029&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=119672&status=done&style=none&taskId=ubd4fe5ca-f57a-45ce-a23b-7cea2089d53&title=&width=857.4999659260128)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717833108052-01fba2eb-9037-4b86-bca8-744fc40555c2.png#averageHue=%23f9f9f9&clientId=u35cba382-74c2-4&from=paste&height=572&id=u8585e0fc&originHeight=687&originWidth=1042&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=112753&status=done&style=none&taskId=u4802f683-deb3-4296-95dc-efa9b6ee1e4&title=&width=868.3332988288681)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717833120675-b2a99e9a-583f-4dc2-a9dd-75bf30eaa81f.png#averageHue=%23f9f9f9&clientId=u35cba382-74c2-4&from=paste&height=354&id=u51dfa2f2&originHeight=425&originWidth=1028&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=80025&status=done&style=none&taskId=ue7e880a3-f6e0-46a3-b1bf-6709a3d8353&title=&width=856.6666326257931)

2. .husky 提交代码时，检查代码是否规范
3. 删除mock模拟数据（因为本地有后瑞）
4. 菩换Logo,去[https:/www.iconfont..cn/](https://www.iconfont.cn/)溲索，替换favicon，使用http://libowen.com/tool/ico/生成
5. 删除国际化locales目录
6. 删除测试相关目录：tests、types
7. prettier、eslint、editorConfig、stylelint,需要开启prettier美化置
8. 替换网站标题名你：全局替换Ant Design Pro和Ant Design （Ctrl + Shift + R）
### 1.2 模板优化
#### 替换logo
在public目录下有一些小图标和 logo，把这三个图片删掉，换成别的 
[https://iconfont.cn/](https://iconfont.cn/)
[https://libowen.com/tool/ico/](https://libowen.com/tool/ico/)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717834987067-4ce1f349-8af9-4590-be4c-4ae22539fcc3.png#averageHue=%23fdfcfc&clientId=ua095d19c-0831-4&from=paste&height=193&id=ud5db3273&originHeight=232&originWidth=631&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=32675&status=done&style=none&taskId=uc0e5fa7b-bb78-4045-bc73-b0e30ec2ac2&title=&width=525.8333124385948)
替换 favicon.ico：
ps.favicon.ico 是网站作上角的小图标。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717835071366-5b98d44e-afe2-488f-866f-5c7c7cc7a456.png#averageHue=%23f2f1ee&clientId=ua095d19c-0831-4&from=paste&height=175&id=ue648aebf&originHeight=210&originWidth=353&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=39637&status=done&style=none&taskId=ub11869a8-8ae9-4802-b069-806414ac414&title=&width=294.166654977534)
制作 favicon.ico 只需一个 .png 的图片，利用网站工具进行转换。
点击PNG下载按钮。
把图片重命名成favicon.ico或者利用网站工具转换。
#### 替换标题
登录页的标题全是 Ant Design、Ant Design Pro，修改一下
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717835519969-664c6fc4-a0c7-412d-b711-2e6836c03513.png#averageHue=%23e9f0d2&clientId=ua095d19c-0831-4&from=paste&height=793&id=u9dd7c979&originHeight=952&originWidth=893&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=141088&status=done&style=none&taskId=ue1d7252b-8ace-43f9-a419-17017c72743&title=&width=744.1666370961412)
vscode或webstorm按[Ctrl + Shift + F]；
全局搜索Ant Design Pro和Ant Design、蚂蚁集团体验技术部出品。
webstorm：输入完后，按[Ctrl + Shift + R]全局替换，输入替换的内容：鱼智能BI。
vscode：输入完后，点击左侧∨按钮，输入替换内容：鱼智能BI。
把页面底下的文字也修改一下，替换成编程导航知识星球出品。
### 1.2 开发登录注册界面
#### 删除多余部分(时间点 33:39-41:38)
删除_snapshots_目录、login.test.tsx。
查看路由里面的path
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717835818519-bffc160c-ca93-4e90-b9ba-efe2e7cb534c.png#averageHue=%23222428&clientId=ua095d19c-0831-4&from=paste&height=582&id=u2d0c4fe4&originHeight=699&originWidth=1485&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=118221&status=done&style=none&taskId=u0e242c07-a2bb-4327-bcae-11f0a245559&title=&width=1237.49995082617)
删除_snapshots_目录、login.test.tsx
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717836050081-f2099827-0d0b-4fd9-af36-ba4cf382b328.png#averageHue=%2323262b&clientId=ua095d19c-0831-4&from=paste&height=542&id=u5701c210&originHeight=651&originWidth=1454&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=117176&status=done&style=none&taskId=u4089a2f5-e11b-49ef-8caa-51a5f5849e3&title=&width=1211.666618519361)
点击index.tsx，看见137行的<Lang />爆红了，选中它，按Ctrl+鼠标左键，点进来看
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717836351820-f3f090ee-b742-4c6c-b56d-cb6ee059a45f.png#averageHue=%2324272b&clientId=ua095d19c-0831-4&from=paste&height=695&id=u9efe530e&originHeight=834&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=171189&status=done&style=none&taskId=u328d8238-7a01-453b-8049-627d5689465&title=&width=1039.9999586741146)
这里都没有用到，删掉，然后把<Lang />也删掉。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717836396811-67213897-af67-45bf-a82f-93c65cc76f19.png#averageHue=%23232529&clientId=ua095d19c-0831-4&from=paste&height=297&id=u0c31b2a6&originHeight=356&originWidth=1057&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=71970&status=done&style=none&taskId=ub52a2b91-1c5b-4ad8-b02c-11b2d8c2c58&title=&width=880.8332983321627)
把鱼智能BI 是西湖区最具影响力的Web设计规范改为原创项目。
```
subTitle={
  <a href="https://blog.csdn.net/qq_43157097?spm=1000.2115.3001.5343" target="_blank">
    YuBI 是🐟码农进阶之路 <a/>
}
```
删掉手机号登录、自动登录的代码。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717836865437-e323bab8-8438-47c8-9a96-bea6acae1862.png#averageHue=%23252e40&clientId=ua095d19c-0831-4&from=paste&height=297&id=u5493d20d&originHeight=357&originWidth=1011&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=69196&status=done&style=none&taskId=ubb472d3f-e684-451a-bf5f-81668b5d3f3&title=&width=842.4999665220591)
```
initialValues={{
  autoLogin: true,
}}
actions={['其他登录方式 :', <ActionIcons key="icons" />]}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717836951749-1b4fca31-abc7-4cdf-943f-8b021c188315.png#averageHue=%2323272e&clientId=ua095d19c-0831-4&from=paste&height=566&id=u3ebd1650&originHeight=679&originWidth=1182&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=116221&status=done&style=none&taskId=u00d5b330-a75c-4ab2-9fa6-2e6b4b34d8d&title=&width=984.9999608596181)
```
{
  key: 'mobile',
  label: '手机号登录',
},
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717837041677-e215ce8e-9e33-40d7-9e16-0a71a160ee5e.png#averageHue=%23232528&clientId=ua095d19c-0831-4&from=paste&height=548&id=ue860d765&originHeight=658&originWidth=1181&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=112123&status=done&style=none&taskId=ud0886707-c73c-47a1-967b-a478dbafc3c&title=&width=984.1666275593985)
```
{status === 'error' && loginType === 'mobile' && <LoginMessage content="验证码错误" />}
{type === 'mobile' && (
  <>
    <ProFormText
      fieldProps={{
        size: 'large',
        prefix: <MobileOutlined />,
      }}
      name="mobile"
      placeholder={'请输入手机号！'}
      rules={[
        {
          required: true,
          message: '手机号是必填项！',
        },
        {
          pattern: /^1\d{10}$/,
          message: '不合法的手机号！',
        },
      ]}
    />
    <ProFormCaptcha
      fieldProps={{
        size: 'large',
        prefix: <LockOutlined />,
      }}
      captchaProps={{
        size: 'large',
      }}
      placeholder={'请输入验证码！'}
      captchaTextRender={(timing, count) => {
        if (timing) {
          return `${count} ${'秒后重新获取'}`;
        }
        return '获取验证码';
      }}
      name="captcha"
      rules={[
        {
          required: true,
          message: '验证码是必填项！',
        },
      ]}
      onGetCaptcha={async (phone) => {
        const result = await getFakeCaptcha({
          phone,
        });
        if (!result) {
          return;
        }
        message.success('获取验证码成功！验证码为：1234');
      }}
    />
  </>
)}
```
```
<ProFormCheckbox noStyle name="autoLogin">
  自动登录
</ProFormCheckbox>
```
修改忘记密码为注册。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717837097416-2a667d19-d83c-468f-8ef9-32cd4720220b.png#averageHue=%2324262a&clientId=ua095d19c-0831-4&from=paste&height=803&id=u86819c19&originHeight=964&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=156173&status=done&style=none&taskId=ua6129867-fb71-4bd1-bec3-ab402ba625e&title=&width=1039.9999586741146)
```
<a
  style={{
    float: 'right',
  }}
>
  忘记密码 ?
</a>
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717837294095-cb5f4324-69a7-4ae4-a4d4-e45901fc494d.png#averageHue=%2324262a&clientId=ua095d19c-0831-4&from=paste&height=803&id=u6f346a86&originHeight=964&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=159701&status=done&style=none&taskId=ua359b3bf-e411-4de0-95fe-21276a427a8&title=&width=1039.9999586741146)
```
<div
  style={{
    float: 'right',
  }}
>
  <Link to="/user/register">注册</Link>
    
</div>
```
```
<div
  style={{
    // float: 'right',
      marginBottom:24
  }}
>
  <Link to="/user/register">注册</Link>

</div>
```
修改后页面
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717837390315-f224188e-1fb9-4e35-a706-078d54a1cd47.png#averageHue=%23dee8cc&clientId=ua095d19c-0831-4&from=paste&height=669&id=ue753ba87&originHeight=803&originWidth=1184&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=145604&status=done&style=none&taskId=u85a39614-db55-4c2d-9aea-37dc21f2a80&title=&width=986.6666274600574)
找到登录的表单，这个是用 Ant Design Pro 提供的组件LoginForm实现的，我们只需把用户输入的内容(key)，和我们后端对应即可。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717837625766-62352a27-d32c-4b89-b18a-d6f16263eed7.png#averageHue=%231f2125&clientId=ua095d19c-0831-4&from=paste&height=229&id=u7068555a&originHeight=275&originWidth=587&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=22349&status=done&style=none&taskId=ud29d1935-d523-4b26-b39d-fad0968af8e&title=&width=489.1666472289305)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717837553722-c309d7ac-0ec4-4a38-95e6-ba984ffd79fd.png#averageHue=%2324262a&clientId=ua095d19c-0831-4&from=paste&height=785&id=u928780ae&originHeight=942&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=170429&status=done&style=none&taskId=ufca8600c-499b-4462-a2a7-69e8b6ad7b1&title=&width=1039.9999586741146)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717837577927-f522c3f1-2b22-4f61-81c9-2b5d323fcc3e.png#averageHue=%2324262a&clientId=ua095d19c-0831-4&from=paste&height=785&id=u9b426388&originHeight=942&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=177402&status=done&style=none&taskId=u66e6b74c-a194-43d4-bff3-190fe78bbff&title=&width=1039.9999586741146)
把后端项目启动，打开后端的接口文档，访问 [http://localhost:8080/api/doc.html](http://localhost:8080/api/doc.html)；
找到用户登录接口userLogin，看一下请求，用户登录接收的请求是userAccount(账号)、userPassword(密码)。
```typescript
{
  "userAccount": "ZKYAAA",
  "userPassword": "12345678"
}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717837709322-8eabddde-2d73-4de8-8b43-d7eedc2ea7f6.png#averageHue=%23bbb190&clientId=ua095d19c-0831-4&from=paste&height=1356&id=ub0dc2c64&originHeight=1627&originWidth=2888&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=323886&status=done&style=none&taskId=ud213fb58-4af6-4eeb-adc3-039e0d9dbe1&title=&width=2406.666571034329)
把name改为userAccount(账号)、userPassword(密码)。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717837782398-cb3fedcf-58ce-456e-b3aa-1e6857640c81.png#averageHue=%2324262a&clientId=ua095d19c-0831-4&from=paste&height=785&id=udf5049f8&originHeight=942&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=177535&status=done&style=none&taskId=u8bc35a19-202a-4897-b593-79237f309d3&title=&width=1039.9999586741146)
修改后：
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717837816752-8cd7cf5f-8b6b-40b7-baaa-9cfc7168e150.png#averageHue=%2324262a&clientId=ua095d19c-0831-4&from=paste&height=785&id=u7a8814ee&originHeight=942&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=179420&status=done&style=none&taskId=u53fb7cff-adf3-4316-85d6-10a47502792&title=&width=1039.9999586741146)
修改占位符提示文本信息。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717837847415-505368c6-a4af-4f5f-bf34-7e14387bdfe5.png#averageHue=%2324262a&clientId=ua095d19c-0831-4&from=paste&height=785&id=u4cb36ebb&originHeight=942&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=179481&status=done&style=none&taskId=u099c7aae-af12-40dd-a932-c09a4bcf867&title=&width=1039.9999586741146)
修改后：
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717837925349-f42e96d3-6435-4eac-8a9b-e765a6caa0b2.png#averageHue=%23232529&clientId=ua095d19c-0831-4&from=paste&height=739&id=u2eb95ada&originHeight=887&originWidth=1195&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=153665&status=done&style=none&taskId=u0bb4e3b0-263a-4eb7-a339-d498fb615c9&title=&width=995.8332937624735)
删除错误填写。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717837963505-0c1ea5f6-7f60-4e45-a0c3-82630cbc6465.png#averageHue=%2324262a&clientId=ua095d19c-0831-4&from=paste&height=785&id=ue83da008&originHeight=942&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=175600&status=done&style=none&taskId=uf5d9ecea-05eb-49f3-b331-e13e7981cfb&title=&width=1039.9999586741146)
```
{status === 'error' && loginType === 'account' && (
  <LoginMessage content={'错误的用户名和密码(admin/ant.design)'} />
)}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717837999082-5f910095-e717-4595-ab3b-74cc344cfcdd.png#averageHue=%2324272c&clientId=ua095d19c-0831-4&from=paste&height=785&id=u24585e53&originHeight=942&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=196560&status=done&style=none&taskId=ubce60c38-7bbf-427b-89b2-719b9a61a13&title=&width=1039.9999586741146)
```typescript
  const { status, type: loginType } = userLoginState;
```
#### 根据后端做出调整(时间点 41:40-59:20)
前端还没有和后端对接，现在前端请求的地址不是我们自己的后端。
找到onFinsh→ 当用户点击登录按钮之后，会执行handleSubmit提交函数。
```
onFinish={async (values) => {
  await handleSubmit(values as API.LoginParams);
}}
```
把 ts 类型改成后端自己的类型UserLoginRequest(用户登录请求)。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717838529258-f7f03cae-53df-4423-859f-879921e6a763.png#averageHue=%2324262a&clientId=ua095d19c-0831-4&from=paste&height=785&id=ucd54846a&originHeight=942&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=177215&status=done&style=none&taskId=u6d609f79-56d3-4a6e-84d7-bb8dceef320&title=&width=1039.9999586741146)
选中handleSubmit，按Ctrl+鼠标左键。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717839126953-d7c6af31-a443-4c09-a978-9298251c5d1c.png#averageHue=%2324272b&clientId=ua095d19c-0831-4&from=paste&height=785&id=u975bf0de&originHeight=942&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=208454&status=done&style=none&taskId=ub6514c67-0ec4-4407-83e5-fa6ce6fcb44&title=&width=1039.9999586741146)
把接收的参数类型改成UserLoginRequest(用户登录请求)。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717839158983-a17e8467-987a-4b5b-98bd-d80e1d5236d5.png#averageHue=%2324262b&clientId=ua095d19c-0831-4&from=paste&height=551&id=u76651ee1&originHeight=661&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=143909&status=done&style=none&taskId=uc37cdbd8-bba2-4b85-b43f-abee1cb12c6&title=&width=1039.9999586741146)
这里实际上是请求咱们的后端。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717839188097-0f5ec501-ffd0-4b80-8a84-b9379e873927.png#averageHue=%2324262b&clientId=ua095d19c-0831-4&from=paste&height=519&id=u23f16f71&originHeight=623&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=137510&status=done&style=none&taskId=u32505d16-1da0-42d0-be10-9fff47b134c&title=&width=1039.9999586741146)
要改成自己的后端代码。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717839359472-89c4953f-fd4f-4fa9-9daf-cdd8fff186e3.png#averageHue=%2325272c&clientId=ua095d19c-0831-4&from=paste&height=439&id=u6fb25429&originHeight=527&originWidth=1248&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=124579&status=done&style=none&taskId=u56bf2da0-35de-4d94-98a3-c7699f78384&title=&width=1039.9999586741146)
看一下后端接口文档的返回值。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717839415505-4499f9e0-bfdb-4003-9816-10a3541c31fc.png#averageHue=%23c1b089&clientId=ua095d19c-0831-4&from=paste&height=822&id=ud18f38b7&originHeight=986&originWidth=1904&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=124630&status=done&style=none&taskId=uf4521f90-a1ef-4b9e-8e44-879aa06fd44&title=&width=1586.6666036182005)
然后把返回值判断改成自己的。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717839861037-08ec6668-0e6d-432c-8808-190309b240e8.png#averageHue=%23fbfaf9&clientId=ua095d19c-0831-4&from=paste&height=999&id=u247ef6f0&originHeight=1199&originWidth=1182&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=426474&status=done&style=none&taskId=u2a069458-05eb-4771-b6dd-cecaa19e42d&title=&width=984.9999608596181)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717840085296-730d353e-d6ab-4c02-920f-72839ffc733a.png#averageHue=%2325272b&clientId=ua095d19c-0831-4&from=paste&height=785&id=u80a4ee6e&originHeight=942&originWidth=1138&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=203589&status=done&style=none&taskId=u3e6fd2d4-329e-4d83-939e-9510a99dcdb&title=&width=948.3332956499538)
这里还要改一下fetchUserinfo函数，选中fetchUserinfo，按Ctrl+鼠标左键。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717840095780-d9d999ac-25bc-4159-8f91-9960180aae43.png#averageHue=%23eff0ea&clientId=ua095d19c-0831-4&from=paste&height=497&id=u9ebd573d&originHeight=597&originWidth=862&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=359734&status=done&style=none&taskId=ufd9c9026-cd4f-4983-a3e7-c1b36024b66&title=&width=718.3333047893324)
这里改成自己后端生成的代码getLoginUserUsingGET，对应后端接口文档中的getLoginUser。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717840130668-237a8341-4d18-452f-86ad-d7bddc0edbb8.png#averageHue=%23f2f0ea&clientId=ua095d19c-0831-4&from=paste&height=493&id=udf83a2aa&originHeight=592&originWidth=886&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=238342&status=done&style=none&taskId=ubbfe52e2-8af8-4de2-a416-33e0181d49d&title=&width=738.3333039946037)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717840137149-6f3ae536-d3e7-49c4-bd5b-ac962d3a8b6d.png#averageHue=%23f5f5f0&clientId=ua095d19c-0831-4&from=paste&height=367&id=u553badf8&originHeight=440&originWidth=882&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=233204&status=done&style=none&taskId=u4af0d442-82d0-416a-9785-7dfb5658025&title=&width=734.9999707937252)
直接去数据库加上数据。
有很多未引用的值，把未引用的全删掉。
webstorm按Ctrl+Alt+O优化导入的类和包；
vscode按Shift+Alt+O。
ps.webstorm 这个快捷键会全删掉，🐟最后是手动删除的🐶
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717840466062-30cdddc9-0270-4b9a-b729-505786ea7a2c.png#averageHue=%23e5eacd&clientId=ua095d19c-0831-4&from=paste&height=669&id=ucfe4636f&originHeight=803&originWidth=1034&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=137138&status=done&style=none&taskId=ua5fd771a-2b31-463a-a3e9-fb54bc969ca&title=&width=861.6666324271109)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717840545671-3ddd1db8-5e12-4d4a-8dc3-4da3b1ab5c5f.png#averageHue=%23f6f6f6&clientId=ua095d19c-0831-4&from=paste&height=867&id=u79a81924&originHeight=1041&originWidth=1903&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=301461&status=done&style=none&taskId=u3b149bdf-85a2-4640-8cd6-e61f8680121&title=&width=1585.8332703179808)
点击app.tsx，找到getInitialState并修改**(修改思路 时间点47:27-48:09)**。
ps.getInitialState 是 Ant Design Pro 用来保存全局状态的一个函数。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717840628234-6bed7e63-2c4e-4005-81e9-00feb7d9724a.png#averageHue=%2325272c&clientId=ua095d19c-0831-4&from=paste&height=785&id=ue5848154&originHeight=942&originWidth=1138&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=212628&status=done&style=none&taskId=u1a062b68-3ba4-4a1d-82c6-eb2d10a09cd&title=&width=948.3332956499538)
```
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: API.CurrentUser;
  loading?: boolean;
  fetchUserInfo?: () => Promise<API.CurrentUser | undefined>;
}> {
```
只留下一个
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717840748108-387bf4ec-3746-4ddb-b190-8ea065cee255.png#averageHue=%2324272b&clientId=ua095d19c-0831-4&from=paste&height=498&id=u97736a72&originHeight=598&originWidth=1138&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=129291&status=done&style=none&taskId=u9eba1348-affa-4087-ad64-d07d630267f&title=&width=948.3332956499538)
```
export async function getInitialState(): Promise<{
  currentUser?: API.LoginUserVO;
}> {
```
继续优化将queryCurrentUser修改为getLoginUserUsingGet
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717840809113-850bc473-202e-4740-87c4-c0061836e233.png#averageHue=%23232529&clientId=ua095d19c-0831-4&from=paste&height=517&id=u55152d99&originHeight=621&originWidth=1077&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=137104&status=done&style=none&taskId=ub358512a-8fc0-4ab7-9adf-d09933fef35&title=&width=897.4999643365556)
```
const fetchUserInfo = async () => {
  try {
    const msg = await queryCurrentUser({
      skipErrorHandler: true,
    });
    return msg.data;
  } catch (error) {
    history.push(loginPath);
  }
  return undefined;
};
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717840911648-2d043bd0-0848-4eae-a1ea-08a3911dee58.png#averageHue=%23232529&clientId=ua095d19c-0831-4&from=paste&height=619&id=u0fd851a3&originHeight=743&originWidth=1082&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=164308&status=done&style=none&taskId=ua704c120-2569-48a1-9bea-c465d60b28e&title=&width=901.6666308376539)
```
const fetchUserInfo = async () => {
  try {
    const res = await getLoginUserUsingGet();
    return res.data;
  } catch (error) {
    history.push(loginPath);
  }
  return undefined;
};
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717840940442-75524814-3218-4e19-8451-2572c9817d37.png#averageHue=%2325272b&clientId=ua095d19c-0831-4&from=paste&height=785&id=udf203800&originHeight=942&originWidth=1138&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=216508&status=done&style=none&taskId=ue34c15d0-f38b-477c-96b8-68035ca4474&title=&width=948.3332956499538)
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659286243-5ae9973a-5bad-4178-9841-1eec3acb2b8c.png#averageHue=%23fcfbfa&from=url&id=fB5XV&originHeight=550&originWidth=797&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
#### 头像加载(时间点 48:47-59:20)
之前登录之后会发现右上角的头像一直在加载状态，这里使用编程导航头像，将链接粘贴至后端数据库中
[https://yupi.icu/logo.png](https://yupi.icu/logo.png)
![12a86f552f13c666fbfc8fffba4e46a7_1.jpg](https://cdn.nlark.com/yuque/0/2024/jpeg/1389428/1717842652865-a6f02016-b778-4405-aca4-616e5a4a3ddd.jpeg#averageHue=%238f9691&clientId=ua095d19c-0831-4&from=paste&height=799&id=u72becf07&originHeight=959&originWidth=959&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=106524&status=done&style=none&taskId=u607a6db1-a502-466d-a0be-d74d6b64032&title=&width=799.1666349106378)
在前端的app.tsx继续往下滑，这里有定义头像，换成我们后端的头像属性userAvatar。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717841187772-381e7a07-a5f3-4afd-95fc-1bb8002b7ac6.png#averageHue=%2325272b&clientId=ua095d19c-0831-4&from=paste&height=785&id=udab63a92&originHeight=942&originWidth=1138&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=208404&status=done&style=none&taskId=u71959690-dcfb-49c6-9f5d-4a0716c86a8&title=&width=948.3332956499538)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717841237947-86e3332d-a80f-4562-b877-c521f14f5044.png#averageHue=%2325272b&clientId=ua095d19c-0831-4&from=paste&height=785&id=u1f9fc332&originHeight=942&originWidth=1138&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=203379&status=done&style=none&taskId=u313285c2-f284-41ca-b597-255d14068ce&title=&width=948.3332956499538)
然后因为用户名不对，所以现在头像还一直在加载中。
进入src目录→commponents→RightContent→AvatarDropdown.tsx(头像下拉框)；
把name改成userName。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659287849-6c617480-cf42-48ca-9afa-9925a32a9e0f.png#averageHue=%23f7f5f3&from=url&id=yZCZe&originHeight=663&originWidth=1000&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

#### 登录状态失效(时间点 50:07-53:55)
求地址还多了一个 token%20=%20123，为什么会出现这个问题？
首先，要了解前端怎么知道后端有登录，前端是通过什么告诉后端的？**通过cookie**
看一下请求头，有 cookie 吗？没有 cookie，后端就不知道是哪个客户端或电脑端登录过了。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659291991-204acddb-38d8-4ca5-a315-06e965a4fecf.png#averageHue=%23dac7af&from=url&id=N9W2Y&originHeight=693&originWidth=1000&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
要改一下前端的全局请求，上次在这里改过全局请求的地址。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717842206204-b4742243-f87b-4355-8e0d-abd394c568a3.png#averageHue=%2324262a&clientId=ua095d19c-0831-4&from=paste&height=731&id=u166e6f3c&originHeight=877&originWidth=1165&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=182898&status=done&style=none&taskId=ucef4a5a6-6aa0-4917-b8b7-1e1a4dd553b&title=&width=970.8332947558843)
要带上 cookie，加上withCredentials: true。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717842237089-85ab23db-7420-4d27-b476-b9ae668224bb.png#averageHue=%2324262b&clientId=ua095d19c-0831-4&from=paste&height=785&id=u72ac9053&originHeight=942&originWidth=1138&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=186430&status=done&style=none&taskId=u5047f37d-02ff-4274-bbc8-a13e10174f2&title=&width=948.3332956499538)
我们所有的请求都带了上 token 的后缀，再看看前端的全局请求，它这里还加载了errorConfig。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659294193-44f37903-f1b1-4369-ba36-e9f368878f9a.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_232%2Climit_0#averageHue=%23f9f6f3&from=url&id=zMr9F&originHeight=85&originWidth=232&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
选中errorConfig按Ctrl+鼠标左键进入，来到requestErrorConfig.ts。
请求配置起这个名字，不是很好，改成requestConfig.ts。
这个文件里定义了请求处理、请求拦截器、响应拦截器，在请求拦截器中它给所有的请求都加上了token....字段，注释掉。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659296062-eae03742-7973-4f59-8c65-9f83fe225a13.png#averageHue=%23fcfaf9&from=url&id=RUjus&originHeight=632&originWidth=811&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
再次登录，就没有多余的字段了。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717842400473-dd2fe472-52e4-4a71-96cc-39678a9aebfe.png#averageHue=%2324272b&clientId=ua095d19c-0831-4&from=paste&height=785&id=ud2636d09&originHeight=942&originWidth=1138&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=175466&status=done&style=none&taskId=u64e45fee-0f7c-4c35-bf30-9ff35971ff7&title=&width=948.3332956499538)

先删掉不需要的，再根据后端去故调整（和后端联调）
如果登录后还是没法进入主页，修改全局请求配置app.tsx：
```typescript
export const request = {
  // 请求后端地址
  baseURL: 'http://localhost:8103',
  // 获取cookie
  withCredentials: true,
  ...errorConfig,
};
```
取消注释token：
```typescript
// 请求拦截器
requestInterceptors: [
  (config: RequestOptions) => {
    // 拦截请求配置，进行个性化处理。
    // const url = config?.url?.concat('?token = 123');  // 携带token
    // return { ...config, url };
    return config;
  },
],
```
头像转圈：去AvatarDropdown.tsx，修改成后端对应的字段
#### 注册界面(新增)

1. 增加路由
2. 复制粘贴登录的界面
3. 添加多一个表单
4. 修改成注册的后端请求
> 更多的可以看我的用户中心项目代码 ：https://github.com/kongshier/shier-usercenter-frontend，同时我也在这里用上了用户中心的功能

## 2、智能分析业务开发

1. 用户输入 
   1. 分析目标
   2. 上传原始数据(excel)
   3. 更精细地控制图表：比如图表类型、图表名你等
2. 后端校验 
   1. 校验用户的输入是否合法（比如长度）
   2. 成本控制（次数统计和校验、鉴权等
3. 把处理后的数据输入给 AI 模型（调用AI接口），让 AI 模型给我们提供图表信息、结论文本
4. 图表信息（是一段 JSON 配置，是一设代码）、结论文本在前端进行展示
### 2.1 业务流程
#### （1）开发接口
根据用户的输入（文本和文件），最后返回图表信息和结论文本
> 给chart 表添加一个字段 chartName

```sql
-- 图表信息表
create table if not exists chart
(
    id         bigint auto_increment comment 'id' primary key,
    goal       text                               null comment '分析目标',
    chartName   varchar(256)                       null comment '图表名称',
    chartData  text                               null comment '图表数据',
    chartType  varchar(256)                       null comment '图表类型',
    genChart   text                               null comment '生成的图表信息',
    genResult  text                               null comment '生成的分析结论',
    userId     bigint                             null comment '创建图标用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
) comment '图表信息表' collate = utf8mb4_unicode_ci;
```
首先，进入到后端，我们需要在接收用户上传的 excel 文件时，在请求的参数中对接一下文件，能够接收到文件，可以仿照模板中文件上传的接口进行编写。
复制FileController.java的文件上传代码。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1717847667064-e7686685-7589-446c-bc5a-c0a4c83a7951.png#averageHue=%23272c34&clientId=ua095d19c-0831-4&from=paste&height=801&id=ude050448&originHeight=961&originWidth=1484&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=244695&status=done&style=none&taskId=u59a8916a-5a52-455f-8afc-25d3fa32dd9&title=&width=1236.6666175259504)
粘贴至ChartController.java。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659297300-784370f2-55d3-4b57-8ddf-17ebe974624b.png#averageHue=%23fbf9f7&from=url&id=BfXxn&originHeight=502&originWidth=1000&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
选中UploadFileRequest，按Ctrl+鼠标左键，进入到UploadFileRequest.java文件中。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659297722-edb85f0d-6202-46a8-b3d7-b57afee373d0.png#averageHue=%23faf8f6&from=url&id=GAdBW&originHeight=548&originWidth=1000&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
复制这个文件，粘贴至com.yupi.springbootinit.model.dto.chart中。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659298322-88949321-51d3-4e58-bd20-bef41b30bd2c.png#averageHue=%23f9f8f7&from=url&id=iO5ng&originHeight=660&originWidth=1000&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
复制这个文件，粘贴至com.yupi.springbootinit.model.dto.chart中。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659298322-88949321-51d3-4e58-bd20-bef41b30bd2c.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_526%2Climit_0#averageHue=%23f9f8f7&from=url&id=pK1bS&originHeight=347&originWidth=526&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
并改名为GenChartByAiRequest.java。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659298798-a3dbf362-79c0-47b2-8cc4-e44212599a67.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_229%2Climit_0#averageHue=%23f9f7f5&from=url&id=ZvoE3&originHeight=156&originWidth=229&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
这里我们要让用户输入什么内容呢？找到之前的图表设计(chart表)。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659299360-07018624-04fa-48bc-9525-7ad859562cba.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_528%2Climit_0#averageHue=%23fbfaf9&from=url&id=HcZdD&originHeight=386&originWidth=528&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
发现少了一个让用户输入名称的属性，找到sql目录→create_table.sql，添加name。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659299721-48a7df8a-0c86-4ba9-abfb-eab3b0dccf68.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_514%2Climit_0#averageHue=%23f7f6f2&from=url&id=i6cag&originHeight=225&originWidth=514&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
首先在 sql 文件定义好，就有一个归档的作用。
然后右键chart表→Modify Table。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659300244-34132000-f7d1-40c7-b83d-2513c21b8a3a.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_504%2Climit_0#averageHue=%23f2eeed&from=url&id=ESji9&originHeight=250&originWidth=504&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
添加新字段。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659300554-bede6cd8-b705-4864-911d-7cbe02144004.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_507%2Climit_0#averageHue=%23f5f3f2&from=url&id=f1Jjn&originHeight=562&originWidth=507&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
点击chart表，看看添加上了没，然后鼠标左键点击name拖到id的右边。				
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659300971-971e9f37-ad23-43cb-adc4-11c09f287bc6.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_537%2Climit_0#averageHue=%23f8f7f7&from=url&id=AwWfK&originHeight=164&originWidth=537&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
去后端补充对应的字段，找到Chart.java，添加字段。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659301415-6416b463-0bcc-4fad-a01c-553cb82478a3.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_537%2Climit_0#averageHue=%23f9f7f6&from=url&id=nSsNO&originHeight=326&originWidth=537&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
添加、编辑、查询、更新的时候，需要用户输入名称，所以要加上图表名称。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659301908-eed79372-b15e-411c-90ac-42f877b754a0.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_536%2Climit_0#averageHue=%23f9f7f6&from=url&id=bTGzJ&originHeight=327&originWidth=536&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659302454-6a518cbf-8cc4-4907-9e26-dbcb1b967112.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_538%2Climit_0#averageHue=%23f9f7f6&from=url&id=ySlvV&originHeight=323&originWidth=538&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659302896-4c49cd59-a10b-4ca7-bd12-7480cfb2a921.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_538%2Climit_0#averageHue=%23f9f7f6&from=url&id=QwDm8&originHeight=323&originWidth=538&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659303351-db9fc718-922c-4ffb-af77-77825fd7ae4b.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_543%2Climit_0#averageHue=%23f9f7f5&from=url&id=ejuPM&originHeight=314&originWidth=543&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
包括GenChartByAiRequest.java让 AI 帮我们智能分析，也需要接收名称、目标、文件、图表类型。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659303841-ab9f12a9-c5e7-4e38-addb-08822291adee.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_574%2Climit_0#averageHue=%23faf8f7&from=url&id=BSjdf&originHeight=357&originWidth=574&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
去ChartMapper补充字段。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659304490-ab48ccd7-797b-4553-b0ec-ba5cddb6cdd4.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_628%2Climit_0#averageHue=%23f8f6f4&from=url&id=KN5Ic&originHeight=316&originWidth=628&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
查询条件多加个字段，名称可以用模糊查询。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659304968-7da2f0be-892d-4bfd-bf01-17791d72f0be.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_621%2Climit_0#averageHue=%23f6f2f0&from=url&id=wwEjY&originHeight=294&originWidth=621&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
修改对应的实现类，在查询时增加上这个字段的模糊查询条件
#### （2）原始数据压缩
AI 接口普遍者都有输入字数处限制，尽可能压缩数据，的够允许多传一些数据
使用csv对excel文件的数据进行提取和压缩
[https://easyexcel.opensource.alibaba.com/docs/current/](https://easyexcel.opensource.alibaba.com/docs/current/)
回到智能分析，继续修改。 
首先，是获取用户的输入和 excel 文件。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659305497-c4424865-7f04-4d1a-ad17-0bc1b22ddfe5.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_634%2Climit_0#averageHue=%23fbfaf9&from=url&id=MiU9w&originHeight=273&originWidth=634&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
```
/**
 * 智能分析
 *
 * @param multipartFile
 * @param genChartByAiRequest
 * @param request
 * @return
 */
@PostMapping("/gen")
public BaseResponse<String> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                         GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
    String name = genChartByAiRequest.getName();
    String goal = genChartByAiRequest.getGoal();
    String chartType = genChartByAiRequest.getChartType();

    // 校验
    // 如果分析目标为空，就抛出请求参数错误异常，并给出提示
    ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
    // 如果名称不为空，并且名称长度大于100，就抛出异常，并给出提示
    ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");

    // 读取到用户上传的 excel 文件，进行一个处理
    User loginUser = userService.getLoginUser(request);
    // 文件目录：根据业务、用户来划分
    String uuid = RandomStringUtils.randomAlphanumeric(8);
    String filename = uuid + "-" + multipartFile.getOriginalFilename();
    File file = null;
    try {

        // 返回可访问地址
        return ResultUtils.success("");
    } catch (Exception e) {
        //            log.error("file upload error, filepath = " + filepath, e);
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
    } finally {
        if (file != null) {
            // 删除临时文件
            boolean delete = file.delete();
            if (!delete) {
                //                    log.error("file delete error, filepath = {}", filepath);
            }
        }
    }
}
```

 读取到用户上传的 excel 文件之后，进行一个处理，要进行什么处理呢？
用 chatGPT 也好，还是用鱼聪明，无论使用哪种 AI 模型，输入都是数据而非文件。那怎么输入数据呢？
ps.AI 接口普遍都有输入字数限制。所以我们要尽可能压缩数据，能够允许多传一些数据。
```
Al提词技巧1:持续输入,持续优化

第一次问:
我给你三行数据，请帮我分析网站的增长趋势，数据如下:
第一行:日期: 1号, 用户数: 10人
第二行:日期: 2号，用户数: 20人
第三行:日期: 3号，用户数: 30人

Al答:
根据给出的数据，我们可以看出这个网站的用户数在三天内呈现快速增长的趋势。
具体来说,该网站用户数从第一天的10人增长到第二天的20人，增加了10人，
增长率为100%;相比之下,第三天用户数从第二天的20人增长到第三天的30人，
增加了同样的10人，增长率降至50%。如果这种增长趋势持续下去,我们可以
预期该网站在未来几天内还会继续吸引更多用户。当然，若要得出更加准确
的结论,需要更多数据的支持。
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # 
AI提词技巧2:数据压缩(内容压缩，比如把很长的内容提取关键词,也可以让Al来做)

第二次问:
我给你三行数据，请帮我分析网站的增长趋势，数据如下:
表头:日期,用户数
1号,10
2号,20
3号,30

Al回答:
根据给出的数据，我们可以计算出每日用户增长量:
● 第一天增长了10个新用户;
● 第二天增长了20 - 10=10个新用户;
● 第三天增长了30 - 20=10个新用户。
可以看出，每天的用户增长量都相等，呈现一个线性增长趋势。如果这个网站
在未来的时间里能够保持现在的增长速度和策略不变,预计每过一天会增加10个
用户。但需要注意的是,这个预测是基于目前的数据情况，实际情况受到许多因
素的影响，不能完全准确预测未来用户增长。
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # 
使用Al压缩

第三次问:
我给你三行数据，请帮我用最少的字数压缩这段数据
第一行:日期: 1号, 用户数: 10人
第二行:日期: 2号，用户数: 20人
第三行:日期: 3号，用户数: 30人

Al回答:
日期1-3号,用户数分别为10/20/30人。
```
我们需要读取excel文件，并且把它转成csv。
去写一个工具类(excel转csv)，在com.yupi.springbootinit.utils下创建ExcelUtils.java。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659305850-dd4592c8-f89a-4483-a7c5-345c3b3e7f52.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_157%2Climit_0#averageHue=%23e7e5e3&from=url&id=JgzRp&originHeight=54&originWidth=157&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
怎么处理 excel？使用csv对excel文件的数据进行提取和压缩 👉[开源库](https://easyexcel.opensource.alibaba.com/docs/current/)
我们请求接收的是multipartFile。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659306207-4a6bb6de-40d5-47c8-8ec2-4457f670f7fd.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_612%2Climit_0#averageHue=%23faf9f7&from=url&id=SfR4W&originHeight=266&originWidth=612&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
把它拿到工具类中(不是剪切到工具类中，是使用它)。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659306718-412da731-8cba-491f-abd0-32e2414e9836.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_614%2Climit_0#averageHue=%23fbf9f7&from=url&id=GNWjP&originHeight=303&originWidth=614&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
怎么读 excel？模板里有。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659307044-2b3bf803-ee3b-4594-8324-37dc9ef69f32.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_619%2Climit_0#averageHue=%23f9f7f6&from=url&id=Fso3y&originHeight=361&originWidth=619&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
或者看官方文档。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659307486-3dfae28c-4c20-4c55-af7b-57d14c7e0419.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_624%2Climit_0#averageHue=%23bfc8b6&from=url&id=QepUS&originHeight=408&originWidth=624&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
直接把模板里的代码复制，粘贴至ExcelUtils.java；
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659307934-f1662103-5e9c-4f85-a2f8-119bdb8827ef.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_624%2Climit_0#averageHue=%23fbfaf8&from=url&id=lQGuk&originHeight=406&originWidth=624&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
模板里还有测试用的 excel 文件，复制这个文件，粘贴至resources下，并重命名为网站数据.xlsx。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659308366-2b242bce-643d-4fa1-9b38-bffbc5022b2f.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_620%2Climit_0#averageHue=%23d9d4bc&from=url&id=eXm9u&originHeight=211&originWidth=620&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
继续修改工具类。
```
package com.yupi.springbootinit.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.SneakyThrows;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Excel 相关工具类
 */
public class ExcelUtils {
    public static String excelToCsv(MultipartFile multipartFile) {
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:网站数据.xlsx");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<Map<Integer, String>> list = EasyExcel.read(file)
        .excelType(ExcelTypeEnum.XLSX)
        .sheet()
        .headRowNumber(0)
        .doReadSync();
        System.out.println(list);
        return "";
    }

    public static void main(String[] args) {
        excelToCsv(null);
    }
}
```
把网站数据修改一下，进入resources下的网站数据.xlsx。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659308738-81025ac9-1f2e-4296-8e36-50f7558ee85e.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_308%2Climit_0#averageHue=%23e1bb7c&from=url&id=FeNv2&originHeight=242&originWidth=308&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
打个断点，试一下能不能读取出来，以debug模式启动。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659309050-81fb879e-c0f5-4ebb-b80e-904790bc87c1.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_418%2Climit_0#averageHue=%23f7f4f2&from=url&id=pdywi&originHeight=261&originWidth=418&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
发现有 4 条 null 的数据，说明表格不干净，要进行过滤。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659309376-456aba57-5dcb-4a84-b79c-dfad3aae4e87.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_688%2Climit_0#averageHue=%23f5f4f3&from=url&id=MTdbW&originHeight=398&originWidth=688&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

**代码思路(时间点 01:39:20-01:45:59)**
```
package com.yupi.springbootinit.utils;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel 相关工具类
 */
public class ExcelUtils {
    public static String excelToCsv(MultipartFile multipartFile) {
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:test_excel.xlsx");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 读取数据
        List<Map<Integer, String>> list = EasyExcel.read(file)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet()
                .headRowNumber(0)
                .doReadSync();
        // 如果数据为空
        if (CollUtil.isEmpty(list)) {
            return "";
        }
        // 转换为 csv
        StringBuilder stringBuilder = new StringBuilder();
        // 读取表头(第一行)
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap) list.get(0);
        List<String> headerList = headerMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
        stringBuilder.append(StringUtils.join(headerList,","));
//        System.out.println(StringUtils.join(headerList,","));
        // 读取数据(读取完表头之后，从第一行开始读取)
        for (int i = 1; i < list.size(); i++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap) list.get(i);
            List<String> dataList = dataMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
            stringBuilder.append(StringUtils.join(dataList,","));
//            System.out.println(StringUtils.join(dataList,","));
        }
//        System.out.println(list);
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        excelToCsv(null);
    }
}
```

运行ExcelUtils.java，看一下效果，数据量已经大大地压缩了。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659309960-43e876b5-e145-43f1-9d71-94de6b62db1a.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_684%2Climit_0#averageHue=%23f7f4f2&from=url&id=tM9wS&originHeight=576&originWidth=684&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
已经测试完压缩流程，再把得到的字符串再完整拼接起来就好了。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659310539-46008c1d-05c2-436d-a736-1b65919b102d.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_681%2Climit_0#averageHue=%23fbf8f6&from=url&id=KHr9H&originHeight=437&originWidth=681&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
现在要真实地把它移到我们的请求处理，咱们前端请求传来的是multipartFile，把multipartFile传进来，其他的东西先注释。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659311075-f2aac514-f3af-46e4-b067-567a8766251e.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_673%2Climit_0#averageHue=%23fbfaf9&from=url&id=YupHz&originHeight=350&originWidth=673&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
```
/**
     * 智能分析
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen")
    public BaseResponse<String> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                             GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();

        // 校验
        // 如果分析目标为空，就抛出请求参数错误异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        // 如果名称不为空，并且名称长度大于100，就抛出异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");
        // 把multipartFile传进来，其他的东西先注释
        String result = ExcelUtils.excelToCsv(multipartFile);
        return ResultUtils.success(result);
//        // 读取到用户上传的 excel 文件，进行一个处理
//        User loginUser = userService.getLoginUser(request);
//        // 文件目录：根据业务、用户来划分
//        String uuid = RandomStringUtils.randomAlphanumeric(8);
//        String filename = uuid + "-" + multipartFile.getOriginalFilename();
//        File file = null;
//        try {
//
//            // 返回可访问地址
//            return ResultUtils.success("");
//        } catch (Exception e) {
////            log.error("file upload error, filepath = " + filepath, e);
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
//        } finally {
//            if (file != null) {
//                // 删除临时文件
//                boolean delete = file.delete();
//                if (!delete) {
////                    log.error("file delete error, filepath = {}", filepath);
//                }
//            }
//        }
    }
```
然后要把multipartFile真实地运用上，之前读的是本地文件。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659311556-68e5732c-e592-4d9a-be98-7c989db3943a.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_672%2Climit_0#averageHue=%23fbfaf8&from=url&id=D9VE8&originHeight=437&originWidth=672&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
```
package com.yupi.springbootinit.utils;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Excel 相关工具类
 */
@Slf4j
public class ExcelUtils {
    /**
     * excel 转 csv
     *
     * @param multipartFile
     * @return
     */
    public static String excelToCsv(MultipartFile multipartFile) {
//        File file = null;
//        try {
//            file = ResourceUtils.getFile("classpath:网站数据.xlsx");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        // 读取数据
        List<Map<Integer, String>> list = null;
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("表格处理错误", e);
        }
        // 如果数据为空
        if (CollUtil.isEmpty(list)) {
            return "";
        }
        // 转换为 csv
        StringBuilder stringBuilder = new StringBuilder();
        // 读取表头(第一行)
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap) list.get(0);
        List<String> headerList = headerMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
        stringBuilder.append(StringUtils.join(headerList, ",")).append("\n");
        // 读取数据(读取完表头之后，从第一行开始读取)
        for (int i = 1; i < list.size(); i++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap) list.get(i);
            List<String> dataList = dataMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
            stringBuilder.append(StringUtils.join(dataList, ",")).append("\n");
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
            excelToCsv(null);
    }
}
```
以debug模式启动项目，打个断点，访问 [http://localhost:8080/api/doc.html](http://localhost:8080/api/doc.html)。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659312080-7b0b92e5-c021-4fa8-be4e-81d00179e71d.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_669%2Climit_0#averageHue=%23f5f4f2&from=url&id=Tvpqa&originHeight=537&originWidth=669&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
选择在项目里的网站数据.xlsx，类型设置为雷达图，目标设置为分析网站用户，图表名称为网站用户趋势。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659312500-91c6cf09-5803-4175-85e5-bb91462ac3e6.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_672%2Climit_0#averageHue=%23bfb6a6&from=url&id=b8EM5&originHeight=313&originWidth=672&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
请求进来了，参数拿到了。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659312826-72fe1cb4-6d1f-4652-b856-145db17cf28c.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_675%2Climit_0#averageHue=%23f7f6f4&from=url&id=tFZoo&originHeight=354&originWidth=675&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
继续往下执行，响应内容如下图所示：
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659313383-84f3fa0e-c955-4770-999b-82ba5472009d.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_680%2Climit_0#averageHue=%23fbfbfb&from=url&id=PEn2A&originHeight=430&originWidth=680&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
搞定🥳
#### （3）调用AI

1. 用户输入
2. 系统预设（提前告诉也职责、功能、回复格式要求）+分析目标+压缩后的数据
3. 最简单的系统预设：你是一个数据分析刊师，接下来我会给你我的分析目标和原始数据，请告诉我分析结论。

ps.建议听一下回放思路
接下来怎么使用 AI 来生成结论、图表？
结论之前已经讨论过了，就是把数据给 AI，再拼接用户的分析目标。
现在把系统预设以文本形式拼接，去 ChartController.java 编写相应代码。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659313739-9dcf5e91-6974-4264-954d-d42336655b71.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_702%2Climit_0#averageHue=%23faf7f5&from=url&id=jSMi6&originHeight=409&originWidth=702&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
如果直接使用openAI，可以把它作为系统参数传进来，现在这个模型是🐟封装好的，之后需要做这样一个模型，直接把预设写在这里，去调用对应的模型即可(如下图所示)。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659314196-9739a357-1da4-476a-a5dd-324529086211.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_702%2Climit_0#averageHue=%23fafafa&from=url&id=oeKJL&originHeight=293&originWidth=702&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

```
/**
     * 智能分析
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen")
    public BaseResponse<String> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                             GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();

        // 校验
        // 如果分析目标为空，就抛出请求参数错误异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        // 如果名称不为空，并且名称长度大于100，就抛出异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");

        // 用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("你是一个数据分析师，接下来我会给你我的分析目标和原始数据，请告诉我分析结论。").append("\n");
        userInput.append("分析目标：").append(goal).append("\n");

        // 压缩后的数据（把multipartFile传进来，其他的东西先注释）
        String result = ExcelUtils.excelToCsv(multipartFile);
        userInput.append("数据：").append(result).append("\n");
        return ResultUtils.success(userInput.toString());
//        // 读取到用户上传的 excel 文件，进行一个处理
//        User loginUser = userService.getLoginUser(request);
//        // 文件目录：根据业务、用户来划分
//        String uuid = RandomStringUtils.randomAlphanumeric(8);
//        String filename = uuid + "-" + multipartFile.getOriginalFilename();
//        File file = null;
//        try {
//
//            // 返回可访问地址
//            return ResultUtils.success("");
//        } catch (Exception e) {
////            log.error("file upload error, filepath = " + filepath, e);
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
//        } finally {
//            if (file != null) {
//                // 删除临时文件
//                boolean delete = file.delete();
//                if (!delete) {
////                    log.error("file delete error, filepath = {}", filepath);
//                }
//            }
//        }
    }
```
再次启动项目，访问 http://localhost:8080/api/doc.html，返回结果如图所示：
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659314499-40f8b372-23e6-4077-afa3-3a3ba786e93c.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_700%2Climit_0#averageHue=%23b9b1a0&from=url&id=K1fFF&originHeight=377&originWidth=700&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
鱼聪明预设效果预览：
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684659315109-748ef826-bb7b-4a3a-b133-1e4d09035db0.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_696%2Climit_0#averageHue=%23b5bab8&from=url&id=QmdEc&originHeight=365&originWidth=696&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
### 2.2 AI 提词技巧
#### 1、A提词技巧1：持续输入，持续优化
第一次问：
我给你三行数据，请帮我分析网站的增长趋势，数据如下：
第一行：日期：1号，用户数：10人
第二行：日期：2号，用户数：20人
第三行：日期：3号，用户数：30人
AI答：
根据给出的数据，我们可以看出这个网站的用户数在三天内呈现快速增长的趋势。具体来说，该网站用户数从第一天的10人增长到第二天的20人，增勖加了10人，增长率100%；相比之下，第三天用户数从第二天的20人增长到第三天的30人，增加了同样的10人，增长率降至50%。如果这种增长趋势特续下去，我们可以预期该网站在未来几天内还会继续吸引更多用户。当然，若要得出更加准确的结论，需要更多数据的支持。

#### 2、AI提词技巧2：数据压缩（内容压缩，比如把很长的内容提取关键词，也可以让AI来故）
我给你三行数据，请帮我分析网站的增长趋势，数据如下：
表头：日期，用户数
1号，10
2号，20
3号，30

AI回答：
根据给出的数据，我们可以计算出每日用户增长量：

- 第一天增长了10个新用户；
- 第二天增长了20-10=10个新用户；
- 第三天增长了30-20=10个新用户。

可以看出，每天的用户增长量都相等，呈现一个线性增长趋势。如果这个网站在未来的时间里能够保持现在的增长速度和策略不变，预计每过一天会增勖加10个用户。但需要注意
的是，这个预测是基于目前的数据情况，实际情况会受到许多因素的响，不完全准确预测未来用户增长。
使用AI来压缩：
问：我给你三行数据，请帮我用最少的字数压缩这设数据
第一行：日期：1号，用户数：10人第二行：日期：2号，用户数：20人第三行：日期：3号，用户数：30人
答：日期1-3号，用户数分别为10/20/30人。
#### 3、AI 提词技巧3：在系统（模型）层面做妤预设效果一股来说，会比直接拼接在用户消息里效果更好一些。

#### 4、AI提词技巧4：除了系统预设外，额外关联一问一答两条消息，相当于给A!一个提示。
# 第三期直播 - 智能分析业务流程开发
## 1、AI生成内容
[鱼聪明](https://www.yucongming.com/)
实例：
> 你是一个数据分析师，请帮我分析网站用户的增长趋势
原始数据如下：
日期，用户数
1号，10
2号，20
3号，30

AI回答：（图片加载不出来的）
### 1.1 AI生成图表
AI无法直接生成现成的图表，但是A!可以生成图表代码=>可以把代码利用前端的组件库([Echarts](https://echarts.apache.org/zh/index.html))在网页进行展示
[Echarts在线调用](https://echarts.apache.org/examples/zh/editor.html?c=line-simple)
#### 1.1.1 AI提问技巧
如果得要让 AI 更好地理解我们的输入，给我们预期的、精确格式的输出，我们就需要严格控制咱们的提问词。

1.  使用系统预设 + 控制输入格式(便于Al精确地理解我们的需求)
2.  控制输入格式（便于A!精确地理解我们的需求） 


用户提问： 
```java
分析需求：
分析网站用户的增长情况
原始数据：
日期,用户数
1号,10
2号,20
3号,30
```

3.  控制输出格式（便于AI返回的内容能够更加方便地为我们所用）
Prompt预设： 
```
你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：
分析需求：
{数据分析的需求或者目标}
原始数据：
{csv格式的原始数据，用,作为分隔符}
请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）
【【【【【
{前端 Echarts V5 的 option 配置对象的json格式代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释}
【【【【【
{明确的数据分析结论、越详细越好，不要生成多余的注释}
```
```java
【【【【【
{
    title: {
        text: '网站用户增长情况',
        subtext: ''
        },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
            }
    },
    legend: {
        data: ['用户数']
        },
    xAxis: {
        data: ['1号', '2号', '3号']
    },
    yAxis: {},
    series: [{
        name: '用户数',
        type: 'bar',
        data: [10, 20, 30]
    }]
}
【【【【【
根据数据分析可得，该网站用户数量逐日增长，时间越长，用户数量增长越多。
```

用户输入： 
```
分析需求：
分析网站用户的增长情况
原始数据：
日期，用户数
1号，10
2号，20
3号，30
```
生成的内容： 
将上面的代码复制到Echarts执行：
![](http://picgo.kongshier.top/typoraImages/image-20230521100605786.png#id=xsK9t&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)

4.  指定一个示例问答，one-shot或者few-shot
### 1.2 调用 AI 方式
```java
final String prompt = "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n" +
                "分析需求：\n" +
                "{数据分析的需求或者目标}\n" +
                "原始数据：\n" +
                "{csv格式的原始数据，用,作为分隔符}\n" +
                "请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\n" +
                "【【【【【\n" +
                "{前端 Echarts V5 的 option 配置对象的json格式代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释}\n" +
                "【【【【【\n" +
                "{明确的数据分析结论、越详细越好，不要生成多余的注释}";
```
```java
/**
 * 智能分析
 *
 * @param multipartFile
 * @param genChartByAiRequest
 * @param request
 * @return
 */
@PostMapping("/gen")
public BaseResponse<String> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                         GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
    String name = genChartByAiRequest.getName();
    String goal = genChartByAiRequest.getGoal();
    String chartType = genChartByAiRequest.getChartType();

    // 校验
    // 如果分析目标为空，就抛出请求参数错误异常，并给出提示
    ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
    // 如果名称不为空，并且名称长度大于100，就抛出异常，并给出提示
    ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");

    final String prompt = "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n" +
            "分析需求：\n" +
            "{数据分析的需求或者目标}\n" +
            "原始数据：\n" +
            "{csv格式的原始数据，用,作为分隔符}\n" +
            "请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\n" +
            "【【【【【\n" +
            "{前端 Echarts V5 的 option 配置对象的json格式代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释}\n" +
            "【【【【【\n" +
            "{明确的数据分析结论、越详细越好，不要生成多余的注释}";


    // 用户输入
    StringBuilder userInput = new StringBuilder();
    userInput.append("你是一个数据分析师，接下来我会给你我的分析目标和原始数据，请告诉我分析结论。").append("\n");
    userInput.append("分析目标：").append(goal).append("\n");

    // 压缩后的数据（把multipartFile传进来，其他的东西先注释）
    String result = ExcelUtils.excelToCsv(multipartFile);
    userInput.append("数据：").append(result).append("\n");
    return ResultUtils.success(userInput.toString());
//        // 读取到用户上传的 excel 文件，进行一个处理
//        User loginUser = userService.getLoginUser(request);
//        // 文件目录：根据业务、用户来划分
//        String uuid = RandomStringUtils.randomAlphanumeric(8);
//        String filename = uuid + "-" + multipartFile.getOriginalFilename();
//        File file = null;
//        try {
//
//            // 返回可访问地址
//            return ResultUtils.success("");
//        } catch (Exception e) {
////            log.error("file upload error, filepath = " + filepath, e);
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
//        } finally {
//            if (file != null) {
//                // 删除临时文件
//                boolean delete = file.delete();
//                if (!delete) {
////                    log.error("file delete error, filepath = {}", filepath);
//                }
//            }
//        }
}
```
#### 1、直接调用OpenAI或者其他 AI 原始模型官网的接口
比如OpenAl或者其他Al原始大模型官网的接口
官方文档：[https://platform.openai.com/docs/api-reference](https://platform.openai.com/docs/api-reference)
优点：不经封装，最灵活，最原始
缺点：要钱、要魔法
本质上OpenAl就是提供了HTTP接口，我们可以用任何语言去调用

1.  在请求头中指定 OPENAI_API_KEY → Authorization: Bearer OPENAI_API_KEY

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684931399566-570ae31f-99ac-43fb-8d60-91d6515e9209.png#averageHue=%23ececeb&from=url&id=QIny9&originHeight=452&originWidth=1263&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
否则它会提示你没有权限调用。

2.  找到你要使用的接☐，比如Al对话接口：[https://platform.openai.com/docs/api-reference/chat/create](https://platform.openai.com/docs/api-reference/chat/create) 

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1718092065473-96426a9a-3009-4296-b5b4-c251e4b5f41a.png#averageHue=%23d5d5d5&clientId=u3d84c29e-7367-4&from=paste&height=813&id=u912018a4&originHeight=976&originWidth=1903&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=181961&status=done&style=none&taskId=u22342763-6c4f-48bf-85cd-7d4735c7b03&title=&width=1585.8332703179808)

3.  按照接口文档的示例，构造HTTP请求，比如用Hutool工具类、或者HTTPClient 
```java
/**
 * AI 对话（需要自己创建请求响应对象）
 *
 * @param request
 * @param openAiApiKey
 * @return
 */
public CreateChatCompletionResponse createChatCompletion(CreateChatCompletionRequest request, String openAiApiKey) { 
    if (StringUtils.isBlank(openAiApiKey)) {
     	throw new BusinessException(ErrorCode.PARAMS_ERROR, "未传 openAiApiKey");
     }
     String url = "https://api.openai.com/v1/chat/completions";
     String json = JSONUtil.toJsonStr(request);
     String result = HttpRequest.post(url)
                                 .header("Authorization", "Bearer " + openAiApiKey)
                                 .body(json)
                                 .execute()
                                 .body();
     return JSONUtil.toBean(result, CreateChatCompletionResponse.class);
}
```
最终结果可能返回以下内容：
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684931619992-c0e6f326-93c9-43f5-8010-9ab1d863271c.png#averageHue=%23222527&from=url&id=n3u7b&originHeight=576&originWidth=645&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
#### 2、使用云服务商提供的封装接口
比如：Azure云
优点：本地都用
缺点：依然要钱，而且可能比直接调用原始的接口更贵
#### 3、鱼聪明AI开放平台
鱼聪明 AI 提供了现成的SDK来让大家更方便地使用AI能力
鱼聪明 Al 网站：[https://yucongming.com/](https://yucongming.com/) (也可以直接公众号搜索【鱼聪明Al】移动端使用)
优点：目前不要钱，而且有很多现成的模型(prompt系统预设)给大家用
缺点：不完全灵活，但是可以定义自己的模型
参考文章进行操作：[https://github.com/liyupi/yucongming-java-sdk](https://github.com/liyupi/yucongming-java-sdk)
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684931619992-c0e6f326-93c9-43f5-8010-9ab1d863271c.png#averageHue=%23222527&from=url&id=ia9B4&originHeight=576&originWidth=645&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
##### 用流程：
右上角点击头像 → 开发平台。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684675698257-7a8692b3-5d77-46d0-ad38-70597be5c2a9.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1239%2Climit_0#averageHue=%23eaf1f1&from=url&id=f0YNs&originHeight=407&originWidth=1239&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

得到开发者密钥，然后点击 Java SDK。
[https://www.yucongming.com/dev](https://www.yucongming.com/dev)
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684675968355-0db825f1-10c3-496a-800e-50d5a6b23924.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1233%2Climit_0#averageHue=%23eed8a4&from=url&id=pxWVL&originHeight=486&originWidth=1233&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

🐟开源到 github 上了。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684676000598-ce256fb4-3f76-498d-95d7-0c569a731783.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1230%2Climit_0#averageHue=%23eed7aa&from=url&id=Twecp&originHeight=939&originWidth=1230&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

往下滑，有一个官方文档，第一步引入sdk。	
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684676094003-8e06bac1-92cd-441c-a3b6-ef9c0a6a83c3.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_800%2Climit_0#averageHue=%23dcba86&from=url&id=r1SGo&originHeight=810&originWidth=800&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
```java
<dependency>
  <groupId>com.yucongming</groupId>
  <artifactId>yucongming-java-sdk</artifactId>
  <version>0.0.2</version>
</dependency>
```
粘贴至pom.xml，然后点击刷新按钮![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684676342985-dddf5644-bb17-4eb6-aa7e-a3ab78d7f0d6.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_33%2Climit_0#averageHue=%23e0e0e0&from=url&id=ATjeP&originHeight=30&originWidth=33&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684932154610-e7118bd2-0df4-4d72-8ed9-d11fe261370e.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1238%2Climit_0#averageHue=%23fbf8f6&from=url&id=mCQBr&originHeight=723&originWidth=1238&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

第二步，初始化 YuCongMingClient 对象；
进入application.yml，通过配置注入对象。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684718614419-f8205d5b-fd7b-4678-8b09-63e74a5da240.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_803%2Climit_0#averageHue=%23f9f7f6&from=url&id=LKlwD&originHeight=639&originWidth=803&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

在manager目录下创建AiManager.java(用来对接第三方接口)。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684719790081-2444aa7c-45e8-4cdf-aa5e-f8169737f986.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_354%2Climit_0#averageHue=%23fbfaf9&from=url&id=p3Mqa&originHeight=399&originWidth=354&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
去鱼聪明 AI 随便找一个 AI 模型，点击进入对话，复制模型 id。
先点击分享得会员。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1692844557569-12afbeb9-4dc6-4a45-b973-66dd5e10c497.png?x-oss-process=image%2Fformat%2Cwebp#averageHue=%23fcfbfb&from=url&id=WFXLY&originHeight=361&originWidth=1200&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

点击复制链接。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1692844584220-eb00f3fa-f2fc-492f-a336-3f36482cbcac.png?x-oss-process=image%2Fformat%2Cwebp#averageHue=%23ad9146&from=url&id=K7A1w&originHeight=890&originWidth=521&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

从链接中我们可以得到模型 id 为 1651468516836098050。
在AiManager.java编写具体的示例代码：
```java
package com.yupi.springbootinit.manager;

import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用于对接 AI 平台
 */
@Service
public class AiManager {

    @Resource
    private YuCongMingClient yuCongMingClient;

    /**
     * AI 对话
     *
     * @param message
     * @return
     */
    public String doChat(String message) {
        // 第三步，构造请求参数
        DevChatRequest devChatRequest = new DevChatRequest();
        // 模型id，尾后加L，转成long类型
        devChatRequest.setModelId(1651468516836098050L);
        devChatRequest.setMessage(message);
        // 第四步，获取响应结果
        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
        // 如果响应为null，就抛出系统异常，提示“AI 响应错误”
        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 响应错误");
        }
        return response.getData().getContent();
    }
}
```
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684720172476-40020dd4-0a41-4bf2-afe7-42e46c297776.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_990%2Climit_0#averageHue=%23fbfaf8&from=url&id=W4gMY&originHeight=808&originWidth=990&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
然后去生成测试方法，测试一下；
选中AiManager，按Alt + Enter键，选择Create Test。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684720578282-cef424aa-8c29-4886-a5a4-88413860ac42.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_598%2Climit_0#averageHue=%23f7f4f1&from=url&id=daOET&originHeight=231&originWidth=598&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
勾选后点击 OK。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684720779589-5cc2e549-c540-43a7-a6dd-faf4d89d2dad.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_734%2Climit_0#averageHue=%23f2f0ef&from=url&id=CMEDU&originHeight=443&originWidth=734&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
生成测试方法。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684720936020-0411727c-173d-4a48-87fa-c9c7a3ed2326.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1056%2Climit_0#averageHue=%23faf9f7&from=url&id=E8Rig&originHeight=655&originWidth=1056&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
编写测试类，让 AI 找和邓紫棋相似的曲风。
```java
package com.yupi.springbootinit.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiManagerTest {

    @Resource
    private AiManager aiManager;

    @Test
    void doChat() {
        String answer = aiManager.doChat("邓紫棋");
        System.out.println(answer);
    }
}
```

点击左侧![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684721384415-0e369f9f-c864-4bdc-bac5-af3f120d0bea.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_19%2Climit_0#averageHue=%23f1f1f1&from=url&id=pByxn&originHeight=19&originWidth=19&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
按钮，运行。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684721350352-f85e0b8f-ca97-4bc2-b5dc-21c1d52e4651.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_747%2Climit_0#averageHue=%23faf9f8&from=url&id=uZAuk&originHeight=393&originWidth=747&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
测试成功╰(￣▽￣)╭
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684727878725-ca5cef2a-9fb0-4e29-a948-7d2bf6750fca.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1021%2Climit_0#averageHue=%23f7f6f4&from=url&id=CEXFg&originHeight=908&originWidth=1021&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

可以把模型 id 换成我们创建的 AI。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1692844831304-f99c90bd-aab5-4bd4-9638-626970908609.png?x-oss-process=image%2Fformat%2Cwebp#averageHue=%23e3c89a&from=url&id=GYmfw&originHeight=262&originWidth=1172&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684730136013-f161378d-fed2-4eb0-ada2-4694720847ed.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_886%2Climit_0#averageHue=%23faf9f7&from=url&id=rjjKC&originHeight=558&originWidth=886&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

把测试类的分析诉求换一下：
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684730303451-b4ed1b64-ca66-4806-afec-093e7cd081d4.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_784%2Climit_0#averageHue=%23fbfaf8&from=url&id=tVK6k&originHeight=561&originWidth=784&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

返回结果如图所示：
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684730374726-f3c37649-e237-4593-a702-235258b579c6.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1007%2Climit_0#averageHue=%23fbfbfb&from=url&id=M9hNS&originHeight=850&originWidth=1007&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

修改代码，在外层指定模型 id：
```java
package com.yupi.springbootinit.manager;

import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用于对接 AI 平台
 */
@Service
public class AiManager {

    @Resource
    private YuCongMingClient yuCongMingClient;

    /**
     * AI 对话
     *
     * @param modelId
     * @param message
     * @return
     */
    // 在外层指定模型id，这样会优雅一点
    public String doChat(long modelId, String message) {
        // 第三步，构造请求参数
        DevChatRequest devChatRequest = new DevChatRequest();
        // 模型id，尾后加L，转成long类型
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);
        // 第四步，获取响应结果
        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
        // 如果响应为null，就抛出系统异常，提示“AI 响应错误”
        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 响应错误");
        }
        return response.getData().getContent();
    }
}
```
在测试类，补充下模型 id 的值。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684731072429-08138ded-4b7e-4a3a-8d7a-a786979fc22f.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1031%2Climit_0#averageHue=%23fcfbfa&from=url&id=C75S0&originHeight=648&originWidth=1031&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
```java
package com.yupi.springbootinit.manager;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

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
        String answer = aiManager.doChat(1654785040361893889L, prompt);
        System.out.println(answer);
    }
}
```

怎么去处理返回结果呢？	
在vo目录下创建BiResponse.java，去定义BI的返回结果。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684804009577-00049795-52a8-4456-8575-ed48be669d28.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_876%2Climit_0#averageHue=%23f8f6f5&from=url&id=wZBfJ&originHeight=708&originWidth=876&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
```java
package com.yupi.springbootinit.model.vo;

import lombok.Data;

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
```

💡 之前我们是拼接用户的输入，现在可以对之前的输入方式进行优化；
通过设定一个提示语(prompt)的方式来输入用户信息，而不必自己编写提示语。
这种优化方式使得我们可以直接调用现有模型来进行处理。
找到controller目录下的ChartController.java，直接调用AiManager的方法。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684764587287-c58da5f6-a11c-4361-9b2d-4d9b59fc163f.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1044%2Climit_0#averageHue=%23faf9f8&from=url&id=u9cok&originHeight=684&originWidth=1044&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

回到genChartByAi方法(智能分析)编写代码。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684764825038-057156d1-997b-48fd-bf14-bfff8d285b1d.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1012%2Climit_0#averageHue=%23faf8f6&from=url&id=XppHj&originHeight=905&originWidth=1012&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
```java
    /**
     * 智能分析
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen")
    public BaseResponse<BiResponse> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                             GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();

        // 校验
        // 如果分析目标为空，就抛出请求参数错误异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        // 如果名称不为空，并且名称长度大于100，就抛出异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");

        // 指定一个模型id(把id写死，也可以定义成一个常量)
        long biModelId = 1659171950288818178L;
        /*
        * 用户的输入(参考)
          分析需求：
          分析网站用户的增长情况
          原始数据：
          日期,用户数
          1号,10
          2号,20
          3号,30
        * */

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
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        userInput.append(csvData).append("\n");

        // 拿到返回结果
        String result = aiManager.doChat(biModelId,userInput.toString());
        // 对返回结果做拆分,按照5个中括号进行拆分
        String[] splits = result.split("【【【【【");
        // 拆分之后还要进行校验
        if (splits.length < 3) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 生成错误");
        }

        String genChart = splits[1].trim();
        String genResult = splits[2].trim();
        // 插入到数据库
//        Chart chart = new Chart();
//        chart.setName(name);
//        chart.setGoal(goal);
//        chart.setChartData(csvData);
//        chart.setChartType(chartType);
//        chart.setGenChart(genChart);
//        chart.setGenResult(genResult);
//        chart.setUserId(loginUser.getId());
//        boolean saveResult = chartService.save(chart);
//        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");
        BiResponse biResponse = new BiResponse();
        biResponse.setGenChart(genChart);
        biResponse.setGenResult(genResult);
//        biResponse.setChartId(chart.getId());
        return ResultUtils.success(biResponse);
        
    }
```

现在要把 AI 返回的结果进行拆分，最终返回给前端。
正常情况下，拆分出来的情况是有三部分，如下图所示：
第一块切割后的空白部分，第二块是中间的代码部分，第三块是最后的文字部分。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684804749298-68ee6808-ac2b-48ee-a72e-ce6af306dbee.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1192%2Climit_0#averageHue=%23eff2f6&from=url&id=wK9dl&originHeight=464&originWidth=1192&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

**代码实现：**
**注意修改**
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1718096846327-f0ff1d19-437e-4bbe-8a66-7d8b95bb7ef3.png#averageHue=%23272b33&clientId=u3d84c29e-7367-4&from=paste&height=430&id=u7228caf0&originHeight=1032&originWidth=1920&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=249749&status=done&style=none&taskId=u997929c5-543f-4065-8bbe-94cd67e3069&title=&width=799.9999682108573)
```java
/**
 * 智能分析
 *
 * @param multipartFile
 * @param genChartByAiRequest
 * @param request
 * @return
 */
@PostMapping("/gen")
// 把返回值改成BiResponse
public BaseResponse<BiResponse> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                         GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
    String name = genChartByAiRequest.getName();
    String goal = genChartByAiRequest.getGoal();
    String chartType = genChartByAiRequest.getChartType();

    // 校验
    // 如果分析目标为空，就抛出请求参数错误异常，并给出提示
    ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
    // 如果名称不为空，并且名称长度大于100，就抛出异常，并给出提示
    ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");
    // 通过response对象拿到用户id(必须登录才能使用)
    User loginUser = userService.getLoginUser(request);

    // 指定一个模型id(把id写死，也可以定义成一个常量)
    long biModelId = 1659171950288818178L;
    /*
    * 用户的输入(参考)
      分析需求：
      分析网站用户的增长情况
      原始数据：
      日期,用户数
      1号,10
      2号,20
      3号,30
    * */

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
    String csvData = ExcelUtils.excelToCsv(multipartFile);
    userInput.append(csvData).append("\n");

    // 拿到返回结果
    String result = aiManager.doChat(biModelId,userInput.toString());
    // 对返回结果做拆分,按照5个中括号进行拆分
    String[] splits = result.split("【【【【【");
    // 拆分之后还要进行校验
    if (splits.length < 3) {
        throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 生成错误");
    }

    String genChart = splits[1].trim();
    String genResult = splits[2].trim();
    // 插入到数据库
    Chart chart = new Chart();
    chart.setName(name);
    chart.setGoal(goal);
    chart.setChartData(csvData);
    chart.setChartType(chartType);
    chart.setGenChart(genChart);
    chart.setGenResult(genResult);
    chart.setUserId(loginUser.getId());
    boolean saveResult = chartService.save(chart);
    ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");
    BiResponse biResponse = new BiResponse();
    biResponse.setGenChart(genChart);
    biResponse.setGenResult(genResult);
    biResponse.setChartId(chart.getId());
    return ResultUtils.success(biResponse);
}
```
以debug模式启动项目，在返回 biResponse 处打个断点，访问 http://localhost:8080/api/doc.html。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684805239851-d42943cf-e1c3-4d65-86fd-c8ab47d96f5b.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1137%2Climit_0#averageHue=%23f7f5f2&from=url&id=SWOJ3&originHeight=567&originWidth=1137&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
先登录。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684933622365-eb270019-8d5f-45bc-806e-f44a8dfedc5d.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1404%2Climit_0#averageHue=%23b1aa9c&from=url&id=uhgdo&originHeight=653&originWidth=1404&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
然后去测试genChartByAi接口，图表类型先不指定。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684823488313-fc3d343b-7c73-4200-ae07-80df31f26eea.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1396%2Climit_0#averageHue=%23d1bd98&from=url&id=EQP6x&originHeight=630&originWidth=1396&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1718095853174-b0dafb36-eebe-44df-a622-91f1417b7cde.png#averageHue=%23c2b088&clientId=u3d84c29e-7367-4&from=paste&height=407&id=ude3ef5a5&originHeight=978&originWidth=1909&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=152870&status=done&style=none&taskId=u81f67eb4-6eef-478d-bf8b-c443d030f0f&title=&width=795.4166350596494)
回到 idea，复制返回结果的图表代码。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684933658391-1a182889-bb58-4f2b-adf1-dfcb9bbd7f05.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1339%2Climit_0#averageHue=%23f4f1f0&from=url&id=dv2Dw&originHeight=653&originWidth=1339&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
来到 [Echarts 支持在线调试](https://echarts.apache.org/examples/zh/editor.html?c=line-simple)，将代码粘贴进去。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684823901599-147ae4d4-d836-4d14-b271-8e51b908163c.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1331%2Climit_0#averageHue=%23d4bb6c&from=url&id=NPwEl&originHeight=864&originWidth=1331&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684823985153-22935c04-818f-4dfa-b6f2-9fe09543e871.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1348%2Climit_0#averageHue=%23fdfdfc&from=url&id=yZViS&originHeight=888&originWidth=1348&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

返回结果的图表代码
```java
{
  "title": {
    "text": "网站用户增长情况"
  },
  "xAxis": {
    "type": "category",
    "data": ["1号", "2号", "3号"]
  },
  "yAxis": {
    "type": "value"
  },
  "series": [{
    "data": [10, 20, 30],
    "type": "line"
  }]
}
```
后端按F9，继续执行，响应内容也正常返回了。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684824287841-bf2f2782-d08e-4a2a-a3b7-75eed0898dff.png?x-oss-process=image%2Fresize%2Cw_478%2Climit_0%2Fformat%2Cwebp%2Fresize%2Cw_478%2Climit_0#averageHue=%23b3b0a8&from=url&id=qUuVZ&originHeight=309&originWidth=478&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
数据库也成功添加了数据。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684824425094-a4649d32-0ad4-4ac3-8aab-2625ffa539de.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1276%2Climit_0#averageHue=%23f7f5f4&from=url&id=RbPqk&originHeight=542&originWidth=1276&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
后端结束咯~喝杯水，走动走动(～o￣▽￣)～o ~。。。 
**1.3 总结：智能接口实现(时间点 01:15:50-01:17:10)**
`AiManager` 类
```java
/**
 * @author Shier
 * CreateTime 2023/5/21 10:42
 */
@Service
public class AiManager {

    @Resource
    private YuCongMingClient congMingClient;

    public String doChat(String message) {
        DevChatRequest devChatRequest = new DevChatRequest();
        // 鱼聪明平台模型ID
        devChatRequest.setModelId(1660118603963301890L);
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = congMingClient.doChat(devChatRequest);
        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 响应错误");
        }
        return response.getData().getContent();
    }
}
```
测试类：
```java
/**
 * @author Shier
 * CreateTime 2023/5/21 10:48
 */
@SpringBootTest
class AiManagerTest {

    @Resource
    private AiManager aiManager;

    @Test
    void doChat() {
        String doChat = aiManager.doChat("湖南长沙");
        System.out.println(doChat);
    }
}
```
最后的结果：
## 2、开发图表管理功能
### 2.1 后端开放流程

1. 构造用户请求（用户消息、CSV数据、图表类型）
2. 调用鱼聪明SDK，得到 AI 响应结果
3. 从 AI 响应结果中，取出需要的信启息
4. 保存图表到数据库

BI分析接口：
```java
	/**
     * 文件上传
     */
    @PostMapping("/gen")
    public BaseResponse<BiResponse> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                                 GenChartByAiRequest chartByAiRequest, HttpServletRequest request) {
        String chartName = chartByAiRequest.getChartName();
        String goal = chartByAiRequest.getGoal();
        String chartType = chartByAiRequest.getChartType();
        User loginUser = userService.getLoginUser(request);
        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "图表分析目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(chartName) && chartName.length() > 200, ErrorCode.PARAMS_ERROR, "图表名称过长");
        ThrowUtils.throwIf(StringUtils.isBlank(chartType), ErrorCode.PARAMS_ERROR, "图表类型为空");

        // 无需Prompt，直接调用现有模型
        // 模型ID
        long biModelId = 1659171950288818178L;

        // 分析需求：
        // 分析网站用户的增长情况
        // 原始数据：
        // 日期，用户数
        // 1号，10
        // 2号，20
        // 3号，30

        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");
        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(goal).append("\n");
        userInput.append("原始数据：").append("\n");

        // 压缩后的数据
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        userInput.append(csvData).append("\n");

        // 调用AI
        String chartResult = aiManager.doChat(biModelId, userInput.toString());
        String[] splits = chartResult.split("【【【【【");
        if (splits.length < 3) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 生成错误");
        }
        // 去掉换行、空格等
		String genChart = splits[1].trim();
        String genResult = splits[2].trim();
        // 插入数据到数据库
        Chart chart = new Chart();
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartName(chartName);
        chart.setChartType(chartType);
        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        chart.setUserId(loginUser.getId());
        boolean saveResult = chartService.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");
        // 返回到前端
        BiResponse biResponse = new BiResponse();
        biResponse.setGenChart(genChart);
        biResponse.setGenResult(genResult);
        return ResultUtils.success(biResponse);
    }
```
查看数据，成功插入数据库表
### 2.2 前端开发
前端开发(时间点 01:25:36-02:21:36)
#### 开发用户表单(时间点 01:25:36-01:38:55)
对照之前预设的页面，来进行开发。
左侧是用户的表单，右侧是生成的图表，以及数据分析的总结。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684934122644-53c78567-4632-4142-bc05-a229fb29bad3.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1221%2Climit_0#averageHue=%23e1e6eb&from=url&id=y7yje&originHeight=641&originWidth=1221&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
对主页进行修改，主页应该是已经建好的图表。
把主页重定向至/add_chart，将/add_chart指定到/AddChart。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684830628113-051f7fb6-0b60-426e-8a60-35211c10c122.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_984%2Climit_0#averageHue=%23eceae8&from=url&id=efi0F&originHeight=618&originWidth=984&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
```javascript
{path: '/', redirect: '/add_chart'},
{ name: '添加图表项', path: '/add_chart', icon: 'smile', component: './AddChart' },
```

复制User目录，粘贴至src→pages目录下；
并改名为AddChart，把其他文件删掉，留下index.tsx。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684934294426-1775e47d-2831-4bf9-9565-232854ed253a.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_915%2Climit_0#averageHue=%23ebe8e6&from=url&id=uIWkS&originHeight=496&originWidth=915&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

对index.tsx进行修改，删掉多余的内容。(比如：：获取用户信息、处理提交.....)
```javascript
import { listChartByPageUsingPOST } from '@/services/yubi/chartController';
import { useModel } from '@umijs/max';
import React, { useEffect, useState } from 'react';

const Login: React.FC = () => {
  const [type, setType] = useState<string>('account');
  const { setInitialState } = useModel('@@initialState');

  useEffect(() => {
    listChartByPageUsingPOST({}).then((res) => {
      console.error('res', res);
    });
  });

  return (
    // 把页面内容指定一个类名add-chart
    <div className="add-chart">

    </div>
  );
};
export default Login;
```

怎么去开发表单？去 [ant.design 组件库](https://ant.design/index-cn) 找一个表单组件；
我们需要用到多行输入、下拉输入、文件上传三个组件。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684832249825-4e7d3648-0ed7-4960-a425-59829e6095e3.png?x-oss-process=image%2Fresize%2Cw_377%2Climit_0%2Fformat%2Cwebp%2Fresize%2Cw_377%2Climit_0#averageHue=%23d4dad4&from=url&id=K99by&originHeight=321&originWidth=377&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

找一个比较全的表单组件，点击![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684832439563-0db1f0ac-c6b4-422b-b5f7-16f32e7d090a.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_30%2Climit_0#averageHue=%23fafafa&from=url&id=tOUti&originHeight=26&originWidth=30&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)显示代码按钮。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684832408396-fff14886-ab52-4544-ab4c-d9f5d9192146.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_966%2Climit_0#averageHue=%23fcfafa&from=url&id=g1bNv&originHeight=978&originWidth=966&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1718106812078-fb1085ba-a291-42f7-949d-65e9d3732c67.png#averageHue=%23fcfcfc&clientId=u3d84c29e-7367-4&from=paste&height=408&id=uebf6451d&originHeight=979&originWidth=1913&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=144994&status=done&style=none&taskId=ub7319b55-4fad-4a3d-bd4a-fe167c8f618&title=&width=797.0833016600886)

把Form组件全复制下来，内容如下：
```javascript
<Form
  <Radio.Button value="a">item 1</Radio.Button>
  <Radio.Button value="b">item 2</Radio.Button>
  <Radio.Button value="c">item 3</Radio.Button>
  </Radio.Group>
  </Form.Item>

  <Form.Item name="checkbox-group" label="Checkbox.Group">
  <Checkbox.Group>
  <Row>
  <Col span={8}>
  <Checkbox value="A" style={{ lineHeight: '32px' }}>
A
  </Checkbox>
  </Col>
  <Col span={8}>
  <Checkbox value="B" style={{ lineHeight: '32px' }} disabled>
  B
  </Checkbox>
  </Col>
  <Col span={8}>
  <Checkbox value="C" style={{ lineHeight: '32px' }}>
  C
  </Checkbox>
  </Col>
  <Col span={8}>
  <Checkbox value="D" style={{ lineHeight: '32px' }}>
  D
  </Checkbox>
  </Col>
  <Col span={8}>
  <Checkbox value="E" style={{ lineHeight: '32px' }}>
  E
  </Checkbox>
  </Col>
  <Col span={8}>
  <Checkbox value="F" style={{ lineHeight: '32px' }}>
  F
  </Checkbox>
  </Col>
  </Row>
  </Checkbox.Group>
  </Form.Item>

  <Form.Item name="rate" label="Rate">
  <Rate />
  </Form.Item>

  <Form.Item
  name="upload"
  label="Upload"
  valuePropName="fileList"
  getValueFromEvent={normFile}
  extra="longgggggggggggggggggggggggggggggggggg"
  >
  <Upload name="logo" action="/upload.do" listType="picture">
  <Button icon={<UploadOutlined />}>Click to upload</Button>
  </Upload>
  </Form.Item>

  <Form.Item label="Dragger">
  <Form.Item name="dragger" valuePropName="fileList" getValueFromEvent={normFile} noStyle>
  <Upload.Dragger name="files" action="/upload.do">
  <p className="ant-upload-drag-icon">
  <InboxOutlined />
  </p>
  <p className="ant-upload-text">Click or drag file to this area to upload</p>
  <p className="ant-upload-hint">Support for a single or bulk upload.</p>
  </Upload.Dragger>
  </Form.Item>
  </Form.Item>

  <Form.Item wrapperCol={{ span: 12, offset: 6 }}>
  <Space>
  <Button type="primary" htmlType="submit">
  Submit
  </Button>
  <Button htmlType="reset">reset</Button>
  </Space>
  </Form.Item>
  </Form>
```

粘贴至index.tsx。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684832755550-e695ccf5-b431-42f7-92bd-b51139c8cfbc.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_706%2Climit_0#averageHue=%23fbfaf9&from=url&id=CvnRp&originHeight=706&originWidth=706&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

还有提交代码后，要执行什么方法，把这个也复制进来。
```javascript
onFinishconst onFinish = (values: any) => {
  console.log('Received values of form: ', values);
};
```

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684833049908-743a9321-18ba-4598-9225-b94e0952cccf.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_891%2Climit_0#averageHue=%23eceae8&from=url&id=sl3vC&originHeight=599&originWidth=891&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684833086491-87127f05-3d4b-4284-a60e-738f38a5f510.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_912%2Climit_0#averageHue=%23eceae8&from=url&id=OlHMP&originHeight=634&originWidth=912&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

简化内容，删除不必要的代码。
(比如：表格布局、样式，最大宽度，普通输入框、slider 滚动选项...)
精简后的index.tsx
```javascript
import { listChartByPageUsingPOST } from '@/services/yubi/chartController';
import { UploadOutlined } from '@ant-design/icons';
import { useModel } from '@umijs/max';
import { Button, Form, Select, Space, Upload } from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React, { useEffect, useState } from 'react';

const Login: React.FC = () => {
  const [type, setType] = useState<string>('account');
  const { setInitialState } = useModel('@@initialState');

  useEffect(() => {
    listChartByPageUsingPOST({}).then((res) => {
      console.error('res', res);
    });
  });

  const onFinish = (values: any) => {
    console.log('Received values of form: ', values);
  };

  return (
    // 把页面内容指定一个类名add-chart
    <div className="add-chart">
      <Form
        // 表单名称改为addChart
        name="addChart"
        onFinish={onFinish}
        // 初始化数据啥都不填，为空
        initialValues={{  }}
  >
  <Form.Item name="rate" label="Rate">
      <TextArea />
  </Form.Item>
  
  <Form.Item
    name="select"
    label="Select"
    hasFeedback
    rules={[{ required: true, message: 'Please select your country!' }]}
    >
    <Select placeholder="Please select a country">
      <Option value="china">China</Option>
      <Option value="usa">U.S.A</Option>
    </Select>
  </Form.Item>
  
  <Form.Item
      name="upload"
      label="Upload"
      valuePropName="fileList"
      extra="longgggggggggggggggggggggggggggggggggg"
    >
      <Upload name="logo" action="/upload.do" listType="picture">
        <Button icon={<UploadOutlined />}>Click to upload</Button>
      </Upload>
    </Form.Item>

    
    <Form.Item wrapperCol={{ span: 12, offset: 6 }}>
      <Space>
        <Button type="primary" htmlType="submit">
          Submit
        </Button>
        <Button htmlType="reset">reset</Button>
      </Space>
    </Form.Item>
  </Form>
    </div>
  );
};
export default Login;
```
我的代码
```javascript
import { UploadOutlined } from '@ant-design/icons';
import {
  AlipayCircleOutlined,
  TaobaoCircleOutlined,
  WeiboCircleOutlined,
} from '@ant-design/icons';
import {useModel} from '@umijs/max';
import {
  Alert,
  Button,
  Form,
  Select,
  Space,
  Upload,
} from 'antd';
import TextArea from 'antd/es/input/TextArea';
import { createStyles } from 'antd-style';
import React, {useEffect, useState} from 'react';
import {listChartByPageUsingPost} from "@/services/yubi/chartController";

const { Option } = Select;

const normFile = (e: any) => {
  console.log('Upload event:', e);
  if (Array.isArray(e)) {
    return e;
  }
  return e?.fileList;
};


const useStyles = createStyles(({ token }) => {
  return {
    action: {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    },
    lang: {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    },
    container: {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    },
  };
});

const Login: React.FC = () => {
  const [userLoginState, setUserLoginState] = useState<API.LoginResult>({});
  const [type, setType] = useState<string>('account');
  const { initialState, setInitialState } = useModel('@@initialState');
  const { styles } = useStyles();

  useEffect(()=>{
    listChartByPageUsingPost({}).then(res =>{
      console.error('res', res)
    })
  })

  const onFinish = (values: any) => {
    console.log('Received values of form: ', values);
  };

  return (
    // 把页面内容指定一个类名add-chart
    <div className="add-chart">
      <Form
        // 表单名称改为addChart
        name="addChart"
        onFinish={onFinish}
        // 初始化数据啥都不填，为空
        initialValues={{
        }}
        style={{ maxWidth: 600 }}
      >

        <Form.Item name="rate" label="Rate">
          <TextArea />
        </Form.Item>

        <Form.Item
          name="select"
          label="Select"
          hasFeedback
          rules={[{ required: true, message: 'Please select your country!' }]}
        >
          <Select placeholder="Please select a country">
            <Option value="china">China</Option>
            <Option value="usa">U.S.A</Option>
          </Select>
        </Form.Item>



        <Form.Item
          name="upload"
          label="Upload"
          valuePropName="fileList"
          getValueFromEvent={normFile}
          extra="longgggggggggggggggggggggggggggggggggg"
        >
          <Upload name="logo" action="/upload.do" listType="picture">
            <Button icon={<UploadOutlined />}>Click to upload</Button>
          </Upload>
        </Form.Item>


        <Form.Item wrapperCol={{ span: 12, offset: 6 }}>
          <Space>
            <Button type="primary" htmlType="submit">
              Submit
            </Button>
            <Button htmlType="reset">reset</Button>
          </Space>
        </Form.Item>
      </Form>
    </div>
  );
};
export default Login;
```

然后在终端使用yarn run start以模拟数据的方式运行；
输入账号密码，进行登录。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684934713314-bf7e3e2f-a7ea-4492-86ce-8627ce756ff1.png?x-oss-process=image%2Fformat%2Cwebp#averageHue=%23b0ccbc&from=url&id=ZjxRG&originHeight=441&originWidth=788&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

看看页面能不能展示出来。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684834681155-3a1de8d3-eef6-4025-b28c-611128d0d463.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1284%2Climit_0#averageHue=%23f6f6f6&from=url&id=MehIP&originHeight=664&originWidth=1284&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

已经确定了有哪些组件，就开始细化一下。
细化后的index.tsx
```javascript
import { listChartByPageUsingPOST } from '@/services/yubi/chartController';
import { UploadOutlined } from '@ant-design/icons';
import { useModel } from '@umijs/max';
import { Button, Form, Input, Select, Space, Upload } from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React, { useEffect, useState } from 'react';

const Login: React.FC = () => {
  const [type, setType] = useState<string>('account');
  const { setInitialState } = useModel('@@initialState');

  useEffect(() => {
    listChartByPageUsingPOST({}).then((res) => {
      console.error('res', res);
    });
  });

  const onFinish = (values: any) => {
    // 看看能否得到用户的输入
    console.log('表单内容: ', values);
  };

  return (
    // 把页面内容指定一个类名add-chart
    <div className="add-chart">
      <Form
        // 表单名称改为addChart
        name="addChart"
        onFinish={onFinish}
        // 初始化数据啥都不填，为空
        initialValues={{  }}
  >
  {/* 前端表单的name，对应后端接口请求参数里的字段，
  此处name对应后端分析目标goal,label是左侧的提示文本，
  rules=....是必填项提示*/}
  <Form.Item name="goal" label="分析目标" rules={[{ required: true, message: '请输入分析目标!' }]}>
      {/* placeholder文本框内的提示语 */}
      <TextArea placeholder="请输入你的分析需求，比如：分析网站用户的增长情况"/>
  </Form.Item>

  {/* 还要输入图表名称 */}
  <Form.Item name="name" label="图表名称">
      <Input placeholder="请输入图表名称" />
  </Form.Item>

  {/* 图表类型是非必填，所以不做校验 */}
  <Form.Item
    name="selchartTypeect"
    label="图表类型"
    >
    <Select
    options={[
      { value: '折线图', label: '折线图' },
      { value: '柱状图', label: '柱状图' },
      { value: '堆叠图', label: '堆叠图' },
      { value: '饼图', label: '饼图' },
      { value: '雷达图', label: '雷达图' },
    ]}
    />
  </Form.Item>

  {/* 文件上传 */}
  <Form.Item
      name="file"
      label="原始数据"
    >
      {/* action:当你把文件上传之后，他会把文件上传至哪个接口。
      这里肯定是调用自己的后端，先不用这个 */}
      <Upload name="file">
        <Button icon={<UploadOutlined />}>上传 CSV 文件</Button>
      </Upload>
    </Form.Item>
    
    <Form.Item wrapperCol={{ span: 12, offset: 6 }}>
      <Space>
        <Button type="primary" htmlType="submit">
          提交
        </Button>
        <Button htmlType="reset">重置</Button>
      </Space>
    </Form.Item>
  </Form>
    </div>
  );
};
export default Login;
```

当点击提交后，能否得到用户的输入？来测试一下。
鼠标右键选择检查，或按F12键。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684839580254-f61de2de-ffac-43f3-97e4-822bd302f3cc.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_973%2Climit_0#averageHue=%23f6f6f6&from=url&id=p6Cmh&originHeight=763&originWidth=973&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

点击![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684839734917-15738e30-a7d6-4dea-a50d-7141445c3909.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_29%2Climit_0#averageHue=%23e7e8e9&from=url&id=R7475&originHeight=21&originWidth=29&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)清除控制台按钮。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684934885604-12f362c3-5c3d-481b-ac02-26541870b4b0.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1296%2Climit_0#averageHue=%23f7f1f1&from=url&id=GFfVn&originHeight=490&originWidth=1296&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

填写分析目标、图表名称、图表类型、原始数据后，并点击提交；
发现 file 里面还套了一个对象，需要先取出这一层对象，再传给后端处理。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684934924162-a3239036-f832-46ba-98b6-634a524e6316.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1296%2Climit_0#averageHue=%23f8f5f5&from=url&id=bXHwZ&originHeight=401&originWidth=1296&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

现在只需要把表单内容提交给后端，与后端对接即可。
#### ![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1718108837221-297dadce-8277-45ac-b6f5-bb5c1562a422.png#averageHue=%23f2f4f4&clientId=u3d84c29e-7367-4&from=paste&height=408&id=u9e4f6154&originHeight=980&originWidth=1911&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=345948&status=done&style=none&taskId=u900a86cc-ebb7-45c7-b33e-334272d51d4&title=&width=796.249968359869)
对接后端(时间点 01:38:55-01:47:20)

怎么去调用后端接口？前端怎么去写这个请求，把文件上传至后端。

之前提到过，可以利用 openapi 自动生成前端调用代码；
启动package.json中的openapi命令，生成完毕后，看一下生成的代码。
![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684935147490-cc013b18-be64-42ca-9c54-0f3e5fbb86c6.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_945%2Climit_0#averageHue=%23eeeceb&from=url&id=l72nx&originHeight=811&originWidth=945&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

现在可以把src→services目录下的ant-design-pro目录删掉，还有引用到的地方也删掉。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684841327760-af610103-8323-4a18-aa06-a19d39727aba.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_242%2Climit_0#averageHue=%23f0edea&from=url&id=BsLCQ&originHeight=333&originWidth=242&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684841255299-b32f77dc-1d89-4bf3-9cc2-e501114b9ce1.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_961%2Climit_0#averageHue=%23ebe7e5&from=url&id=lun1J&originHeight=440&originWidth=961&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684841402982-375dc27e-9329-4531-820e-8a058a698161.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_676%2Climit_0#averageHue=%23faf8f6&from=url&id=LYODH&originHeight=602&originWidth=676&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684841490311-d40b5cc8-eb23-40a1-bf56-28abd59c1611.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_253%2Climit_0#averageHue=%23f2eceb&from=url&id=i6eLw&originHeight=147&originWidth=253&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684841949229-dd59eed1-9431-4710-89ab-71c030559de0.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_861%2Climit_0#averageHue=%23f8f4f2&from=url&id=hlIiN&originHeight=381&originWidth=861&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

开始编写调用后端接口的代码。
index.tsx
```javascript
import { genChartByAiUsingPOST } from '@/services/yubi/chartController';
import { UploadOutlined } from '@ant-design/icons';
import { Button, Form, Input, message, Select, Space, Upload } from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React from 'react';

/**
 * 添加图表页面
 * @constructor
 */
// 把多余的状态删掉，页面名称改为AddChart
const AddChart: React.FC = () => {
  /**
   * 提交
   * @param values
   */
    const onFinish = async (values: any) => {
      // 对接后端，上传数据
      const params = {
        ...values,
        file: undefined,
      };
      try {
        // 需要取到上传的原始数据file→file→originFileObj(原始数据)
        const res = await genChartByAiUsingPOST(params, {}, values.file.file.originFileObj);
        // 正常情况下，如果没有返回值就分析失败，有，就分析成功
        if (!res?.data) {
          message.error('分析失败');
        } else {
          message.success('分析成功');  
        }  
      // 异常情况下，提示分析失败+具体失败原因
      } catch (e: any) {
        message.error('分析失败,' + e.message);
      }
    };  
  
.....此处省略，代码不变.....

export default AddChart;
```

测试一下，在终端使用yarn run dev启动前端项目(注意：后端项目也是启动状态)；
访问[http://localhost:8000/](http://localhost:8000/)，登录一下；

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684846025679-e27e142b-b3b3-47d1-b99a-ba314ded086f.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1346%2Climit_0#averageHue=%23dfc093&from=url&id=ce6KR&originHeight=341&originWidth=1346&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

调用成功，后端debug状态下也拿到了数据。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684935312076-8352c703-5673-4bed-8370-3eaf7fb772d5.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1329%2Climit_0#averageHue=%23f7f6f5&from=url&id=h4HYs&originHeight=603&originWidth=1329&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

在后端按F9 ，来到前端页面。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684846333142-26c8fbf7-4b86-4386-8b32-df1024f071e3.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1142%2Climit_0#averageHue=%23f7f7f7&from=url&id=vtwSH&originHeight=342&originWidth=1142&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
```typescript
import {genChartByAiUsingPost, listChartByPageUsingPost} from "@/services/yubi/chartController";
import { UploadOutlined } from '@ant-design/icons';
import { useModel } from '@umijs/max';
import {Button, Form, Input, message, Select, Space, Upload} from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React, { useEffect, useState } from 'react';

const AddChart: React.FC = () => {
    const [type, setType] = useState<string>('account');
    const { setInitialState } = useModel('@@initialState');

    useEffect(() => {
        listChartByPageUsingPost({}).then((res) => {
            console.error('res', res);
        });
    });

    /**
     * 提交
     * @param values
     */
    const onFinish = async (values: any) => {
        // 对接后端，上传数据
        const params = {
            ...values,
            file: undefined,
        };
        try {
            // 需要取到上传的原始数据file→file→originFileObj(原始数据)
            const res = await genChartByAiUsingPost(params, {}, values.file.file.originFileObj);
            // 正常情况下，如果没有返回值就分析失败，有，就分析成功
            if (!res?.data) {
                message.error('分析失败');
            } else {
                message.success('分析成功');
            }
            // 异常情况下，提示分析失败+具体失败原因
        } catch (e: any) {
            message.error('分析失败,' + e.message);
        }
    };

    return (
        // 把页面内容指定一个类名add-chart
        <div className="add-chart">
            <Form
                // 表单名称改为addChart
                name="addChart"
                onFinish={onFinish}
                // 初始化数据啥都不填，为空
                initialValues={{  }}
            >
                {/* 前端表单的name，对应后端接口请求参数里的字段，
  此处name对应后端分析目标goal,label是左侧的提示文本，
  rules=....是必填项提示*/}
                <Form.Item name="goal" label="分析目标" rules={[{ required: true, message: '请输入分析目标!' }]}>
                    {/* placeholder文本框内的提示语 */}
                    <TextArea placeholder="请输入你的分析需求，比如：分析网站用户的增长情况"/>
                </Form.Item>

                {/* 还要输入图表名称 */}
                <Form.Item name="name" label="图表名称">
                    <Input placeholder="请输入图表名称" />
                </Form.Item>

                {/* 图表类型是非必填，所以不做校验 */}
                <Form.Item
                    name="selchartTypeect"
                    label="图表类型"
                >
                    <Select
                        options={[
                            { value: '折线图', label: '折线图' },
                            { value: '柱状图', label: '柱状图' },
                            { value: '堆叠图', label: '堆叠图' },
                            { value: '饼图', label: '饼图' },
                            { value: '雷达图', label: '雷达图' },
                        ]}
                    />
                </Form.Item>

                {/* 文件上传 */}
                <Form.Item
                    name="file"
                    label="原始数据"
                >
                    {/* action:当你把文件上传之后，他会把文件上传至哪个接口。
      这里肯定是调用自己的后端，先不用这个 */}
                    <Upload name="file">
                        <Button icon={<UploadOutlined />}>上传 CSV 文件</Button>
                    </Upload>
                </Form.Item>

                <Form.Item wrapperCol={{ span: 12, offset: 6 }}>
                    <Space>
                        <Button type="primary" htmlType="submit">
                            提交
                        </Button>
                        <Button htmlType="reset">重置</Button>
                    </Space>
                </Form.Item>
            </Form>
        </div>
    );
};
export default AddChart;
```

看下返回值，分析成功。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684846410727-b821964e-941e-423e-af95-0445d0789782.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1012%2Climit_0#averageHue=%23f4d090&from=url&id=MFIcm&originHeight=532&originWidth=1012&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
#### 
生成图表(时间点 01:47:20-02:21:36)
使用库 —— [ECharts](https://git.hust.cc/echarts-for-react/)
ps.如果大家用的是 reast 项目，推荐使用这个库，它对 ECharts 进行了封装，可以让你在 reast 中使用更方便。
根据文档提示，使用命令进行安装。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684848248554-da858b8f-992d-4416-9685-07c877a125e8.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1396%2Climit_0#averageHue=%23dff5e4&from=url&id=SrBMn&originHeight=675&originWidth=1396&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

可以看看下面的使用例子。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684848509163-e39a4f93-7d95-469a-9f7a-d5d9328c54be.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1046%2Climit_0#averageHue=%23b1ced5&from=url&id=IVuJX&originHeight=952&originWidth=1046&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

在终端执行这条命令。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1718179795416-d3835823-630c-4ea9-a669-86a1ea7f34e3.png#averageHue=%23222428&clientId=ua88b7536-e72c-4&from=paste&height=347&id=uff662d6a&originHeight=417&originWidth=1066&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=58712&status=done&style=none&taskId=uca2bba71-d441-44b3-841a-55028f7df68&title=&width=888.3332980341396)
```javascript
npm install echarts-for-react
```


![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684848406826-6ba1d586-9e92-44fe-90ac-d8cd35fff2a7.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_917%2Climit_0#averageHue=%23f0efee&from=url&id=dgo1I&originHeight=306&originWidth=917&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

先根据官方文档示例，写一些示例代码，看看能不能接入。
```javascript
import { genChartByAiUsingPOST } from '@/services/yubi/chartController';
import { UploadOutlined } from '@ant-design/icons';
import { Button, Form, Input, message, Select, Space, Upload } from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React, { useState } from 'react';
import ReactECharts from 'echarts-for-react';

/**
 * 添加图表页面
 * @constructor
 */
// 把多余的状态删掉，页面名称改为AddChart
const AddChart: React.FC = () => {
  const options = {
    grid: { top: 8, right: 8, bottom: 24, left: 36 },
    xAxis: {
      type: 'category',
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    },
    yAxis: {
      type: 'value',
    },
    series: [
      {
        data: [820, 932, 901, 934, 1290, 1330, 1320],
        type: 'line',
        smooth: true,
      },
    ],
    tooltip: {
      trigger: 'axis',
    },
  };
  // 定义状态，用来接收后端的返回值，让它实时展示在页面上
  const [chart, setChart] = useState<API.BiResponse>();

  /**
   * 提交
   * @param values
   */
  const onFinish = async (values: any) => {

    .....此处省略，代码不变.....
    
     <div>
     分析结论：{chart?.genResult}
     </div>
     <div>
     生成图表：
     <ReactECharts option={options} />
     </div>
   </div>
   );
   };
   export default AddChart;
```
```javascript
import {genChartByAiUsingPost, listChartByPageUsingPost} from "@/services/yubi/chartController";
import { UploadOutlined } from '@ant-design/icons';
import {useModel} from '@umijs/max';
import {
  Input,
  Button,
  Form,
  Select,
  Space,
  Upload, message,
} from 'antd';
import TextArea from 'antd/es/input/TextArea';
import { createStyles } from 'antd-style';
import React, {useEffect, useState} from 'react';
const { Option } = Select;
import ReactECharts from 'echarts-for-react';
const normFile = (e: any) => {
  console.log('Upload event:', e);
  if (Array.isArray(e)) {
    return e;
  }
  return e?.fileList;
};


const useStyles = createStyles(({ token }) => {
  return {
    action: {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    },
    lang: {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    },
    container: {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    },
  };
});

/**
 * 添加图表页面
 * @constructor
 */
// 把多余的状态删掉，页面名称改为AddChart
const AddChart: React.FC = () => {
  const options = {
    grid: { top: 8, right: 8, bottom: 24, left: 36 },
    xAxis: {
      type: 'category',
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    },
    yAxis: {
      type: 'value',
    },
    series: [
      {
        data: [820, 932, 901, 934, 1290, 1330, 1320],
        type: 'line',
        smooth: true,
      },
    ],
    tooltip: {
      trigger: 'axis',
    },
  };
  // 定义状态，用来接收后端的返回值，让它实时展示在页面上
  const [chart, setChart] = useState<API.BiResponse>();

  const onFinish = async (values: any) => {
    // 对接后端，上传数据
    const params = {
      ...values,
      file: undefined,
    };
    try {
      // 需要取到上传的原始数据file→file→originFileObj(原始数据)
      const res = await genChartByAiUsingPost(params, {}, values.file.file.originFileObj);
      // 正常情况下，如果没有返回值就分析失败，有，就分析成功
      if (!res?.data) {
        message.error('分析失败');
      } else {
        message.success('分析成功');
      }
      // 异常情况下，提示分析失败+具体失败原因
    } catch (e: any) {
      message.error('分析失败,' + e.message);
    }
  };

  return (
    // 把页面内容指定一个类名add-chart
    <div className="add-chart">
      <Form
        // 表单名称改为addChart
        name="addChart"
        onFinish={onFinish}
        // 初始化数据啥都不填，为空
        initialValues={{
        }}
        style={{ maxWidth: 600 }}
      >

        {/* 前端表单的name，对应后端接口请求参数里的字段，此处name对应后端分析目标goal,label是左侧的提示文本，rules=....是必填项提示*/}
        <Form.Item name="goal" label="分析目标" rules={[{ required: true, message: '请输入分析目标!' }]}>
          {/* placeholder文本框内的提示语 */}
          <TextArea placeholder="请输入你的分析需求，比如：分析网站用户的增长情况"/>
        </Form.Item>

        {/* 还要输入图表名称 */}
        <Form.Item name="name" label="图表名称">
          <Input placeholder="请输入图表名称" />
        </Form.Item>

        {/* 图表类型是非必填，所以不做校验 */}
        <Form.Item
          name="selchartTypeect"
          label="图表类型"
        >
          <Select
            options={[
              { value: '折线图', label: '折线图' },
              { value: '柱状图', label: '柱状图' },
              { value: '堆叠图', label: '堆叠图' },
              { value: '饼图', label: '饼图' },
              { value: '雷达图', label: '雷达图' },
            ]}
          />
        </Form.Item>

        {/* 文件上传 */}
        <Form.Item
          name="file"
          label="原始数据"
        >
          <Upload name="file" action="/upload.do" listType="picture">
            <Button icon={<UploadOutlined />}>上传 CSV 文件</Button>
          </Upload>
        </Form.Item>


        <Form.Item wrapperCol={{ span: 12, offset: 6 }}>
          <Space>
            <Button type="primary" htmlType="submit">
              提交
            </Button>
            <Button htmlType="reset">重置</Button>
          </Space>
        </Form.Item>
      </Form>

      <div>
        分析结论：{chart?.genResult}
      </div>
      <div>
        生成图表：
        <ReactECharts option={options} />
      </div>

    </div>
  );
};
export default AddChart;
```

重新以dev方式启动前端项目，访问 [http://localhost:8000/](http://localhost:8000/)，登录一下。
图表显示出来了，接入成功。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1718179957382-7fd46df7-b021-4549-825c-2ecb16f3fc10.png#averageHue=%23e3ddd5&clientId=ua88b7536-e72c-4&from=paste&height=1336&id=u86fa992a&originHeight=1603&originWidth=2876&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=672858&status=done&style=none&taskId=u178d39f9-ceb3-41ef-9331-7ba1630ba66&title=&width=2396.6665714316937)

继续完善代码，看看用自己的能不能展示出图表。
```javascript
import { genChartByAiUsingPOST } from '@/services/yubi/chartController';
import { UploadOutlined } from '@ant-design/icons';
import { Button, Form, Input, message, Select, Space, Upload } from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React, { useState } from 'react';
import ReactECharts from 'echarts-for-react';

/**
 * 添加图表页面
 * @constructor
 */
// 把多余的状态删掉，页面名称改为AddChart
const AddChart: React.FC = () => {
  // 定义状态，用来接收后端的返回值，让它实时展示在页面上
  const [chart, setChart] = useState<API.BiResponse>();
  const [option, setOption] = useState<any>();
  // 提交中的状态，默认未提交
  const [submitting, setSubmitting] = useState<boolean>(false);

  /**
   * 提交
   * @param values
   */
    const onFinish = async (values: any) => {
      // 如果已经是提交中的状态(还在加载)，直接返回，避免重复提交
    if (submitting) {
      return;
    }
    // 当开始提交，把submitting设置为true
    setSubmitting(true);

      // 对接后端，上传数据
      const params = {
        ...values,
        file: undefined,
      };
      try {
        // 需要取到上传的原始数据file→file→originFileObj(原始数据)
        const res = await genChartByAiUsingPOST(params, {}, values.file.file.originFileObj);
        // 正常情况下，如果没有返回值就分析失败，有，就分析成功
        if (!res?.data) {
          message.error('分析失败');
        } else {
          message.success('分析成功');  
          // 解析成对象，为空则设为空字符串
          const chartOption = JSON.parse(res.data.genChart ?? '');
          // 如果为空，则抛出异常，并提示'图表代码解析错误'
          if (!chartOption) {
            throw new Error('图表代码解析错误')
          // 如果成功
          } else {
            // 从后端得到响应结果之后，把响应结果设置到图表状态里
            setChart(res.data);
            setOption(chartOption);
          }
        }  
      // 异常情况下，提示分析失败+具体失败原因
      } catch (e: any) {
        message.error('分析失败,' + e.message);
      }
      // 当结束提交，把submitting设置为false
      setSubmitting(false);
    };  

.....此处省略，代码不变.....
  <div>
    分析结论：{chart?.genResult}
  </div>
  <div>
    生成图表：
    {/* 如果它存在，才渲染这个组件 */}
    {
      // 后端返回的代码是字符串，不是对象，用JSON.parse解析成对象
      option && <ReactECharts option={option} />
    }
  </div>
    </div>
  );
};
export default AddChart;
```
```javascript
import {genChartByAiUsingPost, listChartByPageUsingPost} from "@/services/yubi/chartController";
import { UploadOutlined } from '@ant-design/icons';
import {useModel} from '@umijs/max';
import {
  Input,
  Button,
  Form,
  Select,
  Space,
  Upload, message,
} from 'antd';
import TextArea from 'antd/es/input/TextArea';
import { createStyles } from 'antd-style';
import React, {useEffect, useState} from 'react';
const { Option } = Select;
import ReactECharts from 'echarts-for-react';
const normFile = (e: any) => {
  console.log('Upload event:', e);
  if (Array.isArray(e)) {
    return e;
  }
  return e?.fileList;
};


const useStyles = createStyles(({ token }) => {
  return {
    action: {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    },
    lang: {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    },
    container: {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    },
  };
});

/**
 * 添加图表页面
 * @constructor
 */
// 把多余的状态删掉，页面名称改为AddChart
const AddChart: React.FC = () => {
  // 定义状态，用来接收后端的返回值，让它实时展示在页面上
  const [chart, setChart] = useState<API.BiResponse>();
  const [option, setOption] = useState<any>();
  // 提交中的状态，默认未提交
  const [submitting, setSubmitting] = useState<boolean>(false);

  /**
   * 提交
   * @param values
   */
  const onFinish = async (values: any) => {
    // 如果已经是提交中的状态(还在加载)，直接返回，避免重复提交
    if (submitting) {
      return;
    }
    // 当开始提交，把submitting设置为true
    setSubmitting(true);

    // 对接后端，上传数据
    const params = {
      ...values,
      file: undefined,
    };
    try {
      // 需要取到上传的原始数据file→file→originFileObj(原始数据)
      const res = genChartByAiUsingPost(params, {}, values.file.file.originFileObj);
      // 正常情况下，如果没有返回值就分析失败，有，就分析成功
      if (!res?.data) {
        message.error('分析失败');
      } else {
        message.success('分析成功');
        // 解析成对象，为空则设为空字符串
        const chartOption = JSON.parse(res.data.genChart ?? '');
        // 如果为空，则抛出异常，并提示'图表代码解析错误'
        if (!chartOption) {
          throw new Error('图表代码解析错误')
          // 如果成功
        } else {
          // 从后端得到响应结果之后，把响应结果设置到图表状态里
          setChart(res.data);
          setOption(chartOption);
        }
      }
      // 异常情况下，提示分析失败+具体失败原因
    } catch (e: any) {
      message.error('分析失败,' + e.message);
    }
    // 当结束提交，把submitting设置为false
    setSubmitting(false);
  };

  return (
    // 把页面内容指定一个类名add-chart
    <div className="add-chart">
      <Form
        // 表单名称改为addChart
        name="addChart"
        onFinish={onFinish}
        // 初始化数据啥都不填，为空
        initialValues={{
        }}
        style={{ maxWidth: 600 }}
      >

        {/* 前端表单的name，对应后端接口请求参数里的字段，此处name对应后端分析目标goal,label是左侧的提示文本，rules=....是必填项提示*/}
        <Form.Item name="goal" label="分析目标" rules={[{ required: true, message: '请输入分析目标!' }]}>
          {/* placeholder文本框内的提示语 */}
          <TextArea placeholder="请输入你的分析需求，比如：分析网站用户的增长情况"/>
        </Form.Item>

        {/* 还要输入图表名称 */}
        <Form.Item name="name" label="图表名称">
          <Input placeholder="请输入图表名称" />
        </Form.Item>

        {/* 图表类型是非必填，所以不做校验 */}
        <Form.Item
          name="selchartTypeect"
          label="图表类型"
        >
          <Select
            options={[
              { value: '折线图', label: '折线图' },
              { value: '柱状图', label: '柱状图' },
              { value: '堆叠图', label: '堆叠图' },
              { value: '饼图', label: '饼图' },
              { value: '雷达图', label: '雷达图' },
            ]}
          />
        </Form.Item>

        {/* 文件上传 */}
        <Form.Item
          name="file"
          label="原始数据"
        >
          <Upload name="file" action="/upload.do" listType="picture">
            <Button icon={<UploadOutlined />}>上传 CSV 文件</Button>
          </Upload>
        </Form.Item>


        <Form.Item wrapperCol={{ span: 12, offset: 6 }}>
          <Space>
            <Button type="primary" htmlType="submit">
              提交
            </Button>
            <Button htmlType="reset">重置</Button>
          </Space>
        </Form.Item>
      </Form>

      <div>
        分析结论：{chart?.genResult}
      </div>
      <div>
        生成图表：
        {/* 如果它存在，才渲染这个组件 */}
        {
          // 后端返回的代码是字符串，不是对象，用JSON.parse解析成对象
          option && <ReactECharts option={option} />
        }
      </div>

    </div>
  );
};
export default AddChart;
```

来到前端页面，输入完信息，点击提交，图表展示出来了。
ps.没展示出来，再提交几次，可能 AI 返回的结果在代码前多了几句中文。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684893640993-bcec197b-6887-4656-9c76-74f62261870a.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1077%2Climit_0#averageHue=%23f4f4f4&from=url&id=CkvCq&originHeight=788&originWidth=1077&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

现在可以优化一下界面的样式了(可去[ant.design 组件库](https://ant.design/index-cn)参考)：
**比如：**

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684912361994-0a9ba2a4-d0e3-4134-9e5a-0746cf411ab3.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1351%2Climit_0#averageHue=%23debf91&from=url&id=vNiDN&originHeight=756&originWidth=1351&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)
```javascript
import {genChartByAiUsingPost, listChartByPageUsingPost} from "@/services/yubi/chartController";
import { UploadOutlined } from '@ant-design/icons';
import {useModel} from '@umijs/max';
import {
  Divider,
  Spin,
  Card,
  Col,
  Row,
  Input,
  Button,
  Form,
  Select,
  Space,
  Upload, message,
} from 'antd';
import TextArea from 'antd/es/input/TextArea';
import { createStyles } from 'antd-style';
import React, {useEffect, useState} from 'react';
const { Option } = Select;
import ReactECharts from 'echarts-for-react';
const normFile = (e: any) => {
  console.log('Upload event:', e);
  if (Array.isArray(e)) {
    return e;
  }
  return e?.fileList;
};


const useStyles = createStyles(({ token }) => {
  return {
    action: {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    },
    lang: {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    },
    container: {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    },
  };
});

/**
 * 添加图表页面
 * @constructor
 */
// 把多余的状态删掉，页面名称改为AddChart
const AddChart: React.FC = () => {
  // 定义状态，用来接收后端的返回值，让它实时展示在页面上
  const [chart, setChart] = useState<API.BiResponse>();
  const [option, setOption] = useState<any>();
  // 提交中的状态，默认未提交
  const [submitting, setSubmitting] = useState<boolean>(false);

  /**
   * 提交
   * @param values
   */
  const onFinish = async (values: any) => {
    // 如果已经是提交中的状态(还在加载)，直接返回，避免重复提交
    if (submitting) {
      return;
    }
    // 如果提交了，把图表数据和图表代码清空掉，防止和之前提交的图标堆叠在一起
    // 如果option清空了，组件就会触发重新渲染，就不会保留之前的历史记录
    setChart(undefined);
    setOption(undefined);

    // 对接后端，上传数据
    const params = {
      ...values,
      file: undefined,
    };
    try {
      // 需要取到上传的原始数据file→file→originFileObj(原始数据)
      const res = genChartByAiUsingPost(params, {}, values.file.file.originFileObj);
      // 正常情况下，如果没有返回值就分析失败，有，就分析成功
      if (!res?.data) {
        message.error('分析失败');
      } else {
        message.success('分析成功');
        // 解析成对象，为空则设为空字符串
        const chartOption = JSON.parse(res.data.genChart ?? '');
        // 如果为空，则抛出异常，并提示'图表代码解析错误'
        if (!chartOption) {
          throw new Error('图表代码解析错误')
          // 如果成功
        } else {
          // 从后端得到响应结果之后，把响应结果设置到图表状态里
          setChart(res.data);
          setOption(chartOption);
        }
      }
      // 异常情况下，提示分析失败+具体失败原因
    } catch (e: any) {
      message.error('分析失败,' + e.message);
    }
    // 当结束提交，把submitting设置为false
    setSubmitting(false);
  };

  return (
    // 把页面内容指定一个类名add-chart
    <div className="add-chart">
      {/* 变成两列 gutter列与列之间的间隔*/}
      <Row gutter={24}>
        {/* 表单放在第一列,卡片组件里 */}
        <Col span={12}>
          <Card title="智能分析">
            <Form
                // 表单名称改为addChart
                name="addChart"
                // label标签的文本对齐方式
                labelAlign="left"
                // label标签布局，同<Col>组件，设置 span offset 值，如 {span: 3, offset: 12}
                labelCol={{ span: 4 }}
                // 设置控件布局样式
                wrapperCol={{ span: 16 }}
                onFinish={onFinish}
                // 初始化数据啥都不填，为空
                initialValues={{  }}
            >

              {/* 前端表单的name，对应后端接口请求参数里的字段，此处name对应后端分析目标goal,label是左侧的提示文本，rules=....是必填项提示*/}
              <Form.Item name="goal" label="分析目标" rules={[{ required: true, message: '请输入分析目标!' }]}>
                {/* placeholder文本框内的提示语 */}
                <TextArea placeholder="请输入你的分析需求，比如：分析网站用户的增长情况"/>
              </Form.Item>

              {/* 还要输入图表名称 */}
              <Form.Item name="name" label="图表名称">
                <Input placeholder="请输入图表名称" />
              </Form.Item>

              {/* 图表类型是非必填，所以不做校验 */}
              <Form.Item
                name="chartType"
                label="图表类型"
              >
                <Select
                  options={[
                    { value: '折线图', label: '折线图' },
                    { value: '柱状图', label: '柱状图' },
                    { value: '堆叠图', label: '堆叠图' },
                    { value: '饼图', label: '饼图' },
                    { value: '雷达图', label: '雷达图' },
                  ]}
                />
              </Form.Item>

              {/* 文件上传 */}
              <Form.Item
                name="file"
                label="原始数据"
              >
                {/* action:当你把文件上传之后，它会把文件上传至哪个接口。这里肯定是调用自己的后端，先不用这个;maxCount={1} 限制文件上传数量为1 */}
                <Upload name="file" maxCount={1}>
                  <Button icon={<UploadOutlined />}>上传 CSV 文件</Button>
                </Upload>
              </Form.Item>

              {/* offset设置和label标签一样的宽度，这样就能保持对齐；
                        其他占用的列设置成16 */}
              <Form.Item wrapperCol={{ span: 16, offset: 4 }}>
                <Space>
                  {/* 加个loading：就是把submitting的状态加入进来，
                        加个disabled：如果正在提交，就让这个按钮禁用，不允许重复点击*/}
                  <Button type="primary" htmlType="submit" loading={submitting} disabled={submitting}>
                    提交
                  </Button>
                  <Button htmlType="reset">重置</Button>
                </Space>
              </Form.Item>
            </Form>
          </Card>
        </Col>

        {/* 分析结论和图表放在第二列 */}
        <Col span={12}>
          <Card title="分析结论">
            {/* 如果分析结论存在，就展示分析结论；
                不存在则显示'请先在左侧进行提交' */}
            {chart?.genResult ?? <div>请先在左侧进行提交</div>}
            {/* 提交中，还未返回结果，分析结论就显示加载中的组件 */}
            <Spin spinning={submitting}/>
          </Card>
          {/* 加一个间距 */}
          <Divider />
          <Card title="可视化图表">
            {/* 如果它存在，才渲染这个组件 */}
            {
              // 后端返回的代码是字符串，不是对象，用JSON.parse解析成对象
              option ? <ReactECharts option={option} /> : <div>请先在左侧进行提交</div>
            }
            {/* 提交中，还未返回结果，图表就显示加载中的组件 */}
            <Spin spinning={submitting}/>
          </Card>
        </Col>
      </Row>
    </div>
  );
};
export default AddChart;
```

修改页面的名字name和图标icon。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684914109437-aebbb201-0ac7-425a-a875-5766de9aae50.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_835%2Climit_0#averageHue=%23f7f3f1&from=url&id=TnlML&originHeight=247&originWidth=835&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684914342950-6f3a39fa-7af4-4e18-9fe6-282d38553953.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1281%2Climit_0#averageHue=%23f7f7f7&from=url&id=tETK3&originHeight=660&originWidth=1281&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

ps.图标可以去组件库内挑选，点击图标就可复制。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684913845394-62092da4-6763-4811-a03f-5cc095a25427.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1274%2Climit_0#averageHue=%23fcfbfb&from=url&id=BfI9i&originHeight=673&originWidth=1274&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

进入网站数据.xlsx。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684914681878-03ee142a-b13f-4fa7-94cd-1a00718d824b.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_886%2Climit_0#averageHue=%23f4f2ef&from=url&id=jovlf&originHeight=404&originWidth=886&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

在网站数据.xlsx中多添加几条数据，然后保存。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684914832389-eeff193d-7901-4591-b6bc-c8f2e92ddedb.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_516%2Climit_0#averageHue=%23d9ad6d&from=url&id=Ow0lr&originHeight=605&originWidth=516&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

测试一下。
ps.不成功，可以试着刷新页面，重新提交。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684915929934-07067427-5bc9-45f5-803d-9cc61f4ae71c.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1394%2Climit_0#averageHue=%23fafafa&from=url&id=lH6iG&originHeight=448&originWidth=1394&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

图表展示出来了。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684916113463-fe361072-630f-4849-802a-02e3553fa0a9.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1372%2Climit_0#averageHue=%23f8f8f8&from=url&id=MyTqp&originHeight=715&originWidth=1372&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

然后换成雷达图，看看会不会堆叠图表，没有堆叠。

![](https://cdn.nlark.com/yuque/0/2023/png/25430380/1684916268582-fce3b5e4-593b-4577-aa09-261bfee1b431.png?x-oss-process=image%2Fformat%2Cwebp%2Fresize%2Cw_1377%2Climit_0#averageHue=%23f8f8f8&from=url&id=jvESA&originHeight=625&originWidth=1377&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&status=done&style=none&title=)

结束咯~休息休息，走动走动(o≖◡≖) 
#### 总结
#### 生成的图表
[https://git.hust.cc/echarts-for-react/](https://git.hust.cc/echarts-for-react/)
使用下面这个命令安装：
```bash
npm install echarts-for-react
&& 
npm install echarts-for-react --save
```
但是出现以下的报错：（可能是电脑环境的问题）
停止项目运行，安装echarts：
```
npm install echarts --save
```
再次运行程序成功启动
修改AI助手设定：
```
你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：
分析需求：
{数据分析的需求或者目标}
原始数据：
{csv格式的原始数据，用,作为分隔符}
请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）
【【【【【
{前端 Echarts V5 的 option 配置对象的json格式代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释}
【【【【【
{明确的数据分析结论、越详细越好，不要生成多余的注释}
```
#### 生成图表结论
完整界面代码：
```typescript
import { genChartByAiUsingPOST } from '@/services/ShierBI/chartController';
import { Button, Card, Col, Divider, Form, message, Row, Select, Space, Spin, Upload } from 'antd';
import React, { useState } from 'react';

import { UploadOutlined } from '@ant-design/icons';
import TextArea from 'antd/es/input/TextArea';
import ReactECharts from 'echarts-for-react';

const AddChart: React.FC = () => {
  const [chart, setChart] = useState<API.BiResponse>();
  const [option, setOption] = useState<any>();
  const [submitting, setSubmitting] = useState<boolean>(false);

  /**
   * 提交表单
   * @param values
   */
  const onFinish = async (values: any) => {
    // 避免重复提交
    if (submitting) {
      return;
    }
    // 开始提交
    setSubmitting(true);
    setChart(undefined);
    setOption(undefined);
    const param = {
      ...values,
      file: undefined,
    };
    try {
      const res = await genChartByAiUsingPOST(param, {}, values.file.file.originFileObj);
      if (!res?.data) {
        message.error('分析失败');
      } else {
        message.success('分析成功');
        const chartOption = JSON.parse(res.data.genChart ?? '');
        if (!chartOption) {
          throw new Error('图表代码解析错误');
        } else {
          setChart(res.data);
          setOption(chartOption);
        }
      }
    } catch (e: any) {
      message.error('分析失败,' + e.message);
    }
    // 提交完成
    setSubmitting(false);
  };

  return (
    <div className="add-chart">
      <Row gutter={24}>
        <Col span={12}>
          <Card>
            <Divider style={{ fontWeight: 'bold', color: 'blue' }}>智能分析</Divider>
            <Form
              name="addChart"
              labelAlign="left"
              labelCol={{ span: 4 }}
              wrapperCol={{ span: 16 }}
              onFinish={onFinish}
              initialValues={{}}
            >
              <Form.Item
                name="goal"
                label="分析目标"
                rules={[{ required: true, message: '请输入分析目标' }]}
              >
                <TextArea placeholder="请输入你的分析需求，比如分析网站用户的增长情况" />
              </Form.Item>

              <Form.Item
                name="chartName"
                label="图表名称"
                rules={[{ required: true, message: '请输入图表名称！' }]}
              >
                <TextArea placeholder="请输入图表名称" />
              </Form.Item>

              <Form.Item
                name="chartType"
                label="图表类型"
                rules={[{ required: true, message: '请选择图表类型！' }]}
              >
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
                    { value: '漏斗图', label: '漏斗图' },
                    { value: '散点图', label: '散点图' },
                    { value: '仪表盘', label: '仪表盘' },
                    { value: 'K线图', label: 'K线图' },
                    { value: '长图表', label: '长图表' },
                    { value: '区域图', label: '区域图' },
                    { value: '面积热力图', label: '面积热力图' },
                    { value: '三维散点图', label: '三维散点图' },
                  ]}
                ></Select>
              </Form.Item>

              <Form.Item name="file" label="原始数据">
                <Upload name="file" maxCount={1} accept=".csv,.xls,.xlsx,.json,.txt,.xml,.sql">
                  <Button icon={<UploadOutlined />}>上传 CSV 文件(Excel)</Button>
                </Upload>
              </Form.Item>

              <Form.Item wrapperCol={{ span: 16, offset: 4 }}>
                <Space>
                  <Button
                    type="primary"
                    htmlType="submit"
                    loading={submitting}
                    disabled={submitting}
                  >
                    确定上传
                  </Button>
                  <Button htmlType="reset">重置内容</Button>
                </Space>
              </Form.Item>
            </Form>
          </Card>
        </Col>

        <Col span={12}>
          <Card>
            <Divider style={{ fontWeight: 'bold', color: 'blue' }}>可视化图表</Divider>
            <Card>
              {option ? (
                <ReactECharts option={option} />
              ) : (
                <div
                  style={{
                    color: 'red',
                    textAlign: 'center',
                    fontWeight: 'bold',
                    fontSize: '16px',
                  }}
                >
                  请在左侧进行提交分析数据！
                </div>
              )}
              <Spin spinning={submitting} size="large" />
            </Card>

            <Divider style={{ fontWeight: 'bold', color: 'blue' }}>由上图得出结论</Divider>
            <Card>
              {chart?.genResult ?? (
                <div
                  style={{
                    color: 'red',
                    textAlign: 'center',
                    fontWeight: 'bold',
                    fontSize: '16px',
                  }}
                >
                  无可视化图表，无法得出结论！
                </div>
              )}
              <Spin spinning={submitting} size="large" />
            </Card>
          </Card>
        </Col>
      </Row>
    </div>
  );
};
export default AddChart;
```
#### 报错
[JSON.parse: expected property name or '}'](https://stackoverflow.com/questions/8013582/json-parse-expected-property-name-or)
解决办法：{}内不能出现单引号
```typescript
const onFinish = async (values: any) => {
    // 避免重复提交
    if (submitting) {
        return;
    }
    setSubmitting(true);
    setChart(undefined);
    setOption(undefined);
    // 对接后端，上传数据
    const params = {
        ...values,
        file: undefined,
    };
    try {
        const res = await genChartByAiUsingPost(params, {}, values.file.file.originFileObj);
        if (!res?.data) {
            message.error('分析失败');
        } else {
            message.success('分析成功');
            let jsonString = res.data.genChart ?? "";
            // 替换单引号为双引号
            jsonString = jsonString.replace(/'/g, '"');
            // 处理不符合 JSON 规范的内容
            // 比如替换可能的错误格式 (key: value) 为 ("key": "value")
            jsonString = jsonString.replace(/([{,]\s*)(\w+)\s*:/g, '$1"$2":');
            jsonString = jsonString.replace(/: (\w+)/g, ': "$1"');
            const chartOption = JSON.parse(jsonString);
            // const chartOption = JSON.parse(res.data.genChart ?? "");
            if (!chartOption) {
                throw new Error('图表代码解析错误')
            } else {
                setChart(res.data);
                setOption(chartOption);
            }
        }
    } catch (e: any) {
        console.log(e.message)
        message.error('分析失败，' + e.message);
    }
    setSubmitting(false);
};
```
完整代码pages/AddChart/index.tsx：
```typescript
import { UploadOutlined } from '@ant-design/icons';
import {Button, Card, Col, Divider, Form, Input, message, Row, Select, Space, Spin, Upload} from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React, { useState } from 'react';
import ReactECharts from 'echarts-for-react';
import {genChartByAiUsingPost} from "@/services/yubi/chartController";

/**
 * 添加图表页面
 * @constructor
 */
const AddChart: React.FC = () => {
    const [chart, setChart] = useState<API.BiResponse>();
    const [option, setOption] = useState<any>();
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
        setChart(undefined);
        setOption(undefined);
        // 对接后端，上传数据
        const params = {
            ...values,
            file: undefined,
        };
        try {
            const res = await genChartByAiUsingPost(params, {}, values.file.file.originFileObj);
            if (!res?.data) {
                message.error('分析失败');
            } else {
                message.success('分析成功');
                let jsonString = res.data.genChart ?? "";
                // 替换单引号为双引号
                jsonString = jsonString.replace(/'/g, '"');
                // 处理不符合 JSON 规范的内容
                // 比如替换可能的错误格式 (key: value) 为 ("key": "value")
                jsonString = jsonString.replace(/([{,]\s*)(\w+)\s*:/g, '$1"$2":');
                jsonString = jsonString.replace(/: (\w+)/g, ': "$1"');
                const chartOption = JSON.parse(jsonString);
                // const chartOption = JSON.parse(res.data.genChart ?? "");
                if (!chartOption) {
                    throw new Error('图表代码解析错误')
                } else {
                    setChart(res.data);
                    setOption(chartOption);
                }
            }
        } catch (e: any) {
            console.log(e.message)
            message.error('分析失败，' + e.message);
        }
        setSubmitting(false);
    };

    return (
        <div className="add-chart">
            <Row gutter={24}>
                <Col span={12}>
                    <Card title="智能分析">
                        <Form name="addChart" labelAlign="left" labelCol={{ span: 4 }}
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
                </Col>
                <Col span={12}>
                    <Card title="分析结论">
                        {chart?.genResult ?? <div>请先在左侧进行提交</div>}
                        <Spin spinning={submitting}/>
                    </Card>
                    <Divider />
                    <Card title="可视化图表">
                        {
                            option ? <ReactECharts option={option} /> : <div>请先在左侧进行提交</div>
                        }
                        <Spin spinning={submitting}/>
                    </Card>
                </Col>
            </Row>
        </div>
    );
};
export default AddChart;
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723262288199-f884fb8a-c6a5-4f3d-a67d-af9b5e7659ca.png#averageHue=%23f7f7f7&clientId=u0ba0c7ad-c00f-4&from=paste&height=817&id=ucea04b26&originHeight=980&originWidth=1904&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=260426&status=done&style=none&taskId=udaad14a1-b800-4b15-8cca-ad7e82e2b5d&title=&width=1586.6666036182005)
## 第三期 TODO

1.  界面优化  √ 
2.  chartService 处理AI生成业务 √ 
```java

/**
 * @author Shier
 * @description 针对表【chart(图表信息表)】的数据库操作Service实现
 * @createDate 2023-05-14 19:20:33
 */
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart> implements ChartService {

    @Resource
    private UserService userService;

    @Resource
    private AiManager aiManager;

    @Override
    public BiResponse genChartByAi(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String chartName = genChartByAiRequest.getChartName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        User loginUser = userService.getLoginUser(request);
        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "图表分析目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(chartName) && chartName.length() > 200, ErrorCode.PARAMS_ERROR, "图表名称过长");
        ThrowUtils.throwIf(StringUtils.isBlank(chartType), ErrorCode.PARAMS_ERROR, "图表类型为空");
        // 无需Prompt，直接调用现有模型
        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");
        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");

        // 压缩后的数据
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        userInput.append(csvData).append("\n");
        // 调用AI
        String chartResult = aiManager.doChat(userInput.toString());
        // 解析内容
        String[] splits = chartResult.split(GEN_CONTENT_SPLITS);
        if (splits.length < GEN_ITEM_NUM) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI生成错误");
        }
        // 首次生成的内容
        String preGenChart = splits[GEN_CHART_IDX].trim();
        String genResult = splits[GEN_RESULT_IDX].trim();
        String validGenChart = ChartUtils.getValidGenChart(preGenChart);

        // 插入数据到数据库
        Chart chart = new Chart();
        chartName = StringUtils.isBlank(chartName) ? ChartUtils.genDefaultChartName() : chartName;
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartName(chartName);
        chart.setChartType(chartType);
        chart.setGenChart(preGenChart);
        chart.setGenResult(genResult);
        chart.setUserId(loginUser.getId());
        boolean saveResult = this.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");
        // 返回到前端
        BiResponse biResponse = new BiResponse();
        biResponse.setGenChart(preGenChart);
        biResponse.setGenResult(genResult);
        return biResponse;
    }
}
```

```java
/**
 * 图表文件上传
 */
@PostMapping("/gen")
@ApiOperation(value = "图表文件上传")
public BaseResponse<BiResponse> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                             GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
    String chartName = genChartByAiRequest.getChartName();
    String goal = genChartByAiRequest.getGoal();
    String chartType = genChartByAiRequest.getChartType();
    // 校验
    ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "图表分析目标为空");
    ThrowUtils.throwIf(StringUtils.isNotBlank(chartName) && chartName.length() > 200, ErrorCode.PARAMS_ERROR, "图表名称过长");
    ThrowUtils.throwIf(StringUtils.isBlank(chartType), ErrorCode.PARAMS_ERROR, "图表类型为空");
    BiResponse biResponse = chartService.genChartByAi(multipartFile, genChartByAiRequest, request);
    ThrowUtils.throwIf(biResponse == null, ErrorCode.SYSTEM_ERROR, "AI生成错误");
    return ResultUtils.success(biResponse);
}
```


3.  使用正则表达式提取合法的 echarts 配置 json 代码 √ 
4.  用户未取图表名称则给图表生成默认名字 √ 
```java
/**
 * @author Shier
 * CreateTime 2023/5/21 17:41
 * 图表工具类
 */
public class ChartUtils {
    private static final Pattern VALID_GEN_CHART_PATTERN = Pattern.compile(GEN_CHART_REGEX, Pattern.COMMENTS);

    /**
     * 依照正则表达式来匹配合法的图表的echarts配置
     *
     * @param preGenChart 提取前的原数据
     * @return 提取后的json串
     */
    public static String getValidGenChart(String preGenChart) {
        Matcher matcher = VALID_GEN_CHART_PATTERN.matcher(preGenChart);
        ThrowUtils.throwIf(!matcher.find(), ErrorCode.SYSTEM_ERROR, "AI生成图表错误");
        return matcher.group();
    }

    /**
     * 如果用户没有传图表的名称，生成默认的
     *
     * @return 默认的图表名称
     */
    public static String genDefaultChartName() {
        return DEFAULT_CHART_NAME_PREFIX + RandomStringUtils.randomAlphabetic(DEFAULT_CHART_NAME_SUFFIX_LEN);
    }
}
```


5.  常量提取，保证代码可读性  √ 
```java
package com.shier.shierbi.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author Shier
 * CreateTime 2023/5/21 17:38
 */
public interface ChartConstant {
    /**
     * AI生成的内容分隔符
     */
    String GEN_CONTENT_SPLITS = "【【【【【";

    /**
     * AI 生成的内容的元素为3个
     */
    int GEN_ITEM_NUM = 3;

    /**
     * 生成图表的数据下标
     */
    int GEN_CHART_IDX = 1;

    /**
     * 生成图表的分析结果的下标
     */
    int GEN_RESULT_IDX = 2;

    /**
     * 提取生成的图表的Echarts配置的正则
     */
    String GEN_CHART_REGEX = "\\{(?>[^{}]*(?:\\{[^{}]*}[^{}]*)*)}";

    /**
     * 图表默认名称的前缀
     */
    String DEFAULT_CHART_NAME_PREFIX = "分析图表_";

    /**
     * 图表默认名称的后缀长度
     */
    int DEFAULT_CHART_NAME_SUFFIX_LEN = 10;
}
```


6.  新增配置类，在配置文件中传入模型 id  √ 
```java
/**
 * @author Shier
 * CreateTime 2023/5/21 18:27
 */
@Data
@Component
@ConfigurationProperties(prefix = "ai.model")
public class AiModelConfig {
    /**
     * 模型id
     */
    private Long modelId;
}
```

```yaml
# 要使用的模型ID
ai:
  model:
    model-id: 1660100329896673281
```

> 完整代码看GitHub

# 第四期直播 - 图表管理功能、优化系统
图表管理功能很简单，我们之前已经做过伙伴匹配、用户中心和 API 开放平台的管理功能，都是基于增删改查实现的。我们可以用同样的方式来展示图表信息，首先需要做一个列表页。后端已经在星球提供了一个基础的万能项目模板，包含增删改查接口，我们只需要在此基础上进行定制化开发即可。所以本期后端的开发量不多，只需要复用即可，主要是前端。 
## 1、开发图表管理功能 - 增删改查
### 后端
后端：复用 springboot-init 初始化模板的增删改查代码，
核心：获取个人创建的图表列表 listMyChartByPage
### 
前端：

1. 开发一个列表页
2. 支特按照图表名称搜索
> 后端在第一期初始化后端模板时就已经完成了，所以说可以先去开发前端

## 2、前端开发

1. 创建路由和页面，确保页面能访问到
2. 获取需要的数据，定义 state 变量来存储数据，用于给页面展示
3. 先把最简单直观的数据展示给前端，再去调祥式
4. 引入Ant Design的List组件，[https://ant.design/components/list-cn](https://ant.design/components/list-cn)，复制示例代码
5. 调整 List 组件中的内容为自己的（注意，获取用户头像可以从初始化状态中获取）
6. 针对样式，对数据做一些处理，比如统一隐藏图表自身的 title
7. 增加分页
8. 增加搜索框
9. 其他优化：比如 loading 效果
10. 增加删除的功能（自己补充）
###  2.1 创建路由和页面(时间点 15:03-18:32)
第一步：创建路由，进入 routes.ts 。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723263950204-27328aa8-aa7c-49c3-ac1b-7a96052d6f65.png#averageHue=%23f4f3f2&clientId=u0ba0c7ad-c00f-4&from=paste&height=402&id=ub4e38856&originHeight=482&originWidth=694&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=160738&status=done&style=none&taskId=ub1b88b40-00b3-415a-a7a4-df8601708b5&title=&width=578.3333103524324)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723264078852-184579e2-8d69-472a-b94b-f7aa19e99325.png#averageHue=%23232529&clientId=u0ba0c7ad-c00f-4&from=paste&height=969&id=u0234852d&originHeight=1163&originWidth=2540&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=278263&status=done&style=none&taskId=uf055517f-4f4b-4428-b268-aa19f48faee&title=&width=2116.6665825578934)
```typescript
export default [
  { name : '登录', path: '/user', layout: false, routes: [{ path: '/user/login', component: './User/Login' }] },
  // { name: '欢迎页面', path: '/welcome', icon: 'smile', component: './Welcome' },
  {path: '/', redirect: '/add_chart'},
  { name: '智能分析', path: '/add_chart', icon: 'barChart', component: './AddChart' },
  { name: '我的图表', path: '/my_chart', icon: 'pieChart', component: './MyChart' },
  {
    path: '/admin',
    icon: 'crown',
    access: 'canAdmin',
    name : '管理员页面',
    routes: [
      { path: '/admin', redirect: '/admin/sub-page' },
      { path: '/admin/sub-page', component: './Admin' },
    ],
  },
  { path: '/', redirect: '/welcome' },
  { path: '*', layout: false, component: './404' },
];

```
第二步：创建页面，复制 AddChart目录 。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723263973108-09678e83-9b95-4616-b69b-08eaa969ef72.png#averageHue=%23f2f0f0&clientId=u0ba0c7ad-c00f-4&from=paste&height=270&id=u68ccabf5&originHeight=324&originWidth=694&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=58104&status=done&style=none&taskId=ub6b451bd-acb2-460a-8dd6-64f94927ba7&title=&width=578.3333103524324)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723264099813-78c706bd-af44-4eca-96b8-1473de22f42e.png#averageHue=%23282b2f&clientId=u0ba0c7ad-c00f-4&from=paste&height=646&id=ue688607d&originHeight=775&originWidth=1289&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=173437&status=done&style=none&taskId=ubc6b2161-a349-4038-b07a-9a0d9160b59&title=&width=1074.16662398312)
第三步：启动页面(浏览实现效果)，前端在终端使用 yarn run dev 启动项目。
同时启动后端项目，访问 http://localhost:8000 <http://localhost:8000/> ，输入账号和密码进行登录。

1.  创建路由和页面，确保页面能访问到 
2.  获取需要的数据，定义 state 变量来存储数据，用于给页面展示 
3.  先把最简单直观的数据展示给前端，再去调祥式 
```tsx
import { listMyChartByPageUsingPOST } from '@/services/ShierBI/ChartController';
import { message } from 'antd';
import React, { useEffect, useState } from 'react';

const MyChartPage: React.FC = () => {
  /**
   * 初始值
   */
  const initSearchParams = {
    pageSize: 12,
  };

  const [searchParams, setSearchParams] = useState({
    ...initSearchParams,
  });
  /**
   * 分页获取图表
   */
  const [chartList, setChartList] = useState<API.Chart[]>();
  const [chartTotal, setChartTotal] = useState<number>();

  /**
   * 加载图表数据
   */
  const loadData = async () => {
    try {
      let res = await listMyChartByPageUsingPOST(searchParams);
      if (res.data) {
        setChartList(res.data.records ?? []);
        setChartTotal(res.data.total ?? 0);
      } else {
        message.error('获取我的图表失败');
      }
    } catch (e: any) {
      message.error('获取我的图表失败' + e.message);
    }
  };
  /**
   * 变化时执行此处
   */
  useEffect(() => {
    loadData();
  }, [searchParams]);

  return (
    <div className="my-chart-page">
      数据列表：
      {JSON.stringify(chartList)}
      

      总数：
      {chartTotal}
    </div>
  );
};
export default MyChartPage;
```

1.  引入Ant Design的List组件，https://ant.design/components/list-cn，复制示例代码 
2.  调整 List 组件中的内容为自己的（注意，获取用户头像可以从初始化状态中获取） 
```jsx
const { initialState } = useModel('@@initialState');
const { currentUser } = initialState || {};
```

3.  针对样式，对数据做一些处理，比如统一隐藏图表自身的 title 
4.  增加分页 
5.  增加搜索框 
6.  其他优化：比如 loading 效果 
7.  增加删除的功能（自己补充） 
```typescript
import {
  deleteChartUsingPOST,
  listMyChartByPageUsingPOST,
} from '@/services/ShierBI/ChartController';
import { useModel } from '@@/exports';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import { Avatar, Button, Card, Col, Divider, List, message, Modal, Row } from 'antd';
import Search from 'antd/es/input/Search';
import ReactECharts from 'echarts-for-react';
import React, { useEffect, useState } from 'react';

const MyChartPage: React.FC = () => {
  /**
   * 初始值
   */
  const initSearchParams = {
    current: 1,
    pageSize: 6,
  };

  const [searchParams, setSearchParams] = useState<API.ChartQueryRequest>({
    ...initSearchParams,
  });

  // const [selectedChartId, setSelectedChartId] = useState<number | undefined>(undefined);

  /**
   * 分页获取图表
   */
  const [chartList, setChartList] = useState<API.Chart[]>();
  const [chartTotal, setChartTotal] = useState<number>(0);
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
          const res = await deleteChartUsingPOST({ id: chartId });
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
      let res = await listMyChartByPageUsingPOST(searchParams);
      if (res.data) {
        setChartList(res.data.records ?? []);
        setChartTotal(res.data.total ?? 0);
        // 隐藏title
        if (res.data.records) {
          res.data.records.forEach((data) => {
            const chartOption = JSON.parse(data.genChart ?? '{}');
            // 取出title并且设置为 undefined
            chartOption.title = undefined;
            data.genChart = JSON.stringify(chartOption);
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
              chartName: value,
            });
          }}
        />
      </div>

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
                title={currentUser?.userName}
              />
              <p
                style={{
                  textAlign: 'center',
                  fontWeight: 'bold',
                  color: 'black',
                  fontSize: '16px',
                }}
              >
                {'分析目标：' + item.goal}
              </p>
              <List.Item.Meta
                style={{ textAlign: 'right' }}
                description={item.chartType ? '图表类型：' + item.chartType : undefined}
              />
              <ReactECharts option={item.genChart && JSON.parse(item.genChart)} />
              <p
                style={{
                  textAlign: 'center',
                  fontWeight: 'bold',
                  color: '#e617ff',
                  fontSize: '16px',
                }}
              >
                {'图表名称：' + item.chartName}
              </p>
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
            </Card>
          </List.Item>
        )}
      />
    </div>
  );
};
export default MyChartPage;
```
### 报错
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723270525337-8c2fafee-2287-4074-86f3-58ce9a8b5968.png#averageHue=%23fafbd9&clientId=u0ba0c7ad-c00f-4&from=paste&height=439&id=u6a0133d7&originHeight=527&originWidth=1719&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=68279&status=done&style=none&taskId=u07b62b47-baeb-41f8-a8d8-f73b79de886&title=&width=1432.4999430775665)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723270387554-2df4e4e6-5d51-4c2f-83cd-8cee011b67be.png#averageHue=%23202226&clientId=u0ba0c7ad-c00f-4&from=paste&height=631&id=ue0764c33&originHeight=757&originWidth=1914&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=190093&status=done&style=none&taskId=uca5a82d6-54fe-488b-ac61-3be638be5e9&title=&width=1594.999936620397)
完整代码src/pages/MyChart/index.tsx：
```typescript
import { useModel } from '@@/exports';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import { Avatar, Button, Card, Col, Divider, List, message, Modal, Row } from 'antd';
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
                                title={currentUser?.userName}
                            />
                            <p
                                style={{
                                    textAlign: 'center',
                                    fontWeight: 'bold',
                                    color: 'black',
                                    fontSize: '16px',
                                }}
                            >
                                {'分析目标：' + item.goal}
                            </p>
                            <List.Item.Meta
                                style={{ textAlign: 'right' }}
                                description={item.chartType ? '图表类型：' + item.chartType : undefined}
                            />
                            <ReactECharts option={item.genChart && JSON.parse(item.genChart)} />
                            <p
                                style={{
                                    textAlign: 'center',
                                    fontWeight: 'bold',
                                    color: '#e617ff',
                                    fontSize: '16px',
                                }}
                            >
                                {'图表名称：' + item.name}
                            </p>
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
                        </Card>
                    </List.Item>
                )}
            />
        </div>
    );
};
export default MyChartPage;
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723270872165-47ac6496-4b2c-4e23-8b8c-0fd9ceb10f79.png#averageHue=%23fbfbfb&clientId=u0ba0c7ad-c00f-4&from=paste&height=813&id=u406f5a67&originHeight=976&originWidth=1908&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=322248&status=done&style=none&taskId=u508992ab-d3e6-41a9-aec2-30fa7f21740&title=&width=1589.999936819079)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723271047646-1910eb54-2d9f-450d-b2aa-667fc8499eb3.png#averageHue=%23fdfafa&clientId=u0ba0c7ad-c00f-4&from=paste&height=249&id=uc644444e&originHeight=299&originWidth=695&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=63807&status=done&style=none&taskId=uf9f4d186-83f8-4d11-9411-a33450a99a1&title=&width=579.1666436526519)
说明我们的数据库中有脏数据，回到后端，查看 chart表。
怎么知道哪条数据是脏数据，暂时把它不展示呢？

1. 笨办法：
找到 isDelete字段(0-存在,1-删除) ，第一条数据不变，其余改为 1；
如果前端报错依旧存在，说明这条是脏数据，就把这条数据的 isDelete字段 改为 1；
逐个判断修改后的每条数据是否存在前端报错，如果存在前端报错，则将该条数据以及之前所有
被标记为脏数据的数据标记为删除。
2. 也可以通过检查 genChart字段 的数据，判断数据是否合法。比如：
● 检查开头是否有中文
-   检查前后是否有回车、空行； 
- 检查 xAxis（yAxis、series、type、data等）是否被双引号包裹。 等等。

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723271157263-9342d03f-a8ad-4d39-8277-b97d5635b3e3.png#averageHue=%23d1cdc4&clientId=u0ba0c7ad-c00f-4&from=paste&height=352&id=u5da26bc7&originHeight=423&originWidth=656&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=230861&status=done&style=none&taskId=u5d10fe10-e37b-4ac1-ba86-34d5f865312&title=&width=546.6666449440859)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723271173807-a599ee1d-8d08-484a-a861-8f3c49db417b.png#averageHue=%23f3f2f1&clientId=u0ba0c7ad-c00f-4&from=paste&height=860&id=uf3b368da&originHeight=1032&originWidth=1920&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=187824&status=done&style=none&taskId=u49448c48-e90a-4cd8-9c18-3e757cf523f&title=&width=1599.9999364217147)
### 美化
继续美化页面内容，对数据进行处理，统一隐藏图表标题、增加分页、搜索框。
把常用的样式设定成固定的 css 样式(俗称：原子化 css)； 
找到 global.less (全局样式)。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723271355987-1e27b213-2a0b-404d-a17b-615a4832f488.png#averageHue=%2323262a&clientId=u0ba0c7ad-c00f-4&from=paste&height=1420&id=ue0abdf84&originHeight=1704&originWidth=2880&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=347844&status=done&style=none&taskId=u29e4d01a-52d0-4eb9-925e-06fb5acd097&title=&width=2399.999904632572)
```typescript
html,
body,
#root {
  height: 100%;
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial,
    'Noto Sans', sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol',
    'Noto Color Emoji';
}

.colorWeak {
  filter: invert(80%);
}

.ant-layout {
  min-height: 100vh;
}
.ant-pro-sider.ant-layout-sider.ant-pro-sider-fixed {
  left: unset;
}

canvas {
  display: block;
}

body {
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

ul,
ol {
  list-style: none;
}

@media (max-width: 768px) {
  .ant-table {
    width: 100%;
    overflow-x: auto;
    &-thead > tr,
    &-tbody > tr {
      > th,
      > td {
        white-space: pre;
        > span {
          display: block;
        }
      }
    }
  }
}

.margin-16 {
  margin-bottom: 16px;
}
```
回到 index.tsx 打上这个样式。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723271486465-304a56a0-86c9-493c-806d-a73dea00c778.png#averageHue=%23242529&clientId=u0ba0c7ad-c00f-4&from=paste&height=1420&id=u01188587&originHeight=1704&originWidth=2880&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=404628&status=done&style=none&taskId=u852de1a5-e6dd-4812-ab3c-fe3041afd20&title=&width=2399.999904632572)
## 3、系统优化
现在的网站足够安全么？

1. 如果用户上传一个超大的文件怎么办？
2. 如果用户用科技疯狂点击提交，怎么办？
3. 如果 AI 的生成太慢（比如需要一分钟），又有很多用户要同时生成，给系统造成了压力，怎么兼顾用户
4. 体验和系统的可用性？

现在我们的网站有哪几方面都不足？

1. 安全性：如果用户上传一个超大的文件怎么办？比如 1000 G？
2. 数据存储：我们将每个图表的原始数据全部存放在同一个数据表(chart表)中，后期数据量大
的情况下，会导致查询图表或查询 chart表等操作变得缓慢。
3. 限流：在做真正上线的系统中，如果系统需要付费才能使用，比如每次用户调用聪明 AI 发送
一条消息，AI 给出一个回答，这背后都需要进行成本的扣除。
### 3.1 安全性
只要我们允许用户自行上传内容，就有可能会受到攻击。例如，如果我们的网站使用了一个图片存储服务器，那么如果某个用户想攻击我们，他可能会在上传原始数据时通过上传一个巨大的文件（如 1000G）来利用我们服务器的带宽和存储资源。 
这样的攻击是很可怕的，因为服务器的负载和网络流量利用率会大幅提高。由于服务器很贵，而家庭宽带成本很便宜，攻击者可能会利用自己的家庭网络攻击我们(攻击者采用低成本的手段对我们进行攻击，却让我们承担高昂的成本)。所以我们需要注意，如果我们的网站涉及到用户上传操作，一定要对上传的文件进行校验，以防止攻击，如果我们不对上传文件进行校验，我们就要花费更多的资金来进行防御。
**只要涉及到用户自主上传的操作，一定要校验文件（图像）**
校验的维度：

1. 文件的大小
2. 文件的后缀
3. 文件的内容（成本要高一些）
4. 文件的合规性（比如敏感内容，建议用第三方的审核功能）

扩展点：接入腾讯云的图片万象数据审核(COS对象存储的审核功能)
#### 3.1.1 后端校验(时间点 01:15:34-01:24:42)
来到后端，找到 ChartController.java 下的 genChartByAi接口 ，编写校验文件代码：
```java
// 校验文件
long fileSize = multipartFile.getSize();
String originalFilename = multipartFile.getOriginalFilename();
// 校验文件大小
ThrowUtils.throwIf(fileSize > FILE_MAX_SIZE, ErrorCode.PARAMS_ERROR, "文件大小超过 1M");
// 校验文件后缀
String suffix = FileUtil.getSuffix(originalFilename);
ThrowUtils.throwIf(!VALID_FILE_SUFFIX.contains(suffix), ErrorCode.PARAMS_ERROR, "不支持该类型文件");
```
```java
/**
 * 文件大小 1M
 */
long FILE_MAX_SIZE = 1 * 1024 * 1024L;

/**
 * 文件后缀白名单
 */
List<String>  VALID_FILE_SUFFIX= Arrays.asList("xlsx","csv","xls","json");
```
ChartController.java
```typescript
package com.yupi.springbootinit.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.yupi.springbootinit.annotation.AuthCheck;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.DeleteRequest;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.constant.FileConstant;
import com.yupi.springbootinit.constant.UserConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.manager.AiManager;
import com.yupi.springbootinit.manager.CosManager;
import com.yupi.springbootinit.manager.OpenaiManager;
import com.yupi.springbootinit.model.dto.chart.*;
import com.yupi.springbootinit.model.dto.file.UploadFileRequest;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.model.enums.FileUploadBizEnum;
import com.yupi.springbootinit.model.vo.BiResponse;
import com.yupi.springbootinit.model.vo.OpenAIResponse;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.service.UserService;
import com.yupi.springbootinit.utils.ExcelUtils;
import com.yupi.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 帖子接口
 *
 */
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private ChartService chartService;

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    @Resource
    private AiManager aiManager;

    @Resource
    private OpenaiManager  openaiManager;

//    private final static Gson GSON = new Gson();

    // region 增删改查
    /**
     * 创建
     *
     * @param chartAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addChart(@RequestBody ChartAddRequest chartAddRequest, HttpServletRequest request) {
        if (chartAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, chart);
        User loginUser = userService.getLoginUser(request);
        chart.setUserId(loginUser.getId());
        boolean result = chartService.save(chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newChartId = chart.getId();
        return ResultUtils.success(newChartId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldChart.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = chartService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param chartUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateChart(@RequestBody ChartUpdateRequest chartUpdateRequest) {
        if (chartUpdateRequest == null || chartUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartUpdateRequest, chart);
        long id = chartUpdateRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Chart> getChartById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(chart);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Chart>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
            HttpServletRequest request) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<Chart>> listMyChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
            HttpServletRequest request) {
        if (chartQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        chartQueryRequest.setUserId(loginUser.getId());
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫：为了防止恶意抓取，限制每页返回的记录数不能超过20条。如果超过，则抛出异常
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }

    // endregion

    /**
     * 编辑（用户）
     *
     * @param chartEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editChart(@RequestBody ChartEditRequest chartEditRequest, HttpServletRequest request) {
        if (chartEditRequest == null || chartEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartEditRequest, chart);
        User loginUser = userService.getLoginUser(request);
        long id = chartEditRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldChart.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * 获取查询包装类
     *
     * @param chartQueryRequest
     * @return
     */
    private QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        if (chartQueryRequest == null) {
            return queryWrapper;
        }
        Long id = chartQueryRequest.getId();
        String name = chartQueryRequest.getName();
        String goal = chartQueryRequest.getGoal();
        String chartType = chartQueryRequest.getChartType();
        Long userId = chartQueryRequest.getUserId();
        String sortField = chartQueryRequest.getSortField();
        String sortOrder = chartQueryRequest.getSortOrder();

        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chartType", chartType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    /**
     * 智能分析  openai接口
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
//    @PostMapping("/gen")
    public BaseResponse<OpenAIResponse> genChartByAi_V2(@RequestPart("file") MultipartFile multipartFile,
                                             GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {

        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        User loginUser = userService.getLoginUser(request);

        // 校验
        // 如果分析目标为空，就抛出请求参数错误异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        // 如果名称不为空，并且名称长度大于100，就抛出异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");

        // 校验文件
        long size = multipartFile.getSize();
        String originalFilename = multipartFile.getOriginalFilename();
        // 校验文件大小
        final long ONE_MB = 1024 * 1024L;
        ThrowUtils.throwIf(size > ONE_MB, ErrorCode.PARAMS_ERROR, "文件超过 1M");
        // 校验文件后缀 aaa.png
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffixList = Arrays.asList("xlsx","csv","xls","json");
        ThrowUtils.throwIf(!validFileSuffixList.contains(suffix), ErrorCode.PARAMS_ERROR, "文件后缀非法");

        /*
        * 用户的输入(参考)
          分析需求：
          分析网站用户的增长情况
          原始数据：
          日期,用户数
          1号,10
          2号,20
          3号,30
        * */

        // 压缩后的数据（把multipartFile传进来）
        String csvData = ExcelUtils.excelToCsv(multipartFile);

        StringBuilder userInput = new StringBuilder("您是一位数据分析师，擅于根据我给的需求提供echarts核心代码和结论。返回内容请分为代码和结论两块。\n");
        userInput.append("分析目标：").append(goal).append("\n");
        if(StringUtils.isNotBlank(chartType)){
            userInput.append("请使用"+chartType+"图表类型"+"\n");
        }
        userInput.append("原始数据如下：").append('\n');
        userInput.append(csvData);

        // 拿到返回结果
        String response = openaiManager.doPost(userInput.toString());
        if(response==null||response.equals("")){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 生成错误");
        }else{
            int option = response.indexOf("option = ");
            int result = response.indexOf("结论");
            if(option==-1||result==-1){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 生成错误");
            }else {
                String tempOption = response.substring(option, result);
                String Option = tempOption.substring(0, tempOption.indexOf("};") + 2);
                String ResultAI = response.substring(result, response.length());
                System.out.println(Option);
                System.out.println("--------------------------------------------");
                System.out.println(ResultAI);
                Chart chart = new Chart();
                chart.setUserId(loginUser.getId());
                chart.setChartType(chartType);
                chart.setGoal(goal);
                chart.setName(name);
                chart.setChartData(csvData);
                chart.setGenChart(Option);
                chart.setGenResult(ResultAI);
                boolean saveResult = chartService.save(chart);
                ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");
                OpenAIResponse openAIResponse = new OpenAIResponse();
                openAIResponse.setGenChart(Option);
                openAIResponse.setGenResult(ResultAI);
                openAIResponse.setChartId(chart.getId());
                return ResultUtils.success(openAIResponse);
            }
        }
    }


    //    /**
//     * 智能分析  鱼皮接口 V1
//     *
//     * @param multipartFile
//     * @param genChartByAiRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/gen")
//    public BaseResponse<String> genCharByAi(@RequestPart("file") MultipartFile multipartFile,
//                                            GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
//        String name = genChartByAiRequest.getName();
//        String goal = genChartByAiRequest.getGoal();
//        String chartType = genChartByAiRequest.getChartType();
//
//        // 校验
//        // 如果分析目标为空，就抛出请求参数错误异常，并给出提示
//        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
//        // 如果名称不为空，并且名称长度大于100，就抛出异常，并给出提示
//        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");
//
//        // 读取到用户上传的 excel 文件，进行一个处理
//        User loginUser = userService.getLoginUser(request);
//        // 文件目录：根据业务、用户来划分
//        String uuid = RandomStringUtils.randomAlphanumeric(8);
//        String filename = uuid + "-" + multipartFile.getOriginalFilename();
//        File file = null;
//        try {
//            // 返回可访问地址
//            return ResultUtils.success("");
//        } catch (Exception e) {
//            //            log.error("file upload error, filepath = " + filepath, e);
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
//        } finally {
//            if (file != null) {
//                // 删除临时文件
//                boolean delete = file.delete();
//                if (!delete) {
//                    //                    log.error("file delete error, filepath = {}", filepath);
//                }
//            }
//        }
//    }

    /**
     * 智能分析 鱼皮接口
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen")
    public BaseResponse<BiResponse> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                                     GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        // 通过response对象拿到用户id(必须登录才能使用)
        User loginUser = userService.getLoginUser(request);

        // 校验
        // 如果分析目标为空，就抛出请求参数错误异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        // 如果名称不为空，并且名称长度大于100，就抛出异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");

        // 指定一个模型id(把id写死，也可以定义成一个常量)
        long biModelId = 1821914479207112706L;
        /*
        * 用户的输入(参考)
          分析需求：
          分析网站用户的增长情况
          原始数据：
          日期,用户数
          1号,10
          2号,20
          3号,30
        * */
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
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        userInput.append(csvData).append("\n");

        // 鱼皮接口,拿到返回结果
        String result = aiManager.doChat(biModelId,userInput.toString());
        //对返回结果做拆分,按照5个中括号进行拆分
        String[] splits = result.split("【【【【【");
        // 拆分之后还要进行校验
        if (splits.length < 3) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 生成错误");
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();

        // 插入到数据库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        chart.setUserId(loginUser.getId());
        boolean saveResult = chartService.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");

        BiResponse biResponse = new BiResponse();
        biResponse.setGenChart(genChart);
        biResponse.setGenResult(genResult);
        biResponse.setChartId(chart.getId());
        return ResultUtils.success(biResponse);
    }

    /**
     * 校验文件
     *
     * @param multipartFile
     * @param fileUploadBizEnum 业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }
}
```
> 事实上，仅仅校验文件后缀并不能完全保证文件的安全性，因为攻击者可以通过将非法内容更改后缀名来绕过校验。通过修改文件后缀的方式欺骗校验机制，将一些恶意文件
> 伪装成安全的文件类型。现在这个校验的维度是从浅到深，仅仅依靠校验后缀是远远不够的，还需要结合其他严格措施来加强文件的安全性。


### 3.2 数据存储
当前现状：我们把每个图表的原始数据全部存放在了同一个数据表 (chart表) 的字段里。
导致问题：

1. 如果用户上传的原始数据量很大(100MB)、图表数日益增多，查询Chart 表就会很慢。
2. 对于 BI 平台，用户是有查看原始数据、对原始数据进行简单查询的需求的。现在如果把所有数据存放在一个字段（列）中，查询时，只能取出这个列的所有内容。

**补充：**比如下图这张表，我只想要 x,y 这两列，就要先把所有的原始数据查出来，然后再去做过滤。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723383957716-c664021f-b47d-43cc-ab6b-f1d045bcf203.png#averageHue=%23faf9f8&clientId=u310f051a-d124-4&from=paste&height=568&id=u6398ceef&originHeight=682&originWidth=700&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=162149&status=done&style=none&taskId=ube9c4d93-0037-49d3-a4f4-aa870daaf4e&title=&width=583.3333101537502)
**解决方案构思：**
如果将原始数据以表格的形式存储在一个独立的数据表中，而不是放在一个小的格子里，实际上会更方便高效。由于数据表采用了标准的结构方式存储，我们可以通过使用SQL语句进行高效的数据检索，仅查询需要的列或行。此外，我们还可以利用数据库的索引等高效技术，更快、更精确地对数据进行定位和查询，从而提高查询效率和系统的响应速度。

**解决方案=>分库分表：**
把每个图表对应的原始数据单独保存为一个新的数据表，而不是都存在一个字段里。
**比如**：我们的 网站数据.xlsx ，如果要保存这个数据，就单独保存为一个新的数据表，表名 
为 chart_{图表id} 。 
新建表，然后填入下图所示的数据，分开查询测试时会用到
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723384283212-d6f924c0-b883-489a-8d3b-0afa77b22ce6.png#averageHue=%23acaca6&clientId=u310f051a-d124-4&from=paste&height=446&id=u37b6d573&originHeight=535&originWidth=501&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=86865&status=done&style=none&taskId=u725602ab-1c85-49ae-8a51-36579e6e414&title=&width=417.4999834100412)
优点：

1. 存储时，能够分开存储，互不影响（也增加安全性）
2. 查询时，可以使用各种 SQL 语句灵活取出需要的字段，查询性能更快

> 优点构思：
> 使用分开存储的方式可以带来很多好处，其中一个好处就是存储的值相互独立，不会互相影响。例如，如果我们将一个100G的数据保存到同一个表中，其他用户在访问这个数据表时会受到很大的影响，甚至在读取这个数据时可能会非常慢。
> 而通过将**每个表单独存储**，即使一个用户上传了很大的数据，其他用户在访问时也不会受到影响。这样可以保证数据的安全性和稳定性，同时也提高系统的处理能力和效率。
> 以后进行图表数据查询时，可以先根据图表的ID来查找，然后进行数据查询，方便我们排查问题。甚至返回用户原始数据，通过全标扫描的方式直接捞出所有数据，这比对数据库查询数据进行处理更加快速和高效。

#### 3.2.1 实现
##### 分开存储：

- 存储图表信息时，不把数据存储为字段，而是新建一个chart_{ 图表id } 的数据表通过图表id、数据列名、数据类型等字段，生成以下SQL语句，并且执行即可。
```sql
create table chart_chartId{
	图表id bigint not null,
	图表类型 varchar(512) not null,
	图表数据 varchart(512) null,
	...
}
```

##### 分开查询：

-  以前直接查询图表，取chartData字段；现在改为读取chart{图表id}的数据表 
```sql
select * from chart_{图表id}
select 指定查询字段 from chart_{图表id}
```

##### 具体实现：
MyBatis的动态SQL(根据代码灵活地动态生成)

1. 想清楚哪些是需要动态替换的，比如要查询的数据表名 chart_{1659210482555121666}
2. 在ChartMapper.xml中定义sql语句

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723385030854-c2f70828-559d-4ef9-b32a-339f4ae0bc02.png#averageHue=%23282d35&clientId=u310f051a-d124-4&from=paste&height=1217&id=ubcc962c3&originHeight=1461&originWidth=2437&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=426516&status=done&style=none&taskId=udfc4af81-cd3e-4db6-aa13-00d0dfa1827&title=&width=2030.8332526352704)
以下这种方式最灵活，但是要小心SQL注入风险：
比如：
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723385220373-058211ae-4412-41e0-9923-6852a3174f56.png#averageHue=%23282d35&clientId=u310f051a-d124-4&from=paste&height=1241&id=u1939a21b&originHeight=1489&originWidth=2443&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=481410&status=done&style=none&taskId=u2924e968-8529-4a2f-8826-3a561753d55&title=&width=2035.833252436588)
```sql
select * from chart12345 where id=1 or 1=1;    # SQL注入
```
```xml
<select id="queryChartData" resultType="java.util.Map">${queryChartSql}</select>
```
光标放到 ChartMapper ，按 Ctrl+鼠标左键 跳转到 ChartMapper.java 。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723385259098-9b6e7245-65d6-4a55-93f2-fc4f2034ec0c.png#averageHue=%23282d35&clientId=u310f051a-d124-4&from=paste&height=1238&id=u13fd882c&originHeight=1486&originWidth=2659&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=476453&status=done&style=none&taskId=uce7571c6-4f83-47e6-abe9-0551f967052&title=&width=2215.833245284031)
在ChartMapper中定义方法，方法名和上一步的select的id相同：
```java
List<Map<String,object>> queryChartData(String querySql);
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723385352478-66c0bb22-c999-4df4-adf0-b41176c744d9.png#averageHue=%23272c34&clientId=u310f051a-d124-4&from=paste&height=1225&id=ue4919297&originHeight=1470&originWidth=2465&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=308217&status=done&style=none&taskId=ue5843d6f-bf2a-4eb7-920b-97b2bb8a1ed&title=&width=2054.16658504142)
创建测试，把光标放在 ChartMapper ，按 Alt+回车 。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723385414230-97de17f1-37b4-41f4-9b85-b87beaf0051a.png#averageHue=%23282d36&clientId=u310f051a-d124-4&from=paste&height=1006&id=u8937e63a&originHeight=1207&originWidth=2153&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=214407&status=done&style=none&taskId=u386b0e5c-00b4-4bc6-9f18-7db850f35d6&title=&width=1794.1665953728916)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723385459548-6e52fa0c-c4a6-4f7a-91d3-cdebd97732d0.png#averageHue=%23f4f2f2&clientId=u310f051a-d124-4&from=paste&height=267&id=u07f03fd9&originHeight=321&originWidth=495&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=60664&status=done&style=none&taskId=ub8a93a25-aa3a-495e-ad3f-e0dfad55149&title=&width=412.4999836087233)
创造了测试方法。
测试类：
```java
package com.yupi.springbootinit.mapper;

import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ChartMapperTest {
    @Resource
    private ChartMapper chartMapper;

    @Test
    void queryChartData() {
        String chartId = "1659210482555121666";
        String querySql = String.format("select * from chart_%s", chartId);
        List<Map<String, Object>> resultData = chartMapper.queryChartData(querySql);
        System.out.println(resultData);
    }
}
```
> 分库分表的思路：
> 在数据库设计中考虑使用分库分表的思路可以有效地解决大数据量和高并发的问题。可以分水平分表和垂直分库两种方式。
> 水平分表指在数据量大的情况下，将表按照某个字段的值进行拆分和分散存储，例如拆分出前万个用户一个表，后1万个用户一个表。
> 垂直分库则是将不同的业务按照相关性进行划分，例如将用户中心用户相关的内容划分到一个库中，订单、支付信息和订单相关的划分到另一个库中，从而提高系统的可扩展性和稳定性。
> 分库分表是数据库设计中重要的一部分，能有效地优化系统的性能，提高用户体验，也是一个优秀的简历亮点。


#### 3.2.2 分库分表

- 水平分表
- 垂直分库

在大型互联网应用中，为了应对高并发、海量数据等挑战，往往需要对数据库进行拆分。常见的拆分方式有水平分表和垂直分库两种。

##### 水平分表(Sharding)
水平分表是将同一张表中的数据按一定的规侧划分到不同的物理存储位置上，以达到分难单张表的数据及访问压力的目的。对于SQL分为两类：id-based分表和range-based分表。
**水平分表的优点：**

1. 单个表的数据量减少，查询效率提高：
2. 可以通过增加节点，提高系统的扩展性和容措性。

**水平分表的缺点：**

1. 事务并发处理复杂度增加，需要增加分布式事务的管理，性能和复杂度都有所栖牲：
2. 跨节点查询困难，需要设计跨节点的查询模块。

##### 垂直分库(Vertical Partitioning)
垂直分库，指的是根据业务模块的不同，将不同的字段或表分到不同的数据库中。垂直分库基于数据库内核支持，对应用透明，无需额外的开发代码，易于维护升级。
**垂直分库的优点：**

1. 减少单个数据库的数据量，提高系统的查询效率
2. 增加了系统的可扩展性，比水平分表更容易实现。

**垂直分库的缺点：**

1. 不同数据库之间的维护和同步成本较高
2. 现有系统的改造存在一定的难度
3. 系统的性能会受到数据库之间互相影响的影响。

需要根据实际的业务场景和技术架构情况，综合考虑各种因素来选择适合自己的分库分表策略。

### 3.3 限流
[https://www.cnblogs.com/liqiangchn/p/14253924.html](https://www.cnblogs.com/liqiangchn/p/14253924.html)
[http://haedyfish.top](http://haedyfish.top)
> 我们需要控制用户使用系统的次数，以避免超支，比如给不同等级的用户分配不同的调用次数，防止用户过度使用系统造成破产（例如鱼聪明目前给普通用户提供50次调用次数，会员用户提供100次）。但限制用户调用次数仍存在一定风险，用户仍有可通过疯狂调用来刷量，从而导致系统成本过度消耗。
>  假设系统就一台服务器，能同时处理的用户对话数量是有限的，比如系统最多只能支持10个用户同时对话，如果某个用一秒内使用10个账号登录，那么其他用户就无法使用系统。就像去自助餐厅吃饭，如果有人一股脑地把所有美食都拿光了，其他人就无法享用了。比如双11这种大促期间，阿里巴巴就要去限制，不能说所有的用户想抢购都能成功，在前端随机放行一部分用户，而对于其他用户则进行限制，以确保系统不会被恶意用户占满。
>  现在要做一个解决方案，就是限流，比如说限制单个用户在每秒只能使用一次，这里我们怎么去思考这个限流的阈值是多少？多少合适呢？

现在的问题：使用系统是需要消耗成本的，用户有可能疯狂刷量，让你破产。
**解决问题：**
1、控制成本=>限制用户调用总次数
2、用户在短时间内疯狂使用，导致服务器资源被占满，其他用户无法使用=>限流
**思考：**
思考限流阈值多大合适？参考正常用户的使用，比如限制单个用户在每秒只能使用1次。
#### 3.3.1 限流算法
[参考文章](https://juejin.cn/post/6967742960540581918)
1、固定窗口限流
单位时间内允许部分操作
限定：1小时只允许10个用户操作
优点：最简单
缺点：可出现流量突刺
比如：前59分钟没有1个操作，第59分钟来了10个操作；第1小时01分钟又来了10个操作。相当于2分钟内执行了20个操作，服务器仍然有高峰危险。

2、滑动窗口限流
单位时间内允许部分操作，但是这个单位时间是滑动的，需要指定一个滑动单位。
比如滑动单位：1min
开始前时间限流：0s 1h 2h
一分钟限流时间后：1min 1h1min 2h1min
> 解决了固定窗口限流存在的问题

优点：能够解决上述流量突刺的问题，因为第59分钟时，限流窗口是59分~1小时59分，这个时间段内只能接受10次请求，只要还在这个窗口内，更多的操作就会被拒绝。
缺点：实现相对固定窗口来说比较复杂，限流效果和你的滑动单位有关，滑动单位越小，限流效果越好，但往往很难选取到一个特别合适的滑动单位。

3、漏桶限流
特点：以固定的速率处理请求（漏水），当请求桶满了后，拒绝请求。
举例：每秒处理10个请求，桶的容量是10，每0.1秒固定处理一次请求，如果1秒内来了10个请求，这10此请求都可以处理完，但如果1秒内来了11个请求，最后那个请求就会溢出桶，被拒绝请求。
优点：能够一定程度上应对流量突刺，能够固定速率处理请求，保证服务器的安全。
缺点：没有固定速率处理一批请求，只能一个一个按顺序来处理（固定速率的缺点）
> 漏桶算法（Leaky Bucket Algorithm） 漏桶算法可以看作一个漏水的桶，水以恒定的速度流出，当水流入速度超出漏出速度时，多余的水将会溢出。在此算法中，请求被处理为水滴，漏桶则代表着请求被处理的容器。如果当前桶内水滴量小于等于当前请求数，则处理请求；若水滴数量大于当前请求数，则拒绝该请求。
> 实现过程：
> 1. 初始化漏桶容量和速率；
> 2. 不断将请求添加至漏桶；
> 3. 如果漏桶已满，则拒绝该请求；
> 4. 当漏桶中有请求时，在一定时间间隔内按照速率恒定地处理请求，并从漏桶中移除该请求。
> 
漏桶算法在限流的过程中会固定处理请求的速率，因此无法应对突发请求或访问高峰期，容易造成请求处理延时或拒绝。同时，该算法无法准确地保证在短时间内达到整体平均限流速率。


4、令牌桶限流
> 可以解决漏桶存在的问题

管理员先生成一批令牌，每秒生成10个令牌；当用户要操作前，先去拿到一个令牌，有令牌的人就有资格执行操作、同时执行操作；拿不到令牌的就等着
优点：能够并发处理同时的请求，并发性能会更高
需要考虑的问题：还是存在时间单位选取的问题
> 令牌桶算法（Token Bucket Algorithm） 令牌桶算法可以看作一个令牌桶，其中令牌以恒定的速率产生。当一个请求到达时，如果令牌桶中仍然有令牌，则该请求得到处理并从令牌桶中减去一个令牌。如果令牌桶中没有令牌，则请求将被拒绝。在此算法中，令牌代表请求能够被处理的数量，而桶则代表着请求被处理的容器。
> 实现过程：
> 1. 初始化令牌桶容量和速率；
> 2. 以恒定速率往令牌桶中添加令牌；
> 3. 当请求到达时，如令牌桶中仍有令牌，则从桶中移除一个令牌，并处理该请求；
> 4. 如果没有足够的令牌，拒绝该请求。
> 
令牌桶算法可以缓解漏桶算法的缺点，但在一些场景下可能存在一定问题。比如在应对短时间内的高并发请求时，由于令牌数有限，引入过大的并发请求会导致严重的性能问题，也可能会造成请求失败或者拒绝。


限流粒度

1. 针对某个方法限流，即单位时间内最多允许同时X个操作使用这个方法
2. 针对某个用户限流，比如单个用户单位时间内最多执行X次操作
3. 针对某个用户X方法限流，比如单个用户单位时间内最多执行X次这个方法
#### 3.3.2 限流实现
##### 1、本地限流（单机限流）
每个服务器单独限流，一般适用于单体项目，就是你的项目只有一个服务器。
举个例子，假设你的系统有三台服务器，每台服务器限制用户每秒只能请求一次。你可以为 
每台服务器单独设置限流策略，这样每个服务器都能够独立地控制用户的请求频率。但是这种限流方式并不是很可靠，因为你并不知道用户的请求会落在哪台服务器上，它的分布是有一定的偶然性的。即使你采用负载均衡技术，让用户请求轮流发送到每台服务器，仍然存在一定的风险。
在 Java 中，有很多第三方库可以用来实现单机限流： 
Guava RateLimiter：这是谷歌 Guava 库提供的限流工具，可以对单位时间内的请求数量进行限制。

##### 2、分布式限流 (集群限流)
如果你的项目有多个服务器，比如微服务，那么建议使用**分布式限流**。

1. 把用户的使用频率等数据放到一个集中的存储进行统计，比如Rdis,这样无论用户的请求落到了哪台服务器，都以集中的数据存储内的数据为准 (Redisson，是一个操作Redis的工具库)
2. 在网关集中进行限流和统计（比如Sentinel、Spring Cloud Gateway)
#### 3.3.3 Redisson限流实现
Redisson内置了一个限流江具类，可以帮助你利用Redis来存储、来统计。
[参考官网](https://github.com/redisson/redisson)

1. 看文档
2. 下载源码

实现过程

1.  安装Radis 
2.  引入Redisson包 
```
<dependency>
   <groupId>org.redisson</groupId>
   <artifactId>redisson</artifactId>
   <version>3.21.3</version>
</dependency>
```

3.  创建RedissonConfig配置类，用于初始化RedissonClient对象单例： 

在 config目录 下新建 RedissonConfig.java
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723388177066-ade4fdfb-076b-4447-a178-1aefb84eb5f4.png#averageHue=%23fbf9f9&clientId=u310f051a-d124-4&from=paste&height=279&id=uc837afb5&originHeight=335&originWidth=694&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=83023&status=done&style=none&taskId=u3d957f95-33d4-4f7a-8ae3-f8424295502&title=&width=578.3333103524324)
```java
package com.yupi.springbootinit.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-12 17:51
 **/
@Data
@ConfigurationProperties(prefix = "spring.redis")
@Configuration
public class RedissonConfig {
    private Integer database;
    private String host;
    private Integer port;
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
//                .setDatabase(database)
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password);
//        config.useSingleServer().setAddress("redis://127.0.0.1:6380").setPassword("123456");
//        config.useSingleServer().setAddress("redis://192.168.1.100:6379").setPassword("123456");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
```
去 application.yml 取消 redis 配置注释。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723388213222-1a14b34a-9fa4-4fe2-a220-509210df548f.png#averageHue=%23f8f7f6&clientId=u310f051a-d124-4&from=paste&height=314&id=u33965833&originHeight=377&originWidth=583&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=127229&status=done&style=none&taskId=u8c86b66d-00f2-4174-90ee-da088d07104&title=&width=485.8333140280519)
```java
  redis:
#    database: 1
#    host: 192.168.1.100
#    port: 6379
    host: 127.0.0.1
    port: 6380
    password: 123456
    timeout: 5000
    lettuce:
      pool:
        max-active: 10
        max-idle: 10
        min-idle: 1
        time-between-eviction-runs: 10s
```

4.  编写RedisLimiterManager

**题外话：**怎么知道 redis 有没有密码？ 
在本地安装的 redis 目录下找到 redis-server.exe ，双击启动，放那别关掉。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723388251892-05070b37-cea5-429d-ba7e-f6660fd6fbb8.png#averageHue=%231aa315&clientId=u310f051a-d124-4&from=paste&height=233&id=aoLXM&originHeight=280&originWidth=536&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=68072&status=done&style=none&taskId=u89dd0038-8cfd-428d-a4f8-8c88d46313f&title=&width=446.6666489177287)
然后在 redis 目录下找到 redis-cli.exe ，输入命令 config get requirepass 。 
没有设置密码，所以 2) 为空。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723388265947-d33334c8-10dc-465b-86fd-bfd9d7ca3eb8.png#averageHue=%2350b338&clientId=u310f051a-d124-4&from=paste&height=62&id=GDeVu&originHeight=75&originWidth=321&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=13478&status=done&style=none&taskId=u205928eb-4e38-4a7d-a860-59822b9dbbf&title=&width=267.49998937050543)
创建好 redis 客户端，去写一个管理类； 
在 ErrorCode.java 添加错误码。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723388353094-b612e160-1ff0-4e63-ba79-0bf4b3689895.png#averageHue=%23f8f6f5&clientId=u310f051a-d124-4&from=paste&height=309&id=rITIJ&originHeight=371&originWidth=654&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=175574&status=done&style=none&taskId=u1115cf24-05e5-442a-8d6f-901fe10054d&title=&width=544.9999783436466)
```java
TOO_MANY_REQUEST(42900, "请求过于频繁"),
```
编写 RedisLimiterManager：什么是 Manager？专门提供 RedisLimiter 限流基础服务的**(提供了通用的能力，可以放到任何一个项目里)**。
在 manager目录 下创建 RedisLimiterManager.java 。
什么是Manager??专门提供RedisLimiter限流基础服务的（提供了通用的能力，可以放到任何一个项目里） 
```java
@Service
public class RedisLimiterManager {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 限流操作
     *
     * @param key 区分不同的限流器，比如不同的用户 id 应该分别统计
     */
    public void doRateLimit(String key) {
        // 创建一个限流器
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        // 每秒最多访问 2 次
        // 参数1 type：限流类型，可以是自定义的任何类型，用于区分不同的限流策略。
        // 参数2 rate：限流速率，即单位时间内允许通过的请求数量。
        // 参数3 rateInterval：限流时间间隔，即限流速率的计算周期长度。
        // 参数4 unit：限流时间间隔单位，可以是秒、毫秒等。
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
        // 每当一个操作来了后，请求一个令牌
        boolean canOp = rateLimiter.tryAcquire(1);
        ThrowUtils.throwIf(!canOp,ErrorCode.TOO_MANY_REQUEST);
    }
}
```

5.  单元测试 
```java
@SpringBootTest
class RedisLimiterManagerTest {
    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Test
    void doRateLimit() throws InterruptedException {
        String userId = "1";
        for (int i = 0; i < 2; i++) {
            redisLimiterManager.doRateLimit(userId);
            System.out.println("成功");
        }
        Thread.sleep(1000);
        for (int i = 0; i < 5; i++) {
            redisLimiterManager.doRateLimit(userId);
            System.out.println("成功");
        }
    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723519021009-2c9aa6c4-3a02-4df6-9908-6c760d6a1e9e.png#averageHue=%23282d35&clientId=u753b6bfd-32b1-4&from=paste&height=772&id=uc9a2ca35&originHeight=927&originWidth=2655&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=194405&status=done&style=none&taskId=ufa298c04-7416-4540-893f-18f21a75c9e&title=&width=2212.4999120831526)

6.  应用到要限流的方法中，比如智能分析接口 
```java
// 用户每秒限流
redisLimiterManager.doRateLimit("genChartByAi_" + loginUser.getId());
```
todo 如需开启 Redis，须移除 exclude 中的内容
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723471007735-ffb695a0-6f77-45be-865e-bca35a8812bb.png#averageHue=%23292d36&clientId=ueccb7181-e18c-4&from=paste&height=871&id=udeb3dfef&originHeight=1045&originWidth=2275&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=239064&status=done&style=none&taskId=u17975813-fafa-4fa1-97a7-4732d423984&title=&width=1895.8332579996882)
报错端口占用：
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723517747588-53ece713-3888-42b1-84b4-35f350acd0f8.png#averageHue=%23161616&clientId=u753b6bfd-32b1-4&from=paste&height=1018&id=udc30fcb3&originHeight=1222&originWidth=2346&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=115524&status=done&style=none&taskId=u6b1fab69-ef71-49cf-b5a0-b5db5068397&title=&width=1954.9999223152827)
# 第五期 - 异步化生成图表
## 1、当前系统问题分析
相同问题场景：**调用的服务处理能力有限，或者接口的处理（或返回）时长较长时，就应该考虑异步化了。**
> 要处理很大的服务，或者处理的任务很重，处理的时间很久时，就考虑异步化。
> 本项目：
> 不能让用户等待太久生成图表，最后还有可能出现无法生成的情况，这样就考虑异步进行。

1. 用户等待时间有点长（因为要等AI生成）

如果有同学之前做过同步变异步方面的优化，应该能看出来这个图表生成的时间有点长。 
因为我们背后用的 AI 能力是需要一定时间来完成处理的。

2. 业务服务器可能会有很多请求在处理，导致系统资源紧张，严重时导致服务器宕机或者无法处理新的请求

当系统面临大量用户请求时，如果处理能力有限，例如服务器的内存、CPU、网络带宽等资源有限，这可能导致用户处在一个长时间的等待状态。特别是在许多用户同时提交请求的情况下，服务器可能需要较长的时间来处理。 
此外，如果我们后端的 AI 处理能力有限，也有可能引发问题。比如，为了确保平台的安全性， 我们可能会限制用户的访问频率，即每秒或每几秒用户只能访问一次或几次。一旦用户过多地提交请求，就会增大 AI 处理的服务器的压力，导致 AI 服务器处理不了这么多请求。在这种情况下，其他用户只能等待，而在前端界面也只能显示持续等待的状态。长时间等待后，用户可能会收到服务器繁忙的错误信息。这不仅影响了用户的体验，也对服务器和我们使用的第三方服务带来压力。 
我们还需要考虑服务器如 Tomcat 的线程数限制。在极端情况下，比如每十秒只能处理一个请求，但却有 200 个用户在一秒钟内同时提交请求，这就会导致大量用户请求在服务器上积压，数据也无法及时插入到数据库中。如果用户长时间等待最终仍得到请求失败的结果，这种情况下也会对服务器造成压力。

3. 调用的第三方服务(AI能力)的处理能力是有限的，比如每3秒只处理1个请求，当多个请求时会导致 AI 处理不过来，严重时AI可能会对咱们的后台系统拒绝服务。

当我们调用第三方服务，比如我们的 AI 处理能力是有限的，如每三秒只能处理一个请求。在这种情况下，大量用户同时请求可能导致 AI 过载，甚至拒绝我们的请求。 
假设我们正在使用的鱼聪明平台，这是一个提供 AI 回答功能的服务。在我们的开发的智能 BI 中，如果有 100 个用户同时访问，就需要 100 次调用鱼聪明 AI。然而，鱼聪明 AI 可能无法在一秒钟内服务 100 个用户。这种情况下，AI 服务会认为我们在攻击它，或者超过了它的处理能力，可能会对我们施加限制。这构成了一个潜在的风险

## 2、异步化处理图表生成
### 2.1 同步与异步
同步：一件事情做完，再做另外一件事情，不能同时进行其他的任务。（比如你在烧水，就不能去做其他的时间。但是事实并非如此。）
异步：不用等一件事故完，就可以做另外一件事情。等第一件事完成时，可以收到一个通知，通知你这件事做好了，你可以再进行后续处理。（烧水的时候，水壶有一个警报器；烧水时人可以同时处理工作。水烧好后，人听到声音，就知道水烧好了)
### 2.2 业务流程
#### 2.2.1 标准异步化的业务流程 ⭐⭐⭐⭐⭐
##### 标准异步化业务流程讲解
在用户需要进行长时间的操作时，点击提交后不需要在界面空等。而是先保存至数据库。以往 我们会等图表完全生成后再保存，但现在，任务一提交，我们就立即存储，避免让用户在界面上等待。
接着，我们需要将用户的操作或任务添加到**任务队列中**，让程序或线程执行。想象一下，将用户的操作加入任务队列，这个队列就像个备忘录。比如我是公司唯一的员工，正在进行一个项目，当有用户请求修复 bug 时。我无法立即处理，但可以记下这个修复 bug 的任务，待完成项目后再处理。这个队列就像我的备忘录。
由于程序的处理能力或线程数有限，我们可以先把待处理的任务放入队列中等待。当我们有空的时候，再按顺序执行，而不是直接拒绝。因此，如果任务队列有空位，我们可以接受新任务；如果有空闲的线程或员工，我们可以立即开始这个任务；如果所有线程都在忙碌，那么我们可以把任务放入等待队列。但是，如果所有线程都在忙，且任务队列已满，那我们该怎么办？
有很多策略供我们选择。一种做法是直接拒绝任务。更好的方式是记录下这个任务，待有空时再处理。无论任务提交成功与否，我们都应该将其保存到数据库中以备查阅。这样，当我们在后期检查时，可以看到哪些任务由于程序处理能力不足而未得到处理。即使任务队列已满，我们也可以通过查阅数据库记录，找出提交失败的任务，并在程序空闲时取出这些任务，放入任务队列中执行。
当用户需要执行新任务时，即使任务提交失败，或者消息队列满了，也要将其记录下来。建议**将这个任务保存到数据库中记录，或者至少打一个日志**。我们不能只把消息放入消息队列当做唯一的流程。要有一些保险措施，如打更多的日志，以应对网络或电力的突发情况。在开始编写程序前，我们应该清楚这些流程，这些都是我们可能需要处理的情况。
在第三步中，我们的程序（线程）会按照任务队列的顺序逐一执行任务，这就像员工按照备忘录一项接一项地完成任务。任务完成后，我们会更新任务状态，将相关任务记录在数据库中标记为已完成。接下来需要考虑的问题是如何让用户知道他们的任务何时完成。在用户提交任务后，我们应该提供一个查询任务状态的地方，而不是让他们无尽地等待。
通过这一系列流程，用户的体验会比直接等待任务完成更好。尤其是在需要进行复杂分析的情况下，用户不太可能在界面上等待那么久。这时，我们可以采取异步执行，让用户先去做其他事情，可以继续提交新任务，也可以实时查看任务状态。这样的体验更好，远优于等待长时间后任务失败。
❗** 注意**：并非所有的操作都需要异步化。只有在任务执行时间较长的场景下，才考虑采用异步化方式。因为多线程和异步处理会增加代码的复杂度，并可能带来更多的问题。如果同步方式能够解决问题，那么就无需使用异步。 
异步处理是一个复杂的过程。在异步执行中，开发者可能无法清楚地知道程序执行到了哪一步。 对于复杂的任务，我们需要在每一个小任务完成时记录下任务的执行状态或进度，这就像我们下载文件时看到的进度条一样。所以，对于大型、复杂的任务，为了提供更好的用户体验，我们应该提供进度条，让用户在查询状态时能看到任务执行到了哪一步。这是任何异步操作的重要环节，也是优化业务流程的方法。
##### 总结：

1. 当用户要进行耗时很长的操作时，点击提交后，不需要在界面长时间的等待，而是应该把这个任务保存到数据库中记录下来
2. 用户要执行新任务时： 
   1. 任务提交成功： 
      1. 如果我们的程序还有多余的空闲线程，可以立刻去执行这个任务。
      2. 如果我们的程序的线程都在繁忙，无法继续处理，那就放到等待队列里。
   2. 任务提交失败：比如我们的程序所有线程都在忙，**任务队列满了**。 
      1. 拒绝掉这个任务，再也不去执行。
      2. **通过保存到数据库中的记录来看到提交失败的任务，并且在程序空闲的时候，可以把任务从数据库中回调到程序里，再次去执行此任务**。
3. 我们的程序（线程）从任务队列中取出任务依次执行，每完成一件事情要修改一下的任务的状态。
4. 用户可以查询任务的执行状态，或者在任务执行成功或失败时能得到通知（发邮件、系统消息提示、短信），从而优化体验。
5. 如果我们要执行的任务非常复杂，包含很多环节，在每一个小任务完成时，要在程序（数据库中）记录一下任务的执行状态（进度）。
> 你目前是一个小组的技术大牛，小弟A来请求你带他做一个项目，于此同时，又来了一个小弟B叫你去帮他修改一下bug。
因为你已经答应别人带小弟A去做项目了，所以说不能同时帮小弟B去看bug。
> 只能先把小弟B改bug这个任务在备忘录记录下来，等后面带小弟A做完项目后再去查看备忘录（备忘录如果有任务就去执行备忘录最先记录的任务），然后帮小弟B改bug
> 备忘录 => 任务队列（等待队列）

#### 2.2.2 本BI系统的业务流程🌟🌟🌟
##### 系统的业务流程讲解
回归到我们的系统，流程其实可以大大简化。比如，在创建图表的过程中，每次提交可以视为一 个任务，我们甚至可以直接省略一个新的任务记录表。具体来说，将创建图表这个过程视作一个任务，并直接在图表的数据库或数据表中添加一个字段来表示图表的生成状态或任务状态，这样就无需再建立新的表了。
要查询图表是否生成完毕，我们可以直接在页面上给出提示，或者展示一个等待标志、给出其他形式的反馈。这就避免了新建一个任务查询页面的需要，我们完全可以直接在这个界面完成任务状态的展示。

- 首先，用户在点击智能分析页的提交按钮时，系统会立即将图表保存到数据库中作为一个任务。这是立即保存的，而不是等待 AI 生成结果后再保存。这样即使图表生成失败，用户也能明了其状态。 
- 其次，用户可以在图表管理页面查看所有图表的信息和状态，包括已生成的、正在生成的和 生成失败的。此处，如果你想添加一些通知功能，或者定时更新功能，如让这个页面每三秒自动更新一次数据，都是可以的。 
- 最后，用户可以修改生成失败的图表信息，或者选择重新生成等操作。这个功能🐟不一定在这个系统中实现，它类似于增删改查操作中的修改功能，很像鱼聪明的重新生成功能
##### 总结：

1. 用户点击智能分析页面的提交按钮时，先把图表立刻保存到数据库中（作为一个任务）
2. 用户可以在图表管理界面插查看所有的图表的信息和状态 
   - 已生成的
   - 生成中的
   - 生成失败的
3. 用户可以修改生成失败的图表信息，点击重新生成图表
> 此时就会用到异步化消息

异步化流程如下：![](https://cdn.nlark.com/yuque/0/2023/png/35417594/1686747303882-52af52b1-081b-4fa1-a3cd-9e1491fc7fcb.png#averageHue=%23fbfbfb&clientId=u928721fd-246e-4&from=paste&height=503&id=u1a9a07e3&originHeight=503&originWidth=876&originalType=binary&ratio=1&rotation=0&showTitle=false&size=34855&status=done&style=none&taskId=u06cebe2a-c37f-4da1-9bcf-321132402df&title=&width=876#averageHue=%23fbfbfb&errorMessage=unknown%20error&id=xLmfz&originHeight=503&originWidth=876&originalType=binary&ratio=1&rotation=0&showTitle=false&status=error&style=none)
> 但是现在也会存在一定的问题：
> 1. 任务队列的最大容量应该设置多少合适？
> 2. 程序怎么从任务队列中取出任务去执行，这个任务队列的流程怎么实现的，怎么保证程序最多同时执行多少个任务？ 
>    - 阻塞队列
>    - 线程池
>    - 增加更多的人手?

虽然现在大家看完了整个流程，但对于从未接触过异步化开发的同学，可能会有两个疑问。首先，任务队列应该如何设置最大容量，毕竟我们的能力是有限的，不能无限制地接受任务。就像我们公司接外部工作一样，我们也有满载的瓶颈。 
回归到我们的系统中，我们应该如何限制任务队列的最大容量？应该设置多少个任务为最大值？ 
另一个关键的实现点是，我们的程序如何从任务队列中逐一取出任务进行执行？更进一步的话， 我们应该如何实现这个任务队列的流程？这两个问题值得我们深思。 
首先，我们应该如何设置任务队列的最大容量。其次，我们应该如何实现一个程序，使其能自动从任务队列中取任务执行，并确保它在取任务时，最多只能同时取出多少个任务，最多只能执行多少个任务。例如，假设用户提交了八个任务，但我的程序最多只能同时执行四个任务。我需要等待第四个任务执行完毕后才能取出下一个任务，这又该如何实现呢？
实际上，这可能听起来或者说让你自己实现起来可能比较麻烦，需要用到数据结构中的队列知识，可能还要结合一些编程思想去实现。有些人可能会提出使用阻塞队列，这是一种实现方式， 或者说适当增加人手，这也是一种实现方式。 
另一种实现方式是分配四个线程，然后这四个线程执行完一个任务之后，才能再执行下一个任务。也就是说，线程需要取出任务来执行。 
## 3、线程池
### 什么是线程池?
#### 线程池的讲解
首先，我们需要实现的流程必须有一个工作者，也就是程序的线程，比如我们称之为线程小李。 此外，我们还需要一个任务队列，这个队列中有待处理的任务。设想一个情景，用户提交了一个任务，比如智能分析，任务1便加入队列。线程小李现在可以从任务队列中取出任务1进行执行， 关注的就是这个任务队列，有任务则取出，但每次只能执行一个，因为每个线程的工作能力是有限的，不可能同时处理多个任务。
那么问题来了，又来了一个任务2，但线程小李正在处理任务1，没有办法处理任务2。此时，是不是可以考虑再派一个线程小鱼来帮忙？假如再有任务3，但我们的员工已经没有了，只有小李和小鱼，此时又该怎么办？我们可以把新来的任务放入任务队列中，等待小李或者小鱼完成当前任务后，再去取任务3。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723542023199-8c7a3202-4c29-49f0-a623-d7becb37ccdf.png#averageHue=%23f8f8f8&clientId=u6da9e890-ded5-4&from=paste&height=208&id=W49i2&originHeight=250&originWidth=491&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=21306&status=done&style=none&taskId=uffd18ab4-d057-4255-8c94-116f5baa2c9&title=&width=409.16665040784477)
但是，如果任务队列满了怎么办？此时，我们就没办法再接收新任务了。这时，为了系统的稳定性，我们可以选择把新任务记录到数据库中，但不再加入任务队列，等有空闲的线程或者有新的线程加入时，再把这些任务加入队列。
然而，这个方案还不够完美。在某些情况下，如果任务队列满了，不一定非要拒绝新任务。我们还可以有其他策略，比如再开一个队列，甚至再开一个公司。同时，我们还需要考虑，当小李正在处理任务1 的时候，任务2 来了，我们是否有必要立刻调用线程小鱼？
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723542129307-56477a1e-d71a-4e4d-9ba1-2884a52c87fd.png#averageHue=%23f7f7f7&clientId=u6da9e890-ded5-4&from=paste&height=132&id=L6jxd&originHeight=159&originWidth=458&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=13853&status=done&style=none&taskId=u560329c2-c1ce-4ff8-a093-44cf2a2ec67&title=&width=381.6666515005965)
例如，如果我们有四个任务，如果小李处理任务的速度非常快，可能一秒钟就能完成一个任务， 而小鱼处理任务的速度较慢，需要一个小时才能完成一个任务。我们是不是就让小李依次执行这四个任务就可以了。因此，如何分配任务，如何分配线程，这都需要我们制定出适合的策略。所以，我们需要线程池来管理这些线程，任务队列就像是一个备忘录，任务就是我们需要完成的工作。
为什么需要线程池？因为线程管理比较复杂。比如，突然来了很多任务，我们需要临时调用更多的线程来处理，但这些任务可能只是暂时的，正常情况下可能每天只有一个任务。当任务处理完毕后，这些临时的线程就没有工作可做了，那我们怎么办呢？我们应该把他们看作临时工，当任务处理完毕后，他们就可以离开，释放系统资源，等到下一次有大量任务需要处理的时候，再临时招募他们。
> AI 说
> 线程池是一种并发编程技术，用于**优化多线程应用程序的性能和稳定性**。它可以在应用程序启动时创建一组可重用的线程，并将工作任务分配给这些线程，以避免重复地创建和销毁线程，从而提高应用程序的吞吐量、响应时间和资源利用率。
> 线程池通常由两个主要组件组成：线程管理器和工作队列。线程管理器用于创建和销毁线程，并维护线程池中的线程的数量和状态。工作队列用于存储工作任务，并提供一种机制，使得线程管理器能够将工作任务分配给空闲的线程。当所有的线程都处于忙碌状态时，新的工作任务将被加入到队列中等待执行。
> 线程池的优点包括：
> 1. 减少了线程的创建和销毁开销，提高了性能和效率。
> 2. 避免了线程数量过多而导致的系统资源耗尽和线程调度开销增大的问题。
> 3. 允许调整线程池大小，以满足不同应用程序的需求。
> 4. 可以提高代码的可维护性和可重用性，避免了线程相关的错误，使得代码更加健壮和可靠。
> 
总之，线程池是多线程编程中非常重要的一种技术，并且已经被广泛地应用于各种服务器、框架和库中。对于需要高性能和高并发的应用程序来说，线程池是提高程序性能和可靠性的重要工具之一。

> 为什么此项目要实验线程池？

答：

1. 线程的管理比较复杂（比如：什么时候新增线程、什么时候减少空闲线程）
2. 任务存取比较复杂（什么时候接受任务、什么时候拒绝任务、怎么保证大家不抢到同一个线程）

**线程池的作用** ：帮助你轻松管理线程、协调任务的执行过程
> 小李处理任务1
> 小鱼处理任务2
> （不一定要一个人去处理一个任务，也可以一个人处理完所有的任务）
> 当来了更多的任务时，就必须要进入阻塞队列
> 当任务队列占满了，就得拒绝掉其他的任务

扩充：可以向线程池表达你的需求，比如最多只允许四个人同时执行任务。线程池就能自动为你进行管理。在任务紧急时，它会帮你将任务放入队列。而在任务不紧急或者还有线程空闲时，它会直接将任务交给空闲的线程，而不是放入队列。
### 3.1 JUC线程池的实现方式⭐⭐⭐⭐⭐
在大厂面试时，有可能会让你自行实现线程池。这时，你可以借助之前提到的几种场景，例如何时增加线程，何时减少线程，来逐步解答这个问题。 实际上这是一项繁琐的任务。比如，有一种情况，如果大家执行速度都很快，可能会抢到同一个任务，如何协调各线程不去抢相同的任务就成了问题，这就涉及到线程的协调问题。 在 Linux 中，有一种称为任务窃取的概念。比如，如果小鱼的工作效率非常高，而小李的工作效率较低，我们可以将原本分配给小李的任务，转交给小鱼去做。这就是任务窃取的概念。这个问题非常复杂，涉及到的内容很深。任务是否需要锁，取决于你使用的数据结构类型，例如你是否使用了阻塞队列等来实现任务队列，这些策略都相当复杂。

1. 不用自己写，如果是在Spring中，可以用`ThreadPoolTaskExecutor`配合`@Async`注解来实现。(不太建议：进行了封装)
2. 如果是在Java中，可以使用JUC并发编程包，中的`ThreadPoolExecutor`来实现非常灵活地自定义线程池。
#### (1)创建线程配置类
回到后端，在 config目录 下创建 ThreadPoolExecutorConfig.java (线程池配置类)。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723542627237-74d8ecbe-de3e-4e7a-97e2-34d8f3785b43.png#averageHue=%23272c33&clientId=u6da9e890-ded5-4&from=paste&height=947&id=uaa700bd8&originHeight=1137&originWidth=2599&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=249598&status=done&style=none&taskId=ub846a870-7045-4ca3-978e-8db42bfa800&title=&width=2165.8332472708526)
ps.有了这个配置类之后，可以在 application.yml 当中，写一下线程池的配置，搞一个自动注
入，和之前写 redis 配置一样，就不在这里写配置了。
直接在 ThreadPoolExecutorConfig.java 写一个 Configuration类 。
```java
package com.yupi.springbootinit.config;

import org.springframework.context.annotation.Configuration;

/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-13 17:49
 **/

@Configuration
public class ThreadPoolExecutorConfig {

}
```
ps.@Configuration 是 Spring 框架中的一个注解，它用于类级别，表明这个类是一个配置
类，其目的是允许在应用程序上下文中定义额外的 bean。
生成一个 ThreadPoolExecutorConfig 的实例。
把光标放到下图所在位置，然后按[Ctrl+P]显示当前方法的参数，看一下线程池的参数。
按下快捷键后，显示一个提示框，列出了此方法需要的所有参数类型和名称； 
线程池的参数有这么多，我们来分别解释一下。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723547846971-2e03b3af-46db-437e-acca-5c7a51fa4a35.png#averageHue=%232a303b&clientId=u6da9e890-ded5-4&from=paste&height=1267&id=ua13a5733&originHeight=1521&originWidth=2401&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=379075&status=done&style=none&taskId=uac325c97-45f2-44ea-8f7d-e8aade7f9cb&title=&width=2000.833253827363)
JUC中的线程池：
```java
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler)
```
> 怎么确定线程池参数呢？结合实际情况（实际业务场景和系统资源）来测试调整，不断优化。
> 回归到本BI系统的业务，要考虑系统最脆弱的环节（系统的瓶颈）在哪里？
> 现有条件：比如AI生成能力的并发是只允许4个任务同时去执行，AI能力允许20个任务排队

> 经典面试题：说说线程池的参数？


#### (2)AI 能力的瓶颈
我们要做线程池，肯定是为了解决问题。在刚刚提出的问题中，任务队列的最大容量应设置为多少？换句话说，我们允许有多少个线程同时执行我们的 BI 任务？这个问题如何确定呢？我们如何确定线程池参数？是否需要结合业务场景？有位同学给出的答案很到位，根据需求进行测试。 线程池参数在初次设定时并不可能一次就能百分之百精准，要结合**实际测试情况，系统资源和实际业务场景来调整。**
假设回到我们的业务场景。为什么我们需要使用线程？为什么需要异步？是不是因为我们的 AI 服务处理速度很慢，AI 服务的处理能力有限，所以我们需要配合 AI 服务，避免 AI 服务器崩溃。如果 AI 服务的能力强大，我们是否就可以提高并发度，让更多的线程同时去生成 AI。但如果 AI 服务性能较差，只允许你每三秒钟只能访问一次，只允许一个线程同时工作。那么线程池的参数就需要调小，只能让一个线程按顺序执行任务。
我们需要结合实际场景，考虑系统最脆弱的环节，或者找出系统的瓶颈在哪里。比如 AI 生成能 力的并发是一秒两次，或者并发仅允许四个线程同时执行。也就是说，你不能同时生成五个任务，需要等待前四个任务完成后才能进行，那么我们线程池中的工作者数量最多只需要四个。如果 AI 服务允许 20 个线程或 20 个任务排队，那么我们的队列长度是不是也有了。
假设最多允许四个线程同时执行，最多允许20个任务排队。我们的系统同时向 AI 提交了八个任务，那么有四个任务会被执行，剩下的四个任务就会进入队列等待。如果你同时提交了 50 个任务，那么有四个任务会被执行，20 个任务排队，剩下的 26 个任务就会被丢弃。因此，我们的线程池参数需要根据这些条件来设定。

#### (3)ThreadPoolExecutor构造方法的参数
```java
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler)
```
如下：

1.  `int corePoolSize`：核心线程数（正式员工人数）正常情况下，我们的系统应该能同时工作的线程数（随时就绪的状态)，线程池中一直存在的线程数，即使线程闲置。 
> 这些线程就好比是公司的正式员工，他们在正常情况下都是随时待命处理任务的。如何去设定这个参数呢？比如，如果我们的 AI 服务只允许四个任务同时进行，那么我们的核心线程数应该就被设置为四。

2.  `int maximumPoolSize`：（最大线程数 =>  哪怕任务再多，你也只能最多招5个人），极限情况下，我们的线程池最多有多少个线程？，线程池中允许存在的最大线程数。 
> 在极限情况下我们的系统或线程池能有多少个线程在工作。就算任务再多，你最多也只能雇佣这么多的人，因为你需要考虑成本和资源的问题。假设 AI 服务最多只允许四个任务同时执行，那么最大线程数应当设置为四。

3.  `long keepAliveTime`：（空闲线程存活时间），非核心线程在没有任务的情况下，过多久要删除（理解为开除临时工），从而释放无用的线程资源。非核心线程的空闲线程存活时间，单位：毫秒。 
> 这个参数决定了当任务少的时候，临时雇佣的线程会等待多久才会被剔除。这个参数的设定是为了释放无用的线程资源。你可以理解为，多久之后会“解雇”没有任务做的临时工。

4.  `TimeUnit unit`：（空闲线程存活时间的单位）存活时间的单位，可选的单位包括：`TimeUnit.MILLISECONDS、TimeUnit.SECONDS、TimeUnit.MINUTES、TimeUnit.HOURS、TimeUnit.DAYS`（时分秒）等等。 
> 将 keepAliveTime 和 TimeUnit 组合在一起，就能指定一个具体的时间，比如说分钟、秒等等。

5.  `BlockingQueue<Runnable> workQueue`：（工作队列）用于存放给线程执行的任务，存在一个队列的长度（一定要设置，不要说队列长度无限，因为也会占用资源)。 
> 用于存放线程任务的阻塞队列，当线程池中的线程数达到了`corePoolSize`，而阻塞队列中任务满了时，线程池会创建新的线程，直到达到`maximumPoolSize`，此时达到线程池最大容量，若阻塞队列不为空，新加入的任务将会被拒绝，同时也可以通过设置`RejectedExecutionHandler`处理满了阻塞队列和饱和的情况。

6.  `ThreadFactory threadFactory`：（线程工厂）线程创建工厂，控制每个线程的生成、线程的属性（比如线程名），用于创建新的线程，可以自定义ThreadFactory。 
> 它负责控制每个线程的生成，就像一个管理员，负责招聘、管理员工，比如设定员工的名字、工资，或者其他属性。 

7.  `RejectedExecutionHandler handler`：（拒绝策略）线程池拒绝策略，任务队列满的时候，我们采取什么措施，比如抛异常、不抛异常、自定义策略。 
> 当任务队列已满的时候，我们应该怎么处理新来的任务？是抛出异常，还是使用其他策略？比如说，我们可以设定任务的优先级，会员的任务优先级更高。如果你的公司或者产品中有会员业务，或者有一些重要的业务需要保证不被打扰，你可以考虑定义两个线程池或者两个任务队列，一个用于处理VIP任务，一个用于处理普通任务，保证他们不互相干扰，也就是资源隔离策略。(可以写到简历上的点)
当任务无法处理时，会根据设置的拒绝策略进行处理，可选的策略有：AbortPolicy（直接抛出异常，终止程序的执行）、CallerRunsPolicy（让当前的线程来处理该任务）、DiscardPolicy（直接丢弃该任务），DiscardOldestPolicy（丢弃队列中已经存在最久的任务，将当前任务插入队列尝试提交)。

> 资源隔离策略：不同的程度的任务，分为不同的队列，比如VIP一个队列，普通用户一个队列。

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723549242914-5b38791a-8225-4228-b3e9-a76aec406147.png#averageHue=%23f4f2f0&clientId=u6da9e890-ded5-4&from=paste&height=267&id=u45456881&originHeight=320&originWidth=673&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=169933&status=done&style=none&taskId=u12ec37db-1e56-4af9-9954-73e158a4c8c&title=&width=560.8333110478198)
### 3.2 线程池的工作机制
> 假设核心线程数、最大线程数、任务队列为以下的值：
> corePoolSize = 2 ；maximumPoolSize = 4 ，workQueue.size = 2

1、开始时：没有任务的线程，也没有任何的任务。
> 刚开始的核心线程数、最大线程数、任务队列中分别存在的数量为：
> corePoolSize = 0 ；maximumPoolSize = 0 ，workQueue.size = 0

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723549466743-5061bae9-847f-4dcd-9bf2-028537f42cb5.png#averageHue=%23fefefe&clientId=u6da9e890-ded5-4&from=paste&height=193&id=u766bdb83&originHeight=232&originWidth=496&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=7611&status=done&style=none&taskId=u2ea2066d-4e93-4844-8bbb-179695af4c9&title=&width=413.333316908943)
2、来了第一个任务，发现我们的员工没有到达正式的员工人数(核心线程数)，来了一个员工直接处理这个任务。
> 2、第一个任务到来时的核心线程数、最大线程数、任务队列中分别存在的数量为：
> corePoolSize = 1 ；maximumPoolSize = 1 ，workQueue.size = 0

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723549506850-284eb661-0cb8-4b3c-90e2-c14d20f9fdee.png#averageHue=%23fefefe&clientId=u6da9e890-ded5-4&from=paste&height=237&id=u656c366c&originHeight=285&originWidth=511&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=17944&status=done&style=none&taskId=u3c3447ef-c23d-4a93-99db-a895e21ea24&title=&width=425.83331641223765)
3、又来了一个任务，发现我们的员工还没有达到正式员工数，再来一个员工直接处理这个任务。
> 3、第二个任务到来时的核心线程数、最大线程数、任务队列中分别存在的数量为：
> corePoolSize = 2 ；maximumPoolSize = 2 ，workQueue.size = 0
> （一个人处理一个任务，一线程一任务）

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723549553389-5fb47b69-032c-4e0f-9954-f43293e9cef6.png#averageHue=%23fdfdfd&clientId=u6da9e890-ded5-4&from=paste&height=240&id=u3138e3cc&originHeight=288&originWidth=519&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=27040&status=done&style=none&taskId=uc4bee5d2-f6a8-4df7-991c-d8d947856e7&title=&width=432.4999828139948)
4、又来了一个任务，但是我们正式员工数已经满了（当前线程数 = corePoolSize = 2)，将最新来的任务放到任务队列（最大长度 `workQueue.size` 是 2) 里等待，而不是再加新员工。
> 4、第三、四个任务到来时的核心线程数、最大线程数、任务队列中分别存在的数量为：
> corePoolSize = 2 ；maximumPoolSize = 2 ，workQueue.size = 2
> （一个人处理一个任务，一线程一任务）

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723549576022-60274572-1b3d-40fd-bbea-c8515321efb4.png#averageHue=%23fdfdfd&clientId=u6da9e890-ded5-4&from=paste&height=230&id=u15601f96&originHeight=276&originWidth=569&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=30365&status=done&style=none&taskId=u3fa872c2-5cad-46f3-a6e5-9169ccfbf88&title=&width=474.1666478249769)
5、又来了一个任务，但是我们的任务队列已经满了（当前线程数 大于了 corePoolSize=2，己有任务数 = 最大长度 `workQueue.size` = 2)，新增线程(`maximumPoolSize` = 4)来处理任务，而不是丢弃任务。
> 5、第五个任务到来时的核心线程数、最大线程数、任务队列中分别存在的数量为：
> corePoolSize = 2 ；maximumPoolSize = 3 ，workQueue.size = 2
> （找了一个临时工处理这个队新来的这个任务）

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723549594289-1a06c628-543e-4934-9e33-f70fbd2e267a.png#averageHue=%23fdfdfd&clientId=u6da9e890-ded5-4&from=paste&height=259&id=u7cda6bb3&originHeight=311&originWidth=567&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=36607&status=done&style=none&taskId=u8d13f8ef-61c4-4faa-bfd0-089486cc05f&title=&width=472.49998122453763)
6、当到了任务7，但是我们的任务队列已经满了、临时工也招满了（当前线程数 = `maximumPoolSize` = 4，已有任务数 = 最大长度 `workQueue.size` = 2) 调用`RejectedExecutionHandler` 拒绝策略来处理多余的任务。
> 6、（没有画图了，因为和第五步的一样）等到第六个任务到来时的核心线程数、最大线程数、任务队列中分别存在的数量为：
> corePoolSize = 2 ；maximumPoolSize = 4 ，workQueue.size = 2
> （再找了一个临时工处理这个队列任务中最前面的任务4，然后这个第六个新来的线程就进入到任务队列中等待）
> 7、等到第七个任务到来时的核心线程数、最大线程数、任务队列中分别存在的数量为：
> corePoolSize = 2 ；maximumPoolSize = 4 ，workQueue.size = 2
> （此时在核心线程数、最大线程数以及任务队列中都占满了，以及无法接收新的任务了，所以说只能拒绝任务7）

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723549673194-eac37f99-e7a4-48af-b815-d684fca710d7.png#averageHue=%23fdfdfd&clientId=u6da9e890-ded5-4&from=paste&height=272&id=uecf5043d&originHeight=326&originWidth=509&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=47121&status=done&style=none&taskId=u4c9d97f3-98e2-4ebe-aab6-b15ce54fa42&title=&width=424.1666498117983)
7、最后，如果当前线程数超过 corePoolSize (正式员工数)，又没有新的任务给他，那么等 keepAliveTime 时间达到后，就可以把这个线程释放。
### 3.3 线程池的参数如何设置（经典面试题⭐⭐⭐⭐⭐）
#### (1)设置线程池参数（结合实际场景设置）
> 现有条件：比如AI生成能力的并发数是只允许4个任务同时去执行，AI能力允许20个任务排队。
> 那么在本项目中参数的设置如下：

1. `corePoolSize`(核心线程数=>正式员工数)：正常情况下，可以设置为 2 - 4
2. `maximumPoolSize`(最大线程数)：设置为极限情况，设置为 <= 4
3. `keepAliveTime`(空闲线程存活时间)：一般设置为秒级或者分钟级
4. `TimeUnit unit`(空闲线程存活时间的单位)：分钟、秒
5. `workQueue`(工作队列)：结合实际请况去设置，可以设置为20 （2n+1）
6. `threadFactory`(线程工厂)：控制每个线程的生成、线程的属性（比如线程名）
7. `RejectedExecutionHandler`(拒绝策略)：抛异常，标记数据库的任务状态为 "任务满了已拒绝"
#### (2)**线程池的设计主要分为 IO 密集型和计算密集型**
在面试中，如果面试官问你关于线程池的设置，首先你需要明确，设置的依据应该是具体的业务场景。通常，我们可以将任务分为 IO 密集型和 CPU 密集型，也称为计算密集型。 对于计算密集型的任务，它会大量消耗 CPU 资源进行计算，例如音视频处理、图像处理、 程序计算和数学计算等。要最大程度上利用 CPU，避免多个线程间的冲突，一般将核心线程数设置为 CPU 的核数加一。这个“加一”可以理解为预留一个额外的线程，或者说一个备用线程，来处理其他任务。这样做可以充分利用每个 CPU 核心，减少线程间的频繁切换， 降低开销。在这种情况下，对 maximumPoolSize 的设定没有严格的规则，一般可以设为核心线程数的两倍或三倍。
而对于 IO 密集型的任务，它主要消耗的是带宽或内存硬盘的读写资源，对 CPU 的利用率不高。比如说，查询数据库或等待网络消息传输，可能需要花费几秒钟，而在这期间 CPU 实际上是空闲的。在这种情况下，可以适当增大 corePoolSize 的值，因为 CPU 本来就是空闲的。比如说，如果数据库能同时支持 20 个线程查询，那么 corePoolSize 就可以设置得相对较大，以提高查询效率。虽然有一些经验值，比如 2N+1，不太推崇这种经验值，建议根据 IO 的能力来设定。
总结：一般情况下，任务分为IO密集型和计算密集型两种。

1. 计算密集型：吃CPU，比如音视频处理、图像处理、数学计算等，一般是设置corePoolSize为CPU的核数 +1 (空余线程)，可以让每个线程都利用好CPU的每个核，而且线程之间不用频繁切换（减少打架、减少开销）
2. IO密集型：吃带宽/内存/硬盘的读写资源，corePoolSize可以设置大一点，一般经验值是 2n 左右，但是建议以 IO 的能力为主。
#### (3)考虑导入百万数据到数据库
> 考虑导入百万数据到数据库，属于IO密集型任务、还是计算密集型任务？
> 答：只需要考虑这个过程主要消耗哪种资源。考虑导入百万数据到数据库是一个IO密集型任务。导入数据需要从外部读取大量数据，然后将其写入数据库，这个过程中计算的工作量不是很高，相对来说需要更多的磁盘IO和网络IO。因此，这个任务的性能瓶颈通常是在IO操作上，而非计算上。所以，这种情况下，我们可以认为是 IO 密集型的。


### 3.4 线程池实现开发
自定义线程池
回到后端，继续去设置线程池的参数： 

- corePoolSize (核心线程数) 为 2 — 2 个正式员工； 
- maximumPoolSize (最大线程数) 为 4 — 最多允许 4 个任务同时执行； 
- keepAliveTime (空闲线程存活时间) 为 100 — 多余的临时工会等待 100 后，被开除； 
- TimeUnit (空闲线程存活时间的单位) 为 TimeUnit.SECONDS — 与 keepAliveTime 组合使用，指多余临时工会等待 100 **秒**后，被开除； 
- workQueue (工作队列) 为 new ArrayBlockingQueue<>(10000) — 使用数组阻塞队列， 并指定长度为 4，最多等待 4 个任务； 
- threadFactory (线程工厂) 创建一个自己的线程工厂； 
- RejectedExecutionHandler (拒绝策略) 不写，默认拒绝。

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723552343617-57f75bf0-99b6-408f-a41d-b4a675a731d5.png#averageHue=%23282d37&clientId=u6da9e890-ded5-4&from=paste&height=1226&id=ua94a0d9c&originHeight=1471&originWidth=2766&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=438375&status=done&style=none&taskId=u761b3819-3923-4b57-907a-99d7beb58e3&title=&width=2304.999908407533)
```java
package com.yupi.springbootinit.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-13 17:49
 **/

@Configuration
public class ThreadPoolExecutorConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private int count = 1;
            @Override
            public Thread newThread(@NotNull Runnable r) {
                //一定要将这个 r 放入到线程当中
                Thread thread = new Thread(r);
                thread.setName("线程:" + count);
                // 任务++
                count ++;
                return thread;
            }
        };
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4 ,100, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100), threadFactory);
        return threadPoolExecutor;
    }
}
```
为了方便测试效果，新建一个 controller (控制器)，以接口的方式实时控制这个线程池，查看 
线程池的参数； 
复制 controller目录 下的 ChartController ，并重命名为 QueueController ，修改一 
下，删除多余的内容。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723552375824-ef375017-b917-4360-8012-99e08cdbea55.png#averageHue=%23282d36&clientId=u6da9e890-ded5-4&from=paste&height=1280&id=u80369f8c&originHeight=1536&originWidth=2761&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=490185&status=done&style=none&taskId=u5a6ade06-99d8-42e5-87d1-da57f13e98a&title=&width=2300.8332419064345)
```java
package com.yupi.springbootinit.controller;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 队列测试
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-13 20:25
 **/
@RestController
@RequestMapping("/queue")
@Slf4j
@Profile({ "dev", "local" })
public class QueueController {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    // 提交任务到线程池
    @GetMapping("/add")
    public void add(String name) {
        // 异步运行一个任务，将任务提交到线程池中执行
        CompletableFuture.runAsync(() -> {
            log.info("任务执行中：" + name + "，执行人：" + Thread.currentThread().getName());
            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, threadPoolExecutor);
    }

    // 查看线程当前状态
    @GetMapping("/get")
    public String get() {
        Map<String, Object> map = new HashMap<>();
        int size = threadPoolExecutor.getQueue().size();
        map.put("队列长度", size);
        long taskCount = threadPoolExecutor.getTaskCount();
        map.put("任务总数", taskCount);
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        map.put("已完成任务数", completedTaskCount);
        int activeCount = threadPoolExecutor.getActiveCount();
        map.put("正在工作的线程数", activeCount);
        return JSONUtil.toJsonStr(map);
    }
}
```
以 debug模式 重启项目，访问 http://localhost:8080/api/doc.html，找到 queue-controll 
er ；
在add接口，发送一个任务：任务1。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723554191584-83ef9336-06d0-4cc2-9f5f-55c8ec35703b.png#averageHue=%23ccb892&clientId=u6da9e890-ded5-4&from=paste&height=817&id=ua90354c2&originHeight=980&originWidth=1891&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=119909&status=done&style=none&taskId=uaaaf1a51-9801-4877-8068-80bf4bd3651&title=&width=1575.8332707153452)
由于现在刚开始，没有任何的线程和任务，当有新任务进来，发现当前员工数量还未达到设定的正式员工数(corePoolSize = 2)，则会直接增聘一名新员工来处理这个任务，在后端控制台查看信息：
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723554407885-05727b88-0d9f-447d-9cb6-8ee3cd0baa1d.png#averageHue=%232c333c&clientId=u6da9e890-ded5-4&from=paste&height=326&id=u2df8db82&originHeight=391&originWidth=2688&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=158911&status=done&style=none&taskId=u965f1820-47ff-4562-b235-a6a72510910&title=&width=2239.9999109904006)
```java
2024-08-13 21:03:15.630  INFO 32748 --- [           线程:1] c.y.s.controller.QueueController         : 任务执行中：任务1，执行人：线程:1
```
在 add接口，发送一个任务2； 
又来一个任务2，发现当前员工数量还未达到设定的正式员工数(corePoolSize = 2)，则会再次增聘一名新员工来处理这个任务，在后端控制台查看信息：
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723554467553-f68a93c7-6b1e-408f-b4cb-a03a21f5821c.png#averageHue=%23c8b38d&clientId=u6da9e890-ded5-4&from=paste&height=744&id=u2d2c9cca&originHeight=893&originWidth=1893&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=100113&status=done&style=none&taskId=udc6c7dac-ba8f-462f-b5be-713eca2d3c7&title=&width=1577.4999373157843)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723554487510-f36f1a6c-1116-419e-8584-f6fa3fb6343d.png#averageHue=%232d333c&clientId=u6da9e890-ded5-4&from=paste&height=393&id=u7261d833&originHeight=472&originWidth=2721&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=200926&status=done&style=none&taskId=u854a5b49-f7a0-4c57-91c7-7f2aab47b2c&title=&width=2267.499909897649)
在get接口查看队列长度为 0，之前的任务都没有进入队列里，因为都交给线程 1、线程 2了。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723554532204-6eebf1b3-c77f-42bb-b045-179af0e53a95.png#averageHue=%23bca87d&clientId=u6da9e890-ded5-4&from=paste&height=633&id=u539a830a&originHeight=760&originWidth=1893&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=65330&status=done&style=none&taskId=u05889e33-3f4e-4a27-8984-7c24e1a0b34&title=&width=1577.4999373157843)
在 add接口，发送一个任务3； 
又来了一个任务3，但是正式员工数已经达到上限(当前线程数 = corePoolSize = 2)，这个新任务将被放到等待队列中(最大长度 workQueue.size 是 4) ，而不是立即增聘新员工，在后端控制台查看信息，发现没有线程去执行：
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723554609574-44f815e4-a8b8-44d6-9020-7d00cdd72296.png#averageHue=%232c313a&clientId=u6da9e890-ded5-4&from=paste&height=397&id=uf73df408&originHeight=477&originWidth=2623&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=145586&status=done&style=none&taskId=uad9b494c-ff5a-4320-9547-2447da55dd8&title=&width=2185.833246476124)
在 add接口，发送任务4、任务5、任务6； 
在 get接口查看队列长度为 4，现在队列满了，正式员工也满了：
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723554718806-126a8cd3-a7a6-4bab-9e90-b3cf9cd5e283.png#averageHue=%23fdfdfd&clientId=u6da9e890-ded5-4&from=paste&height=427&id=u1c8588b7&originHeight=512&originWidth=1523&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=31990&status=done&style=none&taskId=uddb3c979-94a0-493a-a393-819592d0f11&title=&width=1269.1666162345164)
在 add接口，发送任务7； 
再来一个任务7，我们将增设新线程(最大线程数 maximumPoolSize = 4)来处理任务，而不是选择丢弃这个任务，在后端状态查看信息：
就很好的证明了一点。如果新来一个任务，这时候是先执行任务队列中前面的，还是说先执行新加入的？这种情况下，他就会先执行这个任务7。 
在 add接口，发送任务8； 
再来一个任务8，我们将增设新线程来处理任务，在后端状态查看信息，这个任务由线程 4 处 
理
现在队列满了，正式员工满了，临时工也满了； 
可以到 get接口查看：
开始编写队列测试的代码。
```java
/**
 * 队列测试
 *
 * @author Shier
 */
@RestController
@RequestMapping("/queue")
@Slf4j
@Profile({ "dev", "local" })
@Api(tags = "QueueController")
@CrossOrigin(origins = "http://localhost:8000", allowCredentials = "true")
public class QueueController {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @GetMapping("/add")
    public void add(String name) {
        CompletableFuture.runAsync(() -> {
            log.info("任务执行中：" + name + "，执行人：" + Thread.currentThread().getName());
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },threadPoolExecutor);
    }

    @GetMapping("/get")
    public String get() {
        Map<String, Object> map = new HashMap<>();
        int size = threadPoolExecutor.getQueue().size();
        map.put("队列长度:", size);
        long taskCount = threadPoolExecutor.getTaskCount();
        map.put("任务总数:", taskCount);
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        map.put("已完成任务数:", completedTaskCount);
        int activeCount = threadPoolExecutor.getActiveCount();
        map.put("正在工作的线程数:", activeCount);
        return JSONUtil.toJsonStr(map);
    }
}
```
只有两个任务情况的日志：

## 4、本项目开发异步化前后端
### 4.1 后端实现工作流程

1.  给chart表新增任务状态字段（比如排队中、执行中、已完成、失败），任务执行信息字段（用于记录任务执行中、或者失败的一些信息) 

来实现工作流程的第一步，在 chart表 新增两个字段：
```java
status varchar(128) default 'wait'            not null comment 'wait-等待,running-生成中,succeed-成功生成,failed-生成失败',
execMessage text                                   null comment '执行信息',
```
```java
# 建表脚本
# @author <a href="https://github.com/liyupi">程序员鱼皮</a>
# @from <a href="https://yupi.icu">编程导航知识星球</a>

-- 创建库
create database if not exists yubi;

-- 切换库
use yubi;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_userAccount (userAccount)
    ) comment '用户' collate = utf8mb4_unicode_ci;

-- 图表表
-- 图表信息表
create table if not exists chart
(
    id         bigint auto_increment comment 'id' primary key,
    goal       text                               null comment '分析目标',
    chartName   varchar(256)                       null comment '图表名称',
    chartData  text                               null comment '图表数据',
    chartType  varchar(256)                       null comment '图表类型',
    genChart   text                               null comment '生成的图表信息',
    getResult  text                               null comment '生成的分析结论',
    status varchar(128) default 'wait'            not null comment 'wait-等待,running-生成中,succeed-成功生成,failed-生成失败',
    execMessage text                                   null comment '执行信息',
    userId     bigint                             null comment '创建图标用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
) comment '图表信息表' collate = utf8mb4_unicode_ci;
```
在 create_table.sql 中新增字段，方便后面查看或修改。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723555508449-204772f6-99e4-4bad-817d-d2382a29e30e.png#averageHue=%23292e37&clientId=u6da9e890-ded5-4&from=paste&height=994&id=u54e45b01&originHeight=1193&originWidth=2716&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=356770&status=done&style=none&taskId=u49ccd72f-7002-4f8d-9db5-cf4e40ac342&title=&width=2263.3332433965506)
现在往 chart表 去新增字段：点击右侧菜单栏上的 Database 。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723555551162-22f5f5a5-f84f-4350-b4ba-7b582ddf2d57.png#averageHue=%23272c34&clientId=u6da9e890-ded5-4&from=paste&height=876&id=u629d9222&originHeight=1051&originWidth=2230&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=314315&status=done&style=none&taskId=ubccecec5-8fc4-4c5a-a5b9-34cec1b7085&title=&width=1858.333259489804)
往 genResult字段 后添加字段：点击 genResult字段 → +。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723555613009-ff0a798f-8c90-426c-8408-b23b488ebb79.png#averageHue=%23262b32&clientId=u6da9e890-ded5-4&from=paste&height=1244&id=uf91f9ab5&originHeight=1493&originWidth=1664&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=151553&status=done&style=none&taskId=u34319eed-817f-4ba2-9ade-b07f5973788&title=&width=1386.6666115654862)
直接用新增字段的 SQL 语句替换下面 SQL Script 中 add 后面的部分。
```java
alter table chart
    add status varchar(128) default 'wait'            not null comment 'wait-等待,running-生成中,succeed-成功生成,failed-生成失败';
    add execMessage text                                   null comment '执行信息';
```
双击 chart表 ，可以查看到新增的字段。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723555821287-73787c37-9aa2-4d6a-8693-a4eb1b271fc5.png#averageHue=%232c3039&clientId=u6da9e890-ded5-4&from=paste&height=575&id=u4d03877b&originHeight=690&originWidth=2207&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=169468&status=done&style=none&taskId=u1881f2b8-0659-48c7-b9fa-d39629b623e&title=&width=1839.1665935847523)
现在所有图表默认状态都是 wait (排队中)，全改成 succeed (已完成)。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723555847430-d18f503a-2bbc-4030-a6a3-3851f12edd82.png#averageHue=%23f0ede1&clientId=u6da9e890-ded5-4&from=paste&height=300&id=u5a530a09&originHeight=360&originWidth=643&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=148372&status=done&style=none&taskId=ue589d85b-256e-4c2f-8c93-2bedbfdbecd&title=&width=535.8333120412306)
然后去补充实体类的字段，找到 model文件下的entity文件夹下的Chart.java。
```java
/**
* 任务状态
* 图表状态 wait-等待,running-生成中,succeed-成功生成,failed-生成失败
*/
private String status;

/**
* 执行信息
*/
private String execMessage;
```
 完整内容：
```java
package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 图表信息表
 * @TableName chart
 */
@TableName(value ="chart")
@Data
public class Chart implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 图表名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表信息
     */
    private String chartData;

    /**
     * 图表类型
     */
    private String chartType;

    /**
     * 生成的图表信息
     */
    private String genChart;

    /**
     * 生成的分析结论
     */
    private String genResult;

    /**
     * 任务状态
     * 图表状态 wait-等待,running-生成中,succeed-成功生成,failed-生成失败
     */
    private String status;

    /**
     * 执行信息
     */
    private String execMessage;

    /**
     * 创建图标用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
```

2.  用户点击智能分析页的提交按钮时，先把图表立刻保存到数据库中，然后提交任务 
3.  任务：先修改图表任务状态为"执行中"。等执行成功后，修改为"已完成"、保存执行结果；执行失败后，状态修改为"失败"，记录任务失败信息。 
4.  用户可以在图表管理页面查看所有图表（已生成的、生成中的、生成失败）的信息和状态
异步生成接口： 
```java
/**
 * 图表生成异步
 *
 * @param multipartFile       用户上传的文件信息
 * @param genChartByAiRequest 用户的需求
 * @param request             http request
 * @return
 */
@Override
public BiResponse genChartByAiAsync(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
    String chartName = genChartByAiRequest.getChartName();
    String goal = genChartByAiRequest.getGoal();
    String chartType = genChartByAiRequest.getChartType();
    User loginUser = userService.getLoginUser(request);
    // 校验
    ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "图表分析目标为空");
    ThrowUtils.throwIf(StringUtils.isNotBlank(chartName) && chartName.length() > 200, ErrorCode.PARAMS_ERROR, "图表名称过长");
    ThrowUtils.throwIf(StringUtils.isBlank(chartType), ErrorCode.PARAMS_ERROR, "图表类型为空");

    // 校验文件
    long fileSize = multipartFile.getSize();
    String originalFilename = multipartFile.getOriginalFilename();
    // 校验文件大小
    ThrowUtils.throwIf(fileSize > FILE_MAX_SIZE, ErrorCode.PARAMS_ERROR, "文件大小超过 1M");
    // 校验文件后缀
    String suffix = FileUtil.getSuffix(originalFilename);
    ThrowUtils.throwIf(!VALID_FILE_SUFFIX.contains(suffix), ErrorCode.PARAMS_ERROR, "不支持该类型文件");

    // 用户每秒限流
    redisLimiterManager.doRateLimit("genChartByAi_" + loginUser.getId());

    // 无需Prompt，直接调用现有模型
    // 构造用户输入
    StringBuilder userInput = new StringBuilder();
    userInput.append("分析需求：").append("\n");
    // 拼接分析目标
    String userGoal = goal;
    if (StringUtils.isNotBlank(chartType)) {
        userGoal += "，请使用" + chartType;
    }
    userInput.append(userGoal).append("\n");
    userInput.append("原始数据：").append("\n");

    // 压缩后的数据
    String csvData = ExcelUtils.excelToCsv(multipartFile);
    userInput.append(csvData).append("\n");

    // 先插入数据到数据库
    Chart chart = new Chart();
    chartName = StringUtils.isBlank(chartName) ? ChartUtils.genDefaultChartName() : chartName;
    chart.setGoal(goal);
    chart.setChartData(csvData);
    chart.setChartName(chartName);
    chart.setChartType(chartType);
    chart.setChartStatus(ChartStatusEnum.WAIT.getValue());
    chart.setUserId(loginUser.getId());
    boolean saveResult = this.save(chart);
    ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");

    // todo 任务队列满了，抛个异常
    CompletableFuture.runAsync(() -> {
        // todo 等待太久了，抛异常，超时时间
        // 等待-->执行中--> 成功/失败
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setChartStatus(ChartStatusEnum.RUNNING.getValue());
        boolean updateChartById = this.updateById(updateChart);
        // todo 修改数据库当中的字段为 失败
        //if (!updateChartById){
        //    Chart updateChartFailed = new Chart();
        //    updateChartFailed.setId(chart.getId());
        //    updateChartFailed.setChartStatus(ChartStatusEnum.FAILED.getValue());
        //}
        if (!updateChartById) {
            handleChartUpdateError(chart.getId(), "更新图表·执行中状态·失败");
            return;
        }

        // 调用AI
        String chartResult = aiManager.doChat(userInput.toString());
        // 解析内容
        String[] splits = chartResult.split(GEN_CONTENT_SPLITS);
        if (splits.length < GEN_ITEM_NUM) {
            //throw new BusinessException(ErrorCode.SYSTEM_ERROR, "");
            handleChartUpdateError(chart.getId(), "AI生成错误");
            return;
        }
        // 生成前的内容
        String preGenChart = splits[GEN_CHART_IDX].trim();
        String genResult = splits[GEN_RESULT_IDX].trim();
        // 生成后端检验
        String validGenChart = ChartUtils.getValidGenChart(preGenChart);
        // 生成的最终结果
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setGenChart(validGenChart);
        updateChartResult.setGenResult(genResult);
        updateChartResult.setChartStatus(ChartStatusEnum.SUCCEED.getValue());
        // todo 失败情况
        boolean updateResult = this.updateById(updateChart);
        //if (!updateChartById){
        //    Chart updateChartFailed = new Chart();
        //    updateChartFailed.setId(chart.getId());
        //    updateChartFailed.setChartStatus(ChartStatusEnum.FAILED.getValue());
        //}
        if (!updateResult) {
            handleChartUpdateError(chart.getId(), "更新图表·成功状态·失败");
        }
    }, threadPoolExecutor);

    // 返回到前端
    BiResponse biResponse = new BiResponse();
    biResponse.setChartId(chart.getId());
    return biResponse;
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
    updateChartResult.setChartStatus(ChartStatusEnum.FAILED.getValue());
    updateChartResult.setExecMessage("图表更新失败！！");
    boolean updateResult = this.updateById(updateChartResult);
    if (!updateResult) {
        log.error("更新图表失败状态失败" + chartId + "," + execMessage);
    }
}
```
自己的实现
```java
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @PostMapping("/gen")
    public BaseResponse<BiResponse> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                                     GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        // 通过response对象拿到用户id(必须登录才能使用)
        User loginUser = userService.getLoginUser(request);

        // 校验
        // 如果分析目标为空，就抛出请求参数错误异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        // 如果名称不为空，并且名称长度大于100，就抛出异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");

        // 校验文件
        long size = multipartFile.getSize();
        String originalFilename = multipartFile.getOriginalFilename();
        // 校验文件大小
        final long ONE_MB = 1024 * 1024L;
        ThrowUtils.throwIf(size > ONE_MB, ErrorCode.PARAMS_ERROR, "文件超过 1M");
        // 校验文件后缀 aaa.png
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffixList = Arrays.asList("xlsx","csv","xls","json");
        ThrowUtils.throwIf(!validFileSuffixList.contains(suffix), ErrorCode.PARAMS_ERROR, "文件后缀非法");

        //限流判断，每个用户一个限流器
        redisLimiterManager.doRateLimit("genChartByAi_" + loginUser.getId());

        // 指定一个模型id(把id写死，也可以定义成一个常量)
        long biModelId = 1821914479207112706L;
        /*
        * 用户的输入(参考)
          分析需求：
          分析网站用户的增长情况
          原始数据：
          日期,用户数
          1号,10
          2号,20
          3号,30
        * */
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
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        userInput.append(csvData).append("\n");

        // 先插入数据到数据库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setStatus("wait");   // todo 设置枚举值
        chart.setUserId(loginUser.getId());
        boolean saveResult = chartService.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");

        // todo 任务队列满了，抛个异常
        CompletableFuture.runAsync(()->{
            // todo 等待太久了，抛异常，超时时间
            // 等待-->执行中--> 成功/失败
            //先修改图表任务状态为"执行中"。等执行成功后，修改为"已完成"、保存执行结果；
            // 执行失败后，状态修改为"失败"，记录任务失败信息。
            Chart updateChart = new Chart();
            updateChart.setId(chart.getId());
            updateChart.setStatus("running");  // todo  设置枚举值
            boolean updateChartById = chartService.updateById(updateChart);
            if(!updateChartById) {
//                throw new BusinessException(ErrorCode.OPERATION_ERROR, "图表状态更新失败"); // todo
                handleChartUpdateError(chart.getId(), "更新图表·执行中状态·失败");
                return;
            }
            // 调用鱼皮接口,拿到返回结果
            String result = aiManager.doChat(biModelId,userInput.toString());
            //对返回结果做拆分,按照5个中括号进行拆分
            String[] splits = result.split("【【【【【");
            // 拆分之后还要进行校验
            if (splits.length < 3) {
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
            updateChartResult.setStatus("success");  // todo  设置枚举值
            boolean b = chartService.updateById(updateChartResult);
            if(!b) {
//                throw new BusinessException(ErrorCode.OPERATION_ERROR, "图表状态更新失败"); // todo
                handleChartUpdateError(chart.getId(), "更新图表·成功状态·失败");
            }

        }, threadPoolExecutor);

        // 返回到前端
        BiResponse biResponse = new BiResponse();
        biResponse.setChartId(chart.getId());
        return ResultUtils.success(biResponse);
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
```
将上面的代码分为同步和异步两个步骤来写出来，为了在选择分析的时候，可以异步的更新，在没有分析出来结果的时候显示不报错，具体的代码如下方法中的genChartByAiAsync和genChartByAi两个方法：
> 将智能分析拆分为同步和异步操作。由于异步操作没有返回值信息，我们无法解析并返回图表信息，这导致可视化图表和分析结果无法被展示。因此，我们保留之前的智能分析结果；否则，大家在拉取这一期的代码时可能会遇到问题。
> 如果为了给面试官看，建议把这两种处理方式都保留，你可以向面试官解释为什么要引入异步流 程，异步的优点，以及如何实现异步。只要谈到线程池，有超过 50% 的可能性面试官会问你关于线程池的核心参数。这时你可以把本期中介绍的内容全都讲出来，相信面试官会认为你的思路非常清晰。 
> 接下来需要去前端做一下相应的修改。

```java
package com.yupi.springbootinit.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.yupi.springbootinit.annotation.AuthCheck;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.DeleteRequest;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.constant.FileConstant;
import com.yupi.springbootinit.constant.UserConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.manager.AiManager;
import com.yupi.springbootinit.manager.CosManager;
import com.yupi.springbootinit.manager.OpenaiManager;
import com.yupi.springbootinit.manager.RedisLimiterManager;
import com.yupi.springbootinit.model.dto.chart.*;
import com.yupi.springbootinit.model.dto.file.UploadFileRequest;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.model.enums.FileUploadBizEnum;
import com.yupi.springbootinit.model.vo.BiResponse;
import com.yupi.springbootinit.model.vo.OpenAIResponse;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.service.UserService;
import com.yupi.springbootinit.utils.ExcelUtils;
import com.yupi.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 帖子接口
 *
 */
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private ChartService chartService;

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    @Resource
    private AiManager aiManager;

    @Resource
    private OpenaiManager  openaiManager;

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 智能分析 鱼皮接口（同步）
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen")
    public BaseResponse<BiResponse> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                                 GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        // 通过response对象拿到用户id(必须登录才能使用)
        User loginUser = userService.getLoginUser(request);

        // 校验
        // 如果分析目标为空，就抛出请求参数错误异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        // 如果名称不为空，并且名称长度大于100，就抛出异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");

        // 校验文件
        long size = multipartFile.getSize();
        String originalFilename = multipartFile.getOriginalFilename();
        // 校验文件大小
        final long ONE_MB = 1024 * 1024L;
        ThrowUtils.throwIf(size > ONE_MB, ErrorCode.PARAMS_ERROR, "文件超过 1M");
        // 校验文件后缀 aaa.png
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffixList = Arrays.asList("xlsx","csv","xls","json");
        ThrowUtils.throwIf(!validFileSuffixList.contains(suffix), ErrorCode.PARAMS_ERROR, "文件后缀非法");

        //限流判断，每个用户一个限流器
        redisLimiterManager.doRateLimit("genChartByAi_" + loginUser.getId());

        // 指定一个模型id(把id写死，也可以定义成一个常量)
        long biModelId = 1821914479207112706L;
        /*
        * 用户的输入(参考)
          分析需求：
          分析网站用户的增长情况
          原始数据：
          日期,用户数
          1号,10
          2号,20
          3号,30
        * */
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
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        userInput.append(csvData).append("\n");

        // 鱼皮接口,拿到返回结果
        String result = aiManager.doChat(biModelId,userInput.toString());
        //对返回结果做拆分,按照5个中括号进行拆分
        String[] splits = result.split("【【【【【");
        // 拆分之后还要进行校验
        if (splits.length < 3) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 生成错误");
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();

        // 插入到数据库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        chart.setUserId(loginUser.getId());
        boolean saveResult = chartService.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");

        BiResponse biResponse = new BiResponse();
        biResponse.setGenChart(genChart);
        biResponse.setGenResult(genResult);
        biResponse.setChartId(chart.getId());
        return ResultUtils.success(biResponse);
    }

    /**
     * 智能分析 鱼皮接口（异步）
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen/async")
    public BaseResponse<BiResponse> genChartByAiAsync(@RequestPart("file") MultipartFile multipartFile,
                                                     GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        // 通过response对象拿到用户id(必须登录才能使用)
        User loginUser = userService.getLoginUser(request);

        // 校验
        // 如果分析目标为空，就抛出请求参数错误异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        // 如果名称不为空，并且名称长度大于100，就抛出异常，并给出提示
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");

        // 校验文件
        long size = multipartFile.getSize();
        String originalFilename = multipartFile.getOriginalFilename();
        // 校验文件大小
        final long ONE_MB = 1024 * 1024L;
        ThrowUtils.throwIf(size > ONE_MB, ErrorCode.PARAMS_ERROR, "文件超过 1M");
        // 校验文件后缀 aaa.png
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffixList = Arrays.asList("xlsx","csv","xls","json");
        ThrowUtils.throwIf(!validFileSuffixList.contains(suffix), ErrorCode.PARAMS_ERROR, "文件后缀非法");

        //限流判断，每个用户一个限流器
        redisLimiterManager.doRateLimit("genChartByAi_" + loginUser.getId());

        // 指定一个模型id(把id写死，也可以定义成一个常量)
        long biModelId = 1821914479207112706L;
        /*
        * 用户的输入(参考)
          分析需求：
          分析网站用户的增长情况
          原始数据：
          日期,用户数
          1号,10
          2号,20
          3号,30
        * */
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
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        userInput.append(csvData).append("\n");

        // 先插入数据到数据库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setStatus("wait");   // todo 设置枚举值
        chart.setUserId(loginUser.getId());
        boolean saveResult = chartService.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");

        // todo 任务队列满了，抛个异常
        CompletableFuture.runAsync(()->{
            // todo 等待太久了，抛异常，超时时间
            // 等待-->执行中--> 成功/失败
            //先修改图表任务状态为"执行中"。等执行成功后，修改为"已完成"、保存执行结果；
            // 执行失败后，状态修改为"失败"，记录任务失败信息。
            Chart updateChart = new Chart();
            updateChart.setId(chart.getId());
            updateChart.setStatus("running");  // todo  设置枚举值
            boolean updateChartById = chartService.updateById(updateChart);
            if(!updateChartById) {
//                throw new BusinessException(ErrorCode.OPERATION_ERROR, "图表状态更新失败"); // todo
                handleChartUpdateError(chart.getId(), "更新图表·执行中状态·失败");
                return;
            }
            // 调用鱼皮接口,拿到返回结果
            String result = aiManager.doChat(biModelId,userInput.toString());
            //对返回结果做拆分,按照5个中括号进行拆分
            String[] splits = result.split("【【【【【");
            // 拆分之后还要进行校验
            if (splits.length < 3) {
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
            updateChartResult.setStatus("succeed");  // todo  设置枚举值
            boolean b = chartService.updateById(updateChartResult);
            if(!b) {
//                throw new BusinessException(ErrorCode.OPERATION_ERROR, "图表状态更新失败"); // todo
                handleChartUpdateError(chart.getId(), "更新图表·成功状态·失败");
            }

        }, threadPoolExecutor);

        // 返回到前端
        BiResponse biResponse = new BiResponse();
        biResponse.setChartId(chart.getId());
        return ResultUtils.success(biResponse);
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

    /**
     * 校验文件
     *
     * @param multipartFile
     * @param fileUploadBizEnum 业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }
}
```
### 4.2 前端开发

1.  复制一个异步创建图表页面 
2.  修改一下调用的接口为异步接口 
3.  移除无用元素和变量 
4.  修改成功后的操作，比如提示语和重置表单 
5.  调整我的图表页面，补充错误处理 
6.  调整图表的查询顺序，按照创建时间降序 

来到前端，在 routes.ts 内新增 智能分析(异步) 的路由。
```java
export default [
  { name : '登录', path: '/user', layout: false, routes: [{ path: '/user/login', component: './User/Login' }] },
  // { name: '欢迎页面', path: '/welcome', icon: 'smile', component: './Welcome' },
  {path: '/', redirect: '/add_chart'},
  { name: '智能分析', path: '/add_chart', icon: 'barChart', component: './AddChart' },
  { name: '智能分析异步', path: '/add_chart_async', icon: 'barChart', component: './AddChartAsync' },
  { name: '我的图表', path: '/my_chart', icon: 'pieChart', component: './MyChart' },
  {
    path: '/admin',
    icon: 'crown',
    access: 'canAdmin',
    name : '管理员页面',
    routes: [
      { path: '/admin', redirect: '/admin/sub-page' },
      { path: '/admin/sub-page', component: './Admin' },
    ],
  },
  { path: '/', redirect: '/welcome' },
  { path: '*', layout: false, component: './404' },
];
```
复制 page目录 下的 AddChart目录 ，粘贴至 page目录 下，并重命名为 AddChartAsyn 
c 。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723559574682-45b361c7-e3e1-4064-b715-687564da2038.png#averageHue=%23232529&clientId=u6da9e890-ded5-4&from=paste&height=989&id=u75ae6f68&originHeight=1187&originWidth=2469&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=294390&status=done&style=none&taskId=uaf78f743-03e0-43f3-ac1c-69874e6ab16&title=&width=2057.499918242299)
修改 智能分析(异步) 页面： 
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723559653697-317edcf4-0ee5-4a3b-a884-d2d3163ba531.png#averageHue=%23232529&clientId=u6da9e890-ded5-4&from=paste&height=993&id=u81d67da5&originHeight=1192&originWidth=2530&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=310391&status=done&style=none&taskId=uc2d5fc89-ff9a-4167-8312-f6d1e29ed59&title=&width=2108.333249555697)
修改注释和页面名称。
删除多余的可视化图表和分析结论、一行两列
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723559693002-04f2dcdd-72f8-44ae-8285-4bea7469e157.png#averageHue=%23232529&clientId=u6da9e890-ded5-4&from=paste&height=966&id=u3244cdc6&originHeight=1159&originWidth=2536&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=236728&status=done&style=none&taskId=u4bf48ff9-2c72-46af-a8f0-78c13a9bcda&title=&width=2113.333249357015)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723560371257-e7cb2621-cf6e-4b61-bac6-ab8a9d25b1dd.png#averageHue=%23fdfcfb&clientId=u6da9e890-ded5-4&from=paste&height=354&id=u2f7f95a6&originHeight=425&originWidth=562&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=87268&status=done&style=none&taskId=ue6eed90f-cd96-4f93-9c4d-90d6308ec27&title=&width=468.3333147234394)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723560378983-ec73ff5a-1de5-4fa1-a77e-625b9a5d3183.png#averageHue=%23fdfcfc&clientId=u6da9e890-ded5-4&from=paste&height=402&id=uf66ee3c1&originHeight=482&originWidth=663&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=82141&status=done&style=none&taskId=ue8d2c3a1-95bb-4be7-b5d2-40b80a720e6&title=&width=552.4999780456234)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723560393933-81ea3cec-92f7-4014-8a3f-105f440ebda1.png#averageHue=%23fdfcfc&clientId=u6da9e890-ded5-4&from=paste&height=333&id=ue6e6f0ac&originHeight=400&originWidth=587&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=80780&status=done&style=none&taskId=u9cc42c3a-62a0-4b57-8a49-3fb82f40dc5&title=&width=489.1666472289305)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723560404435-617259af-c8d0-435d-8d3a-36b7df99fb13.png#averageHue=%23fdfcfc&clientId=u6da9e890-ded5-4&from=paste&height=552&id=u66161a9a&originHeight=662&originWidth=667&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=165213&status=done&style=none&taskId=u44dcf3cb-26f3-4c8e-ac12-36a1b57c73a&title=&width=555.8333112465019)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723560415015-b8546d5e-80b8-4450-890d-ebc17a3ea08e.png#averageHue=%23fcfbfb&clientId=u6da9e890-ded5-4&from=paste&height=312&id=u60102730&originHeight=375&originWidth=570&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=85100&status=done&style=none&taskId=u2a9378da-01ec-4f26-b683-1768414ae7d&title=&width=474.9999811251966)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723560430015-6dc4c4ae-cf26-4dec-b19c-1793feff62b0.png#averageHue=%23fcfcfb&clientId=u6da9e890-ded5-4&from=paste&height=547&id=u99db0bea&originHeight=657&originWidth=651&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=191384&status=done&style=none&taskId=ua5a701dd-1163-4387-9bc3-bfecdb2bb43&title=&width=542.4999784429876)
```java
import { UploadOutlined } from '@ant-design/icons';
import {Button, Card, Form, Input, message, Select, Space, Upload} from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React, { useState } from 'react';
import {genChartByAiUsingPost} from "@/services/yubi/chartController";
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
            const res = await genChartByAiUsingPost(params, {}, values.file.file.originFileObj);
            if (!res?.data) {
                message.error('分析失败');
            } else {
                message.success('分析任务提交成功，稍后请在我的图表页面查看');
                // 重置所有字段
                form.resetFields();
            }
        } catch (e: any) {
            console.log(e.message)
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
```
回到前端页面测试一下。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723560690405-993ccda2-268c-42ac-a081-89891be4f6ec.png#averageHue=%23f9f9f9&clientId=u6da9e890-ded5-4&from=paste&height=821&id=u6723d305&originHeight=985&originWidth=1904&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=224079&status=done&style=none&taskId=ub5a65038-1d84-4714-9a7d-9d4f80cb87c&title=&width=1586.6666036182005)
分析成功并且清空了表单内容。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723561083911-9ec98c6e-68e3-45be-93f6-8c729918b9ee.png#averageHue=%23f8f8f8&clientId=u6da9e890-ded5-4&from=paste&height=823&id=ueb78570a&originHeight=988&originWidth=1894&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=233798&status=done&style=none&taskId=u7fac5560-8f1e-4718-8d4b-6cb35562dc9&title=&width=1578.333270616004)
在前端终端执行 yarn run openapi ，把数据库新增的字段、接口补充上。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723600168411-d9131219-8864-4951-bde8-e2524a9306e8.png#averageHue=%23212327&clientId=u76d1bdba-1d35-4&from=paste&height=332&id=u31e66e10&originHeight=399&originWidth=1428&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=52937&status=done&style=none&taskId=ub966789f-743d-4330-9157-bb51c854698&title=&width=1189.9999527136504)
智能分析(异步) 页面改为异步的接口。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723600474977-ccd60721-7869-41aa-b230-245fefc0312e.png#averageHue=%23232428&clientId=u76d1bdba-1d35-4&from=paste&height=926&id=u16c9ff22&originHeight=1111&originWidth=2377&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=258570&status=done&style=none&taskId=u4643005c-cbbb-45e1-ae28-000439c2d98&title=&width=1980.8332546220918)
现在要让用户在 我的图表 页面看到一些结果； 
如果图表生成成功就展示图表，没有生成成功就展示现在的状态(进度条、转圈圈)； 
访问 ant.design 组件库 <[https://ant.design/components/result-cn#result-demo-error](https://ant.design/components/result-cn#result-demo-error)> 找一下有没有失败的组件 。
发现这个组件在 Result 里
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723600547778-2452a8c4-dec2-43cb-a154-45ed85a52022.png#averageHue=%23fcfbfb&clientId=u76d1bdba-1d35-4&from=paste&height=773&id=u89ced687&originHeight=928&originWidth=1920&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=99146&status=done&style=none&taskId=ub07458a1-877c-4fe7-ad4f-b59dadeb165&title=&width=1599.9999364217147)
去调整 我的图表 页面。
去补充错误处理。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723601214139-b4e5bade-c501-495e-8bf9-d0d73dee2283.png#averageHue=%23242529&clientId=u76d1bdba-1d35-4&from=paste&height=1346&id=u00c1e2e1&originHeight=1615&originWidth=2665&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=329267&status=done&style=none&taskId=u93bdf9f1-5927-4744-aeda-a0d2e921997&title=&width=2220.833245085349)
然后在左下图的内容中加一层判断。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723601292641-d073a1b7-7bd0-4d6a-a5d6-add7c776f144.png#averageHue=%2324262a&clientId=u76d1bdba-1d35-4&from=paste&height=1190&id=u996afb00&originHeight=1428&originWidth=2490&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=385396&status=done&style=none&taskId=u53899090-b01b-49ee-a9f6-cf457d5cdcb&title=&width=2074.9999175469115)
如果状态为 succeed，才会解析图表代码。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723601421741-d9fc7f35-74c7-4d41-b5ac-a23787ac3338.png#averageHue=%23242629&clientId=u76d1bdba-1d35-4&from=paste&height=1152&id=u8af31976&originHeight=1383&originWidth=2518&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=369760&status=done&style=none&taskId=ud8b1f684-175c-44b4-a171-e4c06f2a342&title=&width=2098.3332499530616)
```java
import { useModel } from '@@/exports';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import { Avatar, Button, Card, Col, Divider, List, message, Modal, Row } from 'antd';
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
```
来到前端页面，看一下效果，先输入分析的内容。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723601888603-4cd6529c-a979-4da0-98a8-5f7f6d1c9e17.png#averageHue=%23f9f9f9&clientId=u76d1bdba-1d35-4&from=paste&height=818&id=ud6667fe5&originHeight=982&originWidth=1909&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=223675&status=done&style=none&taskId=ud5c63eb6-05e4-40ca-a0eb-c85e06aba21&title=&width=1590.8332701192987)
然后点击 智能分析(异步) 页面的提交 按钮，然后点击我的图表 页面，可以查看到图表正 
在生成。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723601919375-44069b27-3a45-48b1-b9e3-69506202d9bb.png#averageHue=%23f7f7f7&clientId=u76d1bdba-1d35-4&from=paste&height=822&id=ua569d643&originHeight=986&originWidth=1918&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=359328&status=done&style=none&taskId=ud1a6671a-9716-461c-a5e9-d2a25d15ae6&title=&width=1598.3332698212755)
等一会，按 F5 刷新页面，然后就看见图表生成出来； 
在这等待的过程中可以去做其他事，比如去生成新的图表等等。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723601941644-d35681c6-fb50-46b3-b447-57ac3637a537.png#averageHue=%23f9f9f8&clientId=u76d1bdba-1d35-4&from=paste&height=821&id=uca2c56fb&originHeight=985&originWidth=1910&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=354791&status=done&style=none&taskId=uc34addd9-695f-4b26-b414-0be572853b2&title=&width=1591.6666034195184)
然后调整图表的查询顺序，按照创建时间降序。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723602073781-95e2cd2e-fd44-43ab-901c-6b60e94595c8.png#averageHue=%2324262a&clientId=u76d1bdba-1d35-4&from=paste&height=1245&id=u9067eef9&originHeight=1494&originWidth=2787&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=325686&status=done&style=none&taskId=u38570619-c5e4-43b4-8294-4e2fad1dd9a&title=&width=2322.499907712145)
### 4.3 优化点

1. guava Retrying 重试机制
2. 提前考虑到AI生成错误，在后端进行异常处理（比如AI说了多余的话，提前正确的字符串）
3. 如果说任务根本没提交到队列中（或者队列满了），是不是可以用定时任务把失败状态的图表放到队列中（补偿）
4. 建议给任务的执行增加一个超时时间，超时自动标记为失败（超时控制）
5. 反向压力：[https://zhuanlan.zhihu.com/p/404993753](https://zhuanlan.zhihu.com/p/404993753) 通过调用的服务状态来选择当前系统的策略（比如根据AI服务的当前任务队列数来控制该系统的核心线程数），从而最大化利用系统资源
6. 我的图表页面增加一个刷新、定时自动刷新的按钮，保证获取到图表的最新状态（前端轮询）
7. 任务执行成功或失败，给用户发送实时消息通知（实时：websocket、server side event）
> 肯定有些同学会倾向于同步方式，因为他们可以随时查看结果，即使可能需要等待十几秒或者20 秒。然而，另一些同学可能会觉得如果需要等待一分钟或者五分钟的话，异步方式可能会更合适。实际上，你也可以选择实时更新，比如每隔几秒刷新一下页面，自动获取新结果。批量异步也是一种可行的方式。 
> 另外，还有一种策略。你可以根据系统当前的负载动态地调整用户查询的处理方式。比如，如果系统当前状态良好，就可以选择同步返回结果。而如果用户提交请求后发现系统非常繁忙，预计需要等待很长时间，那么就可以选择异步处理方式。这种思考方式在实际的企业项目开发中也是很常见的。 
> 除了刚刚提到的一些点，我们还可以使用定时任务来处理失败的图表，添加重试机制。此外，我们也可以更精确地预见AI生成错误，并在后端进行异常处理，如提取正确的字符串。例如，AI 说一些多余的话，我们就需要提取出正确的信息。同时，如果任务没有提交，我们可以使用定时任务将其提取出来。我们还可以为任务增加一个超时时间。如果超时，任务将自动标记为失败，这就是超时控制，这一点非常重要。对于 AI 生成的脏数据，会导致最后出现错误，因此前端也需要进行异常处理，不能仅仅依赖于后端。
> 🐟之前写过一篇关于**反向压力的文章** <https://zhuanlan.zhihu.com/p/404993753> ，可以看 一下。刚刚提到了一点，那就是在系统压力大的时候，使用异步，而在系统压力小的时候，使用同步，这就是反向压力的概念。
> 进一步扩展一下，关于我们的线程池，现在的核心参数不是设定为二嘛。实际上，如果 AI 最多允许四个任务同时执行，我们是否可以提前确认 AI 当前的业务是否繁忙，即我们调用的第三方API 是否还有多余的资源给我们使用。如果他表示资源已经耗尽，我们为了保证系统的稳定性，是否可以将核心线程数调小一些。反之，如果我们询问 AI 第三方并发现它的状态是空闲，我们是否可以将核心线程数增加，以此来提高系统性能。这种通过下游服务来调整你的业务以及核心线程池参数，进而改变你的系统策略的方式就是反向压力。
> 例如，你发现当前 AI 服务的任务队列中没有任何人提交任务，那么你是否可以提高使用率。这其实是一个很好的点，如果你能在简历上写到反向压力，将会是一个很大的加分项。反向压力其实是我们在做大数据系统中，特别是在做实时数据流系统时经常会用到的一个术语。我们是不是可以在任务执行成功或失败后，给用户发送消息通知。比如说，在图表页面增加一个刷新或者定时自动刷新的按钮，以保证用户能够获取到图表的最新状态。这就是前端轮询的技术。还有就是在任务执行过程中，我们可以向用户发送消息通知，虽然这可能比较复杂。这东西后面🐟可能会带大家做这个系统。但是在短期内，大家可以自己尝试实现，如通过数据库记录消息，这是最简单的方式。当然还有其他的方式，如 websocket 实时通知或者 server side event，这都是实时的。

# 第六、七期 - 消息队列
## 1、分析当前系统的不足
> 系统不足：只是在单机系统上进行操作

通过第五期的直播，已经经过了同步到异步的改造，但是还是存在问题。
异步实现现状：目前的异步是通过本地的线程池实现的。
> 首先，让我们来探讨一下目前系统存在的不足。我们的系统已经完成从同步到异步的转变。为什么需要改变呢？因为 AI 模型分析速度较慢，生成图表可能需要十几秒或更长的时间。虽然OpenAI 最近已经优化了其人工智能服务的效率，但当系统用户量大时，仍可能出现系统资源不足或 AI 服务生成速度跟不上的情况，这会导致系统瓶颈。因此，我们采用异步处理，以提高用户体验、及时反馈。

### 1.1 无法集中限制，只能单机限制
假如 AI 服务限制只有2个用户同时使用，单个线程池可以限制最大核心线程数为2来实现。
假设系统用量增大，改为分布式，多台服务器，每个服务器都要有2个线程，就有可能有2N个线程，超过了AI 服务的限制。

- 解决方案：在一个集中的地方去管理下发任务（比如集中存储当前正在执行的任务数）
### 1.2 任务由于是放在内存中执行的，可能会丢失
虽然可以人工从数据库捞出来再重试，但是其实需要额外开发（比如定时任务），这种重试的场景是非常典型的，其实是不需要我们开发者过于关心、或者自己实现的。

- 解决方案：把任务放在一个可以持久化存储的硬盘
> **目前的任务数据是存在哪里的？**即使任务在数据库存储，但如果执行到一半，服务器宕机，任务可能就会丢失。虽然可以从数据库中重新读取，但这需要额外的编码工作，而且在系统复杂度增加时，实现任务重试或丢失恢复的编码工作成本较高。因此，由于任务是在内存中 
> 执行，就有可能会丢失，尽管可以从数据库手动恢复并重试，但这需要额外开发。
> 此外，随着系统功能的增多，例如 AI 对话和 AI 绘画，如果我们还要增加更多的长时间耗时任务，那么我们可能需要开辟更多的线程池，这将使系统变得越来越复杂。在这种情况下，我们可以考虑将一些耗时的任务或功能单独抽取出来，这就是微服务的思想。将这些耗时任务单独提取出来，既不影响主业务，又可以使我们的核心系统更加安全、稳定和清晰。因此，这里我们可以优化的一个方面就是服务拆分或应用解耦。

### 1.3 优化
如果你的系统功越来越多，长耗时任务越来越多，系统会越来越复杂（比如要开多个线程池、资源可能会出现项目抢占)。

- 服务拆分（应用解耦）：其实我们可以把长耗时、消耗很多的任务把它单独抽成一个程序，不要影响主业务。
- 解决方案：可以有一个中间人，让中间人帮我们去连接两个系统（比如核心系统和智能生成业务）
## 2、分布式消息队列
### 2.1 中间件
> 连接多个系统，帮助多个系统紧密协作的技术（或者组件）。

比如：Redis、消息队列、分布式存储Etcd
**什么是中间件？**可以将其视为在开发系统或多个应用时，用于连接多个系统或使多个系统紧密协 作的工具。常用的中间件包括 Redis，消息队列，以及分布式存储如 ETCD 等。
事实上，如果非要定义，数据库也可以被视为一种中间件。例如，假设我们有两个系统，系统 A 和系统 B，他们需要共享同一份数据。这时，我们可以将数据存储在数据库中，这样，数据库就像是连接这两个系统的“中间人”，即满足中间件的定义。
在 NodeJS 中，中间件的含义稍有不同，它指的是在不影响业务逻辑的前提下，增强系统功能的工具，连接通用功能和系统。
以智能 BI 后台为例，原有的系统包括用户管理和图表管理等。随着系统应用量的增加，智能分析业务可能会消耗大量资源。此时，我们可能会考虑将智能分析业务抽出，创建一个单独的智能分析服务，专门负责生成图表。
例如，我们可以使用 Redis 来存储共享数据。主系统和智能分析服务都可以读取 Redis 中的数据。这样，Redis 就充当了一个"中间人"的角色，连接了这两个系统，这就是中间件的概念。
![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230618161215010.png#id=b9hDm&originHeight=376&originWidth=576&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
### 2.2 消息队列
> 消息队列：存储消息的队列
> 消息队列：一种在应用程序之间传递消息的通信机制。它类似于实际世界中的一个邮箱系统，发送者将消息放入消息队列，接收者从队列中获取消息进行处理。这种异步的方式可以提高系统的性能和可靠性，因为发送者和接收者可以独立地进行操作，并且不需要直接相互依赖。
> 消息队列允许不同的应用程序、服务或组件之间解耦，它们可以独立地进行伸缩和维护。消息队列还提供了**可靠性和持久性**的保证，因为消息可以在发送时进行持久化，并且在接收者处理之前可以进行多次尝试。

消息队列包含的关键词：存储、消息、队列

- 存储：存储数据
- 消息：某种数据结构：比如字符串、对象、二进制、JSON等等
- 队列：一种**先进先出的数据结构**
#### 应用场景（作用）
在多个不同的系统、应用之间实现消息的传输（也可以存储）。不需要考虑传输应用的编程语言、系统、框架等等。
> 例如：可以让Java开发的应用发消息，让PHP开发的应用收消息，这样就不用把所有代码写到同一个项目里（应用解耦)

### 2.3 消息队列模型

-  生产者：Producer，类比为快递员，发送消息的人（客户端） 
-  消费者：Consumer，类比为取快递的人，接受读取消息的人（客户瑞） 
-  消息：Message，类比为你买的快递，就是生产者要传输给消费者的数据 
-  消息队列：Queue：类比为快递接受柜 

![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230618163709919.png#id=ZlUPn&originHeight=391&originWidth=1273&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
为什么不接传输，要用消息队列？
> 生产者不用关心消费者要不要消费、什么时候消费，生产者只需要把东西（消息）给消息队列，生产者的工作就算完成了。
> 比如上面这个送快递的例子：
> 送快递的配送员，只需要将快递放到快递柜，发个消息给你，快递已经放到了快递柜，那么你就可以在有空的时候再去拿这个快递，而快递配送员也不用管你什么时候去拿（不弄丢就可以，弄丢那就得另外说了）

生产者和消费者实现了解耦，互不影响。达到了**不需要考虑传输应用的编程语言、系统、框架**等等。
### 2.4 为什么要用消息队列
使用消息队列有以下几个主要原因：

1.  解耦和提高系统可靠性：消息队列提供了解耦的方式，发送方和接收方之间不直接进行通信，通过消息队列中转。这样，即使其中一个组件或者系统出现故障，不会影响到整个系统的正常运行。 
2.  异步处理和提高系统性能：消息队列可以使发送方和接收方异步处理消息。发送方将消息发送到队列中后，不需要等待接收方立即处理，而是可以继续处理其他任务。这样可以提高系统的性能和响应速度。 
3.  缓冲和流量控制：消息队列可以作为缓冲区，用于处理流量峰值。当系统面临突发的请求量增加时，消息队列可以缓冲请求并逐渐处理，避免系统过载。 
4.  同步数据：在分布式系统中，不同组件或者系统之间可能需要共享数据。通过消息队列可以实现数据的同步和共享，保证各个组件之间的数据一致性。 
5.  扩展和灵活性：使用消息队列可以通过增加消费者来实现系统的扩展性，并且消费者可以根据需求灵活地进行扩缩容。这样可以根据实际需求进行系统的调整和优化。 
#### 2.4.1 异步处理
生产者发送完消息之后，可以继续去忙别的，消费者想什么时候消费都可以，不会产生阻塞。所谓的异步处理，意味着生产者(如小李)在发送完消息后可以立即转而进行其他任务，而无需等待消费者处理消息。这样生产者就无需等待消费者接收消息才能进行下一步操作，避免了阻塞。这与我们之前讨论的异步化处理非常类似，消息队列使我们的系统具备了这种能力。
#### 2.4.2 削峰填谷
先把用户的请求放到消息队列中，消费者（实际执行操作的应用）可以按照自己的需求，慢慢去取。
**没有使用消息队列**：12点时来了10万个请求，原本情况下，10万个请求都在系统内部立刻处理，很快系统压力过大就宕机了。
**使用消息队列**：把这10万个请求放到消息队列中，处理系统以自己的恒定速率（比如每秒1个）慢慢执行，从而保护系统、稳定处理。
### 2.5 分布式消息队列的优势

1. 数据持久化：它可以把消息集中存储到硬盘里，服务器重启就不会丢失
2. 可扩展性：可以根据需求，随时增加（或减少）节点，继续保持稳定的服务
3. 应用解耦：可以连接各个不同语言、框架开发的系统，上这些系统能够灵活传输读取数据
4. 发布订阅：它可以使发布者**将消息发送到一个或多个订阅者**，从而实现解耦、可伸缩性和实时性等优势 
   1. 解耦性：发布者和订阅者之间是松耦合的，彼此不需要直接通信。发布者只需要将消息发布到特定的主题或频道中，而订阅者只需要订阅感兴趣的主题或频道。这种解耦性使得系统中的组件可以独立地进行开发、维护和扩展。
   2. 可伸缩性：发布订阅模式允许多个订阅者同时接收消息，并且可以动态地添加或移除订阅者。这种可伸缩性使系统能够处理更高的并发量，而不会对性能和可用性产生负面影响。
   3. 实时性：由于发布订阅模式支持多个订阅者同时接收消息，可以更快地将消息传递给订阅者。这种实时性使系统能够实现即时的数据传输和处理，适用于需要快速响应的场景。
   4. 可靠性：分布式消息队列通常提供了消息持久化的机制，可以将消息存储到磁盘上，确保消息不会丢失。即使消费者出现故障，稍后再启动时也可以继续接收之前未处理的消息。

#### 2.5.1 应用解耦优点
不使用消息队列时：
> 把所有的功能都放在同一个项目中，调用多个子功能时，一个环节错，整个系统就出错

![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230618164212956.png#id=JIdBH&originHeight=360&originWidth=642&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
使用了消息队列进行解耦：

1. 一个系统挂了，不影响另一个系统：如果发货系统挂了，但是不影响到库存系统进行扣减
2. 系统挂了并恢复后，仍然可以从消息队列当中取出消息，继续执行业务逻辑
3. 只要发送消息到队列，就可以立刻返回，不用同步调用所有系统，性能更高

![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230618164518027.png#id=MM9EK&originHeight=393&originWidth=977&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
> 订单系统只要将消息发送到消息队列，即可返回，并不需要调用其他的系统，而其他的系统只要从消息队列中获取到消息。

发布订阅优势图解：
如果一个非常大的系统要给其他子系统发送通知，最简单直接的方式是**大系统直接依次调用小系统**
> 比如QQ：使用QQ来关联了很多应用的消息，比如王者、吃鸡、微信等等

![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230618165359369.png#id=idqJh&originHeight=689&originWidth=704&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
这样做存在的问题：

1. 每次发通知都要调用很多系统，很麻烦、有可能失败
2. 新出现的项目（或者说大项目感知不到的项目）无法得到通知
3. 发布的消息不知道哪个系统需要

解决方案：大的核心系统始终往一个地方（消息队列）去发消息，其他的系统都去订阅这个消息队列（读取这个消息队列中的消息)
![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230618165720318.png#id=LqXDJ&originHeight=717&originWidth=975&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
### 2.6 分布式消息队列的缺点

1. 增加复杂性：分布式消息队列的部署和运维需要一定的技术知识和经验，对系统架构和设计有一定的要求。同时，因为引入了额外的组件，**系统的复杂度也会增加**，系统会更复杂、额外维护中间件、额外的费用（部署）成本
2. 消息队列：消息丢失、顺序性、重复消费、数据的一致性（分布式系统就要考虑一致性问题）
3. 消息顺序性：由于消息队列的异步性，无法保证消息的顺序性。这可能会对依赖消息顺序的业务场景造成影响，需要额外的处理机制来解决此问题。
### 2.7 分布式消息队列应用场景

1. 耗时的场景（异步）
2. 分布式系统协作（尤其是跨团队、跨业务协作，应用解耦）
3. 强稳定性的场景（比如金融业务：支付、转账，特久化、可靠性、削峰填谷）
4. 异步通信：当系统需要在**不同模块之间进行异步通信**时，分布式消息队列可以提供一种可靠的通信机制。
5. 解耦应用：当**多个应用或服务之间需要解耦**，以降低耦合度、提高灵活性和可维护性时，使用分布式消息队列可以实现解耦。
6. 流量削峰：在**高峰期或服务器负载高的情况下**，分布式消息队列可以将**流量平滑分发到不同的消费者**上，避免系统过载。（**高并发场景、异步、削峰填谷**）
7. 日志处理：对于**大规模的日志处理场景**，分布式消息队列可以帮助将日志收集、传输和处理进行解耦，并且可以容错和持久化。
8. 事件驱动架构：在事件驱动的架构中，各个组件通过订阅消息实现解耦，以便在事件发生时进行相应的处理。
### 2.8 主流分布式消息队列选型
以下是对主流分布式消息队列选型的简要介绍：

1. ActiveMQ：是一个开源的、完全支持JMS规范的分布式消息队列系统。它提供了可靠的消息传递、事务、消息持久化等功能，适用于大多数企业级应用场景。
2. RabbitMQ：也是一个开源的分布式消息队列系统，它实现了AMQP协议。RabbitMQ具有高可用性、可靠性和灵活的路由策略，适用于复杂的消息路由和灵活的消息处理场景。
3. Kafka：是一个分布式流处理平台和消息队列系统，提供高吞吐量、低延迟的消息传递。Kafka适用于大数据流处理场景，例如日志收集、实时分析等。
4. RocketMQ：是一个开源的分布式消息队列系统，最初由阿里巴巴开发，现已进入Apache孵化器。RocketMQ具有高性能、高可用性和分布式事务等特性，适用于大规模的分布式应用场景。
5. ZeroMQ：是一个轻量级的消息队列库，支持多种通信模式和传输协议。ZeroMQ的设计目标是提供简单的消息传递机制，适用于轻量级的异步通信和快速构建分布式系统的场景。
6. Pulsar：是一个开源的分布式消息和流处理平台，由Apache孵化器支持。Pulsar提供了高可伸缩性、多租户、低延迟等特性，并支持云原生架构，适用于大规模的数据处理和实时分析场景。
7. Apache InLong (Tube)：前身为Apache Tube，是一个开源的分布式消息和流式数据传输平台。它提供了灵活的消息传递和数据传输方式，并集成了大数据处理能力，适用于实时分析、数据集成等多样化场景。

#### 2.8.1 不同消息队列对比
主要是参考以下几个技术指标进行选择对应的消息队列来开发

1.  **吞吐量**：IO、并发。（表示单位时间内完成的操作次数、传输的数据量或处理的任务数量） 
2.  **时效性**：类以延迟，消息的发送、到达时间 
3.  **可用性**：系统可用的比率（比如1年365天宕机1s，可用率大概X个9） 
> 来自鱼聪明：
> 可用性的计算公式是：可用时间 / (总时间 - 计划停机时间)。
> 总时间 = 365 _ 24 _ 60 * 60 = 31,536,000 秒。
> 计划停机时间 = 1 秒 * 365 天 = 365 秒。
> 可用时间 = 总时间 - 计划停机时间 = 31,536,000 - 365 = 31,535,635 秒。
> 可用性 = 31,535,635 / 31,536,000 ≈ 0.999988 ≈ 99.9988%。
> 所以，该系统的可用性为约99.9988%。

 

4.  可靠性：消息不丢失（比如不丢失订单）、功能正常完成 
> - 吞吐量（Throughput）指的是系统在**单位时间内能够处理的请求或传输的数据量**。在消息队列中，吞吐量表示系统能够处理的消息数量或传输的数据量。较高的吞吐量意味着系统能够更快地处理请求或传输数据。
> - 时效性（Latency）指的是**从发出请求到收到响应所经历的时间间隔**。在消息队列中，时效性表示从消息被发送到消息被接收和处理的时间。较低的时效性意味着消息能够更快地被传输、接收和处理。

| 消息队列 | 吞吐量（QPS） | 时效性 | 可用性 | 可靠性 | 优势 | 缺点 | 应用场景 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| ActiveMQ | 中等：万级 | 高 | 高 | 中等 | 成熟的JMS支持、简单易学 | 性能相对较低 | 中小型企业应用，可靠的消息传递和事务 |
| RabbitMQ | 中等：万级 | 极高（微秒单位） | 高 | 高 | 灵活的路由策略、生态好、时效性高、易学 | 对大吞吐量的支持相对较弱 | 复杂的消息路由和灵活的消息处理、适用于大部分的分布式系统 |
| Kafka | **高：十万级** | 高（毫秒以内） | 极高 | 极高 | 高吞吐量和低延迟、可靠性、强大的数据流处理能力 | 复杂性较高，初学者上手难度大 | 大数据流处理，日志收集，实时数据流传输、事件流收集传输等 |
| RocketMQ | 高：十万级 | 高（毫秒） | 极高 | 极高 | 可靠性、可用性、吞吐量大、分布式事务支持 | 对复杂性的支持相对不足 | 大规模的分布式应用，高可用性和事务一致性要求
适用于**金融**、电商等对可靠性要求较高的场景，适合大规模的消息处理 |
| ZeroMQ | 中等：万级 | 高 | 高 | 低 | 简单的消息传递机制 | 缺乏对复杂消息路由的支持 | 轻量级异步通信，构建快速分布式系统 |
| Pulsar | 高：十万级 | 高（毫秒） | 极高 | 极高 | 可靠性、可用性很高、基于发布订阅模型、新兴技术结构、云原生架构支持 | 部署和运维相对复杂 | 适用大规模数据处理，实时分析，高并发的分布式系统。适合实时分析、事件流处理、IoT数据处理等。 |
| Apache InLong (Tube) | 高：十万级 | 高 | 高 | 高 | 数据传输和大数据处理集成 | 社区生态相对较小 | 实时分析，数据集成 |

## 3、RabbitMQ 入门实践
[空降官网](https://www.rabbitmq.com/)
特点：生态好，好学习、易于理解，时效性强，支特很多不同语言的客户端，扩展性、可用性都很不错。
学习性价比非常高的消息队列，适用于绝大多数中小规模分布式系统。
RabbitMQ的⽣态优秀，益于学习和理解，且具有良好的实效性，同时它⽀持多种语⾔的客户端，包括Java Python等，这使得rabbit mq具有出⾊的扩展性和可⽤性，因此它是⼀种学习成本⾼，性价⽐⾼的消息队列，适合⼤多数中⼩规模的分布式系统，这就是我们学习rabbit mq的主要原因。
### 3.1 基本消息
AMQP协议（高消息队列协议：Advanced Message Queuing Protocol）：
AMQP的全称是Advanced Message Queue Protocol,即⾼级消息队列协议。 
RabbitMQ就是根据这个协议开发的，AMQP是⼀个标准的协议，不仅RabbitMQ ，如果你想⾃⼰实现⼀个消息队列也得按照这个协议来设计。
[https://www.rabbitmq.com/tutorials/amqp-concepts.html](https://www.rabbitmq.com/tutorials/amqp-concepts.html)
![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/hello-world-example-routing.png#id=YAtHG&originHeight=328&originWidth=700&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)

- 生产者(Publisher)：发消息到某个交换机
- 消费者(Consumer)：从某个队列中取消息
- 交换机(Exchange)：像路由器⼀样，负责把消息转发到对应的队列
- 队列(Queue)：存储消息的
- 路由(Routes)：转发，就是怎么把消息从一个地方转到另一个地方（比如从生产者转发到某个队列）
### 3.2 安装 RabbitMQ
参考：[https://blog.csdn.net/m0_37779600/article/details/136380261](https://blog.csdn.net/m0_37779600/article/details/136380261)
改密码：[https://blog.csdn.net/yuanjinshenglife/article/details/137519496](https://blog.csdn.net/yuanjinshenglife/article/details/137519496)
#### 3.2.1 Windows安装
Windows官网安装教程：[https://www.rabbitmq.com/install-windows.html](https://www.rabbitmq.com/install-windows.html)
安装Erlang 25.3.2 ：因为Rabbit MQ 依赖于erlang ，它的性能非常高
Erlang 下载：[https://www.erlang.org/patches/otp-25.3.2](https://www.erlang.org/patches/otp-25.3.2)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723699668346-e2fd9822-4ce9-4862-b234-3d9e084a4707.png#averageHue=%23d1c7bc&clientId=u4ca622be-41e5-4&from=paste&height=411&id=u5595eb04&originHeight=493&originWidth=647&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=111532&status=done&style=none&taskId=u89a08a87-12d5-40ad-8d61-288eee953a9&title=&width=539.1666452421091)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723699687608-2cd8a6fa-f34b-47d8-8ade-200dac0e3311.png#averageHue=%23faf7f6&clientId=u4ca622be-41e5-4&from=paste&height=612&id=u3f1da8a5&originHeight=735&originWidth=677&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=225729&status=done&style=none&taskId=ud6fdb30d-e1c7-4066-afa8-ae24df08a9f&title=&width=564.1666442486984)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723699911455-50bc8c5e-c32f-4f8f-ae39-199ba15cafd1.png#averageHue=%23f7f6f5&clientId=u4ca622be-41e5-4&from=paste&height=252&id=u398e31b0&originHeight=303&originWidth=545&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=71388&status=done&style=none&taskId=u366068f5-7799-4706-906b-981210cba8d&title=&width=454.1666486197055)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723699961976-318d8468-2efa-4358-9b18-33b9994ddb31.png#averageHue=%23f2f0ef&clientId=u4ca622be-41e5-4&from=paste&height=990&id=ucb83a2d7&originHeight=1188&originWidth=2163&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=279809&status=done&style=none&taskId=ud85bc8bb-805f-4c73-9507-44f094b2df2&title=&width=1802.499928375088)
接下来还需要用到一个东西，大家如果说以后安装一些第三方的这种中间件，基本上都会有的。 就是肯定是这种中间件会有一个管理页面。回到官网，点击 Docs (文档)。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723700009272-660b8bbd-d462-4d16-81a8-00b8fb7c06f7.png#averageHue=%23e7e7e5&clientId=u4ca622be-41e5-4&from=paste&height=191&id=u3ac3abc0&originHeight=229&originWidth=492&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=59894&status=done&style=none&taskId=ub98b5fd0-d9af-4832-8b99-7bb32c1c02f&title=&width=409.9999837080644)
Windows 安装 Rabbit MQ 监控面板，执行下面的命令：
由于是 windows 系统，这个命令不能直接运行； 
windows 系统所有的脚本全是 bat 文件，所以要给这条命令加上 .bat，然后复制这条命令。
```
rabbitmq-plugins.bat enable rabbitmq_management
```
找到安装 RabbitMQ 路径的位置，进入 sbin目录 ，这里面都是 RabbitMQ 的执行脚本。 

- rabbitmq-server.bat：操作 RabbitMQ 服务器相关的命令。 
- rabbitmq-plugins.bat：用来安装 RabbitMQ 的插件。

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723702154953-b79fd518-2d8c-4559-bc2a-aba8e03a3edf.png#averageHue=%23131313&clientId=u4ca622be-41e5-4&from=paste&height=1018&id=ud3609e23&originHeight=1222&originWidth=2346&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=73866&status=done&style=none&taskId=ua7817d85-eb82-45f3-b46f-d5653c9b55e&title=&width=1954.9999223152827)
这里显示要重新启动后才生效，输入以下命令。
管理员身份运行cmd
```java
net stop rabbitmq
net start rabbitmq
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723702480508-4ff28f9c-864d-43cd-b0f9-b824ee083139.png#averageHue=%23100f0f&clientId=u4ca622be-41e5-4&from=paste&height=707&id=ueb78cfe6&originHeight=849&originWidth=1828&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=93438&status=done&style=none&taskId=ufb1c869c-a6b7-40af-8590-3888de29ef9&title=&width=1523.3332728015075)
Rabbit MQ 端口：

1. 5672：程序链接的端口
2. 15672：WebUI
> 文档还有说明端口访问的端口号，15672 是监控面板的 UI；
> ps.里面的 5672、5671 很重要，我们的应用程序之间就是通过 5672 端口来和 RabbitMQ 做链接的，就像 redis 的6379，es 的 9200是一样的。

访问：
[http://localhost:15672/](http://localhost:15672/)
账号 guest 
密码 guest
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723702687651-a704c45f-4e80-482b-af73-98aba42f722c.png#averageHue=%23fdfcfb&clientId=u4ca622be-41e5-4&from=paste&height=258&id=ub9bbd484&originHeight=310&originWidth=1338&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=18050&status=done&style=none&taskId=uc977703d-6415-4cd5-b8c0-6a9048849be&title=&width=1114.9999556938824)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723702754853-58df9223-c1b4-446b-8892-2b9281479cfc.png#averageHue=%23f9f9f2&clientId=u4ca622be-41e5-4&from=paste&height=822&id=u06a7cf99&originHeight=986&originWidth=1902&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=103778&status=done&style=none&taskId=u33e41201-404a-448d-8859-85aeb3bd51e&title=&width=1584.999937017761)
**注意：**如果你打算在自己的服务器上部署 RabbitMQ，你必须知道在默认设置下，使用 guest 账号是无法访问的。为何会这样呢？原因在于系统安全性的需求。如果你想从远程服务器访问管理面板，你需要创建一个新的管理员账号，不能使用默认的 guest 账号，否则会被系统拦截，导致无法访问或登录。 
点击右侧的 User guest，会发现该用户拥有管理员权限，但这仅限于本地访问。如果你直接上线并使用默认的管理员权限，你可以想象可能出现的问题。比如，攻击者可能会扫描你服务器的端口，并不断尝试用 guest 账号登录。一旦找到一个开放的端口并用 guest 登录，攻击者就可以入侵你的服务器，然后在消息队列中不断添加消息，填满你的服务器硬盘。因此，为了安全，系统默认关闭了 guest 账号的远程访问权限，这也是官方出于安全考虑的措施。关于如何创建管理员账号，不在这里演示，可以参考官方文档进行操作：
[https://www.rabbitmq.com/docs/access-control](https://www.rabbitmq.com/docs/access-control)
#### 3.2.2 Linux 安装
安装Docker
```
sudo yum install docker-ce
```
安装完Docker后，启动Docker服务：
```
sudo systemctl start docker
```
通过 docke r拉取 rabbitmq 镜像：
```
sudo docker pull rabbitmq:版本号
docker pull rabbitmq:3.9-management
```
请将 "版本号" 替换为您想要的RabbitMQ版本号。如果不指定版本号，则会拉取最新的稳定版本。
完成镜像拉取后，可以使用以下命令启动RabbitMQ容器：
```
sudo docker run -d --name myrabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:版本号
sudo docker run -d --name myrabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.9-management
```
同样，请将"版本号"替换为您之前选择的RabbitMQ版本号。
容器启动后，可以使用以下命令检查容器是否正在运行：
```
sudo docker ps
```
如果能看到刚刚启动的RabbitMQ容器正在运行，那么恭喜您已成功在CentOS上使用Docker安装了RabbitMQ。
还在将服务器的1562、5672端口开放，
最后，通过浏览器访问 `http:// 服务器IP地址:15672` 来打开RabbitMQ的WebUI管理界面，默认的用户名和密码是"guest"。可以进行用户设置、队列管理等操作。
Linux下的RabbitMQ新增一个用户，分别执行以下的命令：
```bash
# 查看rabbitmq 运行的容器id
dcker ps
## 进入正在运行的 RabbitMQ 容器的 Shell。执行以下命令（将 <container_id> 替换为实际的容器 ID）：
docker exec -it <container_id> /bin/bash
# 添加新用户
rabbitmqctl add_user 用户名 密码
# 管理员权限
rabbitmqctl set_user_tags 上面的用户名 administrator 
# 列出用户列表
rabbitmqctl list_users
# 目录权限
# 第一个".*" 用于在每个实体上配置权限
# 第二个".*" 表示对每个实体的写入权限
# 第三个".*” 对每个实体的读取权限
rabbitmqctl set_permissions -p "/" "用户名" ".*" ".*" ".*"
```

### 3.3 快速入门
现在我们的 RabbitMQ 已经算启动了，去官网点击 RabbitMQ Tutorials
[https://www.rabbitmq.com/tutorials](https://www.rabbitmq.com/tutorials)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723706297781-722c1c0f-c767-4cda-8c10-7f3f55f6075f.png#averageHue=%23faf8f7&clientId=u4ca622be-41e5-4&from=paste&height=1337&id=uced6f59c&originHeight=1605&originWidth=2849&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=476469&status=done&style=none&taskId=ub3d01f0e-8dce-479a-8965-20f8e300617&title=&width=2374.1665723257634)
#### 3.3.1 单向发送
> 一个生产者给一个队列发送消息，一个消费者从这个队列中获取到消息。1 对 1 关系

![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230617172656480.png#id=APDHM&originHeight=225&originWidth=978&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
引入Rabbit MQ 组件依赖
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723706381465-261a4d3e-5123-4292-bbe9-49c67250ab72.png#averageHue=%23fdfcfb&clientId=u4ca622be-41e5-4&from=paste&height=1305&id=ucd2108f3&originHeight=1566&originWidth=2815&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=439509&status=done&style=none&taskId=u77f719ca-2863-4a1b-bc7d-191b7ad56ea&title=&width=2345.833240118295)
这里直接使用最新版本的。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723706435408-2e6ccc5f-47bf-4c03-9f0f-f91c83bc24da.png#averageHue=%23f9f9f8&clientId=u4ca622be-41e5-4&from=paste&height=657&id=ue34d74e8&originHeight=789&originWidth=1496&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=102918&status=done&style=none&taskId=ub1c56bb5-0bba-461f-ac8a-d6da387d216&title=&width=1246.666617128586)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723706492942-a5a6fc48-e601-4580-916b-e7d84a3e9d6b.png#averageHue=%23faf9f9&clientId=u4ca622be-41e5-4&from=paste&height=1335&id=u8a88cf54&originHeight=1602&originWidth=2838&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=380082&status=done&style=none&taskId=u9157f758-52ce-4561-9866-21939625bc3&title=&width=2364.999906023347)
```xml
<!-- https://mvnrepository.com/artifact/com.rabbitmq/amqp-client -->
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.16.0</version>
</dependency>

<!-- https://mvnrepository.com/artifact/com.rabbitmq/amqp-client -->
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.21.0</version>
</dependency>
```
回到官方文档，往下滑： 
它说，首先我们需要定义一个消息发送者负责向队列发送消息。
> 问：刚刚我们启动的是什么？ 答：一个 RabbitMQ 的服务，这就是我们的架构模型。 问：但是这个消息生产者、消息发送者以及消息消费者都是什么呢？ 答：是我们自己的应用程序，我们自己的系统。

因此，我们现在需要做的就是开发消息生产者和消费者。他展示了生产者的代码，并且告诉我们如何使用以下的代码来发送消息。不过，你不必关注这些碎片化的代码。我们可以直接看这份完整的代码，并将其完整复制下来，实际上这只是一个现成的 demo，非常简单。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723707704461-22395c90-10e5-49c3-a867-c0be6012c9e6.png#averageHue=%23debd9c&clientId=u4ca622be-41e5-4&from=paste&height=1374&id=u2f1e4509&originHeight=1649&originWidth=2813&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=449238&status=done&style=none&taskId=uc221c45e-7010-4f98-8547-330cbd572b8&title=&width=2344.1665735178562)
在后端新建 mq包 ，选中这个包
```java
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
```
改名为生产者代码
修改类名为 SingleProducer ，选中类名，并按[Shift+F6]重构
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-15 15:43
 **/
public class SingleProducer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        // 创建链接工场
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
//        factory.setUsername("xxxx");
//        factory.setPassword("xxxx");
//        factory.setHost("xxxx");
        try (Connection connection = factory.newConnection();
             // 建立链接，常见频道
             Channel channel = connection.createChannel()) {
            // 创建消息队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
```
Channel 频道：理解为操作消息队列的client(比如 JDBCClient、redisClient)，提供了和消息队列server建立通信的传输方法（为了复用连接，提高传输效率）。程序通过channel操作 rabbitmq (收发消息)
> 你可以将频道看作客户端。你可能已经接触过其他类型的客户端，如 JDBC（用于连接数据库） 和 Redis Client（用于操作缓存）。在这种情况下，你可以将频道看作是用于操作消息队列的客 户端。 
> 频道(Channel)提供了一种与 RabbitMQ 服务器通信的方法，它可以帮助我们建立与 
> RabbitMQ 服务器的连接。如果你之前学习过 Netty，对 Channel 的概念可能更为熟悉。 
> RabbitMQ 保留 Channel 的原因在于它可以帮助我们实现连接复用，从而提高传输效率。所有 的任务都交给 Channel 去统一处理。

```java
Queue.DeclareOk queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,
                             Map<String, Object> arguments) throws IOException;
```
创建消息队列的参数说明：

-  queueName：消息队列名称（注意，同名称的消息队列，只能用同样的参数创建一次） 
-  durabale：消息队列重启后，消息是否丢失，持久化处理 
-  exclusive：是否**只允许当前这个创建消息队列**的连接操作消息队列 
-  autoDelete：没有人用队列后，是否要删除队列 

打印出了 System.out.println 内的内容。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723741190424-f0bd0b45-b43b-4e5d-aef2-eba8422c08aa.png#averageHue=%232b303a&clientId=u4ca622be-41e5-4&from=paste&height=421&id=u0f76bc50&originHeight=505&originWidth=2579&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=80370&status=done&style=none&taskId=u51cfc1c7-269b-4cf8-adb8-b22d42e0e27&title=&width=2149.1665812664596)
访问:
[http://localhost:15672/#/queues](http://localhost:15672/#/queues)
默认用户名密码都是 
guest，点击 Login 。
点击 Queues and streams，可以查看到一个名叫 hello 的队列
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723741254734-1d9f87bd-c735-4704-9ba7-0f2d0ef3b92c.png#averageHue=%23f8f8f0&clientId=u4ca622be-41e5-4&from=paste&height=1347&id=ua6af075c&originHeight=1617&originWidth=2829&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=259344&status=done&style=none&taskId=uff75f553-c066-4dcc-9376-790bed158e6&title=&width=2357.4999063213704)
消息发送好之后，接下来要去消费这个消息，继续看官方文档，查看完整版的消费者代码。
消费者代码：
```java
package com.yupi.springbootinit.mq;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;
/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-16 01:05
 **/

public class SingleConsumer {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}
```
 **为什么在这里都要创建队列呢？ **
主要是为了确保该队列的存在，否则在后续的操作中可能会出现错误。主要是为了这点，即便你的队列原本并不存在，此语句也能够帮你创建一个新的队列。但是需要特别注意一点，如果你的队列已经存在，并且你想再次执行声明队列的操作，那么所有的参数必须与之前的设置完全一致。这是因为一旦一个队列已经被创建，就不能再创建一个与其参数不一致的同名队列。可以类 比为，一旦你建好了一个快递站，就不能再在同一位置建立一个与之不同的快递站。 
**注意：**请确保消费队列的名称与发送消息的队列名称保持一致。所以在这里，我们统一使 
用"hello"作为队列名。
现在执行之后能立刻消费一个消息，因为之前已经发了一个消息了。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723741741761-07ff89ad-054c-45e3-97bb-94c9010cac9d.png#averageHue=%23292e38&clientId=u4ca622be-41e5-4&from=paste&height=474&id=u72a8ffad&originHeight=569&originWidth=2406&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=59594&status=done&style=none&taskId=u5c674d9e-08e2-42c0-933c-18139778cb8&title=&width=2004.9999203284613)
这时候再看管理页面，变成 0 了，已经消费完了。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723741785054-bebe7f56-c31d-457a-94cf-2b24b2ea6745.png#averageHue=%23f8f7f0&clientId=u4ca622be-41e5-4&from=paste&height=1353&id=u37178f45&originHeight=1624&originWidth=2847&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=276106&status=done&style=none&taskId=u6820d298-087b-491b-ac37-2d51a5e3942&title=&width=2372.499905725324)
点进来。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723741816096-2ff65c5a-3b8e-49a6-9811-41f822979d80.png#averageHue=%23f7f7f6&clientId=u4ca622be-41e5-4&from=paste&height=1139&id=uf5f1ef6a&originHeight=1367&originWidth=2807&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=199339&status=done&style=none&taskId=u025dbcae-c261-40ae-ae19-d6479696adb&title=&width=2339.166573716538)
是不是历史消费过一条消息，现在有一个消费者。
回到管理页面，会看到消息被消费完了(太快了)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723741921227-a65c6355-fc62-46ff-a46e-8e6730bc1eb3.png#averageHue=%23f9f8f8&clientId=u4ca622be-41e5-4&from=paste&height=1211&id=u1bc9a211&originHeight=1453&originWidth=2799&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=227519&status=done&style=none&taskId=u205a8868-ce59-4c99-99bf-3a76bb72ae1&title=&width=2332.499907314781)

#### 3.3.2 多消费者 （使用较少）
> 场景：多个机器同时去接受并处理任务（尤其是每个机器的处理能力有限）
> 一个生产者给一个队列发消息，多个消费者从这个队列取消息。1 对 多关系。
> - 这有点像发布-订阅模型，适用于什么场景呢？假设我们有一个消费者，由于其性能有限，可能无法处理所有任务。此时，我们可以增加机器来提高处理能力。假设一个生产者在不断地生成任务，但一个消费者无法全部处理，那么我们可以增加一个消费者来共同完成这些任 务。因此，这种场景特别适合于多个机器同时接收并处理任务，尤其是在每个机器的处理能力有限的情况下。
> - 有同学提出，一个UP主发布的视频被百万粉丝观看，这个感觉像是发布-订阅模型。但实际上，这个场景与我们现在讨论的并不完全相同。

![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230617173154369.png#id=RdCL4&originHeight=288&originWidth=856&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
继续向下浏览，这里有一个轮询分配算法。就是说如果生产者连续向消息队列中发送了两条消息，那么这些消息将会按照轮询的方式分配给消费者。即，第一条消息会被第一个消费者取走， 第二条消息则会被第二个消费者取走。这样的分配方式使得任务分工更为合理。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723742988173-11afbbc0-84f7-461d-b442-78d237c378fe.png#averageHue=%23d59f6b&clientId=u4ca622be-41e5-4&from=paste&height=1332&id=u4f2c483a&originHeight=1599&originWidth=2819&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=428718&status=done&style=none&taskId=uf80ec1b1-2b39-426a-94de-b8824d5dcd8&title=&width=2349.166573319174)
再往下滑，这里有一个叫消息确认机制，挺重要的(下期讲)。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723743015001-b41b8dc8-0a7c-43e1-97c9-4158f06a8bb2.png#averageHue=%23fdfcfb&clientId=u4ca622be-41e5-4&from=paste&height=1317&id=uc41af95b&originHeight=1581&originWidth=2871&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=532292&status=done&style=none&taskId=u4414d6f6-39ca-4776-9b20-6652411bcd3&title=&width=2392.499904930595)
往下滑，这里有一个叫消息持久化(下期讲)。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723743035846-24bd7442-8105-4470-b004-fdbd6eee1b7d.png#averageHue=%23fdfcfb&clientId=u4ca622be-41e5-4&from=paste&height=1322&id=ue7e548ca&originHeight=1586&originWidth=2841&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=500537&status=done&style=none&taskId=uc68a3d09-abeb-4363-a46b-d58b25f97df&title=&width=2367.4999059240063)
往下滑，这里有一个叫公平分配，就是刚刚说的一人分一条。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723743054671-e9f7d44a-5703-4005-840e-35ab268a6b44.png#averageHue=%23fdfcfb&clientId=u4ca622be-41e5-4&from=paste&height=1338&id=ub7daa974&originHeight=1606&originWidth=2868&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=516657&status=done&style=none&taskId=u3b2479a6-a4ac-4c87-9885-e24ede220ed&title=&width=2389.9999050299366)
往下滑，这个很重要，也是我们需要的，叫 Putting it all together(把所有的代码全放在一起)； 复制这段代码。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723743087553-592d6a95-2540-4018-bce5-8fd8e4200b47.png#averageHue=%2378776a&clientId=u4ca622be-41e5-4&from=paste&height=1327&id=u12420af8&originHeight=1592&originWidth=2874&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=479102&status=done&style=none&taskId=ua12e4d84-86a6-429c-9fc6-3efbed0d5d2&title=&width=2394.9999048312543)
##### 1）队列持久化
第二个参数：durable 参数设置为 true ，服务器重启后队列不丢失
```java
channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
```

##### 2）消息持久化
将 basicPublish 中的第二个参数指定为 MessageProperties.PERSISTENT_TEXT_PLAIN ：
```java
channel.basicPublish("", TASK_QUEUE_NAME,
       MessageProperties.PERSISTENT_TEXT_PLAIN,
       message.getBytes("UTF-8"));
```

##### 3）生产者代码：
使用 Scanner 接受用户输入，便于发送多条消息
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

public class MultiProducer {

  private static final String TASK_QUEUE_NAME = "multi_queue";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {
        // 创建队列
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.nextLine();
            channel.basicPublish("", TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
  }

}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723743369321-bbf92fa2-2565-4b20-bf59-759ad9c5dcc6.png#averageHue=%23282d35&clientId=u4ca622be-41e5-4&from=paste&height=1284&id=u07d396fd&originHeight=1541&originWidth=2810&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=319274&status=done&style=none&taskId=u8d82dd0d-48e2-4326-ac60-afc094cbf94&title=&width=2341.666573617197)
total总数为4
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723743401203-6469b776-0d27-4d59-9d9a-dce72277a7a6.png#averageHue=%23f8f7ef&clientId=u4ca622be-41e5-4&from=paste&height=1292&id=u4e281ec2&originHeight=1551&originWidth=2860&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=271715&status=done&style=none&taskId=u2c9975dd-598e-4959-b02d-fd8477482d1&title=&width=2383.333238628179)
如果消息不小心刷多了，怎么删掉这个队列？ 
###### 1. 在 RabbitMQ 管理界面中删除队列(推荐) 
在 RabbitMQ 的管理界面，点击"Queues"选项，找到你想要删除的队列，点击队列名称进入详情页面。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723744092863-50652d53-7501-4105-9986-cd9142ac228d.png#averageHue=%23f5f5f5&clientId=u4ca622be-41e5-4&from=paste&height=362&id=ua5cecd39&originHeight=435&originWidth=2305&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=59771&status=done&style=none&taskId=u623abfa1-e5ff-4795-89f3-66b646ea7cd&title=&width=1920.8332570062773)
在详情页面下方，点击"Delete"按钮，确认删除操作即可。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723744123588-7d963dd9-3a7a-4d08-ad3b-8d53877143ec.png#averageHue=%23f9f8f2&clientId=u4ca622be-41e5-4&from=paste&height=1247&id=u12e5c28c&originHeight=1496&originWidth=2880&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=203754&status=done&style=none&taskId=ud1ecae57-5117-418d-8044-cfb39502495&title=&width=2399.999904632572)
###### 2. 在代码中删除队列 
在 RabbitMQ 的客户端库中(如 Java 的 RabbitMQ 客户端库)，通常提供了删除队列的函数。在 Java 的RabbitMQ 客户端库中，你可以使用 Channel 的 queueDelete 方法删除队列。 如：
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723744447055-611ac273-fda9-4a68-9f5b-ad7c91711a6b.png#averageHue=%23292e37&clientId=u4ca622be-41e5-4&from=paste&height=527&id=u8c7d9232&originHeight=633&originWidth=2419&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=68009&status=done&style=none&taskId=udedbc8ab-c149-4fe7-8702-640ea2b3ff5&title=&width=2015.8332532313166)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723744424850-284e08ad-2baa-475c-85bf-a51f5c4fd72b.png#averageHue=%23f8f7f7&clientId=u4ca622be-41e5-4&from=paste&height=1247&id=u33521bad&originHeight=1496&originWidth=2880&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=215265&status=done&style=none&taskId=ubc54f060-97bb-4ed4-b4b1-b96832afb7c&title=&width=2399.999904632572)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723744474006-39790abd-fe6f-440d-953b-f277d5107e64.png#averageHue=%23f9f9f8&clientId=u4ca622be-41e5-4&from=paste&height=90&id=u7b29c4bd&originHeight=108&originWidth=625&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=18742&status=done&style=none&taskId=u9522a48a-859a-4afb-893e-7933f593cb9&title=&width=520.8333126372769)
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;
/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-16 01:50
 **/

public class delete {
    private static final String TASK_QUEUE_NAME = "multi_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             // 获取RabbitMQ的Channel
             Channel channel = connection.createChannel()) {
            // 需要删除的队列名
            channel.queueDelete(TASK_QUEUE_NAME);
        }
    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723744508925-f238e0d2-eb4a-4ab9-aa23-e516623d16f5.png#averageHue=%23f8f8f7&clientId=u4ca622be-41e5-4&from=paste&height=1247&id=uf9802c79&originHeight=1496&originWidth=2880&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=201353&status=done&style=none&taskId=uad6c6193-0fe8-413b-8fdb-627527c0e64&title=&width=2399.999904632572)
##### 4）消费者代码：
```java
/**
 * @author Shier
 */
public class MultiConsumer {

    private static final String TASK_QUEUE_NAME = "multi_queue";

    public static void main(String[] argv) throws Exception {
        // 建立链接
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 rabbitmq 对应的信息
        factory.setHost("xxxxx.xxxx.xxxx");
        factory.setUsername("xxxx.xxxx.xxx");
        factory.setPassword("xxx.xxx.xxx");
        final Connection connection = factory.newConnection();

        for (int i = 0; i < 2; i++) {
            // 创建两个消费者频道
            final Channel channel = connection.createChannel();
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            // 控制当个消费者的积压数
            channel.basicQos(1);

            int finalI = i;
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                String message = new String(delivery.getBody(), "UTF-8");
                try {
                    // 处理工作
                    System.out.println(" [x] Received '消费者：" + finalI + "，消费了：" + message + "'");
                    // 指定取某条消息
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                    // 停20秒 模拟机器处理能力有限
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.getStackTrace();
                    // 第三个参数
                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
                } finally {
                    System.out.println(" [x] Done");
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };
            // 开启消费监听，会一直监听生产者的消息
            channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });
        }
    }
}
```
控制单个消费者的处理任务积压数：每个消费者最多同时处理1个任务
```java
channel.basicQos(1);
```
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class MultiConsumer {

  private static final String TASK_QUEUE_NAME = "multi_queue";

  public static void main(String[] argv) throws Exception {
      // 建立链接
    ConnectionFactory factory = new ConnectionFactory();
      // 设置 rabbitmq 对应的信息
    factory.setHost("localhost");
    final Connection connection = factory.newConnection();
    final Channel channel = connection.createChannel();

    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    channel.basicQos(1);

    // 定义如何处理消息
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");

//        System.out.println(" [x] Received '" + message + "'");
        try {
            // 自己定义处理的工作
            System.out.println(" [x] Received '" + message + "'");
//            doWork(message);
            // 停20s，模拟机器处理能力
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(" [x] Done");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    };
    channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> { });
  }

//  private static void doWork(String task) {
//    for (char ch : task.toCharArray()) {
//        if (ch == '.') {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException _ignored) {
//                Thread.currentThread().interrupt();
//            }
//        }
//    }
//  }
}
```
结果：
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723744841115-cff11f64-4e39-436a-81b0-fb2dfb05a263.png#averageHue=%232a2f39&clientId=u4ca622be-41e5-4&from=paste&height=518&id=uaaebf284&originHeight=622&originWidth=2649&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=92551&status=done&style=none&taskId=ua070aeff-c759-43d1-a20b-01670aa7003&title=&width=2207.4999122818344)
完整代码，然后写个 for 循环，把从连接获取一个新的通道的代码都放进去
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class MultiConsumer {

    private static final String TASK_QUEUE_NAME = "multi_queue";

    public static void main(String[] argv) throws Exception {
        // 建立链接
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 rabbitmq 对应的信息
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();

        for (int i = 0; i < 2; i++) {
            //创建两个消费者频道
            final Channel channel = connection.createChannel();

            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            // 控制当个消费者的积压数
            channel.basicQos(1);

            int finalI = i;
            // 定义如何处理消息
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                try {
                    // 自己定义处理的工作
                    System.out.println(" [x] Received '消费者：" + finalI + "，消费了：" + message + "'");
                    // 指定取某条消息
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                    // 停20s，模拟机器处理能力
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.getStackTrace();
                    // 第三个参数
                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
                } finally {
                    System.out.println(" [x] Done");
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };
            // 开启消费监听，会一直监听生产者的消息
            channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });
        }
    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723745294013-2ae82105-b955-4587-8d05-68bcc4379c85.png#averageHue=%23282c34&clientId=u4ca622be-41e5-4&from=paste&height=292&id=u84e8c91a&originHeight=350&originWidth=1599&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=17600&status=done&style=none&taskId=u730795da-69c9-4de2-bf38-13c28eb780c&title=&width=1332.4999470512093)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723745366370-a00f88da-ba7d-4a46-a0af-3672ebbb5aee.png#averageHue=%232a2e37&clientId=u4ca622be-41e5-4&from=paste&height=399&id=uf3cd81d7&originHeight=479&originWidth=2371&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=84414&status=done&style=none&taskId=u0ad41d3c-1af0-465b-951c-d93c4f1105c&title=&width=1975.8332548207738)
##### **消息确认机制** ⭐⭐⭐⭐⭐（面试爱问）
这里有个很有意思的地方，叫 autoack，默认为 false —— 消息确认机制。
```java
channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {});
// basicConsume()方法源码：
String basicConsume(String queue, boolean autoAck, DeliverCallback deliverCallback, CancelCallback cancelCallback) throws IOException;
```
> 消息队列如何确保消费者已经成功取出消息呢？它依赖一个称为消息确认的机制。当消费者从队列中取走消息后，必须对此进行确认。这就像在收到快递后确认收货一样，这样消息队列才能知道消费者已经成功取走了消息，并能安心地停止传输。因此，整个过程就像这样。 
> 在面试中，如果被问及如何保证消息不会丢失，例如当业务流程失败时该怎么办，你可以回答： “我们可以选择拒绝接收失败的消息，并重新启动。重新启动或进行其他处理可以指定拒绝某条消息。

为了保证消息成功被消费（快递成功被取走），rabbitmg提供了消息确认机制，当消费者接收到消息后，比如要给一个反馈：

- ack：消费成功
- nack：消费失败
- reject：拒绝
> 如果告诉 Rabbit MQ 服务器消费成功，服务器才会放心地移除消息。
>  支持配置autoack，会自动执行 ack 命令，接收到消息立刻就成功了。
>  如果在接收到消息后，工作尚未完成，我们是否就不需要确认成功呢？这种情况，建议将 
> autoack 设置为 false，根据实际情况，去手动确认。

###### 指定确认某条消息：
```java
channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
// 源码
void basicAck(long deliveryTag, boolean multiple) throws IOException;
```
> 参数解释：
> 1. `deliveryTag`：表示要确认的消息的标识符。每个消息都有一个唯一的`deliveryTag`，用于标识消息的顺序。
> 2. `multiple`：表示是否批量确认消息。如果设置为`true`，则表示确认所有在`deliveryTag`之前的未确认消息；如果设置为`false`，则只确认当前`deliveryTag`的消息。
> 
第二参数 multiple 批量确认：是指是否要一次性确认所有的历史消息直到当前这条


###### 指定拒绝某条消息：
```java
channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
// 源码
void basicNack(long deliveryTag, boolean multiple, boolean requeue)
            throws IOException;
```
> 参数解释：
> 1. `deliveryTag`：表示要否定确认的消息的标识符。每个消息都有一个唯一的`deliveryTag`，用于标识消息的顺序。
> 2. `multiple`：表示是否批量否定确认消息。如果设置为`true`，则表示否定所有在`deliveryTag`之前的未确认消息；如果设置为`false`，则只否定当前`deliveryTag`的消息。
> 3. `requeue`：表示是否将消息重新放回队列。如果设置为`true`，则消息将被重新放回队列并可以被其他消费者重新消费；如果设置为`false`，则消息将会被丢弃。
> 
第三个参数表示是否重新入队，可用于重试

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723746178441-5b7c8ca4-ed11-4053-b1cc-6c5898153f17.png#averageHue=%23fafaf9&clientId=u4ca622be-41e5-4&from=paste&height=419&id=ua264eb10&originHeight=503&originWidth=580&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=128778&status=done&style=none&taskId=u6c468120-7688-467c-a1fd-25040fc8c00&title=&width=483.333314127393)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723746198595-d0185677-e024-4b72-aa45-8b6f04626bab.png#averageHue=%23f9f7f6&clientId=u4ca622be-41e5-4&from=paste&height=447&id=ud7efe60f&originHeight=536&originWidth=637&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=146838&status=done&style=none&taskId=u04721b42-e0f1-4e04-a658-e91530e913d&title=&width=530.8333122399126)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723746214113-0c1232b6-7130-4b7e-94cb-19a9cd4881a9.png#averageHue=%23faf8f7&clientId=u4ca622be-41e5-4&from=paste&height=422&id=u0b3e4c2e&originHeight=507&originWidth=643&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=109470&status=done&style=none&taskId=u3fd97c8a-8b4a-4612-a766-c85d15dd1d1&title=&width=535.8333120412306)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723746223336-7894b37d-c957-4af4-8c1c-dfd8f3118f7c.png#averageHue=%23fbfaf9&clientId=u4ca622be-41e5-4&from=paste&height=453&id=u4eb38de1&originHeight=544&originWidth=618&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=126466&status=done&style=none&taskId=uf1574d89-878a-4540-8ebb-347a9f83be7&title=&width=514.9999795357394)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723746232957-2456341a-0673-4b6f-9fcd-3cf29968e094.png#averageHue=%23f8f7f6&clientId=u4ca622be-41e5-4&from=paste&height=342&id=u007a2e7b&originHeight=410&originWidth=657&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=130444&status=done&style=none&taskId=u2eec79f1-c64b-4048-80c9-28365b17bc5&title=&width=547.4999782443056)
###### 2个测试小技巧：

1. 使用Scanner接受用户输入，便于快速发送多条消息
2. 使用 for 循环创建多个消费者，便于快速验证队列模型工作机制
### 3.4 Rabbit MQ 交换机（**Publish/Subscribe**）
> [https://www.rabbitmq.com/tutorials/tutorial-three-java](https://www.rabbitmq.com/tutorials/tutorial-three-java)

##### 什么是交换机？ 
**交换机 **是消息队列中的一个组件，其作用类似于网络路由器。它负责将我们发送的消息转发到相应的目标，就像快递站将快递发送到对应的站点，或者网络路由器将网络请求转发到相应的服务器或客户端一样。交换机的主要功能是提供转发消息的能力，根据消息的路由规则将消息投递到合适的队列或绑定的消费者。 我们可以理解为，如果说一个快递站已经承受不了那么多的快递了，就建多个快递站。
一个生产者给多个队列发消息，1个生产者对多个队列。
交换机的作用：提供消息转发功能，类以于网络路由器
要解决的问题：怎么把消息转发到不同的队列上，好让消费者从不同的队列消费。
绑定：交换机和队列关联起来，也可以叫路由，算是一个算法或转发策略
##### 绑定代码方法：
```java
channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, "fanout");
```
![](https://cdn.nlark.com/yuque/0/2023/png/398476/1686839285368-841c3ded-965b-4ed7-8214-091ac7f2c922.png#averageHue=%23ecb9af&id=q3MIG&originHeight=247&originWidth=745&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)

> 后面的就是不同的队列


#### 3.4.1 交换机类型

1. **Fanout Exchange**（广播类型）：Fanout交换机**将消息广播到其绑定的所有队列**。当消息被发送到Fanout交换机时，它会将消息复制到所有绑定的队列上，而不考虑路由键的值。因此，无论消息的路由键是什么，都会被广播到所有队列。Fanout交换机主要用于广播消息给所有的消费者。
2. **Direct Exchange**（直连类型）：Direct交换机是**根据消息的路由键选择将消息路由到与消息具有相同路由键绑定的队列**。例如，当消息的路由键与绑定键完全匹配时，消息将被路由到对应的队列。Direct交换机主要用于一对一的消息路由。
3. **Topic Exchange**（主题类型）：Topic交换机**将消息根据路由键的模式进行匹配，并将消息路由到与消息的路由键匹配的队列**。路由键可以使用通配符匹配，支持两种通配符符号，"#"表示匹配一个或多个单词，"*"表示匹配一个单词。Topic交换机主要用于灵活的消息路由。
4. **Headers Exchange**（头类型）：Headers交换机是**根据消息的头部信息进行匹配，并将消息路由到匹配的队列**。头部信息通常是一组键值对，可以使用各种自定义的标准和非标准的头部信息进行匹配。Headers交换机主要用于复杂的匹配规则。

#### 3.4.2 fanout 交换机
扇出：将消息广播到所有绑定到该交换机的队列的过程。它得名于扇形扩散的形状，类似于 
把一条消息从交换机传播到多个队列，就像扇子展开一样。
广播消息：将消息发送到所有与该交换机绑定的队列的过程。当发布者将消息发送到 fanout 交换机时，交换机会立即将该消息复制并传递给所有绑定的队列，无论目标队列的数量是多 
少。
特点：消息会被转发到所有绑定到该交换机的队列
场景：很适用于发布订阅的场景。比如写日志，可以多个系统间共享
![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/1686839285368-841c3ded-965b-4ed7-8214-091ac7f2c922.png#id=f7932&originHeight=247&originWidth=745&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
实例场景：
![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230619225118563.png#id=jg892&originHeight=388&originWidth=1096&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
生产者代码：
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class FanoutProducer {

  private static final String FANOUT_EXCHANGE_NAME = "fanout-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
      // 设置 rabbitmq 对应的信息
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {
        // 创建交换机
        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, "fanout");
// 发送给所有的队列，所以说不要写队列名称
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.nextLine();
            channel.basicPublish(FANOUT_EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
  }
}
```

消费者代码：

1. 消费者和生产者要绑定同一个交换机
2. 先有队列queueDeclare，才能进行绑定queueBind
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author Shier
 */
public class FanoutConsumer {
    private static final String FANOUT_EXCHANGE_NAME = "fanout-exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 rabbitmq 对应的信息
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel1 = connection.createChannel();
        Channel channel2 = connection.createChannel();

        // 声明交换机
        channel1.exchangeDeclare(FANOUT_EXCHANGE_NAME, "fanout");
        //channel2.exchangeDeclare(FANOUT_EXCHANGE_NAME, "fanout");

        // 员工小红
        String queueName1 = "xiaohong_queue";
        channel1.queueDeclare(queueName1, true, false, false, null);
        channel1.queueBind(queueName1, FANOUT_EXCHANGE_NAME, "");
        // 员工小蓝
        String queueName2 = "xiaolan_queue";
        channel2.queueDeclare(queueName2, true, false, false, null);
        channel2.queueBind(queueName2, FANOUT_EXCHANGE_NAME, "");

        System.out.println(" [*] =========================================");

        DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [xiaohong] Received '" + message + "'");
        };

        DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [xiaolan] Received '" + message + "'");
        };

        // 监听消息
        channel1.basicConsume(queueName1, true, deliverCallback1, consumerTag -> {
        });
        channel2.basicConsume(queueName2, true, deliverCallback2, consumerTag -> {
        });
    }
}
```
> 效果：所有的消费者都能收到消息

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723747368880-772d2d46-37f5-403a-8159-10ede992f2af.png#averageHue=%23292e36&clientId=u4ca622be-41e5-4&from=paste&height=616&id=u686160a3&originHeight=739&originWidth=2508&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=73555&status=done&style=none&taskId=u4160f5e8-dc65-4051-b3d3-5370c477b02&title=&width=2089.999916950865)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723747381129-b266fec1-e3bf-4f42-adbf-f16428c87040.png#averageHue=%232a2e37&clientId=u4ca622be-41e5-4&from=paste&height=612&id=u501ac5da&originHeight=734&originWidth=2458&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=99422&status=done&style=none&taskId=u51dc5362-99aa-4866-9ac4-ab8d2a73398&title=&width=2048.3332519398828)
#### 3.4.3 Direct 交换机
官网介绍教程：[https://www.rabbitmq.com/tutorials/tutorial-four-java.html](https://www.rabbitmq.com/tutorials/tutorial-four-java.html)

- **绑定：**可以让交换机和队列进行关联，可以指定让交换机把什么样的消息发送给哪个队列（类以于计算机网络中，两个路由器，或者网络设备相互连接，也可以理解为网线)
- **routing Key：**路由键，控制消息要转发给哪个队列的 （可以简单的说是 IP地址）
- **特点：**消息**会根据路由键转发到指定的队列**
- **场景：**特定的消息只交给特定的系统（程序）来处理
- **绑定关系：**完全匹配字符串，路由键要完全匹配

![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230620131236850.png#id=rtZLc&originHeight=180&originWidth=450&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
不同的队列也可以绑定同样的路由键。
比如发日志的场景，希望用独立的程序来处理不同级别的日志，比如 C1 系统处理 error 日志，C2系统处理其他级别的日志
![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230620163511572.png#id=fEUcV&originHeight=185&originWidth=466&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
**示例场景**
![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230620165023953.png#id=vyJfR&originHeight=370&originWidth=1096&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
生产者代码：
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class DirectProducer {

  private static final String DIRECT_EXCHANGER = "direct-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {
        channel.exchangeDeclare(DIRECT_EXCHANGER, "direct");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String userInput = scanner.nextLine();
            String[] splits = userInput.split(" ");
            if (splits.length < 1) {
                continue;
            }
            String message = splits[0];
            String routingKey = splits[1];

            channel.basicPublish(DIRECT_EXCHANGER, routingKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + " with routing " + routingKey + "'");
        }
    }
  }
  //..
}
```
消费者代码：
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.*;

public class DirectConsumer {

    private static final String DIRECT_EXCHANGE = "direct-exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(DIRECT_EXCHANGE, "direct");

        // 创建队列，分配一个队列名称：小紫
        String queueName = "zky11_queue";
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, DIRECT_EXCHANGE, "zky1");

        // 创建队列，分配一个队列名称：小黑
        String queueName2 = "zky2_queue";
        channel.queueDeclare(queueName2, true, false, false, null);
        channel.queueBind(queueName2, DIRECT_EXCHANGE, "zky2");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // 小紫队列监听机制
        DeliverCallback xiaoziDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [xiaozi] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        // 小黑队列监听机制
        DeliverCallback xiaoheiDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [xiaohei] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, xiaoziDeliverCallback, consumerTag -> {
        });
        channel.basicConsume(queueName2, true, xiaoheiDeliverCallback, consumerTag -> {
        });
    }
}
```

![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723893967432-4ed6ce4e-3716-4c81-8148-8b723a6a9e70.png#averageHue=%23fcfbf5&clientId=u7dd9098c-b5a5-4&from=paste&height=795&id=uba49ed42&originHeight=954&originWidth=1866&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=80830&status=done&style=none&taskId=ua3b7714f-ae77-4a64-a3b2-f62e5d1dbb0&title=&width=1554.999938209854)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723895706805-bc8532b1-b29b-4561-82ac-a94a45973d7e.png#averageHue=%232a2f38&clientId=u7dd9098c-b5a5-4&from=paste&height=448&id=ueb259c03&originHeight=538&originWidth=1283&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=55643&status=done&style=none&taskId=uf536376a-0787-4849-bce0-c3c558a22f4&title=&width=1069.166624181802)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723895716140-d2d676e7-4c91-434c-9211-b93f8c33bf3e.png#averageHue=%232b303a&clientId=u7dd9098c-b5a5-4&from=paste&height=426&id=u03586d21&originHeight=511&originWidth=1107&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=57484&status=done&style=none&taskId=ufbf83ec1-cffe-4755-8da6-d92a6c320fd&title=&width=922.4999633431449)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723895743716-d2b67321-bc9c-4111-9ad4-48316bb41e3a.png#averageHue=%232a2e37&clientId=u7dd9098c-b5a5-4&from=paste&height=552&id=u656f5eda&originHeight=662&originWidth=2322&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=93637&status=done&style=none&taskId=ud2b13627-153f-415d-b7cb-5d26eabbdb2&title=&width=1934.9999231100112)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723895753885-4e502346-e1e4-413b-b7b8-30ecf6f10cf7.png#averageHue=%232a2f37&clientId=u7dd9098c-b5a5-4&from=paste&height=478&id=u41d3f0d0&originHeight=574&originWidth=2305&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=70254&status=done&style=none&taskId=u984ebd02-3a3b-4c3c-9874-c97268f2e42&title=&width=1920.8332570062773)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723895791769-773b35f0-5bc4-4bac-9f03-72dcd6abe707.png#averageHue=%232a2f37&clientId=u7dd9098c-b5a5-4&from=paste&height=520&id=u6d43718f&originHeight=624&originWidth=2303&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=104290&status=done&style=none&taskId=u8a3ad7bf-7a74-4c42-bbf9-5c350ed5bb0&title=&width=1919.1665904058382)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723895800196-7aaa3ef4-a52d-40bb-a4dd-691fc01ddb9a.png#averageHue=%232b2f38&clientId=u7dd9098c-b5a5-4&from=paste&height=419&id=u8d1cd894&originHeight=503&originWidth=2308&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=88106&status=done&style=none&taskId=u88e6fd64-e000-4a17-b0c8-c918e31ff42&title=&width=1923.3332569069362)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723895829469-d56dbc6a-247a-4689-97cc-15df617936f4.png#averageHue=%23fbfbfb&clientId=u7dd9098c-b5a5-4&from=paste&height=668&id=uaa1cd63b&originHeight=802&originWidth=1496&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=46752&status=done&style=none&taskId=uedb013e0-d1e4-4276-b070-57616c90829&title=&width=1246.666617128586)
#### 3.4.4 **topic 交换**机
官网教程：[https://www.rabbitmq.com/tutorials/tutorial-five-java.html](https://www.rabbitmq.com/tutorials/tutorial-five-java.html)

- **特点**：消息会根据一个模糊的路由键转发到指定的队列
- **场景：**特定的一类消息可以交给特定的一类系统（程序）来处理
- **绑定关系**：可以模湖匹配多个绑定
1.  `*`：匹配一个单词，比如`*.shier`，那么`a.shier`、`b.shier`都能匹配 
2.  `#`：匹配0个或多个单词，比如`a.#`，那么`a.a`、`a.b`、`a.a.a`都匹配 
> `#.a.#` 可以匹配 `a.b`  、`a1.a`  等形式
> a的前面可以是0个或多个字符串，后面也是0个或者多个字符串

注意，这里的匹配和 MySQL 的 like 的 % 不一祥，只按照单词来匹配，每个`'.'`分隔单词，如果是`'#.'`，其实可以忽略，匹配0个词可以的
![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230620131840253.png#id=iItW0&originHeight=175&originWidth=467&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
示例场景：
> 老板下发一个任务，让多个任务组都能接受到这个任务消息

![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230623145040023.png#id=aZxRZ&originHeight=581&originWidth=1194&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
生产者代码：
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

/**
 * @author Shier
 * 主题消费者 - 生产者
 */
public class TopicProducer {

    private static final String TOPIC_EXCHANGE = "topic-exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 rabbitmq 对应的信息
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(TOPIC_EXCHANGE, "topic");

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String userInput = scanner.nextLine();
                String[] splits = userInput.split(" ");
                if (splits.length < 1) {
                    continue;
                }
                String message = splits[0];
                String routingKey = splits[1];

                channel.basicPublish(TOPIC_EXCHANGE, routingKey, null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + " with routing " + routingKey + "'");
            }
        }
    }
}
```
消费者代码：
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author Shier
 * 主题交换机 - 消费者
 */
public class TopicConsumer {

    private static final String TOPIC_EXCHANGE = "topic-exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 rabbitmq 对应的信息
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(TOPIC_EXCHANGE, "topic");

        // 创建前端队列，分配一个队列名称：frontend
        String queueName = "frontend_queue";
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, TOPIC_EXCHANGE, "#.前端.#");

        // 创建后端队列，分配一个队列名称：backend
        String queueName2 = "backend_queue";
        channel.queueDeclare(queueName2, true, false, false, null);
        channel.queueBind(queueName2, TOPIC_EXCHANGE, "#.后端.#");

        // 创建后端队列，分配一个队列名称：backend
        String queueName3 = "product_queue";
        channel.queueDeclare(queueName3, true, false, false, null);
        channel.queueBind(queueName3, TOPIC_EXCHANGE, "#.产品.#");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // A队列监听机制
        DeliverCallback xiaoADeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [xiaoA] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        // B队列监听机制
        DeliverCallback xiaoBDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [xiaoB] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        // C队列监听机制
        DeliverCallback xiaoCDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [xiaoC] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        channel.basicConsume(queueName, true, xiaoADeliverCallback, consumerTag -> {
        });
        channel.basicConsume(queueName2, true, xiaoBDeliverCallback, consumerTag -> {
        });
        channel.basicConsume(queueName3, true, xiaoCDeliverCallback, consumerTag -> {
        });
    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723897991142-c1e4cf49-a1b9-4467-b234-d244b4f82842.png#averageHue=%23f9f9f1&clientId=u7dd9098c-b5a5-4&from=paste&height=663&id=uc3bdb35a&originHeight=1592&originWidth=2851&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=240127&status=done&style=none&taskId=uabf258b4-1775-47b8-ba13-faffc711eaa&title=&width=1187.9166194631011)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723898089424-ebc96fe3-2fdf-45cb-ba04-c1dab86a5c99.png#averageHue=%232b3039&clientId=u7dd9098c-b5a5-4&from=paste&id=u11e9b2f2&originHeight=525&originWidth=1286&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=65230&status=done&style=none&taskId=u5dcb576a-79ae-4ce0-83fd-08904959b6e&title=)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723898098853-58cf3964-f92e-497a-8d48-8555ee645e52.png#averageHue=%232a2f38&clientId=u7dd9098c-b5a5-4&from=paste&id=u9f245aa9&originHeight=480&originWidth=1123&originalType=binary&ratio=1.2000000476837158&rotation=0&showTitle=false&size=66023&status=done&style=none&taskId=uf3fca81b-9121-4180-a267-72004223765&title=)
#### 3.4.5 Headers 交换机
类以主题和直接交换机，可以根据headers中的内容来指定发送到哪个队列。
由于性差、比较复杂，一般不推荐使用。
> rabbitmq中的 headers 交换机是什么？有什么用？什么场景下会用？有什么优缺点？
> AI答：
> 在RabbitMQ消息队列中，Headers交换机是一种特殊类型的交换机，它使用消息头（Headers）中的键值对来进行消息路由。与其他类型的交换机不同，Headers交换机无视消息的路由键，而是根据消息头中的键值对来决定消息的路由。
> Headers交换机的主要用途是基于消息的附加属性来进行路由，而不是通过固定的路由键进行匹配。它适用于一些特殊的场景，例如需要根据消息的特定属性来进行过滤、路由或匹配的情况。Headers交换机可以让我们在消息发布的时候根据消息头的键值对来指定消息的目标队列或者路由规则。
> 优点：
> - 灵活性高：Headers交换机可以根据消息的附加属性进行灵活的路由和匹配，可以满足一些特殊需求。
> - 可以进行复杂的匹配：Headers交换机支持复杂的匹配规则，可以根据多个属性进行匹配和路由。
> 
缺点：
> - 性能相对较低：Headers交换机需要对所有消息的消息头进行匹配，因此在消息数量较大的情况下性能会有所下降。
> - 配置复杂：Headers交换机需要明确指定消息头的键值对作为匹配条件，因此在配置时相对较为复杂。
> 
在实际应用中，Headers交换机常常用于一些特定的场景，例如：
> - 根据消息的自定义属性进行过滤和路由，以达到消息的分发和消费的目的。
> - 对消息进行定制化的路由，根据消息头中的自定义属性选择不同的消费者进行处理。
> 
需要注意的是，Headers交换机并不是常见的交换机类型，一般情况下，使用Direct、Fanout和Topic交换机就可以满足大多数的需求。只有在需要根据消息的附加属性进行路由或过滤时才考虑使用Headers交换机。


#### 3.4.6 RPC：远程过程调用
> 支持用消息队列来模拟RPC的调用，但是一般没必要，直接用Dubbo、GRPC等RPC框架就好了。
> 实现一个场景总有更合适的、更专注的技术。一般都是直接使用Dubbo、GRPC等RPC框架技术即可。

在RabbitMQ消息队列中，RPC（Remote Procedure Call，远程过程调用）是一种通信模式，用于实现分布式系统中的远程调用。RPC允许一个进程（称为客户端）调用另一个进程（称为服务端）上的某个函数并获取返回结果，就像在本地调用函数一样。
RPC的使用可以简化分布式系统间的通信，提供更方便的远程调用方式。客户端可以将请求参数封装成消息发送到消息队列的一个特定队列中，服务端监听该队列并接收消息，然后执行相应的处理逻辑，并将处理结果发送回客户端。
使用**RPC的主要目的**是实现分布式系统的协同工作，例如：

- 将计算任务分发到不同的节点上进行并行处理，提高系统的性能和响应速度。
- 实现微服务架构中的服务间的函数调用。

优点：

- 解耦性：RPC允许服务端和客户端通过异步消息传递进行通信，减少了服务之间的直接依赖。
- 可扩展性：RPC可以方便地加入新的服务或者移除不再需要的服务，通过消息队列可以实现动态的服务发现和注册。
- 并发处理：RPC可以实现并发处理多个请求，提高系统的吞吐量和并发能力。

缺点：

- 性能开销：RPC通过网络进行通信，相对于本地函数调用会有一定的性能开销。
- 配置复杂：RPC需要对客户端和服务端进行正确的配置和协调，包括队列、交换机、路由等参数的设置。

常见的使用场景包括：

- 分布式系统中的服务间调用
- 实现远程方法调用，比如客户端通过RPC调用服务器端的API

在选择使用RPC时，需要考虑系统的性能需求和规模，确保消息队列的性能能够满足RPC的通信需求。同时，还需要合理设置RPC的超时和重试机制，以应对网络故障或服务不可用的情况。

### 3.5 Rabbit MQ 核心特性
#### 3.5.1 消息过期机制
官网：[https://www.rabbitmq.com/ttl.html](https://www.rabbitmq.com/ttl.html)
可以给每条消息指定一个有效期，一段时间内未被消费者处理，就过期了。
示例场景：

- 消费者（库存系统）挂了，一个订单15分钟还没被库存系统处理，这个订单其实已经失效了，哪怕库存系统再恢复，其实也不用扣减库存。

适用场景：清理过期数据、模拟延迟队列的实现（不开会员就慢速）、专门让某个程序处理过期请求

1. 订单超时取消：在电商平台中，可以设置订单的过期时间，如果用户在规定时间内未支付订单，则将订单标记为过期取消。
2. 预约失效处理：在医院或美容院等场所，用户预约服务后，可以设置预约消息的过期时间，如果用户在规定时间内未到达，可以取消预约或释放时间。
3. 缓存更新和失效：在网站或应用中，可以将数据加载到缓存中，设置过期时间以保持数据的新鲜度，并在数据过期后重新加载最新数据。
4. 日志记录和清理：在系统中，可以将日志记录为消息发送到队列中，并设置过期时间以限制日志数据的保留时间，以及自动清理过期的日志。
5. 定时任务调度：可以使用RabbitMQ结合过期时间来实现定时任务调度。通过设置消息的过期时间，将任务消息发送到队列中，并在过期后触发执行相应的任务操作。
> AI 说：
> RabbitMQ提供了消息过期机制（Message Expiration）作为其核心特性之一。该机制允许你为消息设置一个过期时间，在此时间后，如果消息仍然未被消费者消费，将会自动从队列中删除。
> 消息过期机制的主要用途是处理一些具有时间敏感性的消息，例如临时的状态更新、时效性较强的任务等。通过设置消息的过期时间，可以确保消息在一定时间内得到及时消费，避免因消息堆积而导致的资源浪费或业务延迟。
>  
> 优点：
> - 灵活性高：可以根据消息的特定需求，灵活地设置消息的过期时间，以确保消息的时效性。
> - 资源利用率高：过期的消息会被自动从队列中删除，避免了无效消息的堆积和影响队列性能。
> 

> 缺点：
> - 对延迟要求较高：在消息过期之前，需要确保消费者能够及时消费到消息，避免消息过期而导致业务延迟。
> - 不适用于持久化：如果消息是持久化的（persistent），则即使消息过期了，也不会自动从磁盘上删除。
> 

> 使用消息过期机制的常见场景包括：
> - 定时任务：在消息中设置过期时间，确保任务在规定时间内被消费者消费。
> - 缓存管理：对于一些缓存数据，可以设置过期时间来自动清理过期的数据。
> - 临时状态更新：例如在某个状态更新后一段时间内没有被消费者消费，则可以设置消息过期，避免无效状态的堆积。
> 

> 值得注意的是，消息过期机制适用于一些具有时效性需求的场景。如果不需要强制实现消息的时效性，可以不使用消息过期机制，而是使用其他更适合的机制来处理消息的过期和清理，比如定期清理无效消息等。

##### 1）给队列中的所有消息指定过期时间
```java
// 创建队列
Map<String, Object> msg = new HashMap<String, Object>();
msg.put("x-message-ttl", 5000);
// 指定args参数
channel.queueDeclare(TTL_QUEUE, false, false, false, msg);
```
如果在过期时间内，还没有消费者取消息，消息才会过期。
**注意，如果消息已经接收到，但是没确认，是不会过期的。**
> 如果消息处于待消费状态并且过期时间到达后，消息将被标记为过期。但是，如果消息已经被消费者消费，并且正在处理过程中，即使过期时间到达，消息仍然会被正常处理。

生产者代码：
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author Shier
 */
public class TtlProducer {
    private final static String TTL_QUEUE = "ttl-queue";

    public static void main(String[] argv) throws Exception {
        // 创建链接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 rabbitmq 对应的信息
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             // 建立链接，创建频道
             Channel channel = connection.createChannel()) {
            // 创建消息队列 要删除掉，因为已经在消费者中创建了队列，没有必要再重新创建一次这个队列，如果在此处还创建队列，里面的参数必须要和消费者的参数一致
            // channel.queueDeclare(SINGLE_QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";
            channel.basicPublish("", TTL_QUEUE, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
```
消费者代码：
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Shier
 */
public class TtlConsumer {

    private final static String TTL_QUEUE = "ttl-queue";

    public static void main(String[] argv) throws Exception {
        // 建立链接，
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 rabbitmq 对应的信息
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 创建队列
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("x-message-ttl", 5000);
        // 指定args参数
        channel.queueDeclare(TTL_QUEUE, false, false, false, msg);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // 定义如何处理消息
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };
        // 消费消息 autoAck设置为false 取消掉自动确认消息
        channel.basicConsume(TTL_QUEUE, false, deliverCallback, consumerTag -> { });
    }
}
```

##### 2）给某条消息指定过期时间
> 在消息发送者设置，也就是生产者代码处

语法
```java
byte[] messageBodyBytes = "Hello, world!".getBytes();
AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                                   .expiration("60000")
                                   .build();
channel.basicPublish("my-exchange", "routing-key", properties, messageBodyBytes);
```
示例代码：
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author Shier
 */
public class TtlProducer {
    private final static String TTL_QUEUE = "ttl-queue";

    public static void main(String[] argv) throws Exception {
        // 创建链接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 rabbitmq 对应的信息
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             // 建立链接，创建频道
             Channel channel = connection.createChannel()) {
            // 创建消息队列 要删除掉，因为已经在消费者中创建了队列，没有必要再重新创建一次这个队列，如果在此处还创建队列，里面的参数必须要和消费者的参数一致
            // channel.queueDeclare(SINGLE_QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";

            // 给消息指定过期时间
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .expiration("60000")
                    .build();
            channel.basicPublish("", TTL_QUEUE, properties, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
```

#### 3.5.2 消息确认机制⭐⭐⭐⭐⭐（面试爱问）
官网介绍：[https://www.rabbitmq.com/confirms.html](https://www.rabbitmq.com/confirms.html)
```java
channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {});
// basicConsume()方法源码：
String basicConsume(String queue, boolean autoAck, DeliverCallback deliverCallback, CancelCallback cancelCallback) throws IOException;
```
为了保证消息成功被消费（快递成功被取走），rabbitmg提供了消息确认机制，当消费者接收到消息后，比如要给一个反馈：

- ack：消费成功（成功取到快递，我已确认收货，我的消息不用在保存在消息队列）
- nack：消费失败（未能去到快递，可能出于快递消失了、被人拿走了、快递存在问题等等的原因，我需要退货，需要和商家协调，你不能就此将我的消息去除）
- reject：拒绝
> 如果告诉 Rabbit MQ 服务器消费成功，服务器才会放心地移除消息。
> 支持配置autoack，会自动执行 ack 命令，接收到消息立刻就成功了。
> **一般建议 autoack 改为 false ，根据实际情况，去手动确认。**
> 比如：false时，就要自己手动的签收快递，等我收到快递之后，查看没有问题了，再去签收快递，这样可以按照流程减少不必要的事情。
> 		   true时，就是快递员不通知你就帮你签收了快递。（万一快递出了问题就不好搞了，不过现实中帮你签收了的情况也不少，有问题也还是可以处理的）

##### 1）指定确认某条消息：
```java
channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
// 源码
void basicAck(long deliveryTag, boolean multiple) throws IOException;
```
> 参数解释：
> 1. `deliveryTag`：表示要确认的消息的标识符。每个消息都有一个唯一的`deliveryTag`，用于标识消息的顺序。
> 2. `multiple`：表示是否批量确认消息。如果设置为`true`，则表示确认所有在`deliveryTag`之前的未确认消息；如果设置为`false`，则只确认当前`deliveryTag`的消息。
> 
第二参数 multiple 批量确认：是指是否要一次性确认所有的历史消息直到当前这条


##### 2）指定拒绝某条消息：
```java
channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
// 源码
void basicNack(long deliveryTag, boolean multiple, boolean requeue)
            throws IOException;
```
> 参数解释：
> 1. `deliveryTag`：表示要否定确认的消息的标识符。每个消息都有一个唯一的`deliveryTag`，用于标识消息的顺序。
> 2. `multiple`：表示是否批量否定确认消息。如果设置为`true`，则表示否定所有在`deliveryTag`之前的未确认消息；如果设置为`false`，则只否定当前`deliveryTag`的消息。
> 3. `requeue`：表示是否将消息重新放回队列。如果设置为`true`，则消息将被重新放回队列并可以被其他消费者重新消费；如果设置为`false`，则消息将会被丢弃。
> 
第三个参数表示是否重新入队，可用于重试

#### 3.5.3 死信队列
官网介绍：[https://www.rabbitmq.com/dlx.html](https://www.rabbitmq.com/dlx.html)
为了保证消息的可靠性，比如每条消息都成功消费，需要提供一个容措机制，即：失败的消息怎么处理？
死信：过期的消息、拒收的消息、消息队列满了、处理失败的消息的統称
死信队列：专门处理死信的队列（注意，它就是一个普通队列，只不过是专门用来处理死信的，你甚至可以理解这个队列的名称叫“死信队列”)
死信交换机：专门给死信队列转发消息的交换机（注意，它就是一个普通交换机，只不过是专门给死信队列发消息而已，理解为这个交换机的名称就叫“死信交换机”)。也存在路由绑定
死信可以通过死信交换机绑定到死信队列。
> AI说：
> RabbitMQ的死信队列（Dead-Letter Queue）是一种**用于处理无法被消费或者被拒绝的消息的特殊队列**。当消息满足一定条件时，例如消息被拒绝、消息过期或消息无法路由到目标队列时，可以将这些消息投递到死信队列中进行特殊处理。
> 死信队列的主要作用是处理无法正常消费的消息，可以实现以下功能：
> - 错误处理：将处理失败的消息转移到死信队列中，以便进行后续的异常处理、日志记录等。
> - 重新处理：将无法路由的消息转移到死信队列后，可以进行二次处理或重新投递。
> 

> 优点：
> - 异常处理：可以对处理失败的消息进行特殊的异常处理，以便排查和修复错误。
> - 重试机制：死信队列可以用于重新处理失败的消息，可以实现消息的重试机制，提高消息的可靠性。
> 

> 缺点：
> - 增加复杂性：使用死信队列增加了系统的复杂性，需要设计和实现针对死信队列的处理逻辑。
> - 需要额外的资源：死信队列需要额外的队列和处理逻辑，可能增加系统的资源消耗。
> 

> 使用死信队列的常见场景包括：
> - 消息重试：当消息处理失败时，可以将消息投递到死信队列中进行重试。
> - 异常处理：对于无法正常处理的消息，可以将其转移到死信队列以进行特殊的异常处理。
> - 延迟队列：通过设置消息的过期时间以及将过期的消息投递到死信队列，可以模拟实现延迟队列的功能。
> 

> 需要注意的是，死信队列的使用需要谨慎，不宜滥用。合理设置死信队列的条件和处理逻辑，以避免死信队列的堆积和对系统性能的影响。


示例场景：
![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230623211815329.png#id=U9dV8&originHeight=750&originWidth=1321&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)

实现过程：
1）创建死信交换机和死信队列，并且绑定关系
![](https://shierimages.oss-cn-shenzhen.aliyuncs.com/TyporaImages/image-20230623212927797.png#id=B65tr&originHeight=290&originWidth=771&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
```java
// 创建老板的死信队列
String queueName = "laoban_queue";
channel.queueDeclare(queueName, true, false, false, null);
channel.queueBind(queueName, DLX_DIRECT_EXCHANGE, "laoban");

// 创建外包的死信队列
String queueName2 = "waibao_queue";
channel.queueDeclare(queueName2, true, false, false, null);
channel.queueBind(queueName2, DLX_DIRECT_EXCHANGE, "waibao");
```

2）给失败之后需要容错处理的的队列绑定死信交换机
示例代码：
```java
// 指定死信队列的参数
Map<String, Object> args = new HashMap<>();
// 指定死信队列绑定到哪个交换机 ，此处绑定的是 dlx-direct-exchange 交换机
args.put("x-dead-letter-exchange", DLX_DIRECT_EXCHANGE);
// 指定死信要转发到哪个死信队列，此处转发到 laoban 这个死信队列
args.put("x-dead-letter-routing-key", "laoban");

// 创建队列，分配一个队列名称：小红
String queueName = "xiaohong_queue";
channel.queueDeclare(queueName, true, false, false, args);
channel.queueBind(queueName, DIRECT_2_EXCHANGE, "xiaohong");

Map<String, Object> args1 = new HashMap<>();
args.put("x-dead-letter-exchange", DLX_DIRECT_EXCHANGE);
args.put("x-dead-letter-routing-key", "waibao");

// 创建队列，分配一个队列名称：小蓝
String queueName2 = "xiaolan_queue";
channel.queueDeclare(queueName2, true, false, false, args1);
channel.queueBind(queueName2, DIRECT_2_EXCHANGE, "xiaolan");
```
3）可以给要容错的队列指定死信之后的转发规则，死信应该再转发到哪个死信队列
```java
// 指定死信要转发到哪个死信队列，此处转发到 laoban 这个死信队列
args.put("x-dead-letter-routing-key", "laoban");
```

4）可以通过程序来读取死信队列中的消息，从而进行处理
死信队列生产者代码：
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.Scanner;

/**
 * @author Shier
 */
public class DlxDirectProducer {

    private static final String DLX_DIRECT_EXCHANGE = "dlx-direct-exchange";
    private static final String WORK_EXCHANGE_NAME = "direct2-exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();

        // 设置 rabbitmq 对应的信息
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 声明私信交换机
            channel.exchangeDeclare(DLX_DIRECT_EXCHANGE, "direct");

            // 创建老板的死信队列
            String queueName = "laoban_dlx_queue";
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, DLX_DIRECT_EXCHANGE, "laoban");

            // 创建外包的死信队列
            String queueName2 = "waibao_dlx_queue";
            channel.queueDeclare(queueName2, true, false, false, null);
            channel.queueBind(queueName2, DLX_DIRECT_EXCHANGE, "waibao");


            // 老板队列监听机制
            DeliverCallback laobanDeliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                // 拒绝消息
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
                System.out.println(" [laoban] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };
            // 外包队列监听机制
            DeliverCallback waibaoDeliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                // 拒绝消息
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
                System.out.println(" [waibao] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };
            // 开启消费通道进行监听
            channel.basicConsume(queueName, false, laobanDeliverCallback, consumerTag -> {
            });
            channel.basicConsume(queueName2, false, waibaoDeliverCallback, consumerTag -> {
            });


            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String userInput = scanner.nextLine();
                String[] splits = userInput.split(" ");
                if (splits.length < 1) {
                    continue;
                }
                String message = splits[0];
                String routingKey = splits[1];

                channel.basicPublish(WORK_EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + " with routing " + routingKey + "'");
            }
        }
    }
}
```

死信队列消费者代码：
```java
package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Shier
 */
public class DlxDirectConsumer {

    private static final String DLX_DIRECT_EXCHANGE = "dlx-direct-exchange";

    private static final String WORK_EXCHANGE_NAME = "direct2-exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 rabbitmq 对应的信息
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(WORK_EXCHANGE_NAME, "direct");

        // 指定死信队列的参数
        Map<String, Object> args = new HashMap<>();
        // 指定死信队列绑定到哪个交换机 ，此处绑定的是 dlx-direct-exchange 交换机
        args.put("x-dead-letter-exchange", DLX_DIRECT_EXCHANGE);
        // 指定死信要转发到哪个死信队列，此处转发到 laoban 这个死信队列
        args.put("x-dead-letter-routing-key", "laoban");

        // 创建队列，分配一个队列名称：小红
        String queueName = "xiaohong_queue";
        channel.queueDeclare(queueName, true, false, false, args);
        channel.queueBind(queueName, WORK_EXCHANGE_NAME, "xiaohong");

        Map<String, Object> args2 = new HashMap<>();
        args2.put("x-dead-letter-exchange", DLX_DIRECT_EXCHANGE);
        args2.put("x-dead-letter-routing-key", "waibao");

        // 创建队列，分配一个队列名称：小蓝
        String queueName2 = "xiaolan_queue";
        channel.queueDeclare(queueName2, true, false, false, args2);
        channel.queueBind(queueName2, WORK_EXCHANGE_NAME, "xiaolan");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // 小红队列监听机制
        DeliverCallback xiaohongDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            // 拒绝消息
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
            System.out.println(" [xiaohong] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        // 小蓝队列监听机制
        DeliverCallback xiaolanDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            // 拒绝消息
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
            System.out.println(" [xiaolan] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        // 开启消费通道进行监听
        channel.basicConsume(queueName, false, xiaohongDeliverCallback, consumerTag -> {
        });
        channel.basicConsume(queueName2, false, xiaolanDeliverCallback, consumerTag -> {
        });
    }
}
```
> 总得来说就是，老板下发任务给员工（消息进入员工队列），如果员工发现自己处理不了这个任务（员工将此消息交由死信交换机，然后叫老板帮忙解决一下），就会再去请求老板帮忙解决（最后将此消息放入到老板的死信队列中）
> 以下是一个具体的例子来说明死信队列的使用和解决过程：
> 假设有一个订单支付系统，包含订单队列和支付结果队列。当用户下单后，订单信息会被发送到订单队列中进行处理，然后根据支付结果将订单信息发送到支付结果队列。但是有时订单队列中的消息处理失败或者超时，导致支付结果无法被发送到支付结果队列中。
> 在这种情况下，可以使用死信队列来解决问题。具体步骤如下：
> 1. 创建一个正常的订单队列和支付结果队列，并定义它们之间的交换机和绑定关系。
> 2. 创建一个死信交换机和死信队列，并定义它们之间的绑定关系。将订单队列的死信路由到死信交换机和死信队列中。
> 3. 在订单队列中设置消息的过期时间，当消息过期后，会成为死信消息，并被路由到死信交换机和死信队列中。
> 4. 在支付结果队列中消费消息，处理订单的支付结果。
> 5. 在死信队列中消费死信消息，并进行相应的处理。可以记录日志、发送通知等。
> 
通过以上步骤，当订单队列中的消息不能按时处理时，消息会过期成为死信消息，并被路由到死信队列中，然后我们可以在死信队列中进行相应的处理。
> 例如，在支付结果队列中的消费者发现某个订单无法处理时，可以将该消息发送到死信交换机和死信队列中，然后我们可以在死信队列中写入日志或者发送邮件通知相关人员，以便进一步处理这些死信消息。
> 这样，通过使用死信队列，我们可以更好地处理无法被消费的消息，并进行相关的补救措施，从而提高系统的可靠性和容错性。


#### 3.5.4 延迟队列
官网地址：[https://blog.rabbitmq.com/posts/2015/04/scheduling-messages-with-rabbitmq](https://blog.rabbitmq.com/posts/2015/04/scheduling-messages-with-rabbitmq)
延迟队列（Delayed Queue），它允许消息在一定的延迟时间后被消费。
作用：**在消息到达队列后，不立即将消息投递给消费者，而是在一定延迟时间后再进行投递。延迟队列通常用于需要延迟处理的业务场景。**  延迟队列主要用于处理需要在特定时间后执行的任务或延迟消息。它可以为消息设置一个延迟时间，在指定的延迟时间后，消息会被自动投递到指定的消费者。例如定时任务、消息重试、延迟通知等。
延迟队列适用于许多场景，包括：

1. **定时任务：**可以使用延迟队列来实现任务的定时触发，例如定时发送邮件或推送通知。
2. **消息重试：**当某个消息失败后，可以将其放入延迟队列，并设置延迟时间，以便稍后重新投递。
3. **延迟通知：**例如在某个时间后发送提醒通知。

延迟队列的优点有：

1. 灵活性：可以根据实际需求，灵活地设置延迟时间，适应各种业务场景。
2. 解耦性：延迟队列可以将消息发送与消费解耦，提高系统的可扩展性和稳定性。
3. 可靠性：通过延迟队列，可以确保消息在一定时间后被投递，降低消息丢失的风险。

延迟队列的缺点有：

1. 系统复杂性：引入延迟队列会增加系统的复杂性和维护成本。
2. 延迟时间不准确：由于网络延迟、系统负载等原因，延迟时间可能会有一定的误差。

### 3.6 RabbitMQ重点知识 ⭐⭐⭐⭐⭐
> 也是面试考点

1. 消息队列的概念、模型（上面的生产者、消费者图解）、应用场景
2. 交换机的类别、路由绑定的关系
3. 消息可靠性 
   - 消息确认机制(ack、nack、reject)
   - 消息特久化(durable)
   - 消息过期机制
   - 死信队列
4. 延迟队列（类似死信队列）
5. 顺序消费、消费幂等性
6. 可扩展性（仅作了解） 
   - 集群
   - 故障的恢复机制
   - 镜像
7. 运维监控告警（仅作了解）

## 4、RabbitMQ BI 项目实战
### 4.1 选择客户端
> 怎么在项目中使用RabbitMQ?

1. **使用官方的客户端**

优点：兼容性好，换语言成本低，比较灵活
缺点：太灵活，要自己去处理一些事情。比如要自己维护管理链接，很麻烦。

2. **使用封装好的客户端，比如Spring Boot RabbitMQ Starter**

优点：简单易用，直接配置直接用，更方便地去管理连接
缺点：封装的太好了，你没学过的话反而不知道怎么用。不够灵活，被框架限制。
> 根据场景来选择，没有绝对的优劣，类似 jdbc 和 MyBatis
> 优点：
> 1. 强大的功能支持：RabbitMQ Java客户端提供了丰富的功能和特性，例如消息确认、事务支持、消费者拉取、消息重试、可靠性消息传递等。
> 2. 可靠性和稳定性：通过使用可靠的消息确认机制和事务支持，Java客户端可以确保消息的可靠传递和处理。
> 3. 开发人员社区支持：RabbitMQ Java客户端拥有广泛的开发人员社区支持，并且有大量的文档和示例代码可供参考。
> 4. 良好的性能：Java客户端采用了高性能的网络通信协议和线程模型，可以在高并发的场景下表现出较好的性能。
> 
缺点：
> 1. 复杂性：相对于其他语言的RabbitMQ客户端，Java客户端可能会更加复杂和臃肿，需要更多的代码和配置。
> 2. 内存占用：由于Java客户端的一些设计和依赖，可能会占用较多的内存资源，尤其在高并发的情况下。


本次使用 Spring Boot RabbitMQ Starter (因为我们是Spring Boot项目)
如果你有一定水平，有基础，英文好，建议看官方文档，不要看过期博客！
官网地址：[https://spring.io/guides/gs/messaging-rabbitmq/](https://spring.io/guides/gs/messaging-rabbitmq/)

### 4.2 项目实战
##### 4.2.1 引入依赖
首先去 maven 官方仓库
[https://mvnrepository.com/](https://mvnrepository.com/)
引入依赖。
搜索 Spring Boot Starter AMQP
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723912979900-e01d3cf9-968a-4e4d-9969-a6de9ac9641f.png#averageHue=%23fafafa&clientId=ud916e621-faab-4&from=paste&height=668&id=u25e959e3&originHeight=1603&originWidth=2813&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=414983&status=done&style=none&taskId=u26e617a0-e745-406b-ad30-85c0dcbbd0b&title=&width=1172.0832867589281)
```xml
<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-amqp -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
    <version>2.7.2</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-amqp -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
    <version>3.3.2</version>
</dependency>
```
AMQP 是 RabbitMQ 实现的一个协议，进入后可以看到它的说明，支持 AMQP 和 RabbitMQ，他们是复用。
接下来我们需要选择合适的版本，确保选择的版本与使用的 Spring Boot 版本保持一致。为了避免出现不兼容的情况，查看项目使用的 Spring Boot 版本是什么，这里 Spring Boot 的版本是 2.7.2。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723913067982-3f294a08-28c1-4efb-bc4e-029c0610dffb.png#averageHue=%23292e36&clientId=ud916e621-faab-4&from=paste&height=258&id=u9818977a&originHeight=620&originWidth=1439&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=132911&status=done&style=none&taskId=u36195f8b-a31b-45e5-bd49-7a11758646d&title=&width=599.5833095080333)
所以选择 2.7.2 的版本。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723913145578-0223841c-40ec-47ca-9480-845070d2bbb1.png#averageHue=%23fcfcfc&clientId=ud916e621-faab-4&from=paste&height=310&id=u93c9c45d&originHeight=745&originWidth=1441&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=73293&status=done&style=none&taskId=u24fb7523-0482-404e-90cc-cd6ab3fde4c&title=&width=600.4166428082528)
```xml
<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-amqp -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
    <version>2.7.2</version>
</dependency>
```
##### 4.2.2 在yml中进行配置
```yaml
spring: 
    # rabbitmq 信息
    rabbitmq:
        host: localhost
        port: 5672
        password: guest
        username: guest
```
##### 4.2.3 创建交换机和队列
把之前启动的生产者、消费者停掉。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723913409810-42dbe26c-3886-432a-9353-d5c9caeeef7c.png#averageHue=%23f7f3f2&clientId=ud916e621-faab-4&from=paste&height=107&id=ufadf22f2&originHeight=257&originWidth=790&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=75104&status=done&style=none&taskId=uf00fab68-bc83-471d-b11f-a317caecd8f&title=&width=329.166653586759)
新建一个包： bizmq (存放业务相关的 mq 代码)。
##### 4.2.4 生产者代码
```java
package com.yupi.springbootinit.bizmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-18 00:52
 **/
@Component
public class MyMessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;
    /**
     * 发送消息
     * @param exchange
     * @param routingKey
     * @param message
     */
    public void sendMessage(String exchange, String routingKey, String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
```

##### 4.2.5 消费者代码
```java
package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-18 00:54
 **/
@Component
@Slf4j
public class MyMessageConsumer {
    /**
     * 指定程序监听的消息队列和确认机制
     * @param message
     * @param channel
     * @param deliveryTag
     */
    @RabbitListener(queues = {"demo_queue"}, ackMode = "MANUAL")
    private void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        log.info("receiveMessage = {}",message);
        try {
            // 手动确认消息
            channel.basicAck(deliveryTag,false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```
创建后运行一次
```java
package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-18 00:58
 * 创建测试测序用到的交换机和队列 (仅执行一次)
 **/

public class MqInitMain {
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            // 设置 rabbitmq 对应的信息
            factory.setHost("localhost");

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            String demoExchange = "demo_exchange";

            channel.exchangeDeclare(demoExchange, "direct");

            // 创建队列，分配一个队列名称：小紫
            String queueName = "demo_queue";
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, demoExchange, "demo_routingKey");

        }catch (Exception e){

        }
    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723914212687-edbf1fde-2716-4d1b-b1d4-3fd04d5dd1da.png#averageHue=%23eeeeed&clientId=ud916e621-faab-4&from=paste&height=513&id=u43429dff&originHeight=1231&originWidth=2473&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=274961&status=done&style=none&taskId=u9e711fdf-6a7c-4c87-b811-d4b46c3e2b4&title=&width=1030.4166257215886)
##### 4.2.6 单元测试执行
```java
package com.yupi.springbootinit.bizmq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MyMessageProducerTest {

    @Resource
    private MyMessageProducer myMessageProducer;

    @Test
    void sendMessage() {
        myMessageProducer.sendMessage("demo_exchange","demo_routingKey","欢迎来到智能BI系统");
    }
}
```

结果如下：
启动main
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723914504993-08d04680-41c0-4cef-aeb5-d58bc6766147.png#averageHue=%232a3038&clientId=ud916e621-faab-4&from=paste&height=683&id=ue48aeb80&originHeight=1640&originWidth=2811&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=537880&status=done&style=none&taskId=u4ecc4c3e-190f-42c9-996d-96468d78b43&title=&width=1171.2499534587084)
启动测试类
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723914542036-59305375-d12c-4c0b-9c72-977e96973a0d.png#averageHue=%232a2e37&clientId=ud916e621-faab-4&from=paste&height=680&id=ubd6ac4fb&originHeight=1633&originWidth=2817&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=448110&status=done&style=none&taskId=uf18594e4-2639-482d-9478-083f0e38d47&title=&width=1173.7499533593673)
结果
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723914575161-adc93e1b-6a28-4b6a-9201-9efdb4ec77cc.png#averageHue=%232a3038&clientId=ud916e621-faab-4&from=paste&height=698&id=ud62bac14&originHeight=1676&originWidth=2827&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=546013&status=done&style=none&taskId=u8bbfa7ed-346d-4d3f-a79c-a6aba1b6143&title=&width=1177.9166198604655)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723914598033-4b73f8ee-53ec-4ab3-827f-11660380652f.png#averageHue=%232d343d&clientId=ud916e621-faab-4&from=paste&height=166&id=ud75378a1&originHeight=398&originWidth=2698&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=193770&status=done&style=none&taskId=ubae58431-c458-414d-aee9-9f9f53193fe&title=&width=1124.1666219962985)
### 4.3 BI项目改造（2:42-）
##### 4.3.1 改造前BI
以前是把任务提交到线程池，然后在线程池提交中编写处理程序的代码，线程池内排队。
如果程序中断了，任务就没了，就丢了。
##### 4.3.2 改造后的BI
改造后的流程：

1.  把任务提交改为向队列发送消息 
2.  写一个专引门的接受消息的程序，处理任务 
3.  如果程序中断了，消息未被确认，还会重发 
4.  现在，消息全部集中发到消息队列，可以部署多个后端，都从同一个地方取任务，从而实现了分布式负载均衡 

实现步骤：

1. 创建交换机和队列
2. 将线程池中的执行代码移到消费者类中
3. 根据消费者的需求来确认消息的格式(chartld)
4. 将提交线程池改造为发送消息到队列

验证
验证发现，如果程序中断了，没有 ack、也没有 nack (服务中断，没有任何响应)，那么这条消息会被重新放到消息队列中，从而实现了每个任务都会执行。

##### 4.3.3 实现代码
###### 0）定义常量
将常量存放在一个包的接口中(创建一个专门用于存放常量的包，并在该包中定义接口或类来存放 
相关常量)。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723950182441-bf15bf4b-792c-4a19-8e65-fa20455090c2.png#averageHue=%23f9f8f8&clientId=u6a6faefb-591e-4&from=paste&height=826&id=ua8fb7177&originHeight=991&originWidth=1298&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=256899&status=done&style=none&taskId=ub946edd8-1e57-4edc-a339-45bd8f23916&title=&width=1081.6666236850967)
将常量存放在一个包的接口中(创建一个专门用于存放常量的包，并在该包中定义接口或类来存放 
相关常量)。
这里如果为了复用多个消费者，你可以做多个路由键。
```java
package com.yupi.springbootinit.bizmq;

/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-18 10:17
 **/

public class BiMqConstant {
    static final String BI_EXCHANGE_NAME = "bi_exchange";

    static final String BI_QUEUE_NAME = "bi_queue";

    static final String BI_ROUTING_KEY = "bi_routingKey";

    static final String BI_HOST = "localhost";
}
```
**线程池是不是更适用于需要多个线程处理任务，而 MQ 更适用于服务间通信与应用解耦？ **
对，线程池和消息队列（MQ）在不同的场景下有不同的适用性。线程池更适合处理需要多个线 
程并发执行的任务，而 MQ 更适合用于分布式场景下的信息传输、应用解耦、负载均衡以及消息 
可靠性保证。
**也就是说，分布式中使用线程池就不适合了呗，保证不了任务的先后？ **
确实，在分布式环境中使用线程池可能无法保证任务的先后顺序。如果你需要考虑消息的顺序 
性，就需要设计额外的机制来实现。单独使用一个消息队列可以确保消息的顺序传递，但是如果 
引入了其他复杂的机制，就无法保证顺序了。例如，如果你按顺序接收消息 1、2、3、4、5，但 
是将它们作为任务提交给线程池执行，就无法保证它们按照顺序执行。 
然而，只要你保证按顺序将任务提交给线程池，它们实际上也会按顺序执行。这意味着，如果你 
以顺序方式将消息 1、2、3、4、5 进入线程池作为任务，线程池会按照任务的顺序依次执行。 
因此，需要根据具体情况来权衡使用线程池和消息队列，并设计适当的机制来确保任务的顺序 
性。如果消息的顺序对业务很重要，可以考虑使用有序消息队列或其他保证顺序性的解决方案。
###### 1）BI 创建队列和交换机
```java
package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @program: zkybi-backend
 * @description:
 * @author: ZKYAAA
 * @create: 2024-08-18 00:58
 * 创建测试测序用到的交换机和队列 (仅执行一次)
 **/
public class BiInitMain {
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            // 设置 rabbitmq 对应的信息
            factory.setHost(BiMqConstant.BI_HOST);
            // 创建连接到 RabbitMQ 服务器的连接工厂
            Connection connection = factory.newConnection();
            // 用于与 RabbitMQ 服务器的通信
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = BiMqConstant.BI_EXCHANGE_NAME;
            // 定义交换机的名称
            channel.exchangeDeclare(EXCHANGE_NAME, BiMqConstant.BI_DIRECT_EXCHANGE);

            // 创建队列，分配一个队列名称
            String queueName = BiMqConstant.BI_QUEUE_NAME;
            // 定义队列的名称
            channel.queueDeclare(queueName, true, false, false, null);
            // 将队列绑定到交换机
            channel.queueBind(queueName, EXCHANGE_NAME, BiMqConstant.BI_ROUTING_KEY);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
```
###### 2）BI 消费者程序
```java
package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BiMessageConsumer {
    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receiveMessage message = {}", message);
        // 消息确认
        channel.basicAck(deliveryTag, false);
    }
}
```
###### 3）生产者代码
```java
package com.yupi.springbootinit.bizmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BiMessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(BiMqConstant.BI_EXCHANGE_NAME, BiMqConstant.BI_ROUTING_KEY, message);
    }

}
```
##### 4.3.4 改造业务流程
复制之前写的异步业务。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723950542310-01e2b8cf-2c2b-4fc6-a486-dd96eaac3d52.png#averageHue=%23272c34&clientId=u6a6faefb-591e-4&from=paste&height=1303&id=u078b7ae7&originHeight=1564&originWidth=2858&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=510882&status=done&style=none&taskId=u2e43900f-eb5b-4fde-9e0c-c0a64215b45&title=&width=2381.66657202774)
然后引入消息生产者。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723950587565-1678d0f8-0b84-4993-9a79-a2559872b5c5.png#averageHue=%23272c33&clientId=u6a6faefb-591e-4&from=paste&height=1406&id=u1a57052c&originHeight=1687&originWidth=2843&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=484594&status=done&style=none&taskId=ua97f41e9-107c-453f-97e6-c75ab50860f&title=&width=2369.166572524445)
现在要改造异步消息队列，这块地方，以前是我们直接提交一个任务到线程池。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723960268645-9e8f95e5-2e2a-4b76-b3fe-55f7ebd6149e.png#averageHue=%23282d35&clientId=u6a6faefb-591e-4&from=paste&height=1337&id=u6a4b7b3b&originHeight=1604&originWidth=2794&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=533071&status=done&style=none&taskId=u9ac1b9de-b41d-496c-9122-a52ebd4905f&title=&width=2328.333240813683)
把中间这段代码逻辑全部剪切，这一步不提线程池。
```java
// todo 任务队列满了，抛个异常
CompletableFuture.runAsync(()->{
    // todo 等待太久了，抛异常，超时时间
    // 等待-->执行中--> 成功/失败
    //先修改图表任务状态为"执行中"。等执行成功后，修改为"已完成"、保存执行结果；
    // 执行失败后，状态修改为"失败"，记录任务失败信息。
    Chart updateChart = new Chart();
    updateChart.setId(chart.getId());
    updateChart.setStatus("running");  // todo  设置枚举值
    boolean updateChartById = chartService.updateById(updateChart);
    if(!updateChartById) {
//                throw new BusinessException(ErrorCode.OPERATION_ERROR, "图表状态更新失败"); // todo
        handleChartUpdateError(chart.getId(), "更新图表·执行中状态·失败");
        return;
    }
    // 调用鱼皮接口,拿到返回结果
    String result = aiManager.doChat(biModelId,userInput.toString());
    //对返回结果做拆分,按照5个中括号进行拆分
    String[] splits = result.split("【【【【【");
    // 拆分之后还要进行校验
    if (splits.length < 3) {
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
    updateChartResult.setStatus("succeed");  // todo  设置枚举值
    boolean b = chartService.updateById(updateChartResult);
    if(!b) {
//                throw new BusinessException(ErrorCode.OPERATION_ERROR, "图表状态更新失败"); // todo
        handleChartUpdateError(chart.getId(), "更新图表·成功状态·失败");
    }
}, threadPoolExecutor);
```
删掉多余的内容。
```java
// todo 任务队列满了，抛个异常
CompletableFuture.runAsync(()->{
    
}, threadPoolExecutor);
```
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723960323884-6172f70a-8eed-4c09-8bb0-b732cb8a4bcc.png#averageHue=%23292d36&clientId=u6a6faefb-591e-4&from=paste&height=1038&id=ub40a4919&originHeight=1246&originWidth=2262&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=247698&status=done&style=none&taskId=u40d0ee15-4b42-4af2-a868-507fa15aca2&title=&width=1884.9999250968326)
改成往消息队列里发消息。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723960899439-0b9be190-d36b-4c21-9b61-ac01c88b0b73.png#averageHue=%23292d36&clientId=u6a6faefb-591e-4&from=paste&height=1037&id=ud037c8fc&originHeight=1244&originWidth=2070&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=229324&status=done&style=none&taskId=ucc39d656-eb3e-4fb9-96ca-cced4f6f805&title=&width=1724.9999314546612)
那这个消息发送什么呢？是不是要把这个参数传递给消费者。换句话说，将消费者需要的参数作 
为消息的内容传递过去，将其发送到消息队列中。这样，消费者就可以从消息队列中获取消息， 
并恢复当时的场景，以便执行相应的任务。这种方式使得消费者能够独立处理任务，而不是在线 
程池中处理。通过在消息队列中发送消息并携带所需参数，消费者可以获取到这些消息并进行相 
应的处理。 
所以在这里，首先需要查看消费者的代码，以了解消费者需要什么样的参数或数据。根据消费者 
需要的内容来确定生产者发送的消息内容。因此，生产者的代码应该与消费者的代码保持一致， 
根据消费者的要求来发送相应的消息。
###### 1）实现 mq 的接口
```java
/**
 * 智能分析 鱼皮接口（异步消息队列）
 *
 * @param multipartFile
 * @param genChartByAiRequest
 * @param request
 * @return
 */
@PostMapping("/gen/async/mq")
public BaseResponse<BiResponse> genChartByAiAsyncMq(@RequestPart("file") MultipartFile multipartFile,
                                                  GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
    String name = genChartByAiRequest.getName();
    String goal = genChartByAiRequest.getGoal();
    String chartType = genChartByAiRequest.getChartType();
    // 通过response对象拿到用户id(必须登录才能使用)
    User loginUser = userService.getLoginUser(request);

    // 校验
    // 如果分析目标为空，就抛出请求参数错误异常，并给出提示
    ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
    // 如果名称不为空，并且名称长度大于100，就抛出异常，并给出提示
    ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");
    // 校验文件
    long size = multipartFile.getSize();
    String originalFilename = multipartFile.getOriginalFilename();
    // 校验文件大小
    final long ONE_MB = 1024 * 1024L;
    ThrowUtils.throwIf(size > ONE_MB, ErrorCode.PARAMS_ERROR, "文件超过 1M");
    // 校验文件后缀
    String suffix = FileUtil.getSuffix(originalFilename);
    final List<String> validFileSuffixList = Arrays.asList("xlsx","csv","xls","json");
    ThrowUtils.throwIf(!validFileSuffixList.contains(suffix), ErrorCode.PARAMS_ERROR, "文件后缀非法");

    //限流判断，每个用户一个限流器
    redisLimiterManager.doRateLimit("genChartByAi_" + loginUser.getId());
    /*
    * 用户的输入(参考)
      分析需求：
      分析网站用户的增长情况
      原始数据：
      日期,用户数
      1号,10
      2号,20
      3号,30
    * */
    // 无需Prompt，直接调用现有模型
    // 构造用户输入
    long biModelId = CommonConstant.BI_MODEL_ID;

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
    String csvData = ExcelUtils.excelToCsv(multipartFile);
    userInput.append(csvData).append("\n");

    // 先插入数据到数据库
    Chart chart = new Chart();
    chart.setName(name);
    chart.setGoal(goal);
    chart.setChartData(csvData);
    chart.setChartType(chartType);
    chart.setStatus("wait");   // todo 设置枚举值
    chart.setUserId(loginUser.getId());
    boolean saveResult = chartService.save(chart);
    ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");
    // todo 任务队列满了，抛个异常
    if(threadPoolExecutor.getQueue().size()> threadPoolExecutor.getMaximumPoolSize()) {
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "当前队列已满");
    }
    long newChartId = chart.getId();
    biMessageProducer.sendMessage(String.valueOf(newChartId));
    // 返回到前端
    BiResponse biResponse = new BiResponse();
    biResponse.setChartId(newChartId);
    return ResultUtils.success(biResponse);
}
```
###### 2）constant包下的CommonConstant方法
将BIMODEL_ID抽取出来
```java
package com.yupi.springbootinit.constant;

/**
 * 通用常量
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface CommonConstant {

    /**
     * 升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = " descend";

    /**
     * BI 模型 id
     */
    // 指定一个模型id(把id写死，也可以定义成一个常量)
    long BI_MODEL_ID = 1821914479207112706L;
}
```
###### 3）BiMessageConsumer方法完善
```java
package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.manager.AiManager;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.utils.ExcelUtils;
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
        updateChartResult.setStatus("succeed");  // todo  设置枚举值
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
//        String csvData = ExcelUtils.excelToCsv(multipartFile);
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
```
**集群情况是不是得保证消息不重复？ **
是的，集群环境下确实需要考虑消息的去重。RabbitMQ 本身提供了消息确认机制，可以确保消 
息只被消费一次。当同一条消息被确认后，它就会被标记为已确认，这样就不会再被消费者接收 
到。如果你的集群是备份集群，也就是多个机器都可能接收和存储同一条消息，你需要确保消费 
者不会重复读取多个机器的消息。一般情况下，RabbitMQ 已经为你提供了相应的机制来解决这 
个问题。可以参考网上的教程来深入了解。
###### 4）启动 BiInitMain.java 
创建程序用到的交换机和队列。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723964050123-5b8fd92c-86dc-4ca4-afd1-d2f1c5977dbe.png#averageHue=%23282d35&clientId=u6a6faefb-591e-4&from=paste&height=1295&id=u5129c5c4&originHeight=1554&originWidth=2869&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=446480&status=done&style=none&taskId=u5940b2fb-26be-4a06-972f-940adcf255e&title=&width=2390.8332383301563)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723964100309-538ff50a-6671-4d4a-8546-42339a565c17.png#averageHue=%23faf9f9&clientId=u6a6faefb-591e-4&from=paste&height=721&id=u8cf20a97&originHeight=865&originWidth=1870&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=86782&status=done&style=none&taskId=ufc1ba1f7-720d-4b80-8f31-92c1fe2cab3&title=&width=1558.3332714107326)
看到了新的交换机和队列。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723964146156-5d0a94b9-4110-4af1-8d12-1b0db39aaf4d.png#averageHue=%23f8f7f0&clientId=u6a6faefb-591e-4&from=paste&height=770&id=u30e3776d&originHeight=924&originWidth=1876&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=125783&status=done&style=none&taskId=ue1e42cf9-b409-4d1d-b1f8-a5e0414f878&title=&width=1563.3332712120505)
点击交换机，查看绑定关系
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723964195295-4510d7c2-e010-4845-b257-378a3ca3530e.png#averageHue=%23fbfbfb&clientId=u6a6faefb-591e-4&from=paste&height=698&id=uc429849d&originHeight=838&originWidth=1888&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=65956&status=done&style=none&taskId=ud9139135-56a1-4291-8a31-4d3c4166f24&title=&width=1573.333270814686)
关闭所有的程序。
以 debug模式 启动 Spring Boot 项目。
回到前端，我们在后端新写了一个接口，前端想要快速地去调用这个接口，就要去执行 openAPI 。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723964294383-5f61e299-1b09-4e4f-99c9-c684ebcc803c.png#averageHue=%23232529&clientId=u6a6faefb-591e-4&from=paste&height=1351&id=ue1beec7e&originHeight=1621&originWidth=2802&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=414941&status=done&style=none&taskId=u1f5aea0f-aba9-43a9-a016-cfd330b65a0&title=&width=2334.99990721544)
现在已经生成调用后端消息队列异步接口的代码； 
改一下 智能分析(异步) 页的函数名，之前我们是用 genChartByAiAsyncUsingPOST 。
现在将其修改为使用Mq的异步方式 genChartByAiAsyncMqUsingPost
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723964484600-2e7390b8-2802-4a45-9fc2-c35b54f08154.png#averageHue=%23242529&clientId=u6a6faefb-591e-4&from=paste&height=1342&id=u678ece82&originHeight=1610&originWidth=2845&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=388307&status=done&style=none&taskId=u8ea67dde-004a-4d5d-8797-9d2a39af8fc&title=&width=2370.8332391248846)
启动前端。
填写分析数据，点击 提交 。
显示分析成功。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723964842904-e0f94a6d-9836-4109-a197-5461d58a6237.png#averageHue=%23f9f9f9&clientId=u6a6faefb-591e-4&from=paste&height=677&id=u69512d1a&originHeight=1625&originWidth=2863&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=628030&status=done&style=none&taskId=ud391a4f9-6c8c-4d20-ba0d-8722e8d3726&title=&width=1192.916619264419)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723964963891-f79d8866-02e4-4ab6-a03d-ed360408534c.png#averageHue=%23f9f9f9&clientId=u6a6faefb-591e-4&from=paste&height=668&id=ub4e3c12c&originHeight=1603&originWidth=2869&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=603671&status=done&style=none&taskId=uc89f5c72-03a8-4859-b837-52f77ca0652&title=&width=1195.4166191650781)
去 我的图表 页查看，还在生成中。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723964986095-d796525c-5a1f-4ec1-8f33-53071022a6ab.png#averageHue=%23f8f8f8&clientId=u6a6faefb-591e-4&from=paste&height=666&id=u09ef3e56&originHeight=1599&originWidth=2842&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=742389&status=done&style=none&taskId=u083a7cf6-d609-46a0-a208-08d74f5ad0b&title=&width=1184.1666196121128)
刷新几次页面后，图表生成出来了。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723964867579-e0a836a4-90c1-44ef-b105-e811c9f5c5c5.png#averageHue=%23dbcfc1&clientId=u6a6faefb-591e-4&from=paste&height=670&id=u2b102e69&originHeight=1607&originWidth=2867&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=809904&status=done&style=none&taskId=u66d84d80-182b-43da-9543-089737dcd65&title=&width=1194.5832858648585)
🐟提前换掉秘钥，导致连不上鱼聪明AI，生成的图表处理为确认消息的状态，程序中断后，消息被重新放到队列里。
查看管理页面，消息发过去了。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723965012894-45060767-b746-49f3-b21b-a572e0da1427.png#averageHue=%23faf9f2&clientId=u6a6faefb-591e-4&from=paste&height=666&id=u1f2fd06b&originHeight=1599&originWidth=2845&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=251023&status=done&style=none&taskId=u3d7b8486-70f6-4242-b94f-da73f526778&title=&width=1185.4166195624423)
但处于未确认状态。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723965055089-e2df0b49-f435-48db-bb9b-bdf6910a87d4.png#averageHue=%23f9f8f1&clientId=u6a6faefb-591e-4&from=paste&height=668&id=u6b4acd5e&originHeight=1604&originWidth=2826&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=292217&status=done&style=none&taskId=u9d63a338-564f-40e8-80da-e68235eadda&title=&width=1177.4999532103557)
秘钥更新后，图表显示出来了。
![image.png](https://cdn.nlark.com/yuque/0/2024/png/1389428/1723965278889-5c23f5e6-3cfd-42ae-884f-a04748ff0f77.png#averageHue=%23d8cabf&clientId=u6a6faefb-591e-4&from=paste&height=673&id=u42f7e3ed&originHeight=1616&originWidth=2866&originalType=binary&ratio=2.4000000953674316&rotation=0&showTitle=false&size=745894&status=done&style=none&taskId=u45d70e6f-1e1e-49ba-b164-1bff8ab6b6b&title=&width=1194.1666192147486)
**验证：**发现，如果程序中断了，没有 ack、也没有 nack（服务中断，没有任何响应），那么这 
条消息会被重新放到消息队列中，从而实现了每个任务都会执行。 
**新的问题：**它会不会无限执行？这是一个需要考虑的问题。每次重启时，所有失败的任务都会重 
新执行，可能会导致无限循环执行的情况。为了解决这个问题，可以考虑在数据库中记录任务的 
执行状态。当程序重启后，可以检查任务的状态，如果任务已经成功执行过，则可以跳过该任 
务，避免重复执行。这样可以有效地防止任务的无限执行，确保任务的正确性和可靠性。
> 到此本项目已完成，完结撒花🎉🎉🎉🎉🎉🎉🎉
