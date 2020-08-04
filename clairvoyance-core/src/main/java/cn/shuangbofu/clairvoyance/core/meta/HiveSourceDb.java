package cn.shuangbofu.clairvoyance.core.meta;

/**
 * Created by shuangbofu on 2020/8/4 上午12:39
 */
public class HiveSourceDb extends JdbcSourceDb {

    public HiveSourceDb(JdbcParam param) {
        super(param.setClassName(JdbcParam.HIVE_DRIVER_CLASS_NAME));
    }

    @Override
    public JdbcSourceTable sourceTable(String tableName) {
        return new HiveSourceTable(tableName, this);
    }
}
