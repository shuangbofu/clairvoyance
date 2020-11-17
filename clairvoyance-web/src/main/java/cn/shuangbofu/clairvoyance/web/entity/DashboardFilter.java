package cn.shuangbofu.clairvoyance.web.entity;

import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/8/14 17:35
 */
@Data
@Table(name = "dashboard_filter")
@Accessors(chain = true)
public class DashboardFilter extends Model<DashboardFilter> {
    private Long id;
    private Long gmtCreate;
    private Long gmtModified;
    @Column(name = "`status`")
    private Boolean deleted;
    // private String env;

    private Long dashboardId;
    /**
     * 选择字段
     */
    private String template;
    private Long parentId;
    private String name;
    private Boolean visible;
}
