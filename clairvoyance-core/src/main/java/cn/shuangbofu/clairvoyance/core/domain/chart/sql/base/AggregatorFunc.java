package cn.shuangbofu.clairvoyance.core.domain.chart.sql.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by shuangbofu on 2020/8/1 11:38
 */
@AllArgsConstructor
@Getter
public enum AggregatorFunc {
    /**
     * 通用聚合方法
     */
    SUM("求和", "SUM( %s )"),
    AVG("平均值", "AVG( %s )"),
    COUNT("计数", "COUNT( %s )"),
    DISTINCT_COUNT("去重计数", "COUNT( DISTINCT %s )"),
    MIN("最小值", "MIN( %s )"),
    MAX("最大值", "MAX( %s )"),
    ;

    private final String desc;
    private final String funcFormat;

    public String wrapWithField(String field) {
        return String.format(funcFormat, field);
    }

    public String wrapWithTitle(String title) {
        return String.format("%s (%s)", title, getDesc());
    }
}
