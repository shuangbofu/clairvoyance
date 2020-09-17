package cn.shuangbofu.clairvoyance.core.domain.chart.sql;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.AggregatorFunc;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.FieldAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @JsonIgnore
    private List<FieldAlias> allFields;

    /**
     * 字段值显示格式
     */
    private Formatter formatter;

    public void setAllValues(List<FieldAlias> allFields) {
        if (total()) {
            this.allFields = allFields;
        }
    }

    @Override
    public String getRealName() {
        if (total()) {
            return getRowName(aggregator);
        }
        String name = super.getRealName();
        return withAggregator(agg -> agg.wrapWithField(name), name);
    }

    @Override
    @JsonProperty("aggregatorAlias")
    public String getRealAliasName0() {
        if (total()) {
            if (aggregator == null) {
                return "行总计";
            }
            return "行" + aggregator.getDesc();
        }
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

    /**
     * TODO presto需要修改函数
     */
    public String getRowName(AggregatorFunc func) {
        Stream<FieldAlias> fieldAliasStream = allFields.stream().filter(i -> i != this);
        if (AggregatorFunc.SUM.equals(func)) {
            return fieldAliasStream
                    .map(i -> String.format("IFNULL(%s, 0)", i.getRealName()))
                    .collect(Collectors.joining(" + "));
        } else if (AggregatorFunc.AVG.equals(func)) {
            return getRowName(AggregatorFunc.SUM) + "/ " + allFields.size() + " ";
        } else {
            // TODO 其他函数 字段间总计 FIXME
            return null;
        }
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

        @Getter
        public enum FormatterUnit {
            ten_thousand, billion, k, g, m
        }

        @Data
        public static class NumFormatter {
            private int digit;
        }

        @Data
        public static class PercentFormatter extends NumFormatter {
            /**
             * 使用千分分隔符
             */
            private Boolean millesimal;
            private FormatterUnit unit;
        }
    }
}
