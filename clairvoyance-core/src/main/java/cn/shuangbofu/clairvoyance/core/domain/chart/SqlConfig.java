package cn.shuangbofu.clairvoyance.core.domain.chart;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.*;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.FieldAlias;
import cn.shuangbofu.clairvoyance.core.domain.worksheet.Field;
import cn.shuangbofu.clairvoyance.core.meta.Sql;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/7/30 下午11:09
 */
@Data
@Accessors(chain = true)
public class SqlConfig implements Sql {

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
    Order order;

    /**
     * 对比和次轴后期可以在这里扩展
     */

    public static SqlConfig defaultValue() {
        return new SqlConfig().setFilters(new ArrayList<>())
                .setInnerFilters(new ArrayList<>())
                .setX(new ArrayList<>())
                .setY(new ArrayList<>())
                .setOrder(new Order());
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
    public List<String> groupBys() {
        return getX().stream()
                .map(FieldAlias::getName).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> wheres() {
        return null;
    }

    @Override
    public cn.shuangbofu.clairvoyance.core.meta.Order order() {
        return null;
    }

    public List<Value> getY() {
        return getY().stream().filter(FieldAlias::isValid).collect(Collectors.toList());
    }

    public List<Dimension> getX() {
        return getX().stream().filter(FieldAlias::isValid).collect(Collectors.toList());
    }
}
