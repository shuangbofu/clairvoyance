package cn.shuangbofu.clairvoyance.web.entity;

import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.annotation.Table;
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
    // private String env;

    private String name;
    private String tags;
    private String remarks;
    /**
     * 仪表盘布局配置,JSON
     */
    private String layoutConfig;

    private String createUser;
    private String modifyUser;
}
