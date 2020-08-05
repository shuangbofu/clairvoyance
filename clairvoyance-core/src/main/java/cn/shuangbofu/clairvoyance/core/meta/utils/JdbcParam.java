package cn.shuangbofu.clairvoyance.core.meta.utils;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * Created by shuangbofu on 2020/8/3 下午10:00
 */
@Data
@Accessors(chain = true)
public class JdbcParam {
    public static final String MYSQL_DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    public static final String HIVE_DRIVER_CLASS_NAME = "org.apache.hive.jdbc.HiveDriver";
    public static final String PRESTO_DRIVER_CLASS_NAME = "io.prestosql.jdbc.PrestoDriver";

    private String jdbcUrl;
    private String username;
    private String password;
    private String className;

    public JdbcParam() {
    }

    public JdbcParam(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JdbcParam jdbcParam = (JdbcParam) o;
        return Objects.equals(jdbcUrl, jdbcParam.jdbcUrl) &&
                Objects.equals(className, jdbcParam.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jdbcUrl, className);
    }

}
