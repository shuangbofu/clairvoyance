import cn.shuangbofu.clairvoyance.core.meta.source.MysqlSourceDb;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceTable;
import cn.shuangbofu.clairvoyance.core.meta.table.Column;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcParam;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/8/3 下午11:43
 */
public class Test {

    public static void main(String[] args) {
        JdbcParam param = new JdbcParam()
                .setUsername("root")
                .setPassword("root")
                .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/clairvoyance?useSSL=false&characterEncoding=UTF-8");

        MysqlSourceDb db = new MysqlSourceDb(param);
        List<String> tables = db.tables();
        SourceTable table = db.sourceTable("node");

        List<Column> columns = table.columns();

        String comment = table.comment();

        System.out.println(tables);
        System.out.println(columns);
        System.out.println(comment);

        List<Map<String, Object>> results = table.run(new Sql() {

            @Override
            public List<String> selects() {
                return Lists.newArrayList("*");
            }

            @Override
            public String wheres() {
                return "id=1";
            }
        });

        List<Map<String, Object>> maps = db.run("select * from node");
        printResults(results);
        printResults(maps);

        System.out.println(JSON.toJSONString(param));
        String chart = db.sourceTable("chart").comment();
        System.out.println(chart);
    }

    private static void printResults(List<Map<String, Object>> results) {
        for (Map<String, Object> result : results) {
            for (Map.Entry<String, Object> entry : result.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
        }
    }
}
