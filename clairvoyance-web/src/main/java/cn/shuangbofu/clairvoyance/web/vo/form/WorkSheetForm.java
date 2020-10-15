package cn.shuangbofu.clairvoyance.web.vo.form;

import cn.shuangbofu.clairvoyance.web.entity.WorkSheet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/7/31 14:53
 */
@Data
@Accessors(chain = true)
@ApiModel("工作表")
public class WorkSheetForm {
    private Long id;

    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("描述")
    private String description;

    public WorkSheet toModel() {
        return new WorkSheet()
                .setId(id)
                .setTitle(title)
                .setDescription(description);
    }
}
