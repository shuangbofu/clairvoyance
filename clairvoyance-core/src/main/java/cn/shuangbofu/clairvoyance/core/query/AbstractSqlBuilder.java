package cn.shuangbofu.clairvoyance.core.query;

import cn.shuangbofu.clairvoyance.core.domain.chart.Sql;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Dimension;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Value;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.FieldAlias;
import cn.shuangbofu.clairvoyance.core.domain.worksheet.SourceConfig;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/2 下午3:48
 */
public abstract class AbstractSqlBuilder implements SqlBuilder {

    private final SqlExecutor runner;
    private final Sql sql;
    private final SourceConfig sourceConfig;

    public AbstractSqlBuilder(SqlExecutor runner, Sql sql, SourceConfig sourceConfig) {
        this.runner = runner;
        this.sql = sql;
        this.sourceConfig = sourceConfig;
    }

    public String getSelects() {
        List<FieldAlias> fieldAliases = new ArrayList<>();
        fieldAliases.addAll(getX());
        fieldAliases.addAll(getY());

        return fieldAliases.stream()
                .map(FieldAlias::getQueryFinalName)
                .collect(Collectors.joining(", "));
    }

    public String getGroupBys() {
        List<FieldAlias> x = Lists.newArrayList(getX());
        if (x.size() > 0) {
            return x.stream()
                    .map(FieldAlias::getName).collect(Collectors.joining(", "));
        }
        return "";
    }

    public List<Value> getY() {
        return sql.getY().stream().filter(FieldAlias::isValid).collect(Collectors.toList());
    }

    public List<Dimension> getX() {
        return sql.getX().stream().filter(FieldAlias::isValid).collect(Collectors.toList());
    }

    /**
     * TODO 后续增加列之后需要修改逻辑
     *
     * @return
     */
    public boolean isEmpty() {
        return getY().size() == 0 && getX().size() == 0;
    }

    @Override
    public SqlRunner build() {
        String sqlContent = buildSql()
//                + " LIMIT 1000"
                ;
        return new SqlRunner(sqlContent, runner, isEmpty());
    }

    public String getTableName() {
        String tableName = sourceConfig.getTableName();
        tableName = tableName.trim();
        if (!tableName.startsWith("`")) {
            tableName = "`" + tableName;
        }
        if (!tableName.endsWith("`")) {
            tableName += "`";
        }
        return tableName;
    }

    public static class SqlRunner implements Result {
        private static final Logger LOGGER = LoggerFactory.getLogger(SqlRunner.class);
        private final String sql;
        private final SqlExecutor executor;
        private final boolean empty;

        public SqlRunner(String sql, SqlExecutor executor, boolean empty) {
            this.sql = sql;
            this.executor = executor;
            this.empty = empty;
        }

        @Override
        public List<Map<String, Object>> get() {
            if (!empty) {
                LOGGER.debug("\nSQL ===> {}", sql);
                return executor.execute(sql);
            } else {
                return Lists.newArrayList();
            }
        }
    }
}
