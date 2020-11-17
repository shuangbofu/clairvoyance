package cn.shuangbofu.clairvoyance.core.chart.base;

import cn.shuangbofu.clairvoyance.core.meta.utils.SqlUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by shuangbofu on 2020/8/4 17:38
 */
@AllArgsConstructor
@Getter
public enum WhereOperation {
    /**
     *
     */
    eq("="),
    notEq("!="),
    gt(">"),
    lt("<"),
    ge(">="),
    le("<="),

    empty("= ''"),
    notEmpty("!= ''"),
    // TODO 日期怎么搞？

    include(""),
    notInclude(""),

    startInclude(""),
    endInclude(""),

    // 区间
    range(""),
    ;

    private final String symbol;

    public static WhereOperation valueOfSymbol(String symbol) {
        Optional<WhereOperation> any = Arrays.stream(values()).filter(i -> i.symbol.equals(symbol)).findAny();
        if (any.isPresent()) {
            return any.get();
        }
        throw new RuntimeException("not found where operation");
    }

    public String where(String key, Object value) {
        WhereOperation op = this;
        if (op.equals(range)) {
            Object[] arr = (Object[]) value;
            return String.format(" %s >= %s AND %s <= %s ", key, SqlUtil.standardValue(arr[0]), key, SqlUtil.standardValue(arr[1]));
        }
        String s = SqlUtil.standardValue(value);
        if (op.equals(empty)) {
            s = "";
            op = eq;
        } else if (op.equals(notEmpty)) {
            s = "";
            op = notEmpty;
        } else if (op.equals(include)) {
            return String.format("%s IN ( %s )", key, s);
        } else if (op.equals(notInclude)) {
            return String.format("%s NOT IN ( %s ) ", key, s);
        } else if (op.equals(startInclude)) {
            return key + " LIKE '%s" + value + "'";
        } else if (op.equals(endInclude)) {
            return key + " LIKE '" + value + "%s'";
        }
        // TODO 包含不支持 FIXME

        return String.format("%s %s %s", key, op.symbol, s);
    }
}
