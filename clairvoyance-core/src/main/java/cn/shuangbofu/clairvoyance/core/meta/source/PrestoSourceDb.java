package cn.shuangbofu.clairvoyance.core.meta.source;

import cn.shuangbofu.clairvoyance.core.meta.table.JdbcSourceTable;
import cn.shuangbofu.clairvoyance.core.meta.table.PrestoSourceTable;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcParam;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/8/4 11:50
 */
public class PrestoSourceDb extends JdbcSourceDb {
    private final PrestoSqlCache prestoSqlCache = PrestoSqlCache.INSTANCE;

    public PrestoSourceDb(JdbcParam param) {
        super(param.setClassName(JdbcParam.PRESTO_DRIVER_CLASS_NAME));
    }

    @Override
    public JdbcSourceTable sourceTable(String tableName) {
        return new PrestoSourceTable(tableName, this);
    }

    @Override
    public List<Map<String, Object>> query(String sql) {
        return prestoSqlCache.getQueryResult(param, sql);
    }

//    public List<String> dbs() {
//        return list("SHOW SCHEMAS");
//    }
}
