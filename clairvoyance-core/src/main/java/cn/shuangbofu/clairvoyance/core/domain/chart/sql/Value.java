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

/**
 * Created by shuangbofu on 2020/8/1 11:20
 */
@Data
public class Value extends FieldAlias {
    private AggregatorFunc aggregator;
    private String unit;

    // 行总计
    private Boolean total;
    @JsonIgnore
    private List<FieldAlias> allFields;

    private Formatter formatter;

    public void setAllValues(List<FieldAlias> allFields) {
        if (total()) {
            this.allFields = allFields;
        }
    }

    @Override
    public String getRealName() {
        if (total()) {
            return allFields.stream().filter(i -> i != this)
                    .map(i -> String.format("IFNULL(%s, 0)", i.getRealName()))
                    .collect(Collectors.joining(" + "));
        }
        String name = super.getRealName();
        return withAggregator(agg -> agg.wrapWithField(name), name);
    }

    @Override
    @JsonProperty("aggregatorAlias")
    public String getRealAliasName0() {
        if (total()) {
            return "行总计";
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

    @Data
    public static class Formatter {

        private CheckType check;
        // 使用千分分隔符
        private Boolean millesimal;
        private FormatterUnit unit;
        private Integer numDigit;
        private Integer percentDigit;

        @Getter
        public enum CheckType {
            num, percent
        }

        @Getter
        public enum FormatterUnit {
            ten_thousand, billion, k, g, m
        }
    }
}
