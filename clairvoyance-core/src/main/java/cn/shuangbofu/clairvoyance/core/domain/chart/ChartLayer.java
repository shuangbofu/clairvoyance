package cn.shuangbofu.clairvoyance.core.domain.chart;

import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Dimension;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Value;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.FieldAlias;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.OrderType;
import cn.shuangbofu.clairvoyance.core.domain.field.ChartField;
import cn.shuangbofu.clairvoyance.core.domain.field.Field;
import cn.shuangbofu.clairvoyance.core.enums.ChartType;
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
 * Created by shuangbofu on 2020/8/12 12:04
 * <p>
 * 图表图层，用于下钻分析
 */
@Data
@Accessors(chain = true)
public class ChartLayer implements Sql {

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
    private ChartType chartType;

    /**
     * 对比和次轴后期可以在这里扩展
     */

    public static ChartLayer defaultLayer() {
        return new ChartLayer()
                .setX(new ArrayList<>())
                .setY(new ArrayList<>())
                .setSort(new Sort());
    }

    public void setFields(List<Field> fields) {
        getXY().forEach(field -> field.setRealFields(fields));
    }

    private List<FieldAlias> getXY() {
        List<FieldAlias> fieldAliases = new ArrayList<>();
        fieldAliases.addAll(x);
        fieldAliases.addAll(y);
        return fieldAliases;
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

    @Override
    public String groupBys() {
        return x.stream()
                .map(FieldAlias::getRealAliasName)
                .collect(Collectors.joining(", "));
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

    public void check() {
        // 检查sort
        if (sort != null) {
            Long fieldId = sort.getId();
            if (fieldId == null || getXY().stream().noneMatch(i -> i.getId().equals(fieldId))) {
                sort = null;
            }
        }
    }
}
