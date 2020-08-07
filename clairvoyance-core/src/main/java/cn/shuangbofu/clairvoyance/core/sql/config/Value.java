package cn.shuangbofu.clairvoyance.core.sql.config;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.AggregatorFunc;

/**
 * Created by shuangbofu on 2020/8/7 17:07
 */
public class Value extends AliasField {

    /**
     * 单位
     */
    private String unit;

    /**
     * 聚合方法
     */
    private AggregatorFunc func;
}
