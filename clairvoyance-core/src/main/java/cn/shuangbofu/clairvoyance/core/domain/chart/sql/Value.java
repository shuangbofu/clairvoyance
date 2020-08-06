package cn.shuangbofu.clairvoyance.core.domain.chart.sql;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.AggregatorFunc;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.FieldAlias;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/1 11:20
 */
@Data
public class Value extends FieldAlias {
    AggregatorFunc aggregator;
    private String unit;

    @Override
    public String getQueryName() {
        return aggregator.wrapWithField(name);
    }

    @Override
    public String getFinalTitle() {
        if (aggregator != null) {
            return aggregator.wrapWithTitle(title);
        }
        return title;
    }
}
