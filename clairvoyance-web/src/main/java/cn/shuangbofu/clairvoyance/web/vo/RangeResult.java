package cn.shuangbofu.clairvoyance.web.vo;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/6 12:02
 */
@Data
public class RangeResult {

    private List<Object> range;
    private int total;

    public RangeResult() {
        range = Lists.newArrayList();
        total = 0;
    }

    public RangeResult(List<Map<String, Object>> originResult, String fieldName) {
        this();
        originResult.forEach(result -> range.add(result.get(fieldName)));
        total += range.size();
    }

    public <T> RangeResult(List<T> range) {
        List<Object> objects = Lists.newArrayList();
        objects.addAll(range);
        this.range = objects;
        total = range.size();
    }

    public List<Object> getRange() {
        return range.stream().distinct().collect(Collectors.toList());
    }

    public RangeResult concat(RangeResult rangeResult) {
        range.addAll(rangeResult.getRange());
        total += rangeResult.getTotal();
        return this;
    }
}
