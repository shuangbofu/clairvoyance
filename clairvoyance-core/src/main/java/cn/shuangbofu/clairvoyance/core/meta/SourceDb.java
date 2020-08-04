package cn.shuangbofu.clairvoyance.core.meta;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/8/3 下午9:44
 */
public interface SourceDb {

    List<String> tables();

    List<Map<String, Object>> run(String sql);

    List<SourceTable> sourceTables();
}
