package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.Filter;
import cn.shuangbofu.clairvoyance.core.domain.field.AbstractChartField;
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
})
public abstract class ChartFilter extends AbstractChartField implements Filter, InnerFilter {
    public static final String EXACT = "exact";
    public static final String CONDITION = "condition";
    public static final String EXPRESSION = "expression";
    public String filterType;
}
