package cn.shuangbofu.clairvoyance.core.db;

import cn.shuangbofu.clairvoyance.core.enums.SheetType;
import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.annotation.Table;
import io.github.biezhi.anima.core.AnimaQuery;
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

    private SheetType sheetType;
    private String title;
    private String description;
    private String tableName;

    /**
     * 最近数据同步时间
     */
    private Long lastSyncTime;

    /**
     * 字段元数据信息 ----> List<Field>
     */
    private String fieldInfos;

    /**
     * 数据源配置 ----> SourceConfig
     */
    private String sourceConfig;

    public static AnimaQuery<WorkSheet> from() {
        return s(WorkSheet.class)
                .where(WorkSheet::getDeleted, false);
    }
}
