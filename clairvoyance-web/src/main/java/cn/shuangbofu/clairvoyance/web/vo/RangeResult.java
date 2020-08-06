package cn.shuangbofu.clairvoyance.web.vo;

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

    public RangeResult(List<Object> range) {
        this.range = range;
        total = range.size();
    }
}
