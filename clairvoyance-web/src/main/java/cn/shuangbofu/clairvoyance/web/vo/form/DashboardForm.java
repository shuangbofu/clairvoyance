package cn.shuangbofu.clairvoyance.web.vo.form;

import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.web.entity.Dashboard;
import cn.shuangbofu.clairvoyance.web.vo.LayoutConfig;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuangbofu on 2020/7/30 下午10:33
 */
@Data
@Accessors(chain = true)
public class DashboardForm {

    private Long dashboardId;

    private String name;

    private List<String> tags = new ArrayList<>();

    private String remarks;

    private Long parentId;

    public Dashboard toModel() {
        // TODO CHECK
        return new Dashboard()
                .setName(name)
                .setTags(String.join(",", tags))
                .setRemarks(remarks)
//                .setFilterConfig(JSON.toJSONString(new ArrayList<>()))
                .setLayoutConfig(JSON.toJSONString(new LayoutConfig()));
    }
}
