package cn.shuangbofu.clairvoyance.core.chart;

import cn.shuangbofu.clairvoyance.core.chart.base.Filter;
import cn.shuangbofu.clairvoyance.core.chart.base.OrderType;
import cn.shuangbofu.clairvoyance.core.chart.filter.ChartFilter;
import cn.shuangbofu.clairvoyance.core.chart.filter.InnerChartFilter;
import cn.shuangbofu.clairvoyance.core.field.DrillField;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import cn.shuangbofu.clairvoyance.core.result.AdddRowHandler;
import cn.shuangbofu.clairvoyance.core.result.ReplaceKeyHandler;
import cn.shuangbofu.clairvoyance.core.result.ResultHandler;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.core.utils.Pair;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/7/30 下午11:09
 * <p>
 * 图表的SQL配置类（以下作用）
 * 1、存储的配置（转成json）
 * 2、传给前端的数据格式
 * 3、从前端接受的数据格式
 * 4、生成具体sql的类
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
    private List<ChartFilter> otherFilters;
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
        actualFilters.addAll(getLayer().getInnerFilters());
        actualFilters.addAll(drills);
        actualFilters.addAll(otherFilters);
        return actualFilters.stream().map((Filter::where))
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.joining(" AND "));
    }

    @Override
    public String havings() {
        return getLayer().getInnerFilters().stream().map(InnerChartFilter::having)
                .filter(StringUtils::isNotEmpty).collect(Collectors.joining(" AND "));
    }

    @Override
    public Pair<String, OrderType> sort() {
        return getLayer().sort();
    }

    public String toJSONString() {
        return JSON.toJSONString(this);
    }

    @JsonIgnore
    public ChartLayer getLayer() {
        return layers.get(drillLevel);
    }

    @JsonIgnore
    public List<InnerChartFilter> getAllInnerFilters() {
        return layers.stream().map(ChartLayer::getInnerFilters)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResultHandler> handlers() {
        return Lists.newArrayList(
                new ReplaceKeyHandler(this),
                new AdddRowHandler(this)
        );
    }

    public Boolean isEmptyChartSQL() {
        if (layers != null && layers.size() > 0) {
            for (ChartLayer layer : layers) {
                if (!layer.isEmptyLayer()) {
                    return false;
                }
            }
        }
        return true;
    }
}
