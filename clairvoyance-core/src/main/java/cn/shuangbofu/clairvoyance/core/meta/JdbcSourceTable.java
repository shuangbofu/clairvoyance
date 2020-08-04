package cn.shuangbofu.clairvoyance.core.meta;

import com.alibaba.druid.pool.DruidPooledConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/3 下午9:58
 */
public abstract class JdbcSourceTable implements SourceTable, SourceDb {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final String tableName;
    private final JdbcSourceDb sourceDb;
    private String createTableSql;

    public JdbcSourceTable(String tableName, JdbcSourceDb sourceDb) {
        this.sourceDb = sourceDb;
        this.tableName = tableName;
    }

    @Override
    public String name() {
        return tableName;
    }

    @Override
    public List<Map<String, Object>> run(String sql) {
        LOGGER.debug("SQL ===> {}", sql);
        return sourceDb.run(sql);
    }

    protected DruidPooledConnection getConnection() {
        return sourceDb.getConnection();
    }

    @Override
    public List<String> tables() {
        return sourceDb.tables();
    }

    @Override
    public List<SourceTable> sourceTables() {
        return sourceDb.sourceTables();
    }

    private String showCreateTable() {
        return JdbcUtil.query(getConnection(), "SHOW CREATE TABLE " + name(), resultSet -> {
            StringBuilder createSql = new StringBuilder();
            while (resultSet.next()) {
                createSql.append(resultSet.getString(showCreateColumnIndex())).append("\n");
            }
            return createSql.toString();
        });
    }

    protected abstract int showCreateColumnIndex();

    protected String getShowCreateTable() {
        if (createTableSql == null) {
            createTableSql = showCreateTable();
        }
        return createTableSql;
    }

    protected String clear(String origin) {
        return origin.replace("`", "")
                .replace("\"", "")
                .replace("'", "");
    }

    @Override
    public List<Map<String, Object>> run(Sql sql) {
        String sqlContent = getSql(sql);
        return run(sqlContent);
    }

    private String getSql(Sql sql) {
        Map<String, Object> wheres = sql.wheres();
        List<String> selects = sql.selects();
        List<String> groupBys = sql.groupBys();

        String selectsString = "1";
        if (selects != null && selects.size() > 0) {
            selectsString = String.join(",", selects);
        }
        String sqlContent = String.format("SELECT %s FROM %s", selectsString, name());

        if (wheres != null && wheres.size() > 0) {
            List<String> conditions = wheres.keySet().stream()
                    .map(key -> String.format("%s = %s", key, wheres.get(key)))
                    .collect(Collectors.toList());
            sqlContent += " WHERE " + String.join(" AND ", conditions);
        }

        if (groupBys != null && groupBys.size() > 0) {
            sqlContent += " GROUP BY " + String.join(",", groupBys);
        }

        if (sql.order() != null) {
            sqlContent += String.format(" ORDER %s %s", sql.order().getName(), sql.order().isDesc() ? "DESC" : "ASC");
        }
        return sqlContent;
    }
}
