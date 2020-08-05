package cn.shuangbofu.clairvoyance.core.meta.table;

import cn.shuangbofu.clairvoyance.core.meta.source.JdbcSourceDb;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcUtil;
import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/4 上午12:13
 */
public class HiveSourceTable extends JdbcSourceTable {

    public HiveSourceTable(String tableNmae, JdbcSourceDb sourceDb) {
        super(tableNmae, sourceDb);
    }

    @Override
    protected int showCreateColumnIndex() {
        return 1;
    }

    @Override
    public String comment() {
        return null;
    }

    @Override
    public List<Column> columns() {
        return JdbcUtil.query(getConnection(), "DESC " + name(), resultSet -> {
            List<Column> columns = Lists.newArrayList();
            while (resultSet.next()) {
                String colName = resultSet.getString("col_name");
                if (StringUtils.isEmpty(colName)) {
                    break;
                }
                columns.add(new Column()
                        .setType(resultSet.getString("data_type"))
                        .setName(colName)
                        .setComment(resultSet.getString("comment")));
            }
            return columns;
        });
    }
}
