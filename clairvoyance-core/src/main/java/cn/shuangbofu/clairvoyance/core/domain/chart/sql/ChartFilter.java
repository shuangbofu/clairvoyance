package cn.shuangbofu.clairvoyance.core.domain.chart.sql;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.AbstractFilter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.FilterType;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/1 11:14
 */
@Data
public class ChartFilter extends AbstractFilter {
    FilterType filterType;

    @Override
    public String where() {
        return null;
    }
}
