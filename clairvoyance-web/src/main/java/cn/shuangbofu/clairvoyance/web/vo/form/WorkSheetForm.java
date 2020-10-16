package cn.shuangbofu.clairvoyance.web.vo.form;

import cn.shuangbofu.clairvoyance.web.entity.WorkSheet;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/7/31 14:53
 */
@Data
@Accessors(chain = true)
public class WorkSheetForm {
    private Long id;

    private String title;
    private String description;

    public WorkSheet toModel() {
        return new WorkSheet()
                .setId(id)
                .setTitle(title)
                .setDescription(description);
    }
}
