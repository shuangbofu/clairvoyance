package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.web.entity.Dashboard;
import cn.shuangbofu.clairvoyance.web.service.User;
import cn.shuangbofu.clairvoyance.web.service.UserCache;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/7/30 下午10:45
 */
@Data
@Accessors(chain = true)
public class DashboardSimpleVO implements IdVo {

    private Long dashboardId;

    private String name;

    private String remarks;

    private User createUser;
    private User modifyUser;

    public static DashboardSimpleVO toSimpleVO(Dashboard dashboard) {
        return new DashboardSimpleVO()
                .setDashboardId(dashboard.getId())
                .setName(dashboard.getName())
                .setRemarks(dashboard.getRemarks())
                .setCreateUser(UserCache.getUser(dashboard.getCreateUser()))
                .setModifyUser(UserCache.getUser(dashboard.getModifyUser()));
    }

    @Override
    public Long getRefId() {
        return dashboardId;
    }
}
