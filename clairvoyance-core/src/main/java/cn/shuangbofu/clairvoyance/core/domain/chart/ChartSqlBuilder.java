package cn.shuangbofu.clairvoyance.core.domain.chart;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Dimension;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ChartFilter;
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
    private final List<ChartFilter> filters;
    @Setter
    private List<Field> fields;
    @Setter
    private DrillParam drillParam;

    public ChartSqlBuilder(String chartSqlConfig) {
        chartSql = JSON.parseObject(chartSqlConfig, ChartSql.class);
        filters = new ArrayList<>();
    }

    public ChartSqlBuilder(String chartSqlConfig, Long workSheetId) {
        this(chartSqlConfig);
        setFields(getFields(workSheetId));
    }

    public static List<cn.shuangbofu.clairvoyance.core.domain.field.Field> getFields(Long workSheetId) {
        List<cn.shuangbofu.clairvoyance.core.db.Field> fields = FieldLoader.getAllFields(workSheetId);
        return cn.shuangbofu.clairvoyance.core.domain.field.Field.fromDb(fields);
    }

    public void addFilter(ChartFilter filter) {
        filters.add(filter);
    }

    public ChartSql build() {
        if (fields == null || fields.size() == 0) {
            throw new RuntimeException("fields not set");
        }
        // 初始化字段到所有chartField中
        setRealFields();
        chartSql.getOtherFilters().addAll(filters);
        if (drillParam != null) {
            setDrill();
        }
        return chartSql;
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
        fieldList.addAll(chartSql.getAllInnerFilters());
        fieldList.addAll(drillFields);
        fieldList.addAll(filters);
        fieldList.forEach(chartField -> chartField.setRealFields(fields));
        layers.forEach(layer -> layer.setFields(fields));
        // drillFields设置维度为field
        for (int i = 0; i < drillFields.size(); i++) {
            List<Dimension> x = layers.get(i).getX();
            drillFields.get(i).setRealFields(new ArrayList<>(x));
        }
    }
}
