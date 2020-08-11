package cn.shuangbofu.clairvoyance.core.domain.chart.sql;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.AggregatorFunc;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.FieldAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.function.Function;

/**
 * Created by shuangbofu on 2020/8/1 11:20
 */
@Data
public class Value extends FieldAlias {
    private AggregatorFunc aggregator;
    private String unit;

    @Override
    public String getRealName() {
        String name = super.getRealName();
        return withAggregator(agg -> agg.wrapWithField(name), name);
    }

    @Override
    @JsonProperty("aggregatorAlias")
    public String getRealAliasName0() {
        String title = super.getRealAliasName0();
        return withAggregator(agg -> agg.wrapWithTitle(title), title);
    }

    public String withAggregator(Function<AggregatorFunc, String> get, String defaultValue) {
        if (defaultValue == null) {
            return null;
        }
        if (aggregator == null) {
            aggregator = AggregatorFunc.defaultFunc(getType());
        }
        if (aggregator != null) {
            return get.apply(aggregator);
        }
        return defaultValue;
    }
}
