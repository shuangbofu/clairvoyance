package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.OrderType;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.WhereCondition;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.WhereType;
import cn.shuangbofu.clairvoyance.core.meta.table.Sort;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import cn.shuangbofu.clairvoyance.core.meta.utils.SqlUtil;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/4 17:17
 */
@Data
public class PreviewCondition implements Sql {

    private Long workSheetId;
    private List<String> fields;
    private List<WhereCondition> wheres;
    private WhereType whereType;
    private String sql;
    private WhereLinker linker;
    private Sort sort;

    @Override
    public List<String> selects() {
        if (fields != null && fields.size() > 0) {
            return fields;
        } else {
            return Lists.newArrayList("*");
        }
    }

    @Override
    public String wheres() {
        if (WhereType.sql.equals(whereType)) {
            return sql;
        } else if (WhereType.condition.equals(whereType) && wheres.size() > 0) {
            return wheres.stream()
                    .map(WhereCondition::toString)
                    .collect(Collectors.joining(linker.get()));
        }
        return null;
    }

    /**
     * 解析SQL 检查where condition条件是否合法
     *
     * @param fields
     */
    public void checkParams(List<FieldSimpleVO> fields) {
        if (whereType == null) {
            return;
        }
        if (whereType.equals(WhereType.sql)) {
            if (StringUtils.isEmpty(sql)) {
                return;
            }
            List<String> whereKeys = SqlUtil.getWhereKeys(sql);
            String notExistKeys = whereKeys.stream().filter(i -> !fields.stream().map(FieldSimpleVO::getTitle).collect(Collectors.toList()).contains(i))
                    .collect(Collectors.joining(","));
            if (StringUtils.isNotEmpty(notExistKeys)) {
                throw new RuntimeException("不存在字段 " + notExistKeys);
            }
            sql = SqlUtil.standardWhereSql(sql);
            for (FieldSimpleVO field : fields) {
                String token = String.format("[%s]", field.getTitle());
                sql = sql.replace(token, field.getName());
            }
        }
        if (wheres == null || wheres.size() == 0) {
            return;
        }
        wheres.forEach(cond -> {
            long count = fields.stream().filter(i -> i.getName().equals(cond.getName())).count();
            if (count == 0) {
                throw new RuntimeException("不存在字段" + cond.getName());
            }
        });
    }

    @Override
    public Pair<String, OrderType> sort() {
        if (sort == null) {
            return null;
        }
        return new Pair<>(sort.getName(), sort.getOrderType());
    }

    @Override
    public String last() {
        return " LIMIT 1000";
    }

    enum WhereLinker {
        /**
         *
         */
        and, or;

        String get() {
            return String.format(" %s ", toString().toUpperCase());
        }
    }
}
