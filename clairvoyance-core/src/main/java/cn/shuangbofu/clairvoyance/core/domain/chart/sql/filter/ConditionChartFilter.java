package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.WhereCondition;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/9 下午5:44
 */
@Data
@Accessors(chain = true)
public class ConditionChartFilter extends ChartFilter {

    List<WhereCondition> conditions;

    @Override
    public String where() {
        if (conditions != null) {
            return conditions.stream().map(i -> {
                i.setName(getRealAliasName());
                return i.toString();
            }).collect(Collectors.joining(","));
        }
        return null;
    }

    @Override
    public void setupInner() {

    }
}
