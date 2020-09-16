package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.Filter;

/**
 * Created by shuangbofu on 2020/9/14 19:51
 */
public interface InnerFilter extends Filter {
    /**
     * innerFilter的特殊处理
     */
    void setupInner();
}
