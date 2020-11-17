package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.web.entity.Dashboard;

/**
 * Created by shuangbofu on 2020/7/30 下午10:39
 */
public class DashBoardDao extends BaseDao<Dashboard> {

    public DashBoardDao() {
        super(Dashboard.class);
    }

    public int updateLayout(Long id, String layoutConfig) {
        return updateById(id, q -> q.set(Dashboard::getLayoutConfig, layoutConfig));
    }

    public int rename(Long id, String name) {
        return updateById(id, q -> q.set(Dashboard::getName, name));
    }
}
