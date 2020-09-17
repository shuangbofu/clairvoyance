package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Value;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.Filter;
import cn.shuangbofu.clairvoyance.core.domain.field.AbstractChartField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

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

    private Long uniqId;

    @JsonIgnore
    public boolean isY() {
        return uniqId != null;
    }

    @Override
    public void setValues(List<Value> values) {
        // 如果是y轴上的字段，通过uniqueId对应设置为y轴选择的字段
        if (isY()) {
            List<Value> valueList = values.stream()
                    .filter(i -> uniqId.equals(i.getUniqId()))
                    .collect(Collectors.toList());
            setRealFields(Lists.newArrayList(valueList));
        }
    }
}
