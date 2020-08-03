package cn.shuangbofu.clairvoyance.core.enums;

import io.github.biezhi.anima.annotation.EnumMapping;
import lombok.AllArgsConstructor;

/**
 * Created by shuangbofu on 2020/7/30 下午8:43
 */
@EnumMapping("nodeTypeValue")
@AllArgsConstructor
public enum NodeType {
    /**
     *
     */
    workSheet(2),
    dashboard(3),

    ;
    int nodeTypeValue;
}
