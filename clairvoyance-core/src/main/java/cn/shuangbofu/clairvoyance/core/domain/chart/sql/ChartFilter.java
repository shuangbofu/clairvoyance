package cn.shuangbofu.clairvoyance.core.domain.chart.sql;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.AbstractFilter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ExactChartFilter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/1 11:14
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "filterType", visible = true)
@JsonSubTypes(@JsonSubTypes.Type(value = ExactChartFilter.class, name = ChartFilter.EXACT))
public abstract class ChartFilter extends AbstractFilter {
    public static final String EXACT = "exact";
    public static final String CONDITION = "condition";
    public static final String EXPRESSION = "expression";
    public String filterType;
}