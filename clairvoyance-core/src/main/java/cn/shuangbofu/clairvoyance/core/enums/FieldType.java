package cn.shuangbofu.clairvoyance.core.enums;

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
     * condition 自定义字段，计算字段/分组字段
     */
    origin(1),
    condition(2),
    ;

    private final int fieldTypeValue;
}
