package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Value;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

/**
 * Created by shuangbofu on 2020/9/14 19:51
 */
public abstract class AbstractInnerChartFilter extends AbstractChartFilter implements InnerChartFilter {

    /**
     * @param values
     */

    @Override
    public void setValues(List<Value> values) {
        // 如果是y轴上的字段，通过uniqueId对应设置为y轴选择的字段
        if (uniqId != null) {
            Optional<Value> any = values.stream().filter(i -> uniqId.equals(i.getUniqId())).findAny();
            any.ifPresent(value -> setRealFields(Lists.newArrayList(value)));
        }
    }
}
