package cn.shuangbofu.clairvoyance.core.domain.chart.result;

import cn.shuangbofu.clairvoyance.core.domain.chart.ChartSql;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Value;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.AggregatorFunc;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by shuangbofu on 2020/9/30 10:17
 */
public class AdddRowHandler extends AbstractResultHandler {

    public AdddRowHandler(ChartSql chartSql) {
        super(chartSql);
    }

    @Override
    public List<Map<String, Object>> handle(List<Map<String, Object>> origin) {
        List<Value> y = getLayer().getY();
        Value row = y.stream().filter(Value::total).findAny().orElse(null);
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
            res.add(newMap);
        });
        return res;
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
