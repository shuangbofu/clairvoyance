package cn.shuangbofu.clairvoyance.web.entity;

import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Table(name = "dashboard_filter_selected")
@Accessors(chain = true)
public class DashboardFilterSelected extends Model<DashboardFilterSelected> {
    private Long id;

    private Long dashboardId;
    private Long dashboardFilterId;
    private Long workSheetId;
    private Long chartId;
    private Long fieldId;


    private Long gmtCreate;
    private Long gmtModified;
    @Column(name = "`status`")
    private String createUser;
    private String modifyUser;
    private Boolean deleted;
    // private String env;
}
