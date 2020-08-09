package cn.shuangbofu.clairvoyance.core.domain.chart.sql;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ExactChartFilter;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/1 11:15
 */
@Data
public class ChartInnerFilter extends ExactChartFilter {

    @Override
    public String where() {
        included = true;
        return super.where();
    }
}
