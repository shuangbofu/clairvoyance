package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.web.entity.DashboardLink;
import com.google.common.collect.Lists;

import java.util.List;

public class DashboardLinkDao extends BaseDao<DashboardLink> {

    public DashboardLinkDao() {
        super(DashboardLink.class);
    }


    /**
     * 根据图表删除图表关联
     *
     * @param chartId
     */
    public int removeChartLink(Long chartId) {
        return deleteBy(q -> q.where(DashboardLink::getChartId, chartId));
    }

    /**
     * 查询仪表盘的图表联动信息
     *
     * @param dashboardId
     * @return
     */
    public List<DashboardLink> getListByDashboardId(Long dashboardId) {
        return findListBy(q -> q.where(DashboardLink::getDashboardId, dashboardId));
    }

    /**
     * 查询主表的联动信息
     *
     * @param chartId
     * @return
     */
    public List<DashboardLink> getListByChartId(Long chartId) {
        return chartId != null ? findListBy(q -> q.where(DashboardLink::getChartId, chartId)) : Lists.newArrayList();
    }
}
