package cn.shuangbofu.clairvoyance.core.query;

import cn.shuangbofu.clairvoyance.core.constant.SourceConfigConstant;
import cn.shuangbofu.clairvoyance.core.domain.chart.Sql;
import cn.shuangbofu.clairvoyance.core.domain.worksheet.SourceConfig;
import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/8/2 下午2:39
 */
public class MysqlBuilder extends JdbcSqlBuilder {

    public MysqlBuilder(Sql sql, SourceConfig sourceConfig) {
        super(sql, sourceConfig, SourceConfigConstant.MYSQL_DRIVER_CLASS_NAME);
    }

    public static void main(String[] args) {
        String config = "{\"filters\":[],\"innerFilters\":[],\"order\":{},\"tableName\":\"node\",\"x\":[{\"name\":\"node_type\",\"nickName\":\"node_type\",\"queryName\":\"node_type\",\"title\":\"node_type\"},{\"name\":\"ref_id\",\"nickName\":\"ref_id\",\"queryName\":\"ref_id\",\"title\":\"ref_id\"}],\"y\":[{\"aggregator\":\"DISTINCT_COUNT\",\"name\":\"id\",\"nickName\":\"id(去重计数)\",\"queryName\":\"COUNT( DISTINCT id )\",\"title\":\"id\"}]}";

        SourceConfig source = new SourceConfig();
//        source.put(SourceConfigConstant.JDBC_URL, "jdbc:mysql://127.0.0.1:3306/clairvoyance?useSSL=false&characterEncoding=UTF-8");
//        source.put(SourceConfigConstant.USERNAME, "root");
//        source.put(SourceConfigConstant.PASSWORD, "root");
        source.setTableName("node");
        Sql sql = JSON.parseObject(config, Sql.class);

        MysqlBuilder builder = new MysqlBuilder(sql, source);

        String s = builder.buildSql();
        System.out.println(s);
        List<Map<String, Object>> maps = builder.build().get();

        for (Map<String, Object> map : maps) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
        }

//        System.out.println(source.toJSONString());
    }
}
