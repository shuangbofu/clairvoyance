package cn.shuangbofu.clairvoyance.web.service;

import cn.shuangbofu.clairvoyance.core.meta.source.*;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcParam;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.web.dao.Daos;
import cn.shuangbofu.clairvoyance.web.dao.DatasourceDao;
import cn.shuangbofu.clairvoyance.web.entity.Datasource;
import cn.shuangbofu.clairvoyance.web.entity.WorkSheet;
import org.springframework.stereotype.Component;

/**
 * Created by shuangbofu on 2020/8/4 11:35
 */
@Component
public class SqlQueryRunner {

    private final DatasourceDao datasourceDao = Daos.datasource();

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

    public SourceTable getSourceTable(WorkSheet sheet) {
        if (sheet.getSheetType().fromSource()) {
            Datasource datasource = datasourceDao.findOneById(sheet.getDatasourceId());
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

    public SourceDb getSourceDb(Long datasourceId) {
        Datasource datasource = datasourceDao.findOneById(datasourceId);
        return getSourceDb(datasource);
    }
}
