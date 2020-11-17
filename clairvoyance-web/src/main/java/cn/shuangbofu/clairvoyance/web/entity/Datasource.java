package cn.shuangbofu.clairvoyance.web.entity;

import cn.shuangbofu.clairvoyance.web.enums.DatasourceType;
import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/8/4 上午10:49
 */
@Data
@Accessors(chain = true)
@Table(name = "datasource")
public class Datasource extends Model<Datasource> {
    private Long id;
    private Long gmtCreate;
    private Long gmtModified;
    @Column(name = "`status`")
    private Boolean deleted;
    private DatasourceType type;
    // private String env;

    private String name;
    private String description;
    private String config;
    private String dbName;

    private String createUser;
    private String modifyUser;


}
