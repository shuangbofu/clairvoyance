package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import cn.shuangbofu.clairvoyance.core.meta.utils.SqlUtil;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/8 下午8:45
 */
@Data
@Accessors(chain = true)
public class ExactChartFilter extends AbstractInnerChartFilter implements InnerChartFilter {
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
    public String where0() {
        if (range == null || range.size() == 0) {
            return null;
        }
        String values = range.stream().map(SqlUtil::standardValue).collect(Collectors.joining(", "));
//        AggregatorFunc aggregator = getAggregator();
//        if (aggregator == null) {
//            return " " + getRealName() + (!included ? " NOT" : "") + " IN ( " + values + ")";
//        } else {
//            // TODO 根据不同聚合函数生成不同条件 FIXME 不记得啥意思了。。
//        }
        return " " + getRealName() + (!included ? " NOT" : "") + " IN ( " + values + ")";
    }

    @Override
    public void setupInner() {
        included = true;
    }
}
