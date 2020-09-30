package cn.shuangbofu.clairvoyance.core.meta.table;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.OrderType;
import cn.shuangbofu.clairvoyance.core.domain.field.AbstractChartField;
import cn.shuangbofu.clairvoyance.core.domain.field.Field;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/4 上午10:22
 */
@Data
public class Sort extends AbstractChartField {
    private static final String AXIS_X = "x";
    private String axis;
    private OrderType orderType;

    @Override
    public void setRealFields(List<Field> fields) {
        super.setRealChartFields(fields);
    }
}
