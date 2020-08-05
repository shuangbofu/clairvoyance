package cn.shuangbofu.clairvoyance.core.meta.source;

import cn.shuangbofu.clairvoyance.core.meta.table.JdbcSourceTable;
import cn.shuangbofu.clairvoyance.core.meta.table.PrestoSourceTable;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcParam;

/**
 * Created by shuangbofu on 2020/8/4 11:50
 */
public class PrestoSourceDb extends JdbcSourceDb {

    public PrestoSourceDb(JdbcParam param) {
        super(param.setClassName(JdbcParam.PRESTO_DRIVER_CLASS_NAME));
    }

    @Override
    public JdbcSourceTable sourceTable(String tableName) {
        return new PrestoSourceTable(tableName, this);
    }
}
