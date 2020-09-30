package cn.shuangbofu.clairvoyance.core.domain.chart;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Dimension;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ChartFilter;
import cn.shuangbofu.clairvoyance.core.domain.field.ChartField;
import cn.shuangbofu.clairvoyance.core.domain.field.DrillField;
import cn.shuangbofu.clairvoyance.core.domain.field.Field;
import cn.shuangbofu.clairvoyance.core.loader.FieldLoader;
import cn.shuangbofu.clairvoyance.core.meta.table.Sort;
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
    private final List<ChartFilter> otherFilters;
    @Setter
    private List<Field> fields;
    @Setter
    private DrillParam drillParam;

    public ChartSqlBuilder(String chartSqlConfig) {
        chartSql = JSON.parseObject(chartSqlConfig, ChartSql.class);
        otherFilters = new ArrayList<>();
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
        otherFilters.add(filter);
    }

    public ChartSql build() {
        if (fields == null || fields.size() == 0) {
            throw new RuntimeException("fields not set");
        }
        // 加入其他过滤器
        chartSql.getOtherFilters().addAll(otherFilters);
        // 初始化字段到所有chartField中
        initFields();
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

    /**
     * 初始化所有字段
     */
    private void initFields() {
        List<DrillField> drillFields = chartSql.getDrillFields();
        List<ChartField> fieldList = Lists.newArrayList();
        // 筛选器中的字段
        fieldList.addAll(chartSql.getFilters());
        // 图内选择器中的字段
        fieldList.addAll(chartSql.getAllInnerFilters());
        // 下钻字段中的字段
        fieldList.addAll(drillFields);
        // 其他过滤器
        fieldList.addAll(chartSql.getOtherFilters());
        chartSql.getLayers().forEach(layer -> {
            // 加入xy轴字段
            fieldList.addAll(layer.getXY());
            layer.getInnerFilters().forEach(filter -> {
                // 设置图内选择器
                filter.setupInner();
                // 设置图内选择器部分是y轴上的字段
                filter.setRealFields(Lists.newArrayList(layer.getY()));
            });
            // 设置行总计需要的字段
            layer.getY().forEach(value -> value.setAllValues(Lists.newArrayList(layer.getX())));
            // 排序设置字段
            Sort sort = layer.getSort();
            if (sort != null) {
                sort.setRealFields(Lists.newArrayList(layer.getXY()));
                sort.ifNull(() -> layer.setSort(null));
            }
        });
        // 设置所有字段为实际字段
        fieldList.forEach(chartField -> chartField.ifNull(() -> chartField.setRealFields(fields)));
        for (int i = 0; i < drillFields.size(); i++) {
            List<Dimension> x = chartSql.getLayers().get(i).getX();
            // drillFields设置维度字段
            drillFields.get(i).setRealFields(Lists.newArrayList(x));
        }
    }
}
