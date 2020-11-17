package cn.shuangbofu.clairvoyance.core.chart.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.function.Function;

/**
 * Created by shuangbofu on 2020/8/1 11:20
 */
@Data
public class Value extends FieldAlias {
    /**
     *
     */
    private AggregatorFunc aggregator;
    private String unit;

    /**
     * 行总计
     */
    private Boolean total;
    private Position position;

    /**
     * 字段值显示格式
     */
    private Formatter formatter;

    @Override
    public String getRealName() {
        if (total()) {
            return null;
        }
        String name = super.getRealName();
        return withAggregator(agg -> agg.wrapWithField(name), name);
    }

    @Override
    @JsonProperty("aggregatorAlias")
    public String getRealAliasName0() {
        String title = super.getRealAliasName0();
        return withAggregator(agg -> agg.wrapWithTitle(title), title);
    }

    public String withAggregator(Function<AggregatorFunc, String> get, String defaultValue) {
        if (defaultValue == null) {
            return null;
        }
        if (aggregator == null) {
            aggregator = AggregatorFunc.defaultFunc(getType());
        }
        if (aggregator != null) {
            return get.apply(aggregator);
        }
        return defaultValue;
    }

    @JsonIgnore
    public boolean total() {
        return total != null && total;
    }

    public enum Position {
        /**
         *
         */
        left, right
    }

    /**
     * 实际执行SQL无用，定义固定格式前端使用
     */
    @Data
    public static class Formatter {

        private CheckType check;
        private NumFormatter num;
        private PercentFormatter percent;

        @Getter
        public enum CheckType {
            num, percent
        }

        @Data
        public static class PercentFormatter {
            private int digit;
        }

        @Data
        public static class NumFormatter extends PercentFormatter {
            /**
             * 使用千分分隔符
             */
            private Boolean millesimal;

            /**
             * 10000        万
             * 100000000    亿
             * 1000         千
             * 1000000      M
             * 1000000000   G
             */
            private int unit;
        }
    }
}
