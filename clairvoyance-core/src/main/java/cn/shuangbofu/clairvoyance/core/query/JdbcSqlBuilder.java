package cn.shuangbofu.clairvoyance.core.query;

import cn.shuangbofu.clairvoyance.core.domain.chart.SqlConfig;
import cn.shuangbofu.clairvoyance.core.domain.worksheet.SourceConfig;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;

/**
 * Created by shuangbofu on 2020/8/1 14:30
 */
public abstract class JdbcSqlBuilder extends AbstractSqlBuilder implements SqlBuilder {

    public JdbcSqlBuilder(SqlConfig sql, SourceConfig sourceConfig, String className) {
        super(new JdbcExecutor(sourceConfig, className), sql, sourceConfig);
    }

    @Override
    public String buildSql() {
        String selects = getSelects();
        if (StringUtils.isEmpty(selects)) {
            selects = "1";
        }
        String sqlContent = String.format("SELECT %s FROM %s", selects, getTableName());
        String groupBys = getGroupBys();
        if (StringUtils.isNotEmpty(groupBys)) {
            sqlContent += " GROUP BY " + groupBys;
        }
        return sqlContent;
    }
}
