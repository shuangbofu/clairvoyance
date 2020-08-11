package cn.shuangbofu.clairvoyance.core.enums;

import io.github.biezhi.anima.annotation.EnumMapping;
import lombok.AllArgsConstructor;

/**
 * Created by shuangbofu on 2020/7/31 15:26
 */
@EnumMapping("chartTypeValue")
@AllArgsConstructor
public enum ChartType {
    /**
     *
     */
    UNKNOWN(0, "未知"),
    C1(1, "表格"),
    C2(2, "指标卡"),
    C3(3, "计量图"),
    C4(4, "折线图"),
    C5(5, "簇状柱状图"),
    C6(6, "饼图"),
    C7(7, "条形图");
    int chartTypeValue;
    String desc;
}
