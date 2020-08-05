package cn.shuangbofu.clairvoyance.core.domain.chart;

import cn.shuangbofu.clairvoyance.core.db.Field;
import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.ChartFilter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.ChartInnerFilter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Dimension;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Value;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.FieldAlias;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.OrderType;
import cn.shuangbofu.clairvoyance.core.meta.table.Sort;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/7/30 下午11:09
 */
@Data
@Accessors(chain = true)
public class ChartSql implements Sql {

    private static final String AXIS_X = "x";

    /**
     * 筛选器
     */
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

    public void updateFieldTitle(List<Field> fields) {
        updateFieldTitle0(x, fields);
        updateFieldTitle0(y, fields);
    }

    private <T extends FieldAlias> void updateFieldTitle0(List<T> fieldAliases, List<Field> fields) {
        for (FieldAlias alias : fieldAliases) {
            Optional<Field> any = fields.stream().filter(i -> i.getName().equals(alias.getName())).findAny();
            any.ifPresent(i -> alias.setTitle(i.getTitle()));
        }
    }

    @Override
    public List<String> selects() {
        List<FieldAlias> fieldAliases = new ArrayList<>();
        fieldAliases.addAll(getX());
        fieldAliases.addAll(getY());

        return fieldAliases.stream()
                .map(FieldAlias::getQueryFinalName)
                .collect(Collectors.toList());
    }

    @Override
    public String groupBys() {
        return getX().stream()
                .map(FieldAlias::getName).collect(Collectors.joining(","));
    }

    @Override
    public String wheres() {
        return null;
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
}
