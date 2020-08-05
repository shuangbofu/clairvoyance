package cn.shuangbofu.clairvoyance.core.meta.table;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/4 上午10:20
 */
public interface Sql {

    List<String> selects();

    default String groupBys() {
        return null;
    }

    default String wheres() {
        return null;
    }

    default Sort sort() {
        return null;
    }

    default String last() {
        return "";
    }
}
