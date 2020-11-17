package cn.shuangbofu.clairvoyance.core.meta.table;

import cn.shuangbofu.clairvoyance.core.chart.base.OrderType;
import cn.shuangbofu.clairvoyance.core.result.ResultHandler;
import cn.shuangbofu.clairvoyance.core.utils.Pair;

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

    default String havings() {
        return null;
    }

    default Pair<String, OrderType> sort() {
        return null;
    }

    default String last() {
        return "";
    }

    default List<ResultHandler> handlers() {
        return null;
    }
}
