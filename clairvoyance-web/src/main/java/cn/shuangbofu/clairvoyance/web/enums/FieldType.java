package cn.shuangbofu.clairvoyance.web.enums;

import io.github.biezhi.anima.annotation.EnumMapping;
import lombok.AllArgsConstructor;

/**
 * Created by shuangbofu on 2020/8/5 12:05
 */
@EnumMapping("fieldTypeValue")
@AllArgsConstructor
public enum FieldType {

    /**
     * origin 原始字段
     * <p>
     * 自定义字段
     * computed    计算字段
     * group       分组字段
     */
    origin(1),
    computed(2),
    group(3),
    ;

    private final int fieldTypeValue;
}
