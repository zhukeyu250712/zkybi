/*
 Navicat Premium Data Transfer

 Source Server         : zkylocalhost
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : yubi

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 18/08/2024 16:17:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chart
-- ----------------------------
DROP TABLE IF EXISTS `chart`;
CREATE TABLE `chart`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goal` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '分析目标',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图表名称',
  `chartData` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '图表数据',
  `chartType` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图表类型',
  `genChart` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '生成的图表信息',
  `genResult` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '生成的分析结论',
  `userId` bigint NULL DEFAULT NULL COMMENT '创建图标用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `status` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'wait' COMMENT 'wait-等待,running-生成中,succeed-成功生成,failed-生成失败',
  `execMessage` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '执行信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1825067479458201603 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '图表信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chart
-- ----------------------------
INSERT INTO `chart` VALUES (1822150482135158785, '分析网站用户数量增长情况', 'zky网站分析', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '折线图', '{\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        data: [10, 20, 30, 90, 0, 10, 20],\n        type: \'line\'\n    }]\n}', '从折线图可以看出，网站用户数量从1号到4号呈快速增长趋势，4号达到峰值90人，但从4号到5号出现大幅下降至0人，可能是由于数据异常或网站故障。随后从5号开始，用户数量又逐渐恢复并保持增长，但增长速度相对较慢。整体来看，除了5号的数据异常外，用户数量总体呈增长趋势。', 1798988129315102721, '2024-08-10 13:58:23', '2024-08-14 10:14:47', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1822150482135158787, '分析网站用户数量增长情况', 'zky网站分析', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '折线图', '{\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        data: [10, 20, 30, 90, 0, 10, 20],\n        type: \'line\'\n    }]\n}', '从折线图可以看出，网站用户数量从1号到4号呈快速增长趋势，4号达到峰值90人，但从4号到5号出现大幅下降至0人，可能是由于数据异常或网站故障。随后从5号开始，用户数量又逐渐恢复并保持增长，但增长速度相对较慢。整体来看，除了5号的数据异常外，用户数量总体呈增长趋势。', 1798988129315102721, '2024-08-10 13:58:23', '2024-08-14 10:14:47', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1822150573696815106, '分析网站用户数量增长情况', 'zky网站分析', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '堆叠图', '{\n    title: {\n        text: \'网站用户数量增长情况\',\n        subtext: \'\'\n    },\n    tooltip: {\n        trigger: \'axis\'\n    },\n    legend: {\n        data: [\'用户数\']\n    },\n    xAxis: {\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        name: \'用户数\',\n        type: \'bar\',\n        stack: \'总量\',\n        data: [10, 20, 30, 90, 0, 10, 20]\n    }]\n}', '从堆叠图中可以看出，网站用户数量在1号到4号期间增长迅速，其中4号达到了峰值90用户。然而，5号用户数量突然下降到0，可能是数据异常或特殊情况导致。从6号开始，用户数量又开始逐渐增长，但增长速度不如前期快。总体来看，除了5号的数据异常外，用户数量呈现出先快速增长后缓慢上升的趋势。', 1798988129315102721, '2024-08-10 13:58:45', '2024-08-14 10:14:47', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1822159209361903617, '用户变化情况', 'zky网站分析2', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '饼图', '{\n    title: {\n        text: \'用户变化情况\',\n        subtext: \'\',\n        left: \'center\'\n    },\n    tooltip: {\n        trigger: \'item\'\n    },\n    legend: {\n        orient: \'vertical\',\n        left: \'left\'\n    },\n    series: [\n        {\n            name: \'用户数\',\n            type: \'pie\',\n            radius: \'50%\',\n            data: [\n                { value: 10, name: \'1号\' },\n                { value: 20, name: \'2号\' },\n                { value: 30, name: \'3号\' },\n                { value: 90, name: \'4号\' },\n                { value: 0, name: \'5号\' },\n                { value: 10, name: \'6号\' },\n                { value: 20, name: \'7号\' }\n            ],\n            emphasis: {\n                itemStyle: {\n                    shadowBlur: 10,\n                    shadowOffsetX: 0,\n                    shadowColor: \'rgba(0, 0, 0, 0.5)\'\n                }\n            }\n        }\n    ]\n}', '根据数据分析，用户数在不同日期有显著的变化。4号用户数最多，达到90人，而5号没有用户。从1号到2号和从6号到7号，用户数都保持了相同的增长趋势，增加了10人。3号相比2号增加了10人，但在6号又回到了10人。整体来看，用户数波动较大，没有明显的线性增长或减少趋势。', 1798988129315102721, '2024-08-10 14:33:04', '2024-08-14 10:14:47', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1822159871722196993, '用户增长变化情况', 'zky网站分析3', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '面积热力图', '{\n    tooltip: {\n        position: \'top\'\n    },\n    visualMap: {\n        min: 0,\n        max: 90,\n        left: \'left\',\n        top: \'bottom\',\n        calculable: true,\n        inRange: {\n            color: [\'#e0f3f8\', \'#ffffbf\', \'#fee090\', \'#fdae61\', \'#f46d43\', \'#d73027\', \'#a50026\']\n        }\n    },\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'category\',\n        data: [\'用户数\']\n    },\n    series: [{\n        name: \'用户数\',\n        type: \'heatmap\',\n        data: [\n            [0, 0, 10],\n            [0, 1, 20],\n            [0, 2, 30],\n            [0, 3, 90],\n            [0, 4, 0],\n            [0, 5, 10],\n            [0, 6, 20]\n        ],\n        label: {\n            show: true\n        },\n        emphasis: {\n            itemStyle: {\n                shadowBlur: 10,\n                shadowColor: \'rgba(0, 0, 0, 0.5)\'\n            }\n        }\n    }]\n}', '数据分析显示，用户增长变化情况存在波动。从1号到3号用户数稳步增长，4号达到了峰值，用户数突然激增到90，随后在5号急剧下降到0，之后又开始逐步回升。这种变化可能表明网站在4号可能有特殊的推广活动或者事件发生，吸引了大量用户，但在5号可能由于某些原因导致用户数下降。整体来看，用户增长呈现出不稳定的特点。', 1798988129315102721, '2024-08-10 14:35:42', '2024-08-14 10:14:47', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1822160094817226754, '用户增长变化情况分析', 'zky网站分析4', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '条形图', '{\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        data: [10, 20, 30, 90, 0, 10, 20],\n        type: \'bar\'\n    }],\n    tooltip: {\n        trigger: \'item\'\n    },\n    title: {\n        text: \'用户增长变化情况\',\n        left: \'center\'\n    }\n}', '从条形图的分析中可以看出，用户数在前三天呈现稳步增长，从10增长到30。然而，在第四天用户数激增到90，可能是由于某些推广活动或者特殊情况的发生。第五天用户数突然降为0，这可能是数据收集或者系统问题导致的异常。之后，用户数又恢复到10，并在第七天增长到20。整体来看，除了第五天的异常数据外，用户增长趋势在前三天和后两天呈现稳步上升的状态。需要进一步分析第四天用户激增的原因以及第五天用户数为0的具体原因。', 1798988129315102721, '2024-08-10 14:36:35', '2024-08-14 10:14:47', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1822160094817226799, '用户增长变化情况分析', 'zky网站分析4', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '条形图', '{\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        data: [10, 20, 30, 90, 0, 10, 20],\n        type: \'bar\'\n    }],\n    tooltip: {\n        trigger: \'item\'\n    },\n    title: {\n        text: \'用户增长变化情况\',\n        left: \'center\'\n    }\n}', '从条形图的分析中可以看出，用户数在前三天呈现稳步增长，从10增长到30。然而，在第四天用户数激增到90，可能是由于某些推广活动或者特殊情况的发生。第五天用户数突然降为0，这可能是数据收集或者系统问题导致的异常。之后，用户数又恢复到10，并在第七天增长到20。整体来看，除了第五天的异常数据外，用户增长趋势在前三天和后两天呈现稳步上升的状态。需要进一步分析第四天用户激增的原因以及第五天用户数为0的具体原因。', 1798988129315102721, '2024-08-10 14:36:35', '2024-08-14 10:14:47', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1823201139424268290, '分析网站用户情况', 'zky用户数量增长分析1', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '折线图', '{\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        data: [10, 20, 30, 90, 0, 10, 20],\n        type: \'line\'\n    }]\n}', '根据数据分析，网站用户数从1号到4号呈现快速增长，特别是在4号达到了峰值90用户。然而，5号用户数突然下降至0，可能是由于某种异常情况，如网站维护或故障。之后，用户数在6号和7号又逐渐回升至初始水平。这表明网站可能需要关注5号发生的异常情况，并采取措施防止未来用户数的急剧下降。', 1798988129315102721, '2024-08-13 11:33:19', '2024-08-14 10:14:47', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1823201244697104386, '分析网站用户情况', 'zky用户数量增长分析1', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '散点图', '{\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        data: [[0, 10], [1, 20], [2, 30], [3, 90], [4, 0], [5, 10], [6, 20]],\n        type: \'scatter\'\n    }]\n}', '数据分析结论：\n从散点图可以看出，网站用户数在前三天呈现线性增长趋势，从10增加到30。然而，第四天用户数激增到90，这是一个异常点，可能是由于某种特殊事件或推广活动导致的用户访问量激增。第五天用户数突然降为0，这同样是一个异常值，可能是由于网站维护、故障或其他外部因素导致的。第六天和第七天用户数又恢复到增长趋势，分别为10和20。\n\n整体来看，除了第四天和第五天的异常值外，用户数呈现出较为稳定的增长趋势。需要进一步分析第四天和第五天的异常情况，以确定背后的原因，并据此调整网站运营策略。', 1798988129315102721, '2024-08-13 11:33:44', '2024-08-14 10:14:47', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1823362323091918849, '分析网站用户数量增长的情况', 'zky用户数量增长分析2', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '折线图', '{\n    title: {\n        text: \'网站用户数量增长情况\',\n        subtext: \'\'\n    },\n    tooltip: {\n        trigger: \'axis\'\n    },\n    legend: {\n        data: [\'用户数\']\n    },\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        name: \'用户数\',\n        type: \'line\',\n        data: [10, 20, 30, 90, 0, 10, 20],\n        smooth: true\n    }]\n}', '从折线图可以看出，网站用户数量在1号到4号期间呈现出快速增长的趋势，特别是在4号达到了峰值，用户数达到了90人。然而，5号用户数量突然下降到0，可能是由于数据错误或者网站问题导致。随后，6号和7号用户数量又开始逐渐恢复增长，但增长速度较之前放缓。整体来看，除了5号的数据异常外，用户数量整体呈现出增长趋势。需要进一步分析5号用户数量为0的原因，并考虑是否为异常数据。', 1798988129315102721, '2024-08-13 22:13:48', '2024-08-14 10:14:47', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1823364052437012482, '分析网站用户数量的变化情况', 'zky用户数量增长分析2', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '折线图', '{\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        data: [10, 20, 30, 90, 0, 10, 20],\n        type: \'line\'\n    }]\n}', '根据数据分析，网站用户数量呈现出不稳定的波动变化。从1号到3号，用户数量呈线性增长，但在4号突然激增到90，随后在5号急剧下降到0，之后又开始逐渐恢复增长，从6号到7号增长到20。这种变化可能表明网站在4号经历了某些特殊事件或推广活动，导致用户数量的短暂激增，而5号的用户数量下降可能是由于数据录入错误或网站维护等原因造成的。整体来看，用户数量的变化需要进一步分析以确定具体原因。', 1798988129315102721, '2024-08-13 22:20:41', '2024-08-14 10:14:47', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1823373375451979777, '分析网站用户数量变化', 'zky用户数量增长分析3', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '折线图', '{\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        data: [10, 20, 30, 90, 0, 10, 20],\n        type: \'line\'\n    }]\n}', '根据数据分析，网站用户数量呈现出以下变化趋势：\n- 从1号到3号，用户数量呈线性增长，每天增加10个用户。\n- 到4号时，用户数量急剧增加至90，可能是由于某些推广活动或者特殊事件的发生。\n- 5号时，用户数量突然下降至0，这可能是数据异常或者网站出现了重大问题。\n- 从6号开始，用户数量又开始逐渐恢复，但增长速度较之前慢，每天增加10个用户。\n- 到7号时，用户数量恢复到20。\n\n需要注意的是，5号的数据可能需要进一步核实，因为它与其他数据点相比显得异常。如果这是真实数据，那么可能需要调查这一天发生了什么导致用户数量的剧烈下降。', 1798988129315102721, '2024-08-13 22:57:44', '2024-08-14 10:14:47', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1823543111116697601, '分析用户的增长情况', 'zky用户数量增长分析4', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '折线图', '{\n    title: {\n        text: \'用户增长情况\',\n        subtext: \'\'\n    },\n    tooltip: {\n        trigger: \'axis\'\n    },\n    legend: {\n        data: [\'用户数\']\n    },\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        name: \'用户数\',\n        type: \'line\',\n        data: [10, 20, 30, 90, 0, 10, 20],\n        markPoint: {\n            data: [\n                {type: \'max\', name: \'最大值\'},\n                {type: \'min\', name: \'最小值\'}\n            ]\n        },\n        markLine: {\n            data: [\n                {type: \'average\', name: \'平均值\'}\n            ]\n        }\n    }]\n}', '数据分析结论如下：\n- 用户数在1号至4号期间呈现显著增长，特别是在4号达到了峰值，用户数为90。\n- 5号用户数突然下降至0，可能表明了某种异常情况或数据错误。\n- 从6号开始，用户数又开始逐步增长，但增长速度较之前缓慢。\n- 平均来看，1号至7号期间的用户数平均值为20。\n- 总体来看，用户增长情况波动较大，需要进一步分析5号用户数下降的原因，并考虑采取相应措施以促进用户增长。', 1798988129315102721, '2024-08-14 10:12:12', '2024-08-14 10:14:47', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1823543988338601986, '用户数量变化分析', 'zky用户数量增长分析5', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '散点图', '{\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        data: [10, 20, 30, 90, 0, 10, 20],\n        type: \'scatter\'\n    }]\n}', '数据分析结论：\n1. 用户数量从1号至3号呈线性增长，从10增长到30。\n2. 4号用户数量激增，达到90，可能是由于某些特殊事件或推广活动。\n3. 5号用户数量突然降为0，这可能是数据采集或系统问题，或者是一个非常异常的事件。\n4. 6号和7号用户数量恢复增长，但增长速度较慢，分别为10和20。\n5. 总体来看，用户数量的变化趋势包含增长、异常下降和恢复增长三个阶段。需要进一步分析4号和5号的数据异常情况，以确定背后的原因。', 1798988129315102721, '2024-08-14 10:15:41', '2024-08-14 10:17:09', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1823544611779948545, '用户网站数量变化', 'zky用户数量增长分析5', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '折线图', '{\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        data: [10, 20, 30, 90, 0, 10, 20],\n        type: \'line\'\n    }]\n}', '从折线图中可以观察到以下用户数变化趋势：\n\n- 用户数量从1号到3号呈线性增长，从10增长到30。\n- 4号突然激增到90，可能是由于某些推广活动或特殊事件的影响。\n- 5号用户数量跌至0，这可能是数据错误或者网站发生了重大问题，如服务中断等。\n- 6号和7号用户数量又逐渐回升，恢复到10和20。\n\n整体来看，除了5号异常数据外，用户数量总体上呈现出增长趋势，但增长速度不稳定，存在明显的波动。需要进一步分析波动的原因，并针对5号的异常情况进行调查。', 1798988129315102721, '2024-08-14 10:18:09', '2024-08-14 10:18:21', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1825066822051381249, '网站用户增长情况分析', 'zky用户数量增长分析6（新MQ）', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '折线图', '{\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        data: [10, 20, 30, 90, 0, 10, 20],\n        type: \'line\'\n    }],\n    tooltip: {\n        trigger: \'axis\'\n    },\n    title: {\n        text: \'网站用户增长情况\'\n    }\n}', '从折线图的数据分析来看，网站用户数在前四天呈现出快速增长的趋势，特别是在第四天达到了峰值90用户。然而，在第五天用户数突然下降到0，可能是由于数据收集错误或者是网站发生了某些问题导致用户无法访问。之后在第六天和第七天用户数又逐渐回升，但增长速度较之前缓慢。整体来看，用户增长存在波动，需要进一步分析第五天用户数下降的具体原因。', 1798988129315102721, '2024-08-18 15:06:53', '2024-08-18 15:07:01', 0, 'succeed', NULL);
INSERT INTO `chart` VALUES (1825067479458201602, '分析用户数量增长情况', 'zky用户数量增长分析MQ1', '日期,用户数\n1号,10\n2号,20\n3号,30\n4号,90\n5号,0\n6号,10\n7号,20\n', '柱状图', '{\n    xAxis: {\n        type: \'category\',\n        data: [\'1号\', \'2号\', \'3号\', \'4号\', \'5号\', \'6号\', \'7号\']\n    },\n    yAxis: {\n        type: \'value\'\n    },\n    series: [{\n        data: [10, 20, 30, 90, 0, 10, 20],\n        type: \'bar\'\n    }]\n}', '从柱状图可以看出用户数量的增长情况：\n\n- 从1号到3号，用户数量呈现线性增长，每天增长10个用户。\n- 到4号时，用户数量急剧增加，从30增长到90，增长了200%。\n- 5号时，用户数量出现了显著的下降，从90减少到0，可能是因为某种异常情况或者数据录入错误。\n- 从6号开始，用户数量又重新开始增长，但增长速度较慢，每天增长10个用户。\n\n整体来看，除了5号的数据异常外，用户数量呈现出不稳定的增长趋势，其中4号的增长最为突出。需要进一步分析5号用户数量为0的原因，以及4号用户数量激增的动因。', 1798988129315102721, '2024-08-18 15:09:29', '2024-08-18 15:09:38', 0, 'succeed', NULL);

-- ----------------------------
-- Table structure for chart_1659210482555121666
-- ----------------------------
DROP TABLE IF EXISTS `chart_1659210482555121666`;
CREATE TABLE `chart_1659210482555121666`  (
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `chartData` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chart_1659210482555121666
-- ----------------------------
INSERT INTO `chart_1659210482555121666` VALUES ('12', '23');
INSERT INTO `chart_1659210482555121666` VALUES ('2', '34');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `userAvatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户头像',
  `userRole` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userAccount`(`userAccount`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1798988129315102722 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1798988129315102721, 'ZKYAAA', 'b0dd3697a192885d7c055db46155b26a', 'ZKYAAA', 'https://backiee.com/static/wallpapers/1000x563/325910.jpg', 'user', '2024-06-07 15:59:28', '2024-06-11 14:39:10', 0);

SET FOREIGN_KEY_CHECKS = 1;
