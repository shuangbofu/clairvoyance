package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.db.WorkSheet;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/7/30 下午10:56
 */
@Data
@Accessors(chain = true)
public class WorkSheetSimpleVO implements IdVo {

    private Long workSheetId;
    private String title;
    private String description;
    private String tableName;

    public static WorkSheetSimpleVO toSimpleVO(WorkSheet workSheet) {
        return new WorkSheetSimpleVO()
                .setWorkSheetId(workSheet.getId())
                .setTitle(workSheet.getTitle())
                .setDescription(workSheet.getDescription())
                .setTableName(workSheet.getTableName());
    }

    public static List<WorkSheetSimpleVO> toSimpleVOList(List<WorkSheet> workSheets) {
        return workSheets.stream().map(WorkSheetSimpleVO::toSimpleVO).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return workSheetId;
    }
}
