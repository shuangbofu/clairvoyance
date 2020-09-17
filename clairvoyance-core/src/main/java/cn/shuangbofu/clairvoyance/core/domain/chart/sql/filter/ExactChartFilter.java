package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Value;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.AggregatorFunc;
import cn.shuangbofu.clairvoyance.core.domain.field.Field;
import cn.shuangbofu.clairvoyance.core.meta.utils.SqlUtil;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/8 下午8:45
 */
@Data
@Accessors(chain = true)
public class ExactChartFilter extends ChartFilter {
    public List<Object> range;
    public Boolean included;
    /**
     * 是否显示全部
     */
    private Boolean showAll;
    /**
     * 保存的下拉列表
     */
    private List<Object> templates;

    public ExactChartFilter() {

    }

    public ExactChartFilter(List<String> range, Boolean included, Long refId) {
        this.range = range.stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        this.included = included;
        setId(refId);
    }

    @Override
    public String where() {
        if (range == null || range.size() == 0) {
            return null;
        }
        String values = range.stream().map(SqlUtil::standardValue).collect(Collectors.joining(", "));
        if (!isY()) {
            return " " + getRealName() + (!included ? " NOT" : "") + " IN ( " + values + ")";
        } else {
            // TODO 根据不同聚合函数生成不同条件 FIXME
            AggregatorFunc aggregator = getAggregator();
        }
        return null;
    }

    @Override
    public void setupInner() {
        included = true;
    }

    @JsonProperty("aggregator")
    public AggregatorFunc getAggregator() {
        Field realField = getRealField();
        if (realField != null && isY()) {
            Value value = (Value) realField;
            return value.getAggregator();
        }
        return null;
    }
}
