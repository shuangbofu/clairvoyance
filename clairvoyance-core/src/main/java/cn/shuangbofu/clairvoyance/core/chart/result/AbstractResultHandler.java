package cn.shuangbofu.clairvoyance.core.chart.result;

import cn.shuangbofu.clairvoyance.core.chart.ChartLayer;
import cn.shuangbofu.clairvoyance.core.chart.ChartSql;

/**
 * Created by shuangbofu on 2020/9/30 10:18
 */
public abstract class AbstractResultHandler implements ResultHandler {
    protected ChartSql chartSql;

    public AbstractResultHandler(ChartSql chartSql) {
        this.chartSql = chartSql;
    }

    protected ChartLayer getLayer() {
        return chartSql.getLayer();
    }
}
