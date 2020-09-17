package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Value;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.Filter;

import java.util.List;

/**
 * Created by shuangbofu on 2020/9/14 19:51
 */
public interface InnerFilter extends Filter {
    /**
     * innerFilter的特殊处理
     */
    void setupInner();

    /**
     * @param values
     */
    void setValues(List<Value> values);
}
