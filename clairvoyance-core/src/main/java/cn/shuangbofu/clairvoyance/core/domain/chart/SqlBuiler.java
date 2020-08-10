package cn.shuangbofu.clairvoyance.core.domain.chart;

import cn.shuangbofu.clairvoyance.core.domain.field.Field;
import cn.shuangbofu.clairvoyance.core.loader.FieldLoader;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/10 21:16
 */
public class SqlBuiler {

    private static ChartSql buildChartSql(String chartSqlConf, List<Field> fieldList) {
        return ChartSql.buildSql(chartSqlConf, fieldList);
    }

    public static ChartSql buildChartSql(String chartSqlConf, Long workSheetId) {
        return buildChartSql(chartSqlConf, getFields(workSheetId));
    }

    public static List<cn.shuangbofu.clairvoyance.core.domain.field.Field> getFields(Long workSheetId) {
        List<cn.shuangbofu.clairvoyance.core.db.Field> fields = FieldLoader.getAllFields(workSheetId);
        return cn.shuangbofu.clairvoyance.core.domain.field.Field.fromDb(fields);
    }

    public static Sql select(String... selects) {
        return () -> Lists.newArrayList(selects);
    }
}
