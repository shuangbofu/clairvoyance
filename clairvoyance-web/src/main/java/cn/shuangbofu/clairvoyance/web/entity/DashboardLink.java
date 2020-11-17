package cn.shuangbofu.clairvoyance.web.entity;


import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "dashboard_link")
public class DashboardLink extends Model<DashboardLink> {

    private Long id;
    private Long gmtCreate;
    private Long gmtModified;
    @Column(name = "`status`")
    private Boolean deleted;
    // private String env;


    private Long dashboardId;
    private Long chartId;
    private Long linkedChartId;
    private Long fieldId;
    private Long linkedFieldId;
}
