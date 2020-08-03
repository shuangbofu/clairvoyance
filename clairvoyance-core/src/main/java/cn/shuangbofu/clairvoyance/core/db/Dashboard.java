package cn.shuangbofu.clairvoyance.core.db;

import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.annotation.Table;
import io.github.biezhi.anima.core.AnimaQuery;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/7/30 下午8:40
 */
@Data
@Table(name = "dashboard")
@Accessors(chain = true)
public class Dashboard extends Model<Dashboard> {

    private Long id;
    private Long gmtCreate;
    private Long gmtModified;
    @Column(name = "`status`")
    private Boolean deleted;

    private String name;
    private String tags;
    private String remarks;

    /**
     * 仪表盘布局配置,JSON
     */
    private String layoutConfig;

    /**
     * 仪表盘过滤器配置
     */
    private String filterConfig;

    public Dashboard() {
        // TODO 创建时设置默认的表盘布局配置和过滤器配置

    }

    public static AnimaQuery<Dashboard> from() {
        return s(Dashboard.class)
                .where(Dashboard::getDeleted, false);
    }
}
