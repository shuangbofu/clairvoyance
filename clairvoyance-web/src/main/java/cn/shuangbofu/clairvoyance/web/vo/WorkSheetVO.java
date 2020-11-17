package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.web.enums.SheetType;
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
}
