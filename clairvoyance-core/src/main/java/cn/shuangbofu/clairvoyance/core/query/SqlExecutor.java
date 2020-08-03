package cn.shuangbofu.clairvoyance.core.query;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/8/1 14:33
 */
public interface SqlExecutor {

    List<Map<String, Object>> execute(String sql);
}
