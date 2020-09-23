package cn.shuangbofu.clairvoyance.core.meta.utils;

import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.google.common.base.Stopwatch;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by shuangbofu on 2020/8/3 下午10:14
 */
public class JdbcUtil {

    private static final Map<JdbcParam, DruidDataSource> SOURCE_MAP = new ConcurrentHashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcUtil.class);

    public static boolean connectionIsValid(DruidPooledConnection connection) {
        return roundExRt(() -> connection.isValid(3), "check error", connection::close);
    }

    public static List<Map<String, Object>> query(JdbcParam param, String sql) {
        if (StringUtils.isEmpty(sql)) {
            return new ArrayList<>();
        }
        return roundExRt(() -> {
            String className = param.getClassName();
            Stopwatch stopwatch = Stopwatch.createStarted();
            LOGGER.info("SQL SUBMIT ===> {} ({})", sql, className);
            List<Map<String, Object>> result = getQueryRunner(param).query(sql, new MapListHandler());
            LOGGER.info("SQL END ===> [{}ms] {} ({})", stopwatch.stop().elapsed(TimeUnit.MILLISECONDS), sql, className);
            return result;
        }, "query error");
    }

    public static long update(JdbcParam param, String sql) {
        return roundExRt(() -> getQueryRunner(param).update(sql), "update error");
    }

    public static long insert(JdbcParam param, String sql) {
        return roundExRt(() -> {
            BigInteger bigInteger = getQueryRunner(param).insert(sql, new ScalarHandler<>());
            return bigInteger.longValue();
        }, "insert error");
    }

    public static long delete(JdbcParam param, String sql) {
        return update(param, sql);
    }

    public static <T> T queryMeta(DruidPooledConnection connection, String sql, Function<ResultSet, T> fuc) {
        return execute(connection, sql, PreparedStatement::executeQuery, resultSet -> {
            try {
                return fuc.apply(resultSet);
            } finally {
                if (resultSet != null) {
                    resultSet.close();
                }
            }
        });
    }

    public static int executeUpdate(DruidPooledConnection connection, String sql) {
        return execute(connection, sql, PreparedStatement::executeUpdate, i -> i);
    }

    private static <B, C> C execute(DruidPooledConnection connection, String sql, Function<PreparedStatement, B> fuc, Function<B, C> fuc2) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            return fuc2.apply(fuc.apply(statement));
        } catch (Exception e) {
            if (e instanceof SQLException) {
                throw new RuntimeException("run sql error", e);
            } else {
                throw new RuntimeException("parse result error", e);
            }
        } finally {
            if (statement != null) {
                roundEx(statement::close, "close statement error");
            }
            if (connection != null) {
                roundEx(connection::close, "close connection error");
            }
        }
    }

    public static QueryRunner getQueryRunner(JdbcParam param) {
        return new QueryRunner(getDatasource(param), true);
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
        druidDataSource.setUrl(param.getJdbcUrl());
        druidDataSource.setUsername(param.getUsername());
        druidDataSource.setPassword(param.getPassword());
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setValidationQuery("SELECT 1");
        druidDataSource.setConnectionErrorRetryAttempts(1);
        druidDataSource.setBreakAfterAcquireFailure(true);
        druidDataSource.setMaxWait(3000);
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
        return roundExRt(supplier, msg, () -> {
        });
    }

    private static <T> T roundExRt(Supplier<T> supplier, String msg, Runnable runnable) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(msg, e);
        } finally {
            try {
                if (runnable != null) {
                    runnable.run();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
