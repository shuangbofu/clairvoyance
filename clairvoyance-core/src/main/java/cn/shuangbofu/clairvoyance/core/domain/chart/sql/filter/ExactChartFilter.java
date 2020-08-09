package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.ChartFilter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/8 下午8:45
 */
//@JSONType(typeName = ChartFilter.EXACT, orders = {
//        "filterType",
//        "included",
//        "range"
//})
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ExactChartFilter extends ChartFilter {
    public List<Object> range;
    public Boolean included;
    //    @JSONField(ordinal = 1)

    @Override
    public String where() {
        if (range == null || range.size() == 0) {
            return null;
        }
        String values = range.stream().map(i -> {
            if (i instanceof String) {
                return "'" + i.toString() + "'";
            }
            return i.toString();
        }).collect(Collectors.joining(", "));
        return " " + getName() + (!included ? " NOT" : "") + " IN ( " + values + ")";
    }
}
