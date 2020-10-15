package cn.shuangbofu.clairvoyance.core.chart;

import cn.shuangbofu.clairvoyance.core.chart.sql.base.Filter;
import cn.shuangbofu.clairvoyance.core.field.DrillField;
import cn.shuangbofu.clairvoyance.core.meta.utils.SqlUtil;

/**
 * Created by shuangbofu on 2020/8/12 13:27
 * 下钻
 */
public class Drill implements Filter {

    private final String value;
    private final DrillField field;


    public Drill(DrillField field, Object value) {
        this.value = SqlUtil.standardValue(value);
        this.field = field;
    }

    @Override
    public String where() {
        return field.getRealName() + " = " + value;
    }
}
