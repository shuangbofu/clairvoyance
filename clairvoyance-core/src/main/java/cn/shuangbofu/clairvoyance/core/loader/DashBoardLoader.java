package cn.shuangbofu.clairvoyance.core.loader;

import cn.shuangbofu.clairvoyance.core.db.Dashboard;
import cn.shuangbofu.clairvoyance.core.utils.Functions;

import java.util.List;

/**
 * Created by shuangbofu on 2020/7/30 下午10:39
 */
public class DashBoardLoader {

    public static Long create(Dashboard dashboard) {
        return dashboard.insert().getId();
    }

    public static List<Dashboard> inIds(List<Long> ids) {
        return Dashboard.from().in(Dashboard::getId, ids).all();
    }

    public static Dashboard byId(Long id) {
        return Dashboard.from().where(Dashboard::getId, id).one();
    }

    public static void update(Dashboard dashboard) {
        Functions.ifNotNullThen(dashboard, dashboard::update);
    }

    public static boolean delete(Long dashboardId) {
        return new Dashboard()
                .setId(dashboardId)
                .delete() > 0;
    }
}
