package cn.shuangbofu.clairvoyance.core.meta;

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

    private String url;
    private String username;
    private String pwd;
    private String className;

    public JdbcParam() {
    }

    public JdbcParam(String url, String username, String pwd) {
        this.url = url;
        this.username = username;
        this.pwd = pwd;
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
        return Objects.equals(url, jdbcParam.url) &&
                Objects.equals(className, jdbcParam.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, className);
    }

}
