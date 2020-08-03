package cn.shuangbofu.clairvoyance.core.enums;

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
    hive(1),
    excel(2),
    mysql(3),
    view(4),

    ;

    private int sheetTypeValue;
}
