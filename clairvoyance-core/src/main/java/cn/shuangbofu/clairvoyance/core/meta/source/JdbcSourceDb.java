package cn.shuangbofu.clairvoyance.core.meta.source;

import cn.shuangbofu.clairvoyance.core.meta.table.JdbcSourceTable;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcParam;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcUtil;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.google.common.collect.Lists;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/3 下午9:57
 */
public abstract class JdbcSourceDb implements SourceDb {

    protected final JdbcParam param;
    private DruidPooledConnection connection = null;

    public JdbcSourceDb(JdbcParam param) {
        this.param = param;
    }

    @Override
    public List<String> tables() {
        return list("SHOW TABLES");
    }

    /**
     * @return
     */
//    public List<String> dbs() {
//        return list("SHOW DATABASES");
//    }
    private List<String> list(String order) {
        return JdbcUtil.query(getConnection(), order, resultSet -> {
            List<String> tables = Lists.newArrayList();
            while (resultSet.next()) {
                tables.add(resultSet.getString(1));
            }
            return tables;
        });
    }

    public DruidPooledConnection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = JdbcUtil.getConnection(param);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("error");
        }
        return connection;
    }

    @Override
    public List<Map<String, Object>> run(String sql) {
        return JdbcUtil.query(param, sql);
    }

    @Override
    public abstract JdbcSourceTable sourceTable(String tableName);

    @Override
    public List<SourceTable> sourceTables() {
        return tables().stream().map(this::sourceTable).collect(Collectors.toList());
    }

//    public List<JdbcSourceDb> sourceDbs() {
//        return dbs().stream().map(i -> {
//            String url = param.getJdbcUrl();
//            int index = url.lastIndexOf("/");
//            int index2 = url.lastIndexOf("?");
//            String newUrl = url.substring(0, index + 1) + i + url.substring(index2);
//            JdbcParam newParam = new JdbcParam()
//                    .setJdbcUrl(newUrl)
//                    .setUsername(param.getUsername())
//                    .setPassword(param.getPassword())
//                    .setClassName(param.getClassName());
//            JdbcSourceDb jdbcSourceDb = this;
//            return new JdbcSourceDb(newParam) {
//                @Override
//                public JdbcSourceTable sourceTable(String tableName) {
//                    return jdbcSourceDb.sourceTable(tableName);
//                }
//            };
//        }).collect(Collectors.toList());
//    }

    @Override
    public String name() {
        String url = param.getJdbcUrl();
        int index = url.lastIndexOf("/");
        int index2 = url.lastIndexOf("?");
        return url.substring(index + 1, index2);
    }


    @Override
    public boolean isValid() {
        try {
            return JdbcUtil.connectionIsValid(getConnection());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
