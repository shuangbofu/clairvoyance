package cn.shuangbofu.clairvoyance.web.pojo.Model.WorkSheetField;

import cn.shuangbofu.clairvoyance.web.enums.ColumnType;
import lombok.Data;

import java.util.List;

@Data
public class ComputeFieldSaveModel {

    private Long fieldId;
    private Long chartId;
    private Long workSheetId;
    private String title;
    private ColumnType type;
    private String formula;
    private List<Long> fieldList;
    private String remark;
}
