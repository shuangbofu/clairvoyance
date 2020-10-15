package cn.shuangbofu.clairvoyance.core.chart;

import cn.shuangbofu.clairvoyance.core.chart.sql.Dimension;
import cn.shuangbofu.clairvoyance.core.chart.sql.Value;
import cn.shuangbofu.clairvoyance.core.chart.sql.base.FieldAlias;
import cn.shuangbofu.clairvoyance.core.chart.sql.base.OrderType;
import cn.shuangbofu.clairvoyance.core.chart.sql.filter.InnerChartFilter;
import cn.shuangbofu.clairvoyance.core.field.AbstractChartField;
import cn.shuangbofu.clairvoyance.core.meta.table.Sort;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import cn.shuangbofu.clairvoyance.core.utils.Pair;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/12 12:04
 * <p>
 * 图表图层，用于下钻分析
 */
@Data
@Accessors(chain = true)
public class ChartLayer implements Sql {

    /**
     * 图内筛选器
     */
    private List<InnerChartFilter> innerFilters;

    /**
     * 维度，groupBy
     */
    private List<Dimension> x;

    /**
     * 数值，select
     */
    private List<Value> y;

    /**
     * 排序
     */
    private Sort sort;

    /**
     * 对比和次轴后期可以在这里扩展
     */

    /**
     * 次轴
     */
    @JsonProperty("yOptional")
    private List<Value> yOptional;

    public static ChartLayer defaultLayer() {
        return new ChartLayer()
                .setInnerFilters(new ArrayList<>())
                .setX(new ArrayList<>())
                .setY(new ArrayList<>())
                .setYOptional(new ArrayList<>())
                .setSort(new Sort());
    }

    @JsonIgnore
    public List<FieldAlias> getXY() {
        List<FieldAlias> fieldAliases = new ArrayList<>();
        fieldAliases.addAll(x);
        fieldAliases.addAll(getYWithoutRow());
        return fieldAliases;
    }

    /**
     * 拼接sql使用
     *
     * @return
     */
    @JsonIgnore
    public List<Value> getYWithoutRow() {
        List<Value> y = this.y.stream().filter(i -> !i.total()).collect(Collectors.toList());
        y.addAll(yOptional);
        return y;
    }

    /**
     * 转成json使用
     *
     * @return
     */
    public List<Value> getY() {
        y.sort(Comparator.comparingInt(o -> (o.total() ? 1 : 0)));
        return y;
    }

    @JsonIgnore
    public Value getRow() {
        // 设置次轴不允许是表格
        if (yOptional.size() > 0) {
            return null;
        }
        Optional<Value> any = y.stream().filter(Value::total).findAny();
        return any.orElse(null);
    }

    @Override
    public List<String> selects() {
        return getXY().stream().map(FieldAlias::getQueryFinalName)
                .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
    }

    @Override
    public String groupBys() {
        return x.stream()
                .map(AbstractChartField::getRealName)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.joining(", "));
    }

    @Override
    public Pair<String, OrderType> sort() {
        if (sort == null) {
            return null;
        }
        return new Pair<>(sort.getFinalAliasName(), sort.getOrderType());
    }
}
