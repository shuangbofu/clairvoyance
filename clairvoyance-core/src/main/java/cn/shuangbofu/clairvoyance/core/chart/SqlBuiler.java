package cn.shuangbofu.clairvoyance.core.chart;

import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import com.google.common.collect.Lists;

/**
 * Created by shuangbofu on 2020/8/10 21:16
 */
public class SqlBuiler {

    public static Sql select(String... selects) {
        return () -> Lists.newArrayList(selects);
    }
}
