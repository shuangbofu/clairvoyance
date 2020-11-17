package cn.shuangbofu.clairvoyance.core.result;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/9/30 10:06
 */
@FunctionalInterface
public interface ResultHandler {
    List<Map<String, Object>> handle(List<Map<String, Object>> origin);
}
