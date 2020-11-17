package cn.shuangbofu.clairvoyance.core.chart.filter;

import cn.shuangbofu.clairvoyance.core.meta.utils.SqlUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/8/9 下午5:49
 * TODO  保存时需要校验
 */
@Data
@Accessors(chain = true)
public class ExpressionChartFilter extends AbstractInnerChartFilter implements InnerChartFilter {
    private String sql;

    @Override
    public String where0() {
        sql = SqlUtil.standardWhereSql(sql);
        return sql.replace("[" + getTitle() + "]", getRealName());
    }

    @Override
    public void setupInner() {

    }
}
