package cn.shuangbofu.clairvoyance.core.domain.chart;

import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Dimension;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.Filter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.OrderType;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ChartFilter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ChartInnerFilter;
import cn.shuangbofu.clairvoyance.core.domain.field.ChartField;
import cn.shuangbofu.clairvoyance.core.domain.field.DrillField;
import cn.shuangbofu.clairvoyance.core.domain.field.Field;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/7/30 下午11:09
 * <p>
 * 图表的SQL配置类
 */
@Data
@Accessors(chain = true)
public class ChartSql implements Sql {

    /**
     * 图内筛选器
     */
    List<ChartInnerFilter> innerFilters;

    /**
     * 筛选器
     */
    List<ChartFilter> filters;

    /**
     * 一个图表多个图层，下钻
     */
    List<ChartLayer> layers;

    /**
     * 下钻字段
     */
    List<DrillField> drillFields;

    @JsonIgnore
    private DrillParam drillParam;

    public static ChartSql defaultValue() {
        return new ChartSql()
                .setFilters(new ArrayList<>())
                .setInnerFilters(new ArrayList<>())
                .setDrillFields(new ArrayList<>())
                .setLayers(Lists.newArrayList(ChartLayer.defaultLayer()))
                ;
    }

    public static ChartSql buildSql(String config, List<Field> fields) {
        ChartSql sql = JSON.parseObject(config, ChartSql.class);
        sql.setRealFields(fields);
        return sql;
    }

    /**
     * 设置下钻参数
     *
     * @param param
     * @return
     */
    public ChartSql setDrill(DrillParam param) {
        drillParam = param;
        return this;
    }

    /**
     * @param fields
     */
    public void setRealFields(List<Field> fields) {
        List<ChartField> fieldList = Lists.newArrayList();
        fieldList.addAll(filters);
        fieldList.addAll(innerFilters);
        fieldList.addAll(drillFields);
        fieldList.forEach(chartField -> chartField.setRealFields(fields));
        layers.forEach(layer -> layer.setFields(fields));
        // drillFields设置维度为field
        for (int i = 0; i < drillFields.size(); i++) {
            List<Dimension> x = layers.get(i).x;
            drillFields.get(i).setRealFields(new ArrayList<>(x));
        }
    }

    @Override
    public List<String> selects() {
        return getLayer().selects();
    }

    @Override
    public String groupBys() {
        return getLayer().groupBys();
    }

    @Override
    public String wheres() {
        List<Filter> actualFilters = Lists.newArrayList();
        actualFilters.addAll(filters);
        actualFilters.addAll(innerFilters);

        // 下钻
        List<Object> values = drillParam.getValues();
        if (values.size() > 0) {
            List<DrillField> drillFields = this.drillFields.subList(0, drillLevel());
            for (int i = 0; i < drillFields.size(); i++) {
                Drill drill = new Drill(drillFields.get(i), values.get(i));
                actualFilters.add(drill);
            }
        }

        return actualFilters.stream().map((Filter::where))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" AND "));
    }

    @Override
    public Pair<String, OrderType> sort() {
        return getLayer().sort();
    }

    /**
     * 保存到数据库前清除多余配置
     */
    private void clear() {

    }

    public String toJSONString() {
        layers.forEach(ChartLayer::check);
        clear();
        return JSON.toJSONString(this);
    }

    @JsonIgnore
    private ChartLayer getLayer() {
        return layers.get(drillLevel());
    }

    @JsonIgnore
    private int drillLevel() {
        return drillParam.getLevel();
    }
}
