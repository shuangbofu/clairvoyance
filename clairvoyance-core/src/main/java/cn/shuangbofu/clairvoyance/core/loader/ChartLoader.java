package cn.shuangbofu.clairvoyance.core.loader;

import cn.shuangbofu.clairvoyance.core.db.Chart;
import io.github.biezhi.anima.enums.OrderBy;

import java.util.List;

/**
 * Created by shuangbofu on 2020/7/31 15:19
 */
public class ChartLoader {

    public static List<Chart> getChartsByDshId(Long dShId) {
        return Chart.from().where(Chart::getDashboardId, dShId)
                .order(Chart::getGmtCreate, OrderBy.DESC)
                .all();
    }

    public static Chart byId(Long chartId) {
        return Chart.from().where(Chart::getId, chartId).one();
    }

    public static Long create(Chart chart) {
        Chart ct = chart.insert();
        return ct.getId();
    }

    public static void update(Chart chart) {
        chart.update();
    }
}
