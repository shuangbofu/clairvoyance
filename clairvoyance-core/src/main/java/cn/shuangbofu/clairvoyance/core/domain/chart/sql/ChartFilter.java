package cn.shuangbofu.clairvoyance.core.domain.chart.sql;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.Filter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ConditionChartFilter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ExactChartFilter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ExpressionChartFilter;
import cn.shuangbofu.clairvoyance.core.domain.field.Field;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/1 11:14
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "filterType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ExactChartFilter.class, name = ChartFilter.EXACT),
        @JsonSubTypes.Type(value = ConditionChartFilter.class, name = ChartFilter.CONDITION),
        @JsonSubTypes.Type(value = ExpressionChartFilter.class, name = ChartFilter.EXPRESSION),
        @JsonSubTypes.Type(value = ChartInnerFilter.class, name = ChartFilter.INNER)
})
public abstract class ChartFilter extends Field implements Filter {
    public static final String EXACT = "exact";
    public static final String CONDITION = "condition";
    public static final String EXPRESSION = "expression";
    public static final String INNER = "inner";
    public String filterType;
}