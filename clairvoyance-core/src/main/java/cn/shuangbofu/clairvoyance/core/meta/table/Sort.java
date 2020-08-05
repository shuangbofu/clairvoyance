package cn.shuangbofu.clairvoyance.core.meta.table;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.OrderType;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/4 上午10:22
 */
@Data
public class Sort {
    private Long id;
    private String axis;
    private String name;
    private OrderType orderType;
}
