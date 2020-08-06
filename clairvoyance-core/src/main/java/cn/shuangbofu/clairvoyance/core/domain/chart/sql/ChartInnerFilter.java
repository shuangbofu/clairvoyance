package cn.shuangbofu.clairvoyance.core.domain.chart.sql;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.AbstractFilter;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/1 11:15
 */
@Data
public class ChartInnerFilter extends AbstractFilter {

    @Override
    public String where() {
        return null;
    }
}
