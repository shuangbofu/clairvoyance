package cn.shuangbofu.clairvoyance.core.domain.chart;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Dimension;
import cn.shuangbofu.clairvoyance.core.domain.field.ChartField;
import cn.shuangbofu.clairvoyance.core.domain.field.DrillField;
import cn.shuangbofu.clairvoyance.core.domain.field.Field;
import cn.shuangbofu.clairvoyance.core.loader.FieldLoader;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import com.google.common.collect.Lists;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuangbofu on 2020/8/15 17:32
 */
@Accessors(chain = true)
public class ChartSqlBuilder {
    private final ChartSql chartSql;
    @Setter
    private List<Field> fields;
    @Setter
    private DrillParam drillParam;

    public ChartSqlBuilder(String chartSqlConfig) {
        chartSql = JSON.parseObject(chartSqlConfig, ChartSql.class);
    }

    public ChartSqlBuilder(String chartSqlConfig, Long workSheetId) {
        this(chartSqlConfig);
        setFields(getFields(workSheetId));
    }

    public static List<cn.shuangbofu.clairvoyance.core.domain.field.Field> getFields(Long workSheetId) {
        List<cn.shuangbofu.clairvoyance.core.db.Field> fields = FieldLoader.getAllFields(workSheetId);
        return cn.shuangbofu.clairvoyance.core.domain.field.Field.fromDb(fields);
    }

    public ChartSql build() {
        if (fields == null || fields.size() == 0) {
            throw new RuntimeException("fields not set");
        }
        // 初始化字段到所有chartField中
        setRealFields();

        if (drillParam != null) {
            setDrill();
        }
        return chartSql;
    }

    /**
     * 全局筛选
     */
    private void setGlobalFilter() {

    }

    /**
     * 图表关联
     */
    private void setLink() {

    }


    /**
     * 配置的下钻
     */
    private void setDrill() {
        List<Object> values = drillParam.getValues();
        int level = drillParam.getLevel();
        chartSql.setDrillLevel(level);
        if (values.size() > 0) {
            List<DrillField> drillFields = chartSql.getDrillFields().subList(0, level);
            for (int i = 0; i < drillFields.size(); i++) {
                Drill drill = new Drill(drillFields.get(i), values.get(i));
                chartSql.getDrills().add(drill);
            }
        }
    }

    /**
     * 自由钻取
     */
    private void setFreeDrill() {

    }

    private void setRealFields() {
        List<DrillField> drillFields = chartSql.getDrillFields();
        List<ChartLayer> layers = chartSql.getLayers();
        List<ChartField> fieldList = Lists.newArrayList();
        fieldList.addAll(chartSql.getFilters());
        fieldList.addAll(chartSql.getInnerFilters());
        fieldList.addAll(drillFields);
        fieldList.forEach(chartField -> chartField.setRealFields(fields));
        layers.forEach(layer -> layer.setFields(fields));
        // drillFields设置维度为field
        for (int i = 0; i < drillFields.size(); i++) {
            List<Dimension> x = layers.get(i).x;
            drillFields.get(i).setRealFields(new ArrayList<>(x));
        }
    }
}
