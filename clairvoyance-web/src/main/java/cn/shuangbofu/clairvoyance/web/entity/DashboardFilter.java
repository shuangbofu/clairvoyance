package cn.shuangbofu.clairvoyance.web.entity;

import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.annotation.Table;
import io.github.biezhi.anima.core.AnimaQuery;
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

    private Long dashboardId;

    private String selectedCharts;

    /**
     * 选择字段
     */
    private String sheetFieldMap;
    private String template;

    private Long parentId;
    private Boolean included;
    private String name;
    private Boolean visible;

    public static AnimaQuery<DashboardFilter> from() {
        return s(DashboardFilter.class).where(DashboardFilter::getDeleted, false);
    }
}
