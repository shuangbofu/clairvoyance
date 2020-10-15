package cn.shuangbofu.clairvoyance.core.chart.sql.base;

/**
 * Created by shuangbofu on 2020/8/5 下午10:29
 */
public enum OrderType {
    /**
     *
     */
    desc, asc;

    public String get() {
        return toString().toUpperCase();
    }
}
