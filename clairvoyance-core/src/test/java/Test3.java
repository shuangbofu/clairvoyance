import cn.shuangbofu.clairvoyance.core.meta.source.MysqlSourceDb;
import cn.shuangbofu.clairvoyance.core.meta.table.Column;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcParam;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/5 下午11:40
 */
public class Test3 {

    public static void main(String[] args) {
        MysqlSourceDb db = new MysqlSourceDb(new JdbcParam("jdbc:mysql://172.16.248.10:3306/hive_meta_test?useSSL=false", "root", "root"));

        List<Column> columns = db.sourceTable("columns_v2").columns();

        for (Column column : columns) {
            System.out.println(column.getComment());
        }
    }
}
