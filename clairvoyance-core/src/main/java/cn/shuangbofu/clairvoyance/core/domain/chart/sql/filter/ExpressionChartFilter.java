package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.ChartFilter;
import cn.shuangbofu.clairvoyance.core.meta.utils.SqlUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/8/9 下午5:49
 * TODO  保存时需要校验
 */
@Data
@Accessors(chain = true)
public class ExpressionChartFilter extends ChartFilter {
    private String sql;

    @Override
    public String where() {
        return SqlUtil.standardWhereSql(sql);
    }
}
