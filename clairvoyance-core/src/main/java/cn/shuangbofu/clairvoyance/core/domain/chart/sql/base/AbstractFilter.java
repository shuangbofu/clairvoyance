package cn.shuangbofu.clairvoyance.core.domain.chart.sql.base;

import cn.shuangbofu.clairvoyance.core.domain.field.Field;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/1 11:27
 * <p>
 * WHERE 条件
 */
@Data
public abstract class AbstractFilter extends Field {
    FilterType filterType;
}
