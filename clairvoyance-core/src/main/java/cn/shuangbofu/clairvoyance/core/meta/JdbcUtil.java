package cn.shuangbofu.clairvoyance.core.meta;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shuangbofu on 2020/8/3 下午10:14
 */
public class JdbcUtil {

    private static final Map<JdbcParam, DruidDataSource> SOURCE_MAP = new ConcurrentHashMap<>();

    public static List<Map<String, Object>> query(JdbcParam param, String sql) {
        return roundExRt(() -> new QueryRunner(getDatasource(param)).query(sql, new MapListHandler()), "query error");
    }

    public static <T> T query(DruidPooledConnection connection, String sql, Function<ResultSet, T> fuc) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            return fuc.apply(resultSet);
        } catch (Exception e) {
            if (e instanceof SQLException) {
                throw new RuntimeException("run sql error", e);
            } else {
                throw new RuntimeException("parse result error", e);
            }
        } finally {
            if (resultSet != null) {
                roundEx(resultSet::close, "close resultSet error");
            }
            if (statement != null) {
                roundEx(statement::close, "close statement error");
            }
            if (connection != null) {
                roundEx(connection::close, "close connection error");
            }
        }
    }

    public static DruidPooledConnection getConnection(JdbcParam param) {
        DruidDataSource datasource = getDatasource(param);
        return roundExRt(datasource::getConnection, "get connection error");
    }

    public static DruidDataSource getDatasource(JdbcParam param) {
        return SOURCE_MAP.computeIfAbsent(param, k -> createDatasource(param));
    }

    private static DruidDataSource createDatasource(JdbcParam param) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(param.getUrl());
        druidDataSource.setUsername(param.getUsername());
        druidDataSource.setPassword(param.getPwd());
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setValidationQuery("SELECT 1");
        String className = param.getClassName();
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (className != null) {
            druidDataSource.setDriverClassName(className);
        }
        return druidDataSource;
    }

    private static void roundEx(Runnable runnable, String msg) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(msg, e);
        }
    }

    private static <T> T roundExRt(Supplier<T> supplier, String msg) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(msg, e);
        }
    }

    @FunctionalInterface
    public interface Function<T, R> {
        R apply(T t) throws Exception;
    }


    @FunctionalInterface
    private interface Runnable {
        void run() throws Exception;
    }

    @FunctionalInterface
    private interface Supplier<T> {
        T get() throws Exception;
    }
}
