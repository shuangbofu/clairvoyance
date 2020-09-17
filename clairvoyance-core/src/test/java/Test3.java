import cn.shuangbofu.clairvoyance.core.meta.source.HiveSourceDb;
import cn.shuangbofu.clairvoyance.core.meta.source.MysqlSourceDb;
import cn.shuangbofu.clairvoyance.core.meta.source.PrestoSourceDb;
import cn.shuangbofu.clairvoyance.core.meta.table.Column;
import cn.shuangbofu.clairvoyance.core.meta.table.JdbcSourceTable;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcParam;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        HiveSourceDb hiveSourceDb = new HiveSourceDb(new JdbcParam("jdbc:hive://172.16.248.220:7001/hive", "hadoop", ""));

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

        String[] names = {"大内香澄", "石渡怜子", "落合玲子", "徳永洋子", "东郷良子", "宇田川真奈美", "尾崎奈绪子", "森真智子", "水口美千子", "细井千春", "戸田京香", "桜井利江", "田岛珠恵",
                "小柳奈绪子", "滝沢美和子", "奥野麻纪", "高崎紫", "后藤晴", "楠奈保", "黒川美雪", "金井杏子", "管美知子", "须藤秋", "宫城有美子",
                "星野姫子", "金田佐江子", "板垣弓子", "宫田利佳", "天野与志子", "影山恵里", "川名加奈子", "深田兰", "高冈加悦", "落合椎名", "寺岛纪",
                "滝沢志野", "李奈穂子", "井上景子", "塚田泉美", "北田安喜子", "古田薫理", "小野田恭子", "吉野里恵", "平贺ぶ忍", "福山福子", "石山郁子",
                "寺岛美绪", "小野寺美沙", "岩本操", "日野恋", "根本和歌子", "城智子", "大场美咲", "福岛曜子", "长谷川竜子", "西原伊久美", "神苑子",
                "浅井未亜", "平松有美", "柳家美奈子", "洼田纪子", "宇佐美美树", "佐竹美树", "凑与志子", "河原敏子", "桜井桃枝", "副岛敏美", "细野桜子",
                "浜田堇", "村田留美子", "平冈奈子", "大场嘉穂理", "椿美纱绪", "下川优子", "大场尤娜", "奥野有子", "小室纪代美", "矢野代志子", "杉有纪",
                "门田理沙", "吉野沙罗", "高见俊美", "藤田静香", "芝林檎", "三上梨乃", "酒井绫子", "谷本芽衣子", "岩井能子", "田代真理子", "江川莫妮卡",
                "水野裕子", "本间絵理奈", "平井美智子", "柳田安", "豊岛福子", "柏木里奈", "矢岛杏子", "武田江美", "平松雪絵", "奥山恵里"};

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20000; i++) {
            try {
                int index = new SecureRandom().nextInt(100);
                user.insert(String.format("INSERT INTO user Values(%s, '%s')", index, names[index]));
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }

//        aa.columns();

//        prestoSourceDb.insert("insert into user VALUES (1, 'james')");

//        List<Map<String, Object>> result = prestoSourceDb.query("select * from user");

//        result.forEach(m -> m.forEach((k, v) -> System.out.println(k + ":" + v)));

//        System.out.println(prestoSourceDb.sourceTable("aa").columns());
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
