package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.web.entity.DashboardFilterSelected;

import java.util.List;

/**
 * @Date: 2020/11/4 3:44 下午
 */
public class DashboardFilterSelectedDao extends BaseDao<DashboardFilterSelected> {
    protected DashboardFilterSelectedDao() {
        super(DashboardFilterSelected.class);
    }

    /**
     * 根据仪表盘全局筛选器ID获取选项值
     *
     * @param dashboardFilterId
     * @return
     */
    public List<DashboardFilterSelected> getListByDashboardFilterId(Long dashboardFilterId) {
        return findListBy(q -> q.where(DashboardFilterSelected::getDashboardFilterId, dashboardFilterId));
    }

    /**
     * 删除dashboard相关选项值
     *
     * @param dashboardId
     */
    public int deleteByDashboardId(Long dashboardId) {
        return deleteBy(q -> q.where(DashboardFilterSelected::getDashboardId, dashboardId));
    }
}
