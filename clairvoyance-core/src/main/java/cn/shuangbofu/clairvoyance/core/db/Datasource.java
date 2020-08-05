package cn.shuangbofu.clairvoyance.core.db;

import cn.shuangbofu.clairvoyance.core.enums.DatasourceType;
import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.annotation.Table;
import io.github.biezhi.anima.core.AnimaQuery;
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

    private String name;
    private String config;

    public static AnimaQuery<Datasource> from() {
        return s(Datasource.class)
                .where(Datasource::getDeleted, false);
    }
}
