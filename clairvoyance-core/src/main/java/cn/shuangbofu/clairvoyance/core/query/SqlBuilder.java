package cn.shuangbofu.clairvoyance.core.query;

/**
 * Created by shuangbofu on 2020/8/1 14:32
 */
public interface SqlBuilder {
    String buildSql();

    AbstractSqlBuilder.SqlRunner build();
}
