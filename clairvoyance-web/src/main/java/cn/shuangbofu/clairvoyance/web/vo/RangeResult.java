package cn.shuangbofu.clairvoyance.web.vo;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/6 12:02
 */
@Accessors(chain = true)
@Data
public class RangeResult {

    private List<Object> range;
    private int total;

    public RangeResult() {

    }

    public RangeResult(List<Map<String, Object>> originResult) {
        this();
        range = originResult.stream().map(Map::values).flatMap(Collection::stream).collect(Collectors.toList());
        total += range.size();
    }

    public static <T> RangeResult newRangeResult(List<T> range) {
        return new RangeResult().setRange(Lists.newArrayList(range));
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
