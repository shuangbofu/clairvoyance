package cn.shuangbofu.clairvoyance.core.chart.filter;

import cn.shuangbofu.clairvoyance.core.chart.base.WhereCondition;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/9 下午5:44
 */
@Data
@Accessors(chain = true)
public class ConditionChartFilter extends AbstractInnerChartFilter implements InnerChartFilter {

    List<WhereCondition> conditions;

    @Override
    public String where0() {
        if (conditions != null) {
            return conditions.stream().map(i -> {
                i.setName(getRealName());
                return i.toString();
            })
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.joining(","));
        }
        return null;
    }

    @Override
    public void setupInner() {

    }
}
