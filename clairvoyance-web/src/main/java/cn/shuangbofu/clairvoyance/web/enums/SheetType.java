package cn.shuangbofu.clairvoyance.web.enums;

import io.github.biezhi.anima.annotation.EnumMapping;
import lombok.AllArgsConstructor;

/**
 * Created by shuangbofu on 2020/7/30 下午9:15
 */
@AllArgsConstructor
@EnumMapping("sheetTypeValue")
public enum SheetType {
    /**
     *
     */
    // jdbc
//    hive(1),
//    mysql(2),
//    presto(3),
    source(1),

    // other
    excel(8),
    view(9),

    ;

    private final int sheetTypeValue;

    public boolean fromSource() {
        return sheetTypeValue == 1;
    }
}
