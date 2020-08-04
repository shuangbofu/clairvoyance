package cn.shuangbofu.clairvoyance.core.meta;

/**
 * Created by shuangbofu on 2020/8/4 上午12:32
 */
public class MysqlSourceDb extends JdbcSourceDb {

    public MysqlSourceDb(JdbcParam param) {
        super(param.setClassName(JdbcParam.MYSQL_DRIVER_CLASS_NAME));
    }

    @Override
    public JdbcSourceTable sourceTable(String tableName) {
        return new MysqlSourceTable(tableName, this);
    }
}
