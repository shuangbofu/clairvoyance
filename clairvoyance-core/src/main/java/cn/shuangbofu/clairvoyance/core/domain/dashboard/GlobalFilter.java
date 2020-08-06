package cn.shuangbofu.clairvoyance.core.domain.dashboard;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.AbstractFilter;
import io.swagger.annotations.ApiModel;

/**
 * Created by shuangbofu on 2020/7/30 下午10:22
 * 全局过滤器
 */
@ApiModel("仪表盘过滤器配置")
public class GlobalFilter extends AbstractFilter {

    @Override
    public String where() {
        return null;
    }
}
