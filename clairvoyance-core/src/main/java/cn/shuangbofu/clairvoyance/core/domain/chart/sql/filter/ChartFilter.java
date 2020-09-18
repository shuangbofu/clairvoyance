package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.AggregatorFunc;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.Filter;
import cn.shuangbofu.clairvoyance.core.domain.field.ChartField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by shuangbofu on 2020/9/17 下午11:53
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "filterType", visible = true)
public interface ChartFilter extends ChartField, Filter {
    String EXACT = "exact";
    String CONDITION = "condition";
    String EXPRESSION = "expression";

    @JsonProperty("aggregator")
    AggregatorFunc getAggregator();

    /**
     * 获取having条件
     */
    String having();
}
