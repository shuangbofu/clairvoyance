package cn.shuangbofu.clairvoyance.core.domain.chart.sql;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.FieldAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/9/1 14:34
 */
@Data
public class RowTotal extends FieldAlias {

    @JsonIgnore
    private List<Value> allValues;

    public RowTotal setAllValues(List<Value> values) {
        allValues = values;
        return this;
    }

    @Override
    public String getRealName() {
        return allValues.stream().map(i -> String.format("IFNULL(%s, 0)", i.getRealName())).collect(Collectors.joining(" + "));
    }

    @Override
    protected String getRealAliasName0() {
        return "行总计";
    }
}
