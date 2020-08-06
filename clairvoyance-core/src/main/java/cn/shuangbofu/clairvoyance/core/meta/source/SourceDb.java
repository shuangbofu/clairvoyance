package cn.shuangbofu.clairvoyance.core.meta.source;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/8/3 下午9:44
 */
public interface SourceDb {

    long update(String sql);

    /**
     * 建表
     *
     * @param sql
     * @return
     */
    int execute(String sql);

    List<String> tables();

    /**
     * 查询数据
     *
     * @param sql
     * @return
     */
    List<Map<String, Object>> query(String sql);

    /**
     * 插入数据
     *
     * @param sql
     * @return
     */
    long insert(String sql);

    List<SourceTable> sourceTables();

    SourceTable sourceTable(String tableName);

    boolean isValid();

    String name();
}
