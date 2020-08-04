package cn.shuangbofu.clairvoyance.core.meta;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/8/4 上午10:20
 */
public interface Sql {

    List<String> selects();

    List<String> groupBys();

    Map<String, Object> wheres();

    Order order();
}
