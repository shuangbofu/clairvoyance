package cn.shuangbofu.clairvoyance.core.meta.table;

import cn.shuangbofu.clairvoyance.core.utils.Pair;
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
    protected int showCreateColumnIndex() {
        return 2;
    }

    @Override
    protected Pair<String, List<Column>> parse2MetaInfo(String createTableSql) {
        List<Column> columns = Lists.newArrayList();
        String[] lines = createTableSql.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("`") || line.startsWith("\"") || line.startsWith("'")) {
                String[] word = line.split(" ");
                String name = clear(word[0]);
                Column column = new Column()
                        .setName(name)
                        .setType(word[1])
                        .setComment(line.contains("COMMENT") && !"COMMENT".equals(name) ? clear(word[word.length - 1]) : "");
                columns.add(column);
            }
        }

        String comment = "";
        String lastLine = lines[lines.length - 1];
        int index = lastLine.indexOf("COMMENT=");
        if (index != -1) {
            comment = clear(lastLine.substring(index + 8));
        }
        return new Pair<>(comment, columns);
    }
}
