package cn.shuangbofu.clairvoyance.core.meta.table;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.OrderType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/4 上午10:22
 */
@Data
public class Sort {
    private static final String AXIS_X = "x";
    private Long id;
    private String axis;
    private String name;
    private OrderType orderType;

    @JsonIgnore
    public boolean isX() {
        return AXIS_X.equals(axis);
    }
}
