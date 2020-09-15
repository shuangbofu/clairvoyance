package cn.shuangbofu.clairvoyance.core.domain.chart;

import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.Filter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.OrderType;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ChartFilter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.InnerFilter;
import cn.shuangbofu.clairvoyance.core.domain.field.DrillField;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
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

    /**
     * 比如全局过滤器、图表联动的过滤条件
     */
    @JsonIgnore
    private List<Filter> otherFilters;
    @JsonIgnore
    private List<Drill> drills;
    @JsonIgnore
    private int drillLevel;

    public ChartSql() {
        drills = new ArrayList<>();
        otherFilters = new ArrayList<>();
        drillLevel = 0;
    }

    public static ChartSql defaultValue() {
        return new ChartSql()
                .setFilters(new ArrayList<>())
                .setDrillFields(new ArrayList<>())
                .setLayers(Lists.newArrayList(ChartLayer.defaultLayer()))
                ;
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
        List<ChartFilter> innerFilters = getLayer().getInnerFilters();
        innerFilters.forEach(InnerFilter::setup);
        actualFilters.addAll(innerFilters);
        actualFilters.addAll(drills);
        actualFilters.addAll(otherFilters);
        return actualFilters.stream().map((Filter::where))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" AND "));
    }

    @Override
    public Pair<String, OrderType> sort() {
        return getLayer().sort();
    }

    public String toJSONString() {
        layers.forEach(ChartLayer::check);
        return JSON.toJSONString(this);
    }

    @JsonIgnore
    private ChartLayer getLayer() {
        return layers.get(drillLevel);
    }

    @JsonIgnore
    public List<ChartFilter> getAllInnerFilters() {
        return layers.stream().map(ChartLayer::getInnerFilters)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor
    public static class Test {
        private Boolean total;
    }
}
