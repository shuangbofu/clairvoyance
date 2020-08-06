package cn.shuangbofu.clairvoyance.core.meta.table;

import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.OrderType;
import cn.shuangbofu.clairvoyance.core.meta.source.JdbcSourceDb;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceDb;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceTable;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcUtil;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

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

    @Override
    public SourceTable sourceTable(String tableName) {
        return sourceDb.sourceTable(tableName);
    }


    @Override
    public boolean isValid() {
        return sourceDb.isValid();
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
        if (origin.endsWith(",")) {
            origin = origin.substring(0, origin.length() - 1);
        }
        return origin.replace("`", "")
                .replace("\"", "")
                .replace("'", "");
    }

    @Override
    public List<Map<String, Object>> run(Sql sql) {
        String sqlContent = createSql(sql);
        return run(sqlContent);
    }

    private String createSql(Sql sql) {
        String wheres = sql.wheres();
        List<String> selects = sql.selects();
        String groupBys = sql.groupBys();
        Pair<String, OrderType> sort = sql.sort();

        String selectsString = "*";
        if (selects != null && selects.size() > 0) {
            selectsString = String.join(",", selects);
        }
        String sqlContent = String.format("SELECT %s FROM %s", selectsString, getTableName());

        if (StringUtils.isNotEmpty(wheres)) {
            sqlContent += " WHERE " + wheres;
        }

        if (StringUtils.isNotEmpty(groupBys)) {
            sqlContent += " GROUP BY " + groupBys;
        }

        if (sort != null && StringUtils.isNotEmpty(sort.getFirst()) && sort.getSecond() != null) {
            sqlContent += String.format(" ORDER BY %s %s", sort.getFirst(), sort.getSecond().get());
        }

        if (sql.last() != null) {
            sqlContent += sql.last();
        }

        return sqlContent;
    }

    protected String getTableName() {
        String tableName = name().trim();
        if (!tableName.startsWith("`")) {
            tableName = "`" + tableName;
        }
        if (!tableName.endsWith("`")) {
            tableName += "`";
        }
        return tableName;
    }
}
