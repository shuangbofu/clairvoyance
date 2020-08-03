package cn.shuangbofu.clairvoyance.core.query;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/8/2 下午2:52
 */
public interface Result {
    List<Map<String, Object>> get();
}
