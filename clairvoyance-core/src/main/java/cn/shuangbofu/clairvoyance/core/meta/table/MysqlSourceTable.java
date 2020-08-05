package cn.shuangbofu.clairvoyance.core.meta.table;

import cn.shuangbofu.clairvoyance.core.meta.source.JdbcSourceDb;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/3 下午11:31
 */
public class MysqlSourceTable extends JdbcSourceTable {

    public MysqlSourceTable(String tableName, JdbcSourceDb sourceDb) {
        super(tableName, sourceDb);
    }

    @Override
    public String comment() {
        String createTableSql = getShowCreateTable();
        String[] lines = createTableSql.split("\n");
        String lastLine = lines[lines.length - 1];
        int index = lastLine.indexOf("COMMENT=");
        if (index != -1) {
            return clear(lastLine.substring(index + 8));
        }
        return "";
    }

    @Override
    public List<Column> columns() {
        List<Column> columns = Lists.newArrayList();
        String createTableSql = getShowCreateTable();
        String[] lines = createTableSql.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("`") || line.startsWith("\"") || line.startsWith("'")) {
                String[] word = line.split(" ");
                Column column = new Column()
                        .setName(clear(word[0]))
                        .setType(word[1])
                        .setComment(line.contains("COMMENT") ? clear(word[word.length - 1]) : "");
                columns.add(column);
            }
        }
        return columns;
    }

    @Override
    protected int showCreateColumnIndex() {
        return 2;
    }
}
