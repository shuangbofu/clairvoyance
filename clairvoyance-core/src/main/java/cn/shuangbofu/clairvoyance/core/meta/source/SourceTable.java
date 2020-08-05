package cn.shuangbofu.clairvoyance.core.meta.source;

import cn.shuangbofu.clairvoyance.core.meta.table.Column;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/8/3 下午9:41
 */
public interface SourceTable {
    String comment();

    String name();

    List<Column> columns();

    List<Map<String, Object>> run(Sql sql);
}
