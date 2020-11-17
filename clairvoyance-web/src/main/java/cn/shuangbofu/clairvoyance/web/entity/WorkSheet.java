package cn.shuangbofu.clairvoyance.web.entity;

import cn.shuangbofu.clairvoyance.web.enums.SheetType;
import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/7/30 下午8:35
 */
@Data
@Accessors(chain = true)
@Table(name = "work_sheet")
public class WorkSheet extends Model<WorkSheet> {
    private Long id;
    private Long gmtCreate;
    private Long gmtModified;
    @Column(name = "`status`")
    private Boolean deleted;
    // private String env;

    private SheetType sheetType;
    private String title;
    private String description;
    private String tableName;
    private Long datasourceId;

    /**
     * 最近数据同步时间
     */
    private Long lastSyncTime;
}
