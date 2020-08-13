package cn.shuangbofu.clairvoyance.web.vo.form;

import cn.shuangbofu.clairvoyance.core.db.Dashboard;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.web.vo.LayoutConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuangbofu on 2020/7/30 下午10:33
 */
@Data
@ApiModel("仪表盘")
public class DashboardForm {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("标签")
    private List<String> tags = new ArrayList<>();

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("文件夹id")
    private Long parentId;

    public Dashboard toModel() {
        // TODO CHECK
        return new Dashboard()
                .setName(name)
                .setTags(String.join(",", tags))
                .setRemarks(remarks)
                .setFilterConfig("{}")
                .setLayoutConfig(JSON.toJSONString(new LayoutConfig()));
    }
}
