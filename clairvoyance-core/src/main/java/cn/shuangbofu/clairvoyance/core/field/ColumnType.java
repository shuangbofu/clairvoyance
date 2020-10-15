package cn.shuangbofu.clairvoyance.core.field;

import lombok.AllArgsConstructor;

/**
 * Created by shuangbofu on 2020/7/30 下午9:19
 */
@AllArgsConstructor
public enum ColumnType {

    /**
     * 字段类型
     */
    text(1, "文本"),
    value(2, "数值"),
    date(3, "日期"),

    ;

    private final int columnTypeValue;
    private final String desc;


    /**
     * TODO 获取类型
     *
     * @param type
     * @return
     */
    public static ColumnType valueOfType(String type) {
        type = type.toLowerCase();
        if (type.toLowerCase().contains("varchar") || type.toLowerCase().contains("string")) {
            return text;
        } else if (type.toLowerCase().contains("date")) {
            return date;
        } else {
            return value;
        }
    }
}
