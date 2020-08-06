import cn.shuangbofu.clairvoyance.core.meta.source.HiveSourceDb;
import cn.shuangbofu.clairvoyance.core.meta.source.MysqlSourceDb;
import cn.shuangbofu.clairvoyance.core.meta.source.PrestoSourceDb;
import cn.shuangbofu.clairvoyance.core.meta.table.Column;
import cn.shuangbofu.clairvoyance.core.meta.table.JdbcSourceTable;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcParam;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/8/5 下午11:40
 */
public class Test3 {

    @Test
    public void testMysql() {
//        MysqlSourceDb db = new MysqlSourceDb(JdbcParam.mysqlDefault("127.0.0.1", "john", "root", "root"));

        // 建表
//        db.execute("CREATE TABLE \"user2\" (\n" +
//                "  \"id\" bigint(11) unsigned NOT NULL AUTO_INCREMENT,\n" +
//                "  PRIMARY KEY (\"id\")\n" +
//                ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;");
        // 插入数据
//        db.insert("INSERT INTO `user2` (`id`) VALUES (1);");

        // 修改数据
//        db.update("UPDATE `user2` SET id = 2");


        MysqlSourceDb db = new MysqlSourceDb(new JdbcParam("jdbc:mysql://172.16.248.10:3306/hive_meta_test?useSSL=false", "root", "root"));

        List<Column> columns = db.sourceTable("columns_v2").columns();

        System.out.println(columns);
    }

    @Test
    public void testHive() {
        HiveSourceDb hiveSourceDb = new HiveSourceDb(new JdbcParam("jdbc:hive://172.16.248.220:9002/hive", "hadoop", ""));

        boolean valid = hiveSourceDb.isValid();
        System.out.println(valid);

    }


    @Test
    public void testPresto() {
        PrestoSourceDb prestoSourceDb = new PrestoSourceDb(new JdbcParam("jdbc:presto://172.16.248.220:9999/hive/test", "hadoop", ""));
//        prestoSourceDb.execute("CREATE TABLE user (\n" +
//                "  id bigint COMMENT '主键',\n" +
//                "  name varchar COMMENT '姓名'\n" +
//                ")\n" +
//                "COMMENT '用户表'\n" +
//                "WITH (format = 'ORC')");

        List<String> tables = prestoSourceDb.tables();
        System.out.println(tables);

        JdbcSourceTable user = prestoSourceDb.sourceTable("user");
        List<Column> columns = user.columns();
        System.out.println(columns);
        System.out.println(user.comment());

//        aa.columns();

//        prestoSourceDb.insert("insert into user VALUES (1, 'james')");

        List<Map<String, Object>> result = prestoSourceDb.query("select * from user");

        result.forEach(m -> m.forEach((k, v) -> System.out.println(k + ":" + v)));

        System.out.println(prestoSourceDb.sourceTable("aa").columns());
//        aa.

//        List<Map<String, Object>> result = aa.run(() -> Lists.newArrayList("*"));
//        System.out.println(result.size());
//        result.forEach(r -> {
//            r.forEach((k, v) -> {
//                System.out.println(k + ":" + v);
//            });
//        });


//        List<Column> columns = aa.columns();
//        System.out.println(columns);
//        System.out.println(dbs);
//        List<String> tables = prestoSourceDb
//        System.out.println(tables);
    }
}
