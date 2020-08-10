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

    public RangeResult(List<Map<String, Object>> originResult, String fieldName) {
        range = originResult.stream().map(i -> i.get(fieldName)).collect(Collectors.toList());
        total = range.size();
    }

    public <T> RangeResult(List<T> range) {
        List<Object> objects = Lists.newArrayList();
        objects.addAll(range);
        this.range = objects;
        total = range.size();
    }
}
