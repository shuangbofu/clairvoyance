package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.web.entity.DashboardFilter;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/14 17:55
 */
public class DashboardFilterDao {
    public static Long create(DashboardFilter filter) {
        DashboardFilter insert = filter.insert();
        return insert.getId();
    }

    public static DashboardFilter byId(Long id) {
        return DashboardFilter.from().where(DashboardFilter::getId, id).one();
    }

    public static List<DashboardFilter> getListByDashboardId(Long dashboardId) {
        return DashboardFilter.from().where(DashboardFilter::getDashboardId, dashboardId).all();
    }

    public static List<DashboardFilter> inIds(List<Long> dashboardFilterIds) {
        return DashboardFilter.from().in(DashboardFilter::getId, dashboardFilterIds).all();
    }
}
