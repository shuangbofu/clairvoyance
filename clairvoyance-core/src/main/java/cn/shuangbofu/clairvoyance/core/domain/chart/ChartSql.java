package cn.shuangbofu.clairvoyance.core.domain.chart;

import cn.shuangbofu.clairvoyance.core.db.Field;
import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.ChartFilter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.ChartInnerFilter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Dimension;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Value;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.AbstractFilter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.FieldAlias;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.OrderType;
import cn.shuangbofu.clairvoyance.core.meta.table.Sort;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/7/30 下午11:09
 * <p>
 * 图表的SQL配置类
 */
@Data
@Accessors(chain = true)
public class ChartSql implements Sql {

    private static final String AXIS_X = "x";

    /**
     * 筛选器
     */
//    @JSONField(serialzeFeatures = SerializerFeature.WriteClassName)
    List<ChartFilter> filters;

    /**
     * 图内筛选器
     */
    List<ChartInnerFilter> innerFilters;

    /**
     * 维度，groupBy
     */
    List<Dimension> x;

    /**
     * 数值，select
     */
    List<Value> y;

    /**
     * 排序
     */
    Sort sort;

    /**
     * 对比和次轴后期可以在这里扩展
     */

    public static ChartSql defaultValue() {
        return new ChartSql().setFilters(new ArrayList<>())
                .setInnerFilters(new ArrayList<>())
                .setX(new ArrayList<>())
                .setY(new ArrayList<>())
                .setSort(new Sort());
    }

    private static void parseAndPrint(String json) {
        ChartSql chartSql = JSON.parseObject(json, ChartSql.class);
        System.out.println(chartSql.getFilters());
    }

    /**
     * 需要都改为一个类
     *
     * @param fields
     */
    public void setFields(List<Field> fields) {
        updateFieldTitle0(x, fields);
        updateFieldTitle0(y, fields);
        if (sort != null) {
            Optional<Field> any = fields.stream().filter(i -> i.getId().equals(sort.getId())).findAny();
            any.ifPresent(i -> sort.setName(i.getName()));
        }
        List<cn.shuangbofu.clairvoyance.core.domain.field.Field> filterList = Lists.newArrayList();
        filterList.addAll(filters);
        filterList.addAll(innerFilters);
        for (cn.shuangbofu.clairvoyance.core.domain.field.Field field : filterList) {
            Optional<Field> any = fields.stream().filter(i -> i.getId().equals(field.getId())).findAny();
            any.ifPresent(i -> field.setTitle(i.getTitle()).setName(i.getName()));
        }
    }

    private <T extends FieldAlias> void updateFieldTitle0(List<T> fieldAliases, List<Field> fields) {
        for (FieldAlias alias : fieldAliases) {
            Optional<Field> any = fields.stream().filter(i -> i.getId().equals(alias.getId())).findAny();
            any.ifPresent(i -> alias.setTitle(i.getTitle()));
        }
    }

    @Override
    public List<String> selects() {
        List<String> selects = getXY().stream()
                .map(FieldAlias::getQueryFinalName)
                .collect(Collectors.toList());
        if (selects.size() == 0) {
            selects.add("1");
        }
        return selects;
    }

    private List<FieldAlias> getXY() {
        List<FieldAlias> fieldAliases = new ArrayList<>();
        fieldAliases.addAll(getX());
        fieldAliases.addAll(getY());
        return fieldAliases;
    }

    @Override
    public String groupBys() {
        return getX().stream()
                .map(FieldAlias::getName)
                .collect(Collectors.joining(","));
    }

    @Override
    public String wheres() {
        List<AbstractFilter> actualFilters = Lists.newArrayList();
        if (filters != null && filters.size() > 0) {
            actualFilters.addAll(filters);
        }
        if (innerFilters != null && innerFilters.size() > 0) {
            actualFilters.addAll(innerFilters);
        }
        return actualFilters.stream().map((AbstractFilter::where))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" AND "));
    }

    @Override
    public Pair<String, OrderType> sort() {
        List<FieldAlias> fieldAliases = Lists.newArrayList();
        if (sort == null) {
            return null;
        }
        if (AXIS_X.equals(sort.getAxis())) {
            fieldAliases.addAll(getX());
        } else {
            fieldAliases.addAll(getY());
        }
        Optional<FieldAlias> any = fieldAliases.stream().filter(i -> i.getId().equals(sort.getId())).findAny();
        return any.map(fieldAlias -> new Pair<>(fieldAlias.getQueryName(), sort.getOrderType())).orElse(null);
    }

    public List<Value> getY() {
        return y.stream().filter(FieldAlias::isValid).collect(Collectors.toList());
    }

    public List<Dimension> getX() {
        return x.stream().filter(FieldAlias::isValid).collect(Collectors.toList());
    }

    private void check() {
        // 检查sort
        if (sort != null) {
            Long fieldId = sort.getId();
            if (fieldId == null || getXY().stream().noneMatch(i -> i.getId().equals(fieldId))) {
                sort = null;
            }
        }
    }

    /**
     * 保存到数据库前清除多余配置
     */
    private void clear() {

    }

    public String toJSONString() {
        check();
        clear();
        return JSON.toJSONString(this);
    }
}
