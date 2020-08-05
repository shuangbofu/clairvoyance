package cn.shuangbofu.clairvoyance.core.domain.chart.sql.base;

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
        if (op.equals(empty)) {
            value = "";
            op = eq;
        } else if (op.equals(notEmpty)) {
            value = "";
            op = notEmpty;
        }
        return String.format("%s %s %s", key, op.symbol, value);
    }
}
