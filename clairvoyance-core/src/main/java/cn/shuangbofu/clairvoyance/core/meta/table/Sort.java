package cn.shuangbofu.clairvoyance.core.meta.table;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.OrderType;
import cn.shuangbofu.clairvoyance.core.domain.field.AbstractChartField;
import cn.shuangbofu.clairvoyance.core.domain.field.Field;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/4 上午10:22
 */
@Data
public class Sort extends AbstractChartField {
    private static final String AXIS_X = "x";
    private String axis;
    private String name;
    private OrderType orderType;

    @JsonIgnore
    public boolean isX() {
        return AXIS_X.equals(axis);
    }

    @Override
    public void setRealFields(List<Field> fields) {
        super.setRealChartFields(fields);
    }
}
