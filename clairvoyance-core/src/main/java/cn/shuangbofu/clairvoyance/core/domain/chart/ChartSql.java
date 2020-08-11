package cn.shuangbofu.clairvoyance.core.domain.chart;

import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Dimension;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Value;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.FieldAlias;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.Filter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.OrderType;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ChartFilter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ChartInnerFilter;
import cn.shuangbofu.clairvoyance.core.domain.field.ChartField;
import cn.shuangbofu.clairvoyance.core.domain.field.Field;
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

    public static ChartSql buildSql(String config, List<Field> fields) {
        ChartSql sql = JSON.parseObject(config, ChartSql.class);
        sql.setRealFields(fields);
        return sql;
    }

    /**
     * @param fields
     */
    public void setRealFields(List<Field> fields) {
        List<ChartField> fieldList = Lists.newArrayList();
        fieldList.addAll(getXY());
        fieldList.addAll(filters);
        fieldList.addAll(innerFilters);
        for (ChartField field : fieldList) {
            field.setRealFields(fields);
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
        fieldAliases.addAll(x);
        fieldAliases.addAll(y);
        return fieldAliases;
    }

    @Override
    public String groupBys() {
        return x.stream()
                .map(FieldAlias::getRealAliasName)
                .collect(Collectors.joining(", "));
    }

    @Override
    public String wheres() {
        List<Filter> actualFilters = Lists.newArrayList();
        if (filters != null && filters.size() > 0) {
            actualFilters.addAll(filters);
        }
        if (innerFilters != null && innerFilters.size() > 0) {
            actualFilters.addAll(innerFilters);
        }
        return actualFilters.stream().map((Filter::where))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" AND "));
    }

    @Override
    public Pair<String, OrderType> sort() {
        List<ChartField> chartFields = Lists.newArrayList();
        if (sort == null) {
            return null;
        }
        if (sort.isX()) {
            chartFields.addAll(x);
        } else {
            chartFields.addAll(y);
        }
        Optional<ChartField> any = chartFields.stream().filter(i -> i.getId().equals(sort.getId())).findAny();
        return any.map(fieldAlias -> new Pair<>(fieldAlias.getRealAliasName(), sort.getOrderType())).orElse(null);
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
