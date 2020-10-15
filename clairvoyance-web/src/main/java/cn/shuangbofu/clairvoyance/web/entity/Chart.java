package cn.shuangbofu.clairvoyance.web.entity;

import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.annotation.Table;
import io.github.biezhi.anima.core.AnimaQuery;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/7/30 下午8:39
 */
@Data
@Table(name = "chart")
@Accessors(chain = true)
public class Chart extends Model<Chart> {

    private Long id;
    private Long gmtCreate;
    private Long gmtModified;
    @Column(name = "`status`")
    private Boolean deleted;

    private String name;
    private String remarks;

    private Long workSheetId;
    private Long dashboardId;


    /**
     * 图表布局配置,JSON
     */
    private String layoutConfig;

    /**
     * SQL配置
     */
    private String sqlConfig;

    /**
     * 告警配置
     */
    private String alarmConfig;

    private String createUser;
    private String modifyUser;

    public static AnimaQuery<Chart> from() {
        return s(Chart.class)
                .where(Chart::getDeleted, false);
    }
}
