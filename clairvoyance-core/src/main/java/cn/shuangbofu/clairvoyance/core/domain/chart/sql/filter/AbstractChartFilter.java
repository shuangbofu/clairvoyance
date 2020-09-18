package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Value;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.AggregatorFunc;
import cn.shuangbofu.clairvoyance.core.domain.field.AbstractChartField;
import cn.shuangbofu.clairvoyance.core.domain.field.Field;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/1 11:14
 */
@Data
public abstract class AbstractChartFilter extends AbstractChartField implements ChartFilter {
    public String filterType;

    @Override
    @JsonProperty("aggregator")
    public AggregatorFunc getAggregator() {
        Field realField = getRealField();
        if (realField != null) {
            if (realField instanceof Value) {
                Value value = (Value) realField;
                return value.getAggregator();
            }
        }
        return null;
    }
}
