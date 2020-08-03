package cn.shuangbofu.clairvoyance.core.query;

import cn.shuangbofu.clairvoyance.core.domain.worksheet.SourceConfig;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/8/2 下午2:39
 */
public class JdbcExecutor implements SqlExecutor {

    QueryRunner runner;

    public JdbcExecutor(SourceConfig config, String driverClassName) {
        runner = new QueryRunner(getDatasource(config.getJdbcUrl(), config.getUsername(), config.getPassword(), driverClassName));
    }

    public static DruidDataSource getDatasource(String url, String username, String password, String driverClassName) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setValidationQuery("SELECT 1");
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("------------error----------");
        }
        if (driverClassName != null) {
            druidDataSource.setDriverClassName(driverClassName);
        }
        return druidDataSource;
    }

    @Override
    public List<Map<String, Object>> execute(String sql) {
        try {
            return runner.query(sql, new MapListHandler());
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("error");
        }
    }
}
