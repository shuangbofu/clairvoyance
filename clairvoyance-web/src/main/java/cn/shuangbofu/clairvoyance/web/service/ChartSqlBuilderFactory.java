package cn.shuangbofu.clairvoyance.web.service;


import cn.shuangbofu.clairvoyance.core.chart.ChartSqlBuilder;
import cn.shuangbofu.clairvoyance.core.field.Field;

import java.util.List;

/**
 * Created by shuangbofu on 2020/10/14 19:25
 */
public class ChartSqlBuilderFactory {

    public static ChartSqlBuilder create(String chartSqlConfig, Long workSheetId) {
        List<Field> fields = FieldService.getFields(workSheetId);
        return new ChartSqlBuilder(chartSqlConfig, fields);
    }
}
