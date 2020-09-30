package cn.shuangbofu.clairvoyance.web.service;

import cn.shuangbofu.clairvoyance.core.db.Datasource;
import cn.shuangbofu.clairvoyance.core.db.WorkSheet;
import cn.shuangbofu.clairvoyance.core.loader.DatasourceLoader;
import cn.shuangbofu.clairvoyance.core.meta.source.*;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcParam;
import cn.shuangbofu.clairvoyance.core.utils.JSON;

/**
 * Created by shuangbofu on 2020/8/4 11:35
 */
public class SqlQueryRunner {

    public static SourceTable getSourceTable(WorkSheet sheet) {
        if (sheet.getSheetType().fromSource()) {
            Datasource datasource = DatasourceLoader.getSource(sheet.getDatasourceId());
            SourceDb sourceDb = getSourceDb(datasource);
            if (sourceDb != null) {
                SourceTable sourceTable = sourceDb.sourceTable(datasource.getDbName() + "." + sheet.getTableName());
                if (sourceTable != null) {
                    return sourceTable;
                }
            }
        }

        throw new RuntimeException("not found source table");
    }

    public static SourceDb getSourceDb(Long datasourceId) {
        Datasource datasource = DatasourceLoader.getSource(datasourceId);
        return getSourceDb(datasource);
    }

    public static SourceDb getSourceDb(Datasource datasource) {
        if (datasource != null) {
            JdbcParam jdbcParam = JSON.parseObject(datasource.getConfig(), JdbcParam.class);
            switch (datasource.getType()) {
                case mysql:
                    return new MysqlSourceDb(jdbcParam);
                case hive:
                    return new HiveSourceDb(jdbcParam);
                case presto:
                    return new PrestoSourceDb(jdbcParam);
                default:
                    return null;
            }
        }
        return null;
    }

    public static boolean checkConnectivity(Datasource datasource) {
        return getSourceDb(datasource).isValid();
    }
}
