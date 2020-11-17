package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.web.entity.Chart;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/7/31 15:19
 */
public class ChartDao extends BaseDao<Chart> {

    private final WhereCondition<Chart, Long> dshIdWhere = value -> q -> q.where(Chart::getDashboardId, value);

    public ChartDao() {
        super(Chart.class);
    }

    public List<Chart> findChartsByDshId(Long dshId) {
        return findListBy(q -> q.where(Chart::getDashboardId, dshId));
    }

    public List<Long> getWorkSheetIdsByDshId(Long dshId) {
        List<Chart> charts = findListBy(q -> dshIdWhere.where(dshId).apply(q.select("work_sheet_id")));
        return charts.stream().map(Chart::getWorkSheetId).collect(Collectors.toList());
    }

    public int updateSqlConfig(Long id, String sqlConfig) {
        return updateById(id, q -> q.set(Chart::getSqlConfig, sqlConfig));
    }
}
