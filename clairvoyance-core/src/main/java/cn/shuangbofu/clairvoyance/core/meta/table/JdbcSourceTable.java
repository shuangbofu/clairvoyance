package cn.shuangbofu.clairvoyance.core.meta.table;

import cn.shuangbofu.clairvoyance.core.chart.sql.base.OrderType;
import cn.shuangbofu.clairvoyance.core.meta.source.JdbcSourceDb;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcUtil;
import cn.shuangbofu.clairvoyance.core.utils.Pair;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/3 下午9:58
 */
public abstract class JdbcSourceTable extends AbstractSourceTable {

    private final JdbcSourceDb sourceDb;
    private Pair<String, List<Column>> metaInfo;

    public JdbcSourceTable(String tableName, JdbcSourceDb sourceDb) {
        super(sourceDb, tableName);
        this.sourceDb = sourceDb;
    }

    protected String formatName(String origin) {
        return "`" + origin + "`";
    }

    @Override
    public String comment() {
        return getMetaInfos().getFirst();
    }

    @Override
    public List<Column> columns() {
        return getMetaInfos().getSecond();
    }

    protected DruidPooledConnection getConnection() {
        return sourceDb.getConnection();
    }

    private String showCreateTable() {
        return JdbcUtil.queryMeta(getConnection(), "SHOW CREATE TABLE " + name(), resultSet -> {
            StringBuilder createSql = new StringBuilder();
            while (resultSet.next()) {
                createSql.append(resultSet.getString(showCreateColumnIndex())).append("\n");
            }
            return createSql.toString();
        });
    }

    protected abstract int showCreateColumnIndex();

    protected abstract Pair<String, List<Column>> parse2MetaInfo(String createTableSql);

    private Pair<String, List<Column>> getMetaInfos() {
        if (metaInfo == null) {
            String showCreateTable = showCreateTable();
            metaInfo = parse2MetaInfo(showCreateTable);
        }
        return metaInfo;
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
    public String createSql(Sql sql) {
        String wheres = sql.wheres();
        List<String> selects = sql.selects();
        String groupBys = sql.groupBys();
        String havings = sql.havings();
        Pair<String, OrderType> sort = sql.sort();
        if (selects == null || selects.size() == 0) {
            return null;
        }
        String sqlContent = String.format("SELECT %s FROM %s", String.join(",", selects), getTableName());

        if (StringUtils.isNotEmpty(wheres)) {
            sqlContent += " WHERE " + wheres;
        }

        if (StringUtils.isNotEmpty(groupBys)) {
            sqlContent += " GROUP BY " + groupBys;
        }

        if (StringUtils.isNotEmpty(havings)) {
            sqlContent += " HAVING " + havings;
        }

        if (sort != null && StringUtils.isNotEmpty(sort.getFirst()) && sort.getSecond() != null) {
            sqlContent += String.format(" ORDER BY `%s` %s", sort.getFirst(), sort.getSecond().get());
        }

        if (sql.last() != null) {
            sqlContent += sql.last();
        }

        return sqlContent;
    }

    @Override
    public String getTableName() {
        return Arrays.stream(name().split("\\."))
                .map(this::formatName)
                .collect(Collectors.joining("."));
    }
}
