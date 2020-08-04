package cn.shuangbofu.clairvoyance.core.meta;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/8/3 下午11:43
 */
public class Test {

    public static void main(String[] args) {
        JdbcParam param = new JdbcParam()
                .setUsername("root")
                .setPwd("root")
                .setUrl("jdbc:mysql://127.0.0.1:3306/clairvoyance?useSSL=false&characterEncoding=UTF-8");

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
            public List<String> groupBys() {
                return null;
            }

            @Override
            public Map<String, Object> wheres() {
                HashMap<String, Object> whereMap = Maps.newHashMap();
                whereMap.put("id", 1);
                return whereMap;
            }

            @Override
            public Order order() {
                return null;
            }
        });

        List<Map<String, Object>> maps = db.run("select * from node");
        printResults(results);
        printResults(maps);

        System.out.println(JSON.toJSONString(param));
    }

    private static void printResults(List<Map<String, Object>> results) {
        for (Map<String, Object> result : results) {
            for (Map.Entry<String, Object> entry : result.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
        }
    }
}
