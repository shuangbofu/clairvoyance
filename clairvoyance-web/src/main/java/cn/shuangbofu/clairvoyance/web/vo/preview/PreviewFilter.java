package cn.shuangbofu.clairvoyance.web.vo.preview;

import cn.shuangbofu.clairvoyance.core.chart.base.Filter;
import cn.shuangbofu.clairvoyance.core.chart.base.OrderType;
import cn.shuangbofu.clairvoyance.core.meta.table.Sort;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import cn.shuangbofu.clairvoyance.core.utils.Pair;
import cn.shuangbofu.clairvoyance.web.vo.FieldSimpleVO;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/9 下午6:01
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "whereType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ExpressionPreviewFilter.class, name = PreviewFilter.SQL),
        @JsonSubTypes.Type(value = ConditionPreviewFilter.class, name = PreviewFilter.CONDITION)
})
@Data
public abstract class PreviewFilter implements Sql, Filter {

    public static final String SQL = "sql";
    public static final String CONDITION = "condition";

    protected String whereType;
    private List<String> fields;
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
    public Pair<String, OrderType> sort() {
        if (sort == null) {
            return null;
        }
        return new Pair<>(sort.getName(), sort.getOrderType());
    }

    @Override
    public String wheres() {
        return where();
    }

    public abstract void checkParams(List<FieldSimpleVO> fields);

    @Override
    public String last() {
        return " LIMIT 1000";
    }
}
