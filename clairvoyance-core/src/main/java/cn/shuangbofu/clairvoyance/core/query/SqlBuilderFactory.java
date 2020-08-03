package cn.shuangbofu.clairvoyance.core.query;

import cn.shuangbofu.clairvoyance.core.domain.chart.Sql;
import cn.shuangbofu.clairvoyance.core.domain.worksheet.SourceConfig;
import cn.shuangbofu.clairvoyance.core.enums.SheetType;

/**
 * Created by shuangbofu on 2020/8/2 下午4:50
 */
public class SqlBuilderFactory {

    public static SqlBuilder getSqlBuilder(SheetType sheetType, Sql sql, SourceConfig sourceConfig) {
        switch (sheetType) {
            case mysql:
                return new MysqlBuilder(sql, sourceConfig);
            // TODO
            default:
                return null;
        }
    }
}
