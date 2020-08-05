package cn.shuangbofu.clairvoyance.core.meta.table;

import cn.shuangbofu.clairvoyance.core.meta.source.JdbcSourceDb;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/4 11:51
 */
public class PrestoSourceTable extends JdbcSourceTable {

    public PrestoSourceTable(String tableName, JdbcSourceDb sourceDb) {
        super(tableName, sourceDb);
    }

    @Override
    protected int showCreateColumnIndex() {
        return 0;
    }

    @Override
    public String comment() {
        return null;
    }

    @Override
    public List<Column> columns() {
        return null;
    }
}
