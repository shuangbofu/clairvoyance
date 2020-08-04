package cn.shuangbofu.clairvoyance.core.db;

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
public class DataSource extends Model<DataSource> {
    private Long id;
    private Long gmtCreate;
    private Long gmtModified;
    @Column(name = "`status`")
    private Boolean deleted;

    private String name;
    private String config;

    public static AnimaQuery<DataSource> from() {
        return s(DataSource.class)
                .where(DataSource::getDeleted, false);
    }
}
