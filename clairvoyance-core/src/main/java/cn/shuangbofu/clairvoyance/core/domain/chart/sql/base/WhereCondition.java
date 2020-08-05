package cn.shuangbofu.clairvoyance.core.domain.chart.sql.base;

import cn.shuangbofu.clairvoyance.core.meta.utils.SqlUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/8/4 17:37
 */
@Data
@Accessors(chain = true)
public class WhereCondition {

    String name;
    WhereOperation operation;
    Object value;

    @Override
    public String toString() {
        if (value instanceof String) {
            value = "'" + SqlUtil.clearQuotationMark((String) value) + "'";
        }
        return operation.where(name, value);
    }
}
