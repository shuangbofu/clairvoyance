package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.db.WorkSheet;
import cn.shuangbofu.clairvoyance.core.enums.SheetType;
import cn.shuangbofu.clairvoyance.web.service.FieldVOloader;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by shuangbofu on 2020/7/31 15:12
 */
@Data
@Accessors(chain = true)
public class WorkSheetVO extends WorkSheetSimpleVO {

    private SheetType sheetType;
    private List<FieldSimpleVO> fields;

    public static WorkSheetVO toVO(WorkSheet workSheet) {
        WorkSheetVO vo = new WorkSheetVO()
                .setSheetType(workSheet.getSheetType())
                .setFields(FieldVOloader.getAllFields(workSheet.getId()));

        vo.setWorkSheetId(workSheet.getId())
                .setDescription(workSheet.getDescription())
                .setTitle(workSheet.getTitle())
                .setTableName(workSheet.getTableName());
        return vo;
    }
}
