package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.web.entity.Dashboard;
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
//
//    @ApiModelProperty("标签")
//    private List<String> tags;

    private String remarks;

    public static DashboardSimpleVO toSimpleVO(Dashboard dashboard) {
        DashboardSimpleVO res = new DashboardSimpleVO()
                .setDashboardId(dashboard.getId())
                .setName(dashboard.getName())
                .setRemarks(dashboard.getRemarks());
//        String[] split = dashboard.getTags().split(",");
//        if (split.length > 0) {
//            res.setTags(Arrays.asList(split));
//        }
        return res;
    }

    @Override
    public Long getRefId() {
        return dashboardId;
    }
}
