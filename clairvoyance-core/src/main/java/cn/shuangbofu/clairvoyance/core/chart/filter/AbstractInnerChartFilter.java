package cn.shuangbofu.clairvoyance.core.chart.filter;

import cn.shuangbofu.clairvoyance.core.field.Field;

import java.util.List;

/**
 * Created by shuangbofu on 2020/9/14 19:51
 */
public abstract class AbstractInnerChartFilter extends AbstractChartFilter implements InnerChartFilter {

    @Override
    public void setRealFields(List<Field> fields) {
        super.setRealChartFields(fields);
    }
}
