package cn.shuangbofu.clairvoyance.core.meta.table;

import cn.shuangbofu.clairvoyance.core.chart.result.ResultHandler;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceDb;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceTable;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/9/29 11:11
 */
public abstract class AbstractSourceTable implements SourceTable, SourceDb {

    protected final String tableName;
    protected SourceDb sourceDb;

    public AbstractSourceTable(SourceDb sourceDb, String tableName) {
        this.sourceDb = sourceDb;
        this.tableName = tableName;
    }

    @Override
    public String name() {
        return tableName;
    }

    @Override
    public List<Map<String, Object>> run(Sql sql) {
        String sqlContent = createSql(sql);
        if (sqlContent == null) {
            return Lists.newArrayList();
        }
        List<Map<String, Object>> origin = query(sqlContent);
        return handle(origin, 0, sql.handlers());
    }


    private List<Map<String, Object>> handle(List<Map<String, Object>> origin, int index, List<ResultHandler> handlers) {
        if (handlers != null && handlers.size() > index) {
            ResultHandler handler = handlers.get(index);
            List<Map<String, Object>> after = handler.handle(origin);
            index += 1;
            return handle(after, index, handlers);
        }
        return origin;
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

    @Override
    public SourceDb getSourceDb() {
        return sourceDb;
    }
}
