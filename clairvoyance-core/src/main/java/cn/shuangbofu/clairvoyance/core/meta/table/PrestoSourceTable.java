package cn.shuangbofu.clairvoyance.core.meta.table;

import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.meta.source.JdbcSourceDb;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/4 11:51
 */
public class PrestoSourceTable extends cn.shuangbofu.clairvoyance.core.meta.table.JdbcSourceTable {

    public PrestoSourceTable(String tableName, JdbcSourceDb sourceDb) {
        super(tableName, sourceDb);
    }

    @Override
    protected int showCreateColumnIndex() {
        return 1;
    }

    @Override
    protected Pair<String, List<Column>> parse2MetaInfo(String createTableSql) {
        String[] lines = createTableSql.split("\n");
        boolean fieldLine = false;
        String comment = "";
        boolean fieldEnd = false;
        List<Column> columns = Lists.newArrayList();
        for (String line : lines) {
            if (line.contains(")")) {
                fieldLine = false;
                fieldEnd = true;
            }
            if (fieldLine) {
                line = line.trim();
                String[] words = line.split(" ");
                String name = words[0];
                Column column = new Column()
                        .setName(name)
                        .setType(clear(words[1]))
                        .setComment(line.contains("COMMENT") && !"COMMENT".equals(name) ? clear(words[words.length - 1]) : "");
                columns.add(column);
            } else {
                if (line.contains("COMMENT")) {
                    String[] words = line.split(" ");
                    comment = clear(words[words.length - 1]);
                }
            }
            if (line.contains("(") && !fieldEnd) {
                fieldLine = true;
            }
        }
        return new Pair<>(comment, columns);
    }

    @Override
    protected String formatName(String origin) {
        return "\"" + origin + "\"";
    }

    @Override
    public String createSql(Sql sql) {
        String sqlContent = super.createSql(sql);
        if (StringUtils.isEmpty(sqlContent) || sqlContent.contains("SELECT 1 FROM " + getTableName())) {
            return null;
        }
        return convertQuotes(sqlContent);
    }

    private String convertQuotes(String sql) {
        return sql.replace("`", "\"");
    }
}
