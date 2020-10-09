package cn.shuangbofu.clairvoyance.core.domain.chart.result;

import cn.shuangbofu.clairvoyance.core.domain.chart.ChartSql;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Value;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.AggregatorFunc;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.OrderType;
import cn.shuangbofu.clairvoyance.core.meta.table.Sort;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by shuangbofu on 2020/9/30 10:17
 */
public class AdddRowHandler extends AbstractResultHandler {
    Value row;

    public AdddRowHandler(ChartSql chartSql) {
        super(chartSql);
        row = getLayer().getRow();
    }

    @Override
    public List<Map<String, Object>> handle(List<Map<String, Object>> origin) {
        List<Map<String, Object>> res = Lists.newArrayList();
        origin.forEach(map -> {
            Value.Position position = null;
            Object computedValue = null;
            AggregatorFunc func;
            String rowKey = null;
            if (row != null) {
                rowKey = row.getUniqId().toString();
                func = row.getAggregator();
                position = row.getPosition();
                computedValue = compute(map.values().stream()
                        .filter(i -> i instanceof Number)
                        .map(i -> (Number) i)
                        .mapToInt(Number::intValue), func);
            }
            Map<String, Object> newMap = new LinkedHashMap<>();
            if (Value.Position.left.equals(position) && computedValue != null) {
                newMap.put(rowKey, computedValue);
            }
            newMap.putAll(map);
            if (Value.Position.right.equals(position) && computedValue != null) {
                newMap.put(rowKey, computedValue);
            }
            res.add(sortMap(newMap));
        });
        return sortList(res);
    }

    /**
     * 默认x y排序
     *
     * @param originMap
     * @return
     */
    private Map<String, Object> sortMap(Map<String, Object> originMap) {
        Map<String, Object> xMap = new LinkedHashMap<>();
        Map<String, Object> yMap = new LinkedHashMap<>();
        List<String> xKeys = getLayer().getX().stream().map(i -> i.getUniqId().toString()).collect(Collectors.toList());
        originMap.keySet().forEach(key -> {
            if (xKeys.contains(key)) {
                xMap.put(key, originMap.get(key));
            } else {
                yMap.put(key, originMap.get(key));
            }
        });
        Map<String, Object> resMap = new LinkedHashMap<>();
        resMap.putAll(xMap);
        resMap.putAll(yMap);
        return resMap;
    }

    private List<Map<String, Object>> sortList(List<Map<String, Object>> originList) {
        Sort sort = getLayer().getSort();
        if (row != null && sort != null && sort.equal(row)) {
            originList.sort((o1, o2) -> {
                Object obj1 = o1.get(row.getUniqId().toString());
                Object obj2 = o2.get(row.getUniqId().toString());
                int i = 0;
                if (obj1 instanceof Number && obj2 instanceof Number) {
                    i = ((Integer) obj1) - ((Integer) obj2);
                } else {
                    if (obj1 instanceof Number) {
                        i = 1;
                    } else if (obj2 instanceof Number) {
                        i = -1;
                    }
                }
                if (sort.getOrderType().equals(OrderType.desc)) {
                    i = -i;
                }
                return i;
            });
        }
        return originList;
    }

    private Object compute(IntStream stream, AggregatorFunc func) {
        if (func.equals(AggregatorFunc.MAX)) {
            return stream.max().orElse(0);
        } else if (func.equals(AggregatorFunc.MIN)) {
            return stream.min().orElse(0);
        } else if (func.equals(AggregatorFunc.SUM)) {
            return stream.sum();
        } else if (func.equals(AggregatorFunc.AVG)) {
            return stream.average().orElse(0);
        } else {
            throw new RuntimeException("not supported");
        }
    }
}
