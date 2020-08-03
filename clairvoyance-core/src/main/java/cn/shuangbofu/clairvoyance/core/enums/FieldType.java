package cn.shuangbofu.clairvoyance.core.enums;

import lombok.AllArgsConstructor;

/**
 * Created by shuangbofu on 2020/7/30 下午9:19
 */
@AllArgsConstructor
public enum FieldType {

    /**
     * 字段类型
     */
    text("文本"),
    value("数值"),
    date("日期"),

    ;

    private String desc;
}
