package cn.shuangbofu.clairvoyance.core.meta.table;

import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.OrderType;
import cn.shuangbofu.clairvoyance.core.meta.source.JdbcSourceDb;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceDb;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceTable;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcUtil;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/8/3 下午9:58
 */
public abstract class JdbcSourceTable implements SourceTable, SourceDb {

    private final String tableName;
    private final JdbcSourceDb sourceDb;
    private Pair<String, List<Column>> metaInfo;

    public JdbcSourceTable(String tableName, JdbcSourceDb sourceDb) {
        this.sourceDb = sourceDb;
        this.tableName = tableName;
    }

    @Override
    public String name() {
        return tableName;
    }

    @Override
    public String comment() {
        return getMetaInfos().getFirst();
    }

    @Override
    public List<Column> columns() {
        return getMetaInfos().getSecond();
    }

    @Override
    public List<Map<String, Object>> query(String sql) {
        return sourceDb.query(sql);
    }

    @Override
    public long insert(String sql) {
        return sourceDb.insert(sql);
    }

    @Override
    public long update(String sql) {
        return sourceDb.update(sql);
    }

    @Override
    public int execute(String sql) {
        return sourceDb.execute(sql);
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
    public List<Map<String, Object>> run(Sql sql) {
        String sqlContent = createSql(sql);
        return query(sqlContent);
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

    @Override
    public String getTableName() {
        return "`" + name().trim() + "`";
    }
}
