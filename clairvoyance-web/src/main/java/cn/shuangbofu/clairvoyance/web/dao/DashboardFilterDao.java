package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.web.entity.DashboardFilter;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/14 17:55
 */
public class DashboardFilterDao extends BaseDao<DashboardFilter> {
    public DashboardFilterDao() {
        super(DashboardFilter.class);
    }

    public List<DashboardFilter> getListByDashboardId(Long dashboardId) {
        return findListBy(q -> q.where(DashboardFilter::getDashboardId, dashboardId));
    }

    public void deleteByDashboardId(Long dashboardId) {
        updateBy(q -> q.set(DashboardFilter::getDeleted, true).where(DashboardFilter::getDashboardId, dashboardId));
    }
}
