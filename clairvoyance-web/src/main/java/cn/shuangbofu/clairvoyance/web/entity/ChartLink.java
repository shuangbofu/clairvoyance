package cn.shuangbofu.clairvoyance.web.entity;

import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.annotation.Table;
import io.github.biezhi.anima.core.AnimaQuery;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/10/15 11:18
 */
@Data
@Table(name = "chart_link")
@Accessors(chain = true)
public class ChartLink extends Model<ChartLink> {
    private Long id;
    private Long gmtCreate;
    private Long gmtModified;
    @Column(name = "`status`")
    private Boolean deleted;

    public static AnimaQuery<Chart> from() {
        return s(Chart.class)
                .where(Chart::getDeleted, false);
    }
}
