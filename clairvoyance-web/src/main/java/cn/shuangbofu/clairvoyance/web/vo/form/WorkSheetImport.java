package cn.shuangbofu.clairvoyance.web.vo.form;

import cn.shuangbofu.clairvoyance.core.enums.SheetType;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/4 13:47
 */
@Data
public class WorkSheetImport {

    private Long datasourceId;
    private Long folderId;
    private List<String> tables;
    private SheetType sheetType;

    private boolean newFolder;
    private boolean allTables;
}
