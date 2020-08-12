package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import cn.shuangbofu.clairvoyance.core.meta.utils.SqlUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/8 下午8:45
 */
@Data
@Accessors(chain = true)
public class ExactChartFilter extends ChartFilter {
    public List<Object> range;
    public Boolean included;

    @Override
    public String where() {
        if (range == null || range.size() == 0) {
            return null;
        }
        String values = range.stream().map(SqlUtil::standardValue).collect(Collectors.joining(", "));
        return " " + getRealName() + (!included ? " NOT" : "") + " IN ( " + values + ")";
    }
}
